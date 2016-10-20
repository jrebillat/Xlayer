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
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import net.alantea.xlayer.util.ClassUtils;
import net.alantea.xlayer.util.MethodUtils;
import net.alantea.xlayer.util.PrimitiveUtils;
import net.alantea.xlayer.util.Variable;

/**
 * The Class Manager.
 */
public final class Manager
{
   private Map<String, Object> variables = new HashMap<>();

   ClassUtils classUtils;
   static
   {
   }

   /**
    * Instantiates a new manager. No to be used.
    */
   public Manager()
   {
      classUtils = new ClassUtils();
      classUtils.addPackage("net.alantea.xlayer");
      classUtils.addPackage("java.lang");
   }

   // ========================================================================================================
   // Public methods
   // ========================================================================================================

   /**
    * Gets the class utils.
    *
    * @return the class utils
    */
   public ClassUtils getClassUtils()
   {
      return classUtils;
   }
   
   /**
    * Clear all information.
    */
   public void clearAll()
   {
      classUtils.clear();
      variables = new HashMap<>();
      classUtils.addPackage("net.alantea.xlayer");
      classUtils.addPackage("java.lang");
   }
   
   public Object getInstance(String namespace, String name, Attributes attrs)
   {
      return classUtils.getInstance(this, namespace, name, attrs);
   }
   
   /**
    * Adds a package. The packages are scanned for classes used in parsing files. This will not
    * override previously defined class names.
    *
    * @param pack the package to add.
    */
   public void addPackage(String pack)
   {
      classUtils.addPackage(pack);
   }
   
   /**
    * Adds a specific class. Override previous definition.
    *
    * @param className the class name to load
    * @throws ClassNotFoundException
    */
   public void addClass(String className) throws ClassNotFoundException
   {
      classUtils.addClass(className);
   }
   
   /**
    * Parses the string.
    *
    * @param root the root object
    * @param text the text containing the XML
    * @return true, if successful
    */
   public List<String> parse(Object root, String text)
   {
      if (text == null)
      {
         List<String> ret = new ArrayList<>();
         ret.add("Null XML source");
         return ret;
      }
      return parse(root, new StringReader(text));
   }

   /**
    * Parses the file.
    *
    * @param root the root object
    * @param path the file path containing the XML
    * @return true, if successful
    */
   public List<String> parseFile(String path)
   {
      return parseHandledFile(new Handler(this, null), path);
   }

   /**
    * Parses the file.
    *
    * @param root the root object
    * @param path the file path containing the XML
    * @return true, if successful
    */
   public List<String> parseFile(Object root, String path)
   {
      return parseHandledFile(new Handler(this, root), path);
   }

   /**
    * Parses the resource.
    *
    * @param root the root object
    * @param path the resource path containing the XML
    * @return true, if successful
    */
   public List<String> parseResource(String path)
   {
      return parseFile(new Handler(this, null), path);
   }

   /**
    * Parses the resource.
    *
    * @param root the root object
    * @param path the resource path containing the XML
    * @return true, if successful
    */
   public List<String> parseResource(Object root, String path)
   {
      return parseHandledResource(new Handler(this, root), path);
   }

