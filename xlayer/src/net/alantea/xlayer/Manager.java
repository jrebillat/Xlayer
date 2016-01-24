package net.alantea.xlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The Class Manager.
 */
public final class Manager
{
   
   /** The classes. */
   private static Map<String, Class<?>> classes = new HashMap<>();
   
   /** The system packages. */
   private static List<String> systemPackages = new ArrayList<>();

   private static Map<String, Object> variables = new HashMap<>();
   
   static
   {
      addPackage("net.alantea.xlayer");
      addPackage("java.lang");
   }
   
   /**
    * Instantiates a new manager. No to be used.
    */
   private Manager() {}
   
   //========================================================================================================
   // Public methods
   //========================================================================================================
   
   /**
    * Adds a package. The packages are scanned for classes used in parsing files.
    * This will not override previously defined class names.
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
      
      // Sometimes there is nothing found. This may mean there is no class in package. But usually, this
      // means in fact the package is in a system jar, not scanned by Reflections. Thus
      // we will use other means (slower) to get classes when necessary.
       systemPackages.add(pack);
      
      // Store found classes.
      for (Class<?> cl : allClasses)
      {
         if (classes.get(cl.getSimpleName()) == null)
         {
            classes.put(cl.getSimpleName(), cl);
         }
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
    * Parses the string.
    *
    * @param root the root object
    * @param text the text containing the XML
    * @return true, if successful
    */
   public static List<String> parse(Object root, String text)
   {
      return parse(root, new StringReader(text));
   }

   /**
    * Parses the file.
    *
    * @param root the root object
    * @param path the file path containing the XML
    * @return true, if successful
    */
   public static List<String> parseFile(String path)
   {
      return parseFile(new Handler(null), path);
   }

   /**
    * Parses the file.
    *
    * @param root the root object
    * @param path the file path containing the XML
    * @return true, if successful
    */
   public static List<String> parseFile(Object root, String path)
   {
      return parseFile(new Handler(root), path);
   }

   /**
    * Parses the resource.
    *
    * @param root the root object
    * @param path the resource path containing the XML
    * @return true, if successful
    */
   public static List<String> parseResource(String path)
   {
      return parseFile(new Handler(null), path);
   }

   /**
    * Parses the resource.
    *
    * @param root the root object
    * @param path the resource path containing the XML
    * @return true, if successful
    */
   public static List<String> parseResource(Object root, String path)
   {
      InputStream in = Object.class.getResourceAsStream(path); 
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      return parse(new Handler(root), reader);
   }

   /**
    * Parses the file.
    *
    * @param root the root object
    * @param path the file path containing the XML
    * @return true, if successful
    */
   static List<String> parseFile(Handler handler, String path)
   {
      try
      {
         return parse(handler, new FileReader(new File(path)));
      }
      catch (FileNotFoundException e)
      {
         List<String> errors = new ArrayList<>();
         errors.add("File not found : " + path);
         return errors;
      }
   }

   /**
    * Parses the reader.
    *
    * @param root the root
    * @param reader the reader to get the XML from
    * @return true, if successful
    */
   public static List<String> parse(Object root, Reader reader)
   {
      return parse(new Handler(root), reader);
   }

   /**
    * Parses the reader.
    *
    * @param root the root
    * @param reader the reader to get the XML from
    * @return true, if successful
    */
   static List<String> parse(Handler handler, Reader reader)
   {
      try
      {
         SAXParserFactory spf = SAXParserFactory.newInstance();
         spf.setNamespaceAware(true);
         spf.setXIncludeAware(true);
         SAXParser saxParser = spf.newSAXParser();
         
         XMLReader xmlReader = saxParser.getXMLReader();
         xmlReader.setContentHandler(handler);
         InputSource inputSource = new InputSource(reader);
         xmlReader.parse(inputSource);
      }
      catch (SAXException | IOException | ParserConfigurationException e)
      {
         List<String> errors = new ArrayList<>();
         errors.add("Parsing error : " + e.getMessage());
         return errors;
      }
      return handler.getErrors();
   }

   /**
    * Set a variable.
    *
    * @param name the name
    * @param content the content
    */
   public static void setVariable(String name, Object content)
   {
      variables .put(name, content);
   }

   /**
    * Gets a variable.
    *
    * @param name the name
    * @return the variable
    */
   public static Object getVariable(String name)
   {
      return variables .get(name);
   }

   /**
    * Get the variables map.
    *
    * @return the variable map
    */
   public static Map<String, Object> getVariables()
   {
      return Collections.unmodifiableMap(variables);
   }

