package net.alantea.xlayer;

import java.lang.reflect.Field;
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
         currentBundle.setObject(concatChars);
         currentBundle.addParm(str);
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
      if (listStartHandler(localName)) return;
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
         String replaced = innerBundle.getMethodName();
         String script = (String) innerBundle.getObject();

         Object obj = new ScriptedProxy(script).as(Manager.searchClass("", replaced));
         currentBundle.addParm(obj);
         return;
      }
      
      // Add variable.
      if ("variable".equals(localName))
      {
         Manager.setVariable(innerBundle.getAddition(), innerBundle.getObject());
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
      }
      // coming out of a method
      else if (innerBundle.getInMethod() != null)
      {
         // end of method definition : execute it with arguments
         MethodReturnedInformation ret = Manager.applyMethod(currentBundle.getObject(), innerBundle.getInMethod(), innerBundle.getParms());
         if (!ret.isSuccess())
         {
            addError("Invalid method execution in " + innerBundle.getInMethod() + ".");
         }
         else
         {
            // store result for upper usage
            if (ret.isNonvoid())
            {
               innerBundle.setObject(ret.getValue());
               currentBundle.addParm(ret.getValue());
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
               addError("Error concerning " + innerBundle.getMethodName() + ".");
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
         includeLevel++;
         Manager.parseFile(this, filePath);
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
      // Search for 'localName' as a method only if a field is not available.
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
   private boolean listStartHandler(String localName)
   {
      if ("list".equals(localName))
      {
         currentBundle.setObject(superBundle.getObject());
         currentBundle.setInMethod("list");
         currentBundle.setMethodName(localName);
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
         catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
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
         else
         {
            // Search for object class
            String className = localName;
            String classAttr = atts.getValue("_class");
            if (classAttr != null)
            {
               className = classAttr;
            }
            if (Manager.verifyNotReserved(className))
            {
               Object newObject = Manager.getInstance(namespace, className, atts);
               if (newObject != null)
               {
                  currentBundle.setObject(newObject);
                  String varName = atts.getValue("_put");
                  if (varName != null)
                  {
                     Manager.setVariable(varName, newObject);
                  }
               }
            }
         }
      }
      return false;
   }
}
