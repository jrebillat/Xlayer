package net.alantea.xlayer;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import net.alantea.xlayer.bundles.ArrayBundle;
import net.alantea.xlayer.bundles.BaseBundle;
import net.alantea.xlayer.bundles.CharsBundle;
import net.alantea.xlayer.bundles.ConstantBundle;
import net.alantea.xlayer.bundles.EqualsVariableBundle;
import net.alantea.xlayer.bundles.FakeBundle;
import net.alantea.xlayer.bundles.FieldBundle;
import net.alantea.xlayer.bundles.IncludeBundle;
import net.alantea.xlayer.bundles.ListBundle;
import net.alantea.xlayer.bundles.MethodBundle;
import net.alantea.xlayer.bundles.ObjectBundle;
import net.alantea.xlayer.bundles.RootBundle;
import net.alantea.xlayer.bundles.ScriptBundle;
import net.alantea.xlayer.bundles.SimpleBundle;
import net.alantea.xlayer.bundles.StaticBundle;
import net.alantea.xlayer.bundles.VariableBundle;
import net.alantea.xlayer.util.ClassUtils;
import net.alantea.xlayer.util.MethodUtils;
import net.alantea.xlayer.util.PrimitiveUtils;

/**
 * Handler for Xlayer parsing.
 */
public class Handler extends DefaultHandler
{
   private Manager manager;
   
   /** The current object. */
   private BaseBundle currentBundle = null;
   
   /** The errors. */
   private List<String> errors = new ArrayList<>();
   
   /** concatenated characters. */
   private String concatChars;

   private int includeLevel;
   
   //================================================================================
   // Base methods
   //-------------
   /**
    * Instantiates a new handler.
    *
    * @param manager the manager to use
    * @param root the root
    */
   public Handler(Manager manager, Object root)
   {
      super();
      this.manager = manager;
      BaseBundle oldBundle = currentBundle;
      currentBundle = new RootBundle(root);
      
      if (oldBundle != null)
      {
         currentBundle.setFatherBundle(oldBundle);
      }
      
      // Initiate error list if at top level parsing only
      if (includeLevel <= 0)
      {
         errors = new ArrayList<>();
      }
      concatChars = "";
   }
   
   /**
    * Gets the manager.
    *
    * @return the manager
    */
   public Manager getManager()
   {
      return manager;
   }
   
   public ClassUtils getClassUtils()
   {
      return manager.getClassUtils();
   }

   /**
    * to do when starting document parsing.
    */
   @Override
   public void startDocument()
   {
      // nothing so far
   }

   /* (non-Javadoc)
    * @see org.xml.sax.helpers.DefaultHandler#processingInstruction(java.lang.String, java.lang.String)
    */
   @Override
   public void processingInstruction (String target, String data)
         throws SAXException
   {      
      // Add package.
      if ("package".equals(target))
      {
         manager.addPackage(data);
      }     
      // Include file.
      if ("include".equals(target))
      {
         if (data.startsWith("file="))
         {
            addErrors(manager.parseHandledFile(this,stripChain(data.substring(5))));
         }
         else if (data.startsWith("resource="))
         {
            addErrors(manager.parseHandledResource(this,stripChain(data.substring(9))));
         }
      }
   }
   
   /**
    * Strip chain from starting ' or " characters.
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
      // store namespace if needed
      manager.addPackage(namespaceURI);

      // Clear characters
      concatChars = "";
      
      // Gather for special attributes
      String varAttr = atts.getValue("_variable");
      
      BaseBundle fatherBundle = currentBundle;
      currentBundle = null;

      // Placeholder, normally at document start.
      if ("xlayer".equals(localName))
      {
         currentBundle = new FakeBundle(fatherBundle);
      }
      
      // Scripted Placeholder.
      else if ("script".equals(localName))
      {
         currentBundle = new ScriptBundle(fatherBundle);
      }
      
      // include other file.
      else if ("include".equals(localName))
      {
         currentBundle = new IncludeBundle(fatherBundle);
      }

      // Add variable.
      else if ("constant".equals(localName))
      {
         currentBundle = new ConstantBundle(fatherBundle);
      }

      // Create variable.
      else if ("variable".equals(localName))
      {
         currentBundle = new VariableBundle(fatherBundle);
      }
      
      // Create list
      else if ("list".equals(localName))
      {
         currentBundle = new ListBundle(fatherBundle);
      }
      
      // Create array
      else if ("array".equals(localName))
      {
         currentBundle = new ArrayBundle(fatherBundle);
      }
      
      // Create array
      else if ("static".equals(localName))
      {
         currentBundle = new StaticBundle(fatherBundle);
      }
      
      // load a variable
      else if ((varAttr != null) 
    		  && ((!PrimitiveUtils.verifyNotReserved(localName)) 
    				  || (!PrimitiveUtils.verifyNotReservedContainer(localName))))
      {
         currentBundle = new EqualsVariableBundle(fatherBundle);
      }
      
      // Method, field or object
      else
      {
         Object fatherObject = fatherBundle.getCurrentObject();
         if (fatherObject != null)
         {
            Object parm = null;
            String objClass = atts.getValue("_class");
            if (objClass != null)
            {
               parm = manager.getInstance(namespaceURI, objClass, new AttributesImpl());
            }

            // Search for fields to put an object in
            if ((objClass != null) && (manager.searchField(fatherObject, localName) != null))
            {
               currentBundle = new FieldBundle(fatherBundle);
               if (varAttr != null)
               {
            	   currentBundle.setValue(manager.getVariable(varAttr));
               }
            }
            // search for a method
            else if (MethodUtils.searchMethod(fatherObject, localName, -1) != null)
            {
               currentBundle = new MethodBundle(fatherBundle);
               if (varAttr != null)
               {
            	   // add variable as first parameter
            	   new SimpleBundle(currentBundle, manager.getVariable(varAttr));
               }
            }
            // search for a field without setter (else it was found just before)
            else if ( manager.searchField(fatherObject, localName) != null)
            {
               currentBundle = new FieldBundle(fatherBundle);
               if (varAttr != null)
               {
            	   currentBundle.setValue(manager.getVariable(varAttr));
               }
            }
            
            if (parm != null)
            {
               new SimpleBundle(currentBundle, parm);
            }
         }
         
         // nothing found so far. Presume this is an object
         if (currentBundle == null)
         {
            currentBundle = new ObjectBundle(fatherBundle);
            if (varAttr != null)
            {
         	   currentBundle.setValue(manager.getVariable(varAttr));
            }
         }
      }
      
      // Launch specific handling of start element
      if (currentBundle != null)
      {
         addErrors(currentBundle.manageStartElement(this, namespaceURI, localName, qName, atts));
      }
      else
      {
         addError("Parse error with : " + localName);
      }
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
      // Add a chars bundle if string found
      if (!concatChars.isEmpty())
      {
         new CharsBundle(currentBundle, concatChars);
         // clear for next call
         concatChars = "";
      }
      
      if (currentBundle.isValid())
      {
         // Launch specific handling of end element
         addErrors(currentBundle.manageEndElement(this, uri, localName, qName));
      }
      currentBundle = currentBundle.getFatherBundle();
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
    * @param errs the errors
    */
   public void addErrors(List<String> errs)
   {
      if (errs != null)
      {
         errors.addAll(errs);
      }
   }
}
