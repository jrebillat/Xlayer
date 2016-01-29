package net.alantea.xlayer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for Xlayer parsing.
 */
public class Handler extends DefaultHandler
{
   
   /** The bundle fifo. */
   private LinkedList<Bundle> fifo = new LinkedList<>();
   
   /** The current object. */
   private Bundle currentBundle = null;
   
   /** The current object father. */
   private Bundle superBundle = null;
   
   /** The errors. */
   private List<String> errors;
   
   /** The include level. */
   private int includeLevel = 0;
   
   /** concatenated characters. */
   private String concatChars;
   
   //================================================================================
   // Base methods
   //-------------
   /**
    * Instantiates a new handler.
    *
    * @param root the root
    */
   public Handler(Object root)
   {
      super();
      currentBundle = new Bundle();
      currentBundle.setObject(root);
      
      // Initiate error list if at top level parsing only
      if (includeLevel <= 0)
      {
         errors = new ArrayList<>();
      }
      concatChars = "";
   }

   /**
    * to do when starting document parsing.
    */
   @Override
   public void startDocument()
   {
      // nothing so far
   }

   @Override
   public void processingInstruction (String target, String data)
         throws SAXException
   {      
      // Add package.
      if ("package".equals(target))
      {
         Manager.addPackage(data);
      }     
      // Include file.
      if ("include".equals(target))
      {
         if (data.startsWith("file="))
         {
            addErrors(Manager.parseHandledFile(this,stripChain(data.substring(5))));
         }
         else if (data.startsWith("resource="))
         {
            addErrors(Manager.parseHandledResource(this,stripChain(data.substring(9))));
         }
      }
   }
   
   /**
    * Strip chain.
    *
    * @param data the data
    * @return the string
    */
   private String stripChain(String data)
   {
      String ret = data.trim();
      
      if (ret.startsWith("'"))
      {
         ret = ret.substring(1);
      }
      else if (ret.startsWith("\""))
      {
         ret = ret.substring(1);
      }
      
      if (ret.endsWith("'"))
      {
         ret = ret.substring(0, ret.length() - 1);
      }
      else if (ret.endsWith("\""))
      {
         ret = ret.substring(0, ret.length() - 1);
      }
      return ret;
   }


   /**
    * Data actions.
    *
    * @param chars the chars
    * @param start the start position in string
    * @param length the string length
    */
   @Override
   public void characters(char[] chars, int start, int length)
   {
      String str = new String(chars, start, length);
      str = str.replaceAll("\\R", "");
      str = str.trim();
      if (!str.isEmpty())
      {
         concatChars += str;
      //   currentBundle.setObject(concatChars);
      //   currentBundle.addParm(str);
      }
   }
   
