package net.alantea.xlayer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.xml.sax.Attributes;

import com.google.common.base.Predicate;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.alantea.xlayer.Manager;

public final class ClassUtils
{

   /** The classes. */
   private static Map<String, Class<?>> classes = new HashMap<>();

   /** The system packages. */
   private static List<String> systemPackages = new ArrayList<>();

   private static Set<String> allPackages;
   

   /**
    * Do not instantiates.
    */
   private ClassUtils()
   {
   }
   
   /**
    * Clear all packages.
    */
   public static void clear()
   {
      classes = new HashMap<>();
      systemPackages = new ArrayList<>();
   }
   
   /**
    * Adds a package and its subpackages. The packages are scanned for classes used in parsing files.
    * This will not override previously defined class names.
    *
    * @param pack the top package to add.
    */
   public static void addPackages(String pack)
   {
	   addPackage(pack);
	   findAllSubPackages(pack).forEach((subpack) -> addPackage(subpack));
   }
   
   /**
    * Adds a package. The packages are scanned for classes used in parsing files. This will not
    * override previously defined class names.
    *
    * @param pack the package to add.
    */
   public static void addPackage(String pack)
   {
      if ((pack == null) || (pack.trim().isEmpty()))
      {
         return;
      }

      // do not do it twice !
      if (systemPackages.contains(pack))
      {
         return;
      }
      // use reflections to find content
      List<String> allClasses = new FastClasspathScanner(pack).scan().getNamesOfAllClasses();

      // Sometimes there is nothing found. This may mean there is no class in package. But usually,
      // this
      // means in fact the package is in a system jar, not scanned by Reflections. Thus
      // we will use other means (slower) to get classes when necessary.
      systemPackages.add(pack);

      // Store found classes.
      for (String cl : allClasses)
      {
         if (classes.get(cl) == null)
         {
			try {
				Class<?> theClass = ClassLoader.getSystemClassLoader().loadClass(cl);
	            classes.put(cl.substring(cl.lastIndexOf(".")), theClass);
	            addInnerClasses(theClass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
      }
   }

   /**
    * Adds the inner classes.
    *
    * @param mainClass the main class
    */
   private static void addInnerClasses(Class<?> mainClass)
   {
      Class<?>[] innerClasses = mainClass.getClasses();
      for (Class<?> innerClass : innerClasses)
      {
    	  System.out.println("add inner class : " + innerClass.getSimpleName());
         classes.put(innerClass.getSimpleName(), innerClass);
         addInnerClasses(innerClass);
      }
   }

   /**
    * Adds a specific class. Override previous definition.
    *
    * @param className the class name to load
    * @throws ClassNotFoundException
    */
   public static void addClass(String className) throws ClassNotFoundException
   {
      Class<?> cl = Class.forName(className);
      classes.put(cl.getSimpleName(), cl);
   }


   /**
    * Gets a class. Store it for future use.
    *
    * @param clName the class name
    * @return the class
    */
   static Class<?> getKnownClass(String clName)
   {
      Class<?> cl = classes.get(clName);
      if (cl == null)
      {
         for (String pack : systemPackages)
         {
            try
            {
               cl = Class.forName(pack + "." + clName);
               classes.put(clName, cl);
               break;
            }
            catch (ClassNotFoundException e)
            {
               // go to next...
            }
         }
      }
      return cl;
   }
   
   /**
    * Check compatibility of a derived class with a base class.
    *
    * @param namespace the namespace
    * @param baseName the base name
    * @param derivedName the derived name
    * @return true, if successful
    */
   static boolean checkCompatibility(String namespace, String baseName, String derivedName)
   {
      Class<?> base = searchClass(namespace, baseName);
      Class<?> derived = searchClass("", derivedName);
      return base.isAssignableFrom(derived);
   }
   
   /**
    * Gets a class. Store it for future use.
    *
    * @param name the class name (maybe lower case)
    * @return the class
    */
   public static Class<?> searchClass(String namespace, String name)
   {
      String clName = name;

      // class short name : ensure first char is uppercase.
      if (!name.contains("."))
      {
         clName = name.substring(0, 1).toUpperCase() + name.substring(1);
      }

      // add namespace
      if (!namespace.trim().isEmpty())
      {
         clName = namespace + "." + clName;
      }

      // search in already known classes
      Class<?> cl = getKnownClass(clName);

      if (cl == null)
      {
         // Search for class itself
         try
         {
            cl = Class.forName(clName);
            classes.put(clName, cl);
         }
         catch (ClassNotFoundException e)
         {
            // well, maybe an inner class ?
            if (clName.contains("."))
            {
               String outerName = clName.substring(0, clName.lastIndexOf("."));
               String innerName = clName.substring(clName.lastIndexOf(".") + 1);
               Class<?> outerClass = searchClass(namespace, outerName);
               if (outerClass != null)
               {
                  Class<?>[] innerClasses = outerClass.getClasses();
                  for (Class<?> innerClass : innerClasses)
                  {
                     if (innerClass.getSimpleName().equals(innerName))
                     {
                        cl = innerClass;
                        break;
                     }
                  }
                  classes.put(clName, cl);
               }
            }
         }
      }
      return cl;
   }

   /**
    * Gets a new instance of a class. Store it for future use.
    *
    * @param namespace the class namespace
    * @param name the class name
    * @param attrs the attributes to set
    * @return the new class instance
    */
   public static Object getInstance(String namespace, String name, Attributes attrs)
   {
      Class<?> cl = searchClass(namespace, name);
      //
      if (cl == null)
      {
         return null;
      }

      if (cl.isEnum())
      {
         return cl.getEnumConstants()[0];
      }
      else
      {
         try
         {
            Object object = cl.newInstance();
            for (int i = 0; i < attrs.getLength(); i++)
            {
               String key = attrs.getQName(i);
               String value = attrs.getValue(i);
               Manager.setAttribute(object, key, value);
            }

            return object;
         }
         catch (InstantiationException | IllegalAccessException e)
         {
            return null;
         }
      }
   }
   
	/**
	 * Finds all package names starting with prefix
	 * @param prefix prefix to search for
	 * @return Set of package names
	 */
	public static Set<String> findAllSubPackages(String prefix) {
	    findAllPackages(prefix);

		Set<String> ret = new TreeSet<String>();
	    for (String name : allPackages) {
	        if (name.startsWith(prefix)) {
	            ret.add(name);
	        }
	    }
	    return ret;
	}

	/**
	 * Finds all package names in path
	 * derived from http://stackoverflow.com/questions/13944633/java-reflection-get-list-of-packages
	 * @return Set of package names
	 */
	public static void findAllPackages(String prefix) {
		allPackages = new TreeSet<String>();
	    List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
	    classLoadersList.add(ClasspathHelper.contextClassLoader());
	    classLoadersList.add(ClasspathHelper.staticClassLoader());
	    Reflections reflections = new Reflections(new ConfigurationBuilder()
	            .setScanners(new SubTypesScanner(false), new ResourcesScanner())
	            .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
	            .filterInputsBy(new PackagePredicate(prefix)));

	    reflections.getSubTypesOf(Object.class);
	}
	
	/**
	 * The Class PackagePredicate.
	 * Well... to explain :
	 *   I was unable to understand the 'filterInputsBy' method of ConfigurationBuilder. It seems not to render
	 *   the way I expected. But, fortunately, it is calling correctly the predicate. Thus, I created a predicate
	 *   that returns always false but store interesting information in its own way (in the allPackages field).
	 *   So : I do not rely on the return from "getSubTypesOf" method, but I got my list.
	 *   It seems to work and is faster than other, more standard, methods to do the job.
	 */
	private static class PackagePredicate implements Predicate<String>
	{
		private String prefix;

		PackagePredicate(String prefix)
		{
			this.prefix = prefix;
		}

		@Override
		public boolean apply(String name) {
			if (name.startsWith(prefix))
			{
			   String packName = name.substring(0, name.lastIndexOf("."));
			   packName = packName.substring(0, packName.lastIndexOf("."));
			   allPackages.add(packName);
			}
			return false;
		}
	}
}
