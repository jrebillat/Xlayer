package net.alantea.xlayer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class Handler.
 */
public class Handler extends DefaultHandler
{
   
   /** The bundle fifo. */
   private LinkedList<Bundle> fifo = new LinkedList<>();
   
   /** The current object. */
   private Bundle currentBundle = null;
   
   /** The errors. */
   private List<String> errors;
   
   /** The include level. */
   private int includeLevel = 0;
   
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
      if (includeLevel <= 0)
      {
         errors = new ArrayList<>();
      }
   }

   /**
    * to do when starting document parsing.
    */
   @Override
   public void startDocument()
   {
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
         currentBundle.setObject(str);
         currentBundle.addParm(str);
      }
   }
   
   /**
    * Actions à réaliser lors de la détection d'un nouvel élément.
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
      // store namespace if needed
      Manager.addPackage(namespaceURI);

      // store bundle of father.
      fifo.addLast(currentBundle);
      Bundle superBundle = currentBundle;
      
      // new bundle for current stage
      currentBundle = new Bundle();
      
      // check chain validity
      if (!superBundle.isValid())
      {
         currentBundle.setValid(false);
         return;
      }

      // By default, working on same object
      currentBundle.setObject(superBundle.getObject());
      
      // Placeholder, normally at document start.
      if ("xlayer".equals(localName))
      {
         return;
      }
      
      // include other file.
      if ("include".equals(localName))
      {
         String filePath = atts.getValue("path");
         includeLevel++;
         Manager.parseFile(this, filePath);
         return;
      }

      // Search for 'localName' as a method only if a field is not available.
      if (Manager.searchField(superBundle.getObject(), localName) == null)
      {
         currentBundle.setInMethod(Manager.searchMethod(superBundle.getObject(), localName, -1));
      }
      else
      {
         currentBundle.setInMethod(null);
      }

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
            return;
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
            return;
         }
         ((Variable)currentBundle.getObject()).name = name;
         currentBundle.setInMethod(null);
      }
      else if ("list".equals(localName))
      {
         currentBundle.setObject(superBundle.getObject());
         currentBundle.setInMethod("list");
      }
      else if ("constant".equals(localName))
      {
         String cstName = atts.getValue("name");
         if (cstName == null)
         {
            currentBundle.setValid(false);
            addError("Invalid constant definition : no constant name given.");
            return;
         }
         String cstClass = atts.getValue("class");
         if (cstClass == null)
         {
            currentBundle.setValid(false);
            addError("Invalid constant definition : no class name given.");
            return;
         }
         Class<?> cl = Manager.getKnownClass(cstClass);
         if (cl == null)
         {
            currentBundle.setValid(false);
            addError("Invalid constant definition : class " + cstClass + " not found.");
            return;
         }
         try
         {
            currentBundle.setObject(cl.getField(cstName).get(null));
         }
         catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      else if (currentBundle.isInMethod() == null)
      {
         Field field = Manager.searchField(superBundle.getObject(), localName);
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
               Object newObject = Manager.getInstance(namespaceURI, className, atts);
               if (newObject != null)
               {
                  currentBundle.setObject(newObject);
               }
            }
         }
      }

      currentBundle.setMethName(qName);
   }

/**
    * Actions à réaliser lors de la détection de la fin d'un élément.
    *
    * @param uri the uri
    * @param localName the local name
    * @param qName the q name
    */
   @SuppressWarnings("unchecked")
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

      // include other file.
      if ("include".equals(localName))
      {
         includeLevel--;
         return;
      }
      
      if (!innerBundle.isValid())
      {
         return;
      }

      if (innerBundle.getObject() instanceof Variable)
      {
         return;
      }
      else if (currentBundle.getObject() instanceof Variable)
      {
         ((Variable) currentBundle.getObject()).content(innerBundle.getObject());
         Manager.addVariable((Variable)currentBundle.getObject());
      }
      else if ("constant".equals(localName))
      {
         currentBundle.setObject(innerBundle.getObject());
         currentBundle.addParm(innerBundle.getObject());
      }
      else if ("list".equals(innerBundle.getInMethod()))
      {
         currentBundle.addParm(innerBundle.getParms());
      }
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
            if (ret.isNonvoid())
            {
               innerBundle.setObject(ret.getValue());
               currentBundle.addParm(ret.getValue());
            }
         }
      }
      else if (currentBundle.getInMethod() != null)
      {
         // Defining an object in a method : add object to argument list
         currentBundle.addParm(innerBundle.getObject());
      }
      else if (currentBundle.getObject() != null)
      {
         // Object in Object : try to set directly object as attribute.
         boolean done = Manager.setOrAddAttribute(currentBundle.getObject().getClass(), currentBundle.getObject(),
               innerBundle.getMethodName(), innerBundle.getObject());
         if (!done)
         {
            // try using adding the object (ex: for use for adding AWT elements in a Container)
            Set<Method> meths = ReflectionUtils.getAllMethods(currentBundle.getObject().getClass(),
                  ReflectionUtils.withName("add"), ReflectionUtils.withParametersCount(1));
            if (!meths.isEmpty())
            {
               try
               {
                  meths.toArray(new Method[0])[0].invoke(currentBundle.getObject(), innerBundle.getObject());
               }
               catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
               {
                  addError("Error concerning " + innerBundle.getMethodName() + ".");
               }
            }
         }
      }
   }

   /**
    * Actions à réaliser lors de la fin du document XML.
    */
   @Override
   public void endDocument()
   {
      // TODO : add possibility that root element of XML may be a method.
      if (includeLevel <= 0)
      {
         System.out.println("------- Error output -------");
         for (String str : getErrors())
         {
            System.out.println(str);
         }
         System.out.println("----------------------------");
      }
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
}