   /**
    * Actions to do when starting a new element.
    *
    * @param namespaceURI the namespace uri
    * @param localName the local name
    * @param qName the q name
    * @param atts the atts
    */
   @Override
   public void startElement(String namespaceURI,
         String localName,
         String qName, 
         Attributes atts)
   {
      concatChars = "";
      if (beginStartHandler(namespaceURI)) return;
      if (xlayerStartHandler(localName, atts)) return;
      if (methodStartHandler(localName)) return;
      if (variableStartHandler(localName, atts)) return;
      if (listStartHandler(localName, atts)) return;
      if (constantStartHandler (localName, atts)) return;
      if (objectStartHandler (namespaceURI, localName, atts)) return;
      
      currentBundle.setMethodName(qName);
   }

/**
    * Actions to do when ending an element.
    *
    * @param uri the uri
    * @param localName the local name
    * @param qName the q name
    */
   @Override
   public void endElement(String uri, String localName,
         String qName)
   {
      if (!concatChars.isEmpty())
      {
         currentBundle.addParm(concatChars);
         if ((currentBundle.getObject() != null) && (Manager.testPrimitive(currentBundle.getObject().getClass())))
         {
           Object newObject = Manager.getSimpleObject(currentBundle.getObject().getClass(), concatChars);
           Manager.replaceVariable(currentBundle.getObject(), newObject);
           currentBundle.setObject(newObject);
         }
         else
         {
               currentBundle.setObject(concatChars);
         }
         concatChars = "";
      }
      
      
      // copy current state
      Bundle innerBundle = currentBundle;
      
      // restore previous state
      currentBundle = fifo.removeLast();
      
      if ("xlayer".equals(localName))
      {
         return;
      }

      // include another file.
      if ("include".equals(localName))
      {
         includeLevel--;
         return;
      }
      
      // current state is erroneous
      if (!innerBundle.isValid())
      {
         return;
      }
      
      // Scripted Placeholder.
      if ("script".equals(localName))
      {
         String script = (String) innerBundle.getObject();
         String replaced = innerBundle.getMethodName();
         if (replaced == null)
         {
            Object obj = ScriptedProxy.launch(this, script, innerBundle.getParms());
            if (obj != null)
            {
               currentBundle.setObject(obj);
               currentBundle.addParm(obj);
            }
         }
         else
         {
            Object obj = new ScriptedProxy(script).as(Manager.searchClass("", replaced));
            currentBundle.addParm(obj);
         }
         return;
      }
      
      // Add parametrized field.
      if (currentBundle.getAddition() instanceof Field)
      {
         Field field = (Field) currentBundle.getAddition();
         try
         {
            field.setAccessible(true);
            field.set(currentBundle.getObject(), innerBundle.getParms());
         }
         catch (IllegalArgumentException | IllegalAccessException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         return;
      }
      
      // Add variable.
      if ("variable".equals(localName))
      {
         Manager.setVariable((String)innerBundle.getAddition(), innerBundle.getObject());
         return;
      }

      // we have got a variable
      if (innerBundle.getObject() instanceof Variable)
      {
         return;
      }
      // parsing a variable
      else if (currentBundle.getObject() instanceof Variable)
      {
         ((Variable) currentBundle.getObject()).content(innerBundle.getObject());
         Manager.addVariable((Variable)currentBundle.getObject());
      }
      // parsing a constant
      else if ("constant".equals(localName))
      {
         currentBundle.setObject(innerBundle.getObject());
         currentBundle.addParm(innerBundle.getObject());
      }
      // parsing a list
      else if ("list".equals(innerBundle.getInMethod()))
      {
         currentBundle.addParm(innerBundle.getParms());
         if (innerBundle.getAddition() != null)
         {
            Manager.setVariable((String) innerBundle.getAddition(), innerBundle.getParms());
         }
      }
      // coming out of a method
      else if (innerBundle.getInMethod() != null)
      {
         // end of method definition : execute it with arguments
         MethodReturnedInformation ret = Manager.applyMethod(currentBundle.getObject(), innerBundle.getInMethod(), innerBundle.getParms());
         if (!ret.isSuccess())
         {
            addError(ret.getErrorMessage());
         }
         else
         {
            // store result for upper usage
            if (ret.isNonvoid())
            {
               if ( ret.getValue() != null)
               {
                  currentBundle.setObject(ret.getValue());
                  currentBundle.addParm(ret.getValue());
               }
            }
         }
      }
      // currently in a method parameter
      else if (currentBundle.getInMethod() != null)
      {
         // Defining an object in a method : add object to argument list
         currentBundle.addParm(innerBundle.getObject());
      }
      // parsing an object
      else if (currentBundle.getObject() != null)
      {
         // Object in Object : try to set directly object as attribute.
         boolean done = Manager.setOrAddAttribute(currentBundle.getObject().getClass(), currentBundle.getObject(),
               innerBundle.getMethodName(), innerBundle.getObject());
         if (!done)
         {
            boolean ret = Manager.addObjectInObject(currentBundle.getObject(), innerBundle.getObject());
            if (!ret)
            {
               // try to copy object
               if ((Manager.testPrimitive(currentBundle.getObject().getClass())) 
                     && (innerBundle.getObject() != null))
               {
                  if (currentBundle.getObject().getClass().isAssignableFrom(innerBundle.getObject().getClass()))
                  {
                     currentBundle.setObject(innerBundle.getObject());
                     done = true;
                  }
               }
               if (!done)
               {
                  addError("Error concerning " + innerBundle.getMethodName() + ".");
               }
            }
         }
      }
   }

   /**
    * Actions when ending XML document.
    */
   @Override
   public void endDocument()
   {
      // nothing so far.
   }

   /**
    * Gets the errors.
    *
    * @return the errors
    */
   public List<String> getErrors()
   {
      return errors;
   }

   /**
    * Adds the error.
    *
    * @param error the error
    */
   public void addError(String error)
   {
      this.errors.add(error);
   }

   /**
    * Adds the errors.
    *
    * @param errors the error list
    */
   public void addErrors(List<String> errors)
   {
      this.errors.addAll(errors);
   }
   
   /**
    * Begin start handler.
    *
    * @param namespace the namespace
    * @return true, if successful
    */
   private boolean beginStartHandler(String namespace)
   {
      // store namespace if needed
      Manager.addPackage(namespace);

      // store bundle of father.
      fifo.addLast(currentBundle);
      superBundle = currentBundle;
      
      // new bundle for current stage
      currentBundle = new Bundle();
      
      // check chain validity
      if (!superBundle.isValid())
      {
         currentBundle.setValid(false);
         return true;
      }

      // By default, working on same object
      currentBundle.setObject(superBundle.getObject());
      return false;
   }
   
   /**
    * Xlayer start handler.
    *
    * @param name the name
    * @param atts the atts
    * @return true, if successful
    */
   private boolean xlayerStartHandler(String name, Attributes atts)
   {
      // Placeholder, normally at document start.
      if ("xlayer".equals(name))
      {
         return true;
      }
      
      // Scripted Placeholder.
      if ("script".equals(name))
      {
         currentBundle.setMethodName(atts.getValue("as"));
         return true;
      }
      
      // include other file.
      if ("include".equals(name))
      {
         String filePath = atts.getValue("path");
         String fileType = atts.getValue("type");
         includeLevel++;
         List<String> errors = null;
         if ((fileType != null) && ("file".equals(fileType.toLowerCase())))
         {
            errors = Manager.parseHandledFile(this, filePath);
         }
         else
         {
            errors = Manager.parseHandledResource(this, filePath);
         }
         if (errors == null)
         {
            addError("Bad include call");
         }
         else if (!errors.isEmpty())
         {
            addErrors(errors);
         }
         return true;
      }
      return false;
   }
   
   /**
    * Method start handler.
    *
    * @param name the name
    * @return true, if successful
    */
   private boolean methodStartHandler(String name)
   {
      // Search for 'localName' as a method or a field with accessor.
      if ((superBundle.getObject() != null) && (Manager.searchField(superBundle.getObject(), name) == null))
      {
         currentBundle.setInMethod(Manager.searchMethod(superBundle.getObject(), name, -1));
      }
      else
      {
         currentBundle.setInMethod(null);
      }
      return false;
   }
   
   /**
    * Variable start handler.
    *
    * @param localName the local name
    * @param atts the atts
    * @return true, if successful
    */
   private boolean variableStartHandler(String localName, Attributes atts)
   {
      // Search if we should load a variable
      String varAttr = atts.getValue("_variable");
      if (varAttr != null)
      {
         Object varContent = Manager.getVariable(varAttr);
         // invalid value
         if (varContent == null)
         {
            currentBundle.setValid(false);
            addError("Variable '" + varAttr + "' has no value.");
            return true;
         }
         currentBundle.setObject(varContent);
      }
      else if ("variable".equals(localName))
      {
         currentBundle.setObject(new Variable());
         String name = atts.getValue("name");
         if (name == null)
         {
            currentBundle.setValid(false);
            addError("Invalid variable definition : no name given.");
            return true;
         }
         ((Variable)currentBundle.getObject()).name = name;
         currentBundle.setAddition(name);
      }
      else
      {
         return false;
      }
      currentBundle.setMethodName(localName);
      return true;
   }
   
   /**
    * List start handler.
    *
    * @param localName the local name
    * @return true, if successful
    */
   private boolean listStartHandler(String localName, Attributes atts)
   {
      if ("list".equals(localName))
      {
         currentBundle.setObject(superBundle.getObject());
         currentBundle.setInMethod("list");
         currentBundle.setMethodName(localName);
         String varName = atts.getValue("_put");
         if (varName != null)
         {
            currentBundle.setAddition(varName);
         }
         return true;
      }
      return false;
   }
   
   /**
    * Constant start handler.
    *
    * @param localName the local name
    * @param atts the atts
    * @return true, if successful
    */
   private boolean constantStartHandler(String localName, Attributes atts)
   {
      if ("constant".equals(localName))
      {
         String cstName = atts.getValue("name");
         if (cstName == null)
         {
            currentBundle.setValid(false);
            addError("Invalid constant definition : no constant name given.");
            return true;
         }
         String cstClass = atts.getValue("class");
         if (cstClass == null)
         {
            currentBundle.setValid(false);
            addError("Invalid constant definition : no class name given.");
            return true;
         }
         Class<?> cl = Manager.searchClass("", cstClass);
         if (cl == null)
         {
            currentBundle.setValid(false);
            addError("Invalid constant definition : class  " + cstClass + " not found.");
            return true;
         }
         try
         {
            currentBundle.setObject(cl.getField(cstName).get(null));
         }
         catch (NoSuchFieldException e)
         {
            currentBundle.setValid(false);
            addError("Invalid constant name : " + cstName + " in " + cstClass);
            return true;
         }
         catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
         {
            e.printStackTrace();
            currentBundle.setValid(false);
            addError("Invalid constant definition : class " + cstClass + " generates exception.");
            return true;
         }
         currentBundle.setMethodName(localName);
         return true;
      }
      return false;
   }

   /**
    * Object start handler.
    *
    * @param namespace the namespace
    * @param localName the local name
    * @param atts the atts
    * @return true, if successful
    */
   private boolean objectStartHandler(String namespace, String localName, Attributes atts)
   {
      if (currentBundle.isInMethod() == null)
      {
         Field field = null;
         if (superBundle.getObject() != null)
         {
            field = Manager.searchField(superBundle.getObject(), localName);
         }
         if ((field != null) && (Manager.testPrimitive(field.getType())))
         {
            currentBundle.setObject(superBundle.getObject());
         }
         else if ((field != null) && (field.getType().isEnum()))
         {
            // just initialize. Will be overriden at end.
            currentBundle.setObject(field.getType().getEnumConstants()[0]);
         }
         else if ((field != null) && (field.getGenericType() instanceof ParameterizedType))
         {
            // A list, or something alike...
            currentBundle.setAddition(field);
         }
         else
         {
            // Search for object class
            String className = localName;
            String classAttr = atts.getValue("_class");
            if (classAttr != null)
            {
               className = classAttr;
            }
            
            Object newObject = null;
            if (Manager.verifyNotReserved(className))
            {
               newObject = Manager.getInstance(namespace, className, atts);
            }
            else
            {
               newObject = Manager.createReserved(className);
            }
            if (newObject != null)
            {
               currentBundle.setObject(newObject);
               String varName = atts.getValue("_put");
               if (varName != null)
               {
                  Manager.setVariable(varName, newObject);
               }
            }
            else
            {
               addError("Invalid object definition : class " + localName);
            }
         }
      }
      return false;
   }
}