   //========================================================================================================
   // Package methods
   //========================================================================================================
   /**
    * Apply method.
    *
    * @param target the target object
    * @param methName the method name
    * @param objects the objects to push to method
    * @return the information
    */
   @SuppressWarnings("unchecked")
   static MethodReturnedInformation applyMethod(Object target, String methName, List<Object> objects)
   {
      MethodReturnedInformation info = new MethodReturnedInformation();
      boolean oneArg = false;
      List<Class<?>> parmClasses = new ArrayList<>();
      for (Object object : objects)
      {
         parmClasses.add(object.getClass());
      }
      Set<Method> methods = ReflectionUtils.getAllMethods(target.getClass(),
            ReflectionUtils.withName(methName),
            ReflectionUtils.withParametersCount(objects.size()));

      Method meth = null;
      Class<?>[] pcls = null;
      if (!methods.isEmpty())
      {
         for (Method method : methods)
         {
            boolean found = false;
            pcls = method.getParameterTypes();
            if (pcls.length == objects.size())
            {
               found = true;
               for (int i = 0; i < pcls.length; i++)
               {
                  Class<?> pcl = pcls[i];
                  if ((!pcl.isAssignableFrom(objects.get(i).getClass())) && (pcl.isEnum()))
                  {
                     found = false;
                     break;
                  }
               }
               
               if (found)
               {
                  meth = method;
                  break;
               }
            }
         }
      }
      else
      {
         // search for one List parameter
         oneArg = true;
         methods = ReflectionUtils.getAllMethods(target.getClass(),
                   ReflectionUtils.withName(methName),
                  ReflectionUtils.withParametersCount(1));
         if (!methods.isEmpty())
         {
            meth = methods.toArray(new Method[0])[0];
         }
      }
        
      if (meth != null)
      {
         try
         {
            if (oneArg)
            {
               meth.setAccessible(true);
               Object ret = meth.invoke(target, objects);
               info.setValue(ret);
               info.setNonvoid(!meth.getReturnType().equals(Void.class));
            }
            else
            {
               meth.setAccessible(true);
               Object[] parms = new Object[objects.size()];
               for (int i = 0; i < pcls.length; i++)
               {
                  Class<?> pcl = pcls[i];
                  Object obj = getSimpleObject(pcl, objects.get(i).toString());
                  if (obj != null)
                  {
                     parms[i] = obj;
                  }
                  else
                  {
                     parms[i] = objects.get(i);
                  }
               }
               Object ret = null;
               if (parms.length == 0)
               {
                  ret = meth.invoke(target);
               }
               else
               {
                  ret = meth.invoke(target, parms);
               }
               info.setValue(ret);
               info.setNonvoid(!meth.getReturnType().equals(Void.class));
            }
            info.setSuccess(true);
            return info;
         }
         catch (NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
         {
            e.printStackTrace();
         }
      }
      info.setSuccess(false);
      return info;
   }

   /**
    * Search for a method.
    *
    * @param target the target object
    * @param methName the method name
    * @return true, if successful
    */
   @SuppressWarnings("unchecked")
   static String searchMethod(Object target, String methName, int nbParms)
   {
      String root = methName.substring(0, 1).toUpperCase() + methName.substring(1);
      String realName = methName;
      Set<Method> methods = null;
      if (nbParms > -1)
      {
         methods = ReflectionUtils.getAllMethods(target.getClass(), 
            ReflectionUtils.withName(methName), 
            ReflectionUtils.withParametersCount(nbParms));
      }
      else
      {
         methods = ReflectionUtils.getAllMethods(target.getClass(), 
               ReflectionUtils.withName(methName));
      }
      if ((methods.isEmpty()) && (nbParms > -1))
      {
         realName = "set" + root;
         methods = ReflectionUtils.getAllMethods(target.getClass(), 
            ReflectionUtils.withName(realName), 
            ReflectionUtils.withParametersCount(nbParms));
      }
      if ((methods.isEmpty()) && (nbParms > -1))
      {
         realName = "add" + root;
         methods = ReflectionUtils.getAllMethods(target.getClass(), 
            ReflectionUtils.withName(realName), 
            ReflectionUtils.withParametersCount(nbParms));
      }

      if (methods.isEmpty())
      {
         realName = null;
      }
      
      return realName;
   }

   /**
    * Search for a field.
    *
    * @param target the target object
    * @param fieldName the field name
    * @return true, if successful
    */
   @SuppressWarnings("unchecked")
   static Field searchField(Object target, String fieldName)
   {
      Field ret = null;
      Set<Field> fields = ReflectionUtils.getAllFields(target.getClass(), 
            ReflectionUtils.withName(fieldName));
      if (!fields.isEmpty())
      {
         ret = fields.toArray(new Field[0])[0];
      }
      
      return ret;
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
    * Gets a class. Store it for future use.
    *
    * @param name the class name (maybe lower case)
    * @return the class
    */
   static Class<?> searchClass(String namespace, String name)
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
               String innerName = clName.substring(clName.lastIndexOf(".") +1);
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
   static Object getInstance(String namespace, String name, Attributes attrs)
   {
      Class<?> cl = searchClass(namespace, name); 
      //
      if (cl == null)
      {
         return null;
      }

      try
      {
         Object object = cl.newInstance();
         for (int i = 0 ; i < attrs.getLength(); i++)
         {
            String key = attrs.getQName(i);
            String value = attrs.getValue(i);
            setAttribute(object, key, value);
         }
         
         return object;
      }
      catch (InstantiationException | IllegalAccessException e)
      {
         return null;
      }
   }
   
   /**
    * Sets the attribute.
    *
    * @param target the target object
    * @param key the key for the attribute
    * @param value the value to set
    */
   static void setAttribute(Object target, String key, Object value)
   {
      setOrAddAttribute(target.getClass(), target, key, value);
   }
   
   /**
    * Sets the attribute or add it.
    *
    * @param cl the cl
    * @param target the target
    * @param key the key
    * @param value the value
    * @return true, if successful
    */
   static boolean setOrAddAttribute(Class<?> cl, Object target, String key, Object value)
   {
      // Search for a simple method
      if (searchAndRunMethod(cl, target, null, key, value))
      {
         return true;
      }
      
      // Search for a set method
      if (searchAndRunMethod(cl, target, "set", key, value))
      {
         return true;
      }
      
      // Search for an add method
      if (searchAndRunMethod(cl, target, "add", key, value))
      {
         return true;
      }
      
      // Search for a field
      try
      {
         Field field =  cl.getDeclaredField(key);
         field.setAccessible(true);
         if (testPrimitive(field.getType()))
         {
            Object parm = getSimpleObject(field.getType(), value.toString());
            if (parm != null)
            {
               field.set(target, parm);
               return true;
            }
         }
         else
         {
            field.set(target, value);
            return true;
         }
      }
      catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
      {
         // let's go to other tries...
      }
      
      // Search for a "getXXX().add" method
      if (applyGetAndAddMethod(cl, target, key, value))
      {
         return true;
      }
      
      Class<?> superCl = cl.getSuperclass();
      if ((superCl != null) && (! superCl.equals(Object.class)))
      {
         return setOrAddAttribute(superCl, target, key, value);
      }
      return false;
   }
   
   /**
    * Gets a simple object from a String.
    *
    * @param clazz the class for the returned object
    * @param value the value as a String
    * @return the simple object parsed from the string, or null
    */
   static Object getSimpleObject(Class<?> clazz, String value)
   {

      Object parm = null;
      if (clazz == Long.class || clazz == long.class)
      {
         parm = Long.parseLong(value);
      }
      else if (clazz == Float.class || clazz == float.class)
      {
         parm = Float.parseFloat(value);
      }
      else if (clazz == Integer.class || clazz == int.class)
      {
         parm = Integer.parseInt(value);
      }
      else if (clazz == Double.class || clazz == double.class)
      {
         parm = Double.parseDouble(value);
      }
      else if (clazz == Boolean.class || clazz == boolean.class)
      {
         parm = Boolean.parseBoolean(value);
      }
      else if (clazz == Byte.class || clazz == byte.class)
      {
         parm = Byte.parseByte(value);
      }
      else if (clazz == Short.class || clazz == short.class)
      {
         parm = Short.parseShort(value);
      }
      else if (clazz == Boolean.class || clazz == boolean.class)
      {
         parm = Boolean.parseBoolean(value);
      }
      else if (clazz == String.class)
      {
         parm = value;
      }
      return parm;
   }

   static boolean verifyNotReserved(String className)
   {
	switch(className)
	{
	case "integer" :
	case "boolean" :
	case "long" :
	case "float" :
	case "byte" :
	case "short" :
	case "String" :
	case "double" :
		return false;
		
    default :
    	return true;
	}
}

   /**
    * Adds the variable.
    *
    * @param variable the variable
    */
   static void addVariable(Variable variable)
   {
      variables .put(variable.name, variable.getContent());
   }
   
   /**
    * Test if class is convertible in a primitive type.
    *
    * @param clazz the class to analyze
    * @return true, if successful
    */
   static boolean testPrimitive(Class<?> clazz)
   {
      if (clazz.isPrimitive())
      {
         return true;
      }
      else if (Number.class.isAssignableFrom(clazz))
      {
         return true;
      }
      else if (Boolean.class.isAssignableFrom(clazz))
      {
         return true;
      }
      else if (String.class.isAssignableFrom(clazz))
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   //========================================================================================================
   // Private methods
   //========================================================================================================
   /**
    * Search a method w/one arg. This will search for a method whose name is constructed from arguments.
    * If found, runs it.
    *
    * @param cl the class used to search for method
    * @param target the target object to apply method on
    * @param prefix the prefix for method (like 'set' or 'add') or null for no prefix. It key
    * is null, the method name will be 'prefix()'.
    * @param key the key containing the core method name. If a prefix exists, the name will be
    * 'prefixKey()'. If prefix is null, the name is 'key()'.
    * @param value the value to give to the method.
    * @return true, if successful
    */
   private static boolean searchAndRunMethod(Class<?> cl, Object target, String prefix,
         String key, Object value)
   {
      try
      {
         // elaborate method name
         String methName = null;
         if ((prefix != null) && (key != null))
         {
            methName = prefix + key.substring(0, 1).toUpperCase() + key.substring(1);
         }
         else if (key != null)
         {
            methName = key;
         }
         else if (prefix != null)
         {
            methName = prefix;
         }
         else
         {
            // No name : can't find a method.
            return false;
         }
         
         Method[] methods =  cl.getMethods();
         for (Method method : methods)
         {
            if ((method.getName().equals(methName)) && (method.getParameterCount()== 1))
            {
               method.setAccessible(true);
               Parameter parameter = method.getParameters()[0];
               Class<?> clazz = parameter.getType();
               if (testPrimitive(clazz))
               {
                  Object parm = getSimpleObject(clazz, value.toString());
                  method.invoke(target, parm);
                  return true;
               }
               else
               {
                  method.invoke(target, value);
                  return true;
               }
            }
         }
      }
      catch (SecurityException | IllegalAccessException | InvocationTargetException e)
      {
         // let's go to other tries...
         e.printStackTrace();
      }
      catch (IllegalArgumentException e)
      {
         // let's go to other tries...
         e.printStackTrace();
      }
      
      Class<?> sup = cl.getSuperclass();
      if ((sup != null) && (! sup.equals(Object.class)))
      {
         return searchAndRunMethod(sup, target, prefix, key, value);
      }
      return false;
   }
   
   /**
    * Apply getKEYs().yyy() method chain.
    *
    * @param cl the class used to search for method
    * @param target the target object to apply the getKEYs() method on
    * @param key the key containing the KEY method name part. it will be converted with first
    * character in uppercase
    * @param value the value to give to the method.
    * @return true, if successful
    */
   private static boolean applyGetAndAddMethod(Class<?> cl, Object target, String key, Object value)
   {
      // Search for a getXXXs() method
      try
      {
         String methName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "s";
         Method meth = cl.getMethod(methName);
         
         Object pack = meth.invoke(target);
         // analyse for parametrized class information
         Class<?> clazz = parametrizedReturnType(meth);
         Method method = null;
         Class<?> cl0 = clazz;
         while (method == null) 
         {
            // search for add() method in returned type
            try
            {
               method =  meth.getReturnType().getMethod("add", cl0);
            }
            catch (NoSuchMethodException e)
            {
               if (Object.class.equals(cl0))
               {
                  return false;
               }
               cl0 = cl0.getSuperclass();
            }
         }
         method.setAccessible(true);
         
         if (testPrimitive(clazz))
         {
            // apply method on a primitive argument
            Object parm = getSimpleObject(clazz, value.toString());
            method.invoke(pack, parm);
            return true;
         }
         else
         {
            // apply method on a complex argument
            method.invoke(target, value);
            return true;
         }
      }
      catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
      {
         // nothing to do here...
      }
      return false;
   }
   
   /**
    * Adds the child object in the parent object.
    *
    * @param parent the parent
    * @param child the child
    * @return true, if successful
    */
   static boolean addObjectInObject(Object parent, Object child)
   {
      // try using adding the object (ex: for use for adding AWT elements in a Container)
      @SuppressWarnings("unchecked")
      Set<Method> meths = ReflectionUtils.getAllMethods(parent.getClass(),
            ReflectionUtils.withName("add"), ReflectionUtils.withParametersCount(1));
      if (!meths.isEmpty())
      {
         for (Method method : meths)
         {
            Class<?>[] pcls = method.getParameterTypes();
            Class<?> pcl = pcls[0];
            if (pcl.isAssignableFrom(child.getClass()))
            {
               try
               {
                  method.invoke(parent, child);
               }
               catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
               {
                  return false;
               }
               break;
            }
         }
         return true;
      }
      return false;
   }
   
   /**
    * Get parametrized return type if any.
    *
    * @param method the method to analyze
    * @return the class for parametrized (or not) return type
    */
   private static Class<?> parametrizedReturnType(Method method) {
      Type returnType = method.getGenericReturnType();
      Class<?> clazz = method.getReturnType();
      if(returnType instanceof ParameterizedType)
      {
         ParameterizedType type = (ParameterizedType) returnType;
         Type[] typeArguments = type.getActualTypeArguments();
         if (typeArguments.length > 0)
         {
            clazz = (Class<?>) typeArguments[0];
         }
      }
      return clazz;
  }
}