   /**
    * Parses the resource.
    *
    * @param root the root object
    * @param path the resource path containing the XML
    * @return true, if successful
    */
   public List<String> parseHandledResource(Handler handler, String path)
   {
      if (path == null)
      {
         List<String> ret = new ArrayList<>();
         ret.add("Null XML source");
         return ret;
      }
      InputStream in = Manager.class.getResourceAsStream(path);
      if (in == null)
      {
         List<String> ret = new ArrayList<>();
         ret.add("Invalid XML source");
         return ret;
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      return parse(handler, reader);
   }

   /**
    * Parses the file.
    *
    * @param root the root object
    * @param path the file path containing the XML
    * @return true, if successful
    */
   public List<String> parseHandledFile(Handler handler, String path)
   {
      if (path == null)
      {
         List<String> ret = new ArrayList<>();
         ret.add("Null XML source");
         return ret;
      }
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
   public List<String> parse(Object root, Reader reader)
   {
      return parse(new Handler(this, root), reader);
   }

   /**
    * Parses the reader.
    *
    * @param root the root
    * @param reader the reader to get the XML from
    * @return true, if successful
    */
   private List<String> parse(Handler handler, Reader reader)
   {
      if (reader == null)
      {
         List<String> ret = new ArrayList<>();
         ret.add("Null XML source");
         return ret;
      }
      try
      {
         SAXParserFactory spf = SAXParserFactory.newInstance();
         spf.setNamespaceAware(true);
//         spf.setXIncludeAware(true);
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
   public void setVariable(String name, Object content)
   {
      variables.put(name, content);
   }

   /**
    * Gets a variable.
    *
    * @param name the name
    * @return the variable
    */
   public Object getVariable(String name)
   {
      Object object = variables.get(name);
      if ((object != null) && (object instanceof Variable))
      {
         object = ((Variable)object).getContent();
      }
      return object;
   }

   /**
    * Get the variables map.
    *
    * @return the variable map
    */
   public Map<String, Object> getVariables()
   {
      return Collections.unmodifiableMap(variables);
   }

   // ========================================================================================================
   // Package methods
   // ========================================================================================================

   /**
    * Search for a field.
    *
    * @param target the target object
    * @param fieldName the field name
    * @return true, if successful
    */
   public Field searchField(Object target, String fieldName)
   {
      Field ret = searchField(target.getClass(), fieldName);
      if ((ret != null) && (ret.getGenericType() instanceof ParameterizedType))
      {
         // need accessor
         ret = null;
      }
      return ret;
   }
   
   private Field searchField(Class<?> target, String fieldName)
   {
      for (Field f : target.getDeclaredFields())
      {
    	  String name = f.getName();
    	  if (name.equals(fieldName))
    	  {
    		  return f;
    	  }
      }
      if (! target.equals(Object.class))
      {
    	  return searchField(target.getSuperclass(), fieldName);
      }
      return null;
   }

   /**
    * Sets the attribute.
    *
    * @param target the target object
    * @param key the key for the attribute
    * @param value the value to set
    */
   public void setAttribute(Object target, String key, Object value)
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
   public boolean setOrAddAttribute(Class<?> cl, Object target, String key, Object value)
   {
      // Search for a simple method
      if (MethodUtils.searchAndRunMethod(cl, target, null, key, value))
      {
         return true;
      }

      // Search for a set method
      if (MethodUtils.searchAndRunMethod(cl, target, "set", key, value))
      {
         return true;
      }

      // Search for an add method
      if (MethodUtils.searchAndRunMethod(cl, target, "add", key, value))
      {
         return true;
      }

      // Search for a field
      try
      {
         Field field = cl.getDeclaredField(key);
         field.setAccessible(true);
         if ((PrimitiveUtils.testPrimitive(field.getType())) && (value != null))
         {
            Object parm = PrimitiveUtils.getSimpleObject(field.getType(), value.toString());
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
      if (MethodUtils.applyGetAndAddMethod(cl, target, key, value))
      {
         return true;
      }

      Class<?> superCl = cl.getSuperclass();
      if ((superCl != null) && (!superCl.equals(Object.class)))
      {
         return setOrAddAttribute(superCl, target, key, value);
      }
      return false;
   }


   /**
    * Adds the variable.
    *
    * @param name the variable name
    * @param value the variable value
    */
   public void addVariable(String name, Object value)
   {
      variables.put(name, value);
   }

   /**
    * Adds the variable.
    *
    * @param variable the variable
    */
   void addVariable(Variable variable)
   {
      variables.put(variable.getName(), variable.getContent());
   }


   // ========================================================================================================
   // Private methods
   // ========================================================================================================
 
   /**
    * Replace a variable. Needed to change Number value.
    *
    * @param oldObj the old object
    * @param newObj the new object
    */
   void replaceVariable(Object oldObj, Object newObj)
   {
      for (String key : variables.keySet())
      {
         Object obj = variables.get(key);
         if (obj.equals(oldObj))
         {
            variables.put(key, newObj);
         }
      }
   }
}
