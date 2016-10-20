package net.alantea.xlayer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.alantea.xlayer.Manager;

public final class ClassUtils
{

   /** The classes. */
   private Map<String, Class<?>> classes = new HashMap<>();

   /** The system packages. */
   private List<String> systemPackages = new ArrayList<>();
   

   /**
    * Do not instantiates.
    */
   public ClassUtils()
   {
   }
   
   /**
    * Clear all packages.
    */
   public void clear()
   {
      classes = new HashMap<>();
      systemPackages = new ArrayList<>();
   }
   
   /**
    * Adds a package. The packages are scanned for classes used in parsing files. This will not
    * override previously defined class names.
    *
    * @param pack the package to add.
    */
   public void addPackage(String pack)
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

      systemPackages.add(pack);

      // Store found classes.
      for (String cl : allClasses)
      {
         if (classes.get(cl) == null)
         {
			try {
				Class<?> theClass = ClassLoader.getSystemClassLoader().loadClass(cl);
	            classes.put(cl.substring(cl.lastIndexOf(".")+1), theClass);
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
   private void addInnerClasses(Class<?> mainClass)
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
   public void addClass(String className) throws ClassNotFoundException
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
  private Class<?> getKnownClass(String clName)
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
   boolean checkCompatibility(String namespace, String baseName, String derivedName)
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
   public Class<?> searchClass(String namespace, String name)
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
   public Object getInstance(Manager manager, String namespace, String name, Attributes attrs)
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
               manager.setAttribute(object, key, value);
            }

            return object;
         }
         catch (InstantiationException | IllegalAccessException e)
         {
            return null;
         }
      }
   }
}
