package net.alantea.xlayer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.xml.sax.Attributes;

import net.alantea.xlayer.Manager;

public final class ClassUtils
{

   /** The classes. */
   private static Map<String, Class<?>> classes = new HashMap<>();

   /** The system packages. */
   private static List<String> systemPackages = new ArrayList<>();


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
      Reflections reflections = new Reflections(pack, new SubTypesScanner(false));
      Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

      // Sometimes there is nothing found. This may mean there is no class in package. But usually,
      // this
      // means in fact the package is in a system jar, not scanned by Reflections. Thus
      // we will use other means (slower) to get classes when necessary.
      systemPackages.add(pack);

      // Store found classes.
      for (Class<?> cl : allClasses)
      {
         if (classes.get(cl.getSimpleName()) == null)
         {
            classes.put(cl.getSimpleName(), cl);
            addInnerClasses(cl);
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
}
