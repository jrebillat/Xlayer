package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * The base class for Bundle to store information for a parsing level.
 */
public abstract class BaseBundle
{

   //-------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------
   
   /** The father bundle. */
   private BaseBundle fatherBundle;
   
   /**  The children bundles. */
   List<BaseBundle> children = new ArrayList<>();
   
   /**  Whether the bundle concerns a valid information. */
   private boolean valid;
   
   /** The returned value. */
   private Object value;
   
   /** The variable name to store content. */
   private String varName;

   //-------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------
   /**
    * Instantiates a new base bundle.
    *
    * @param father the father or null
    */
   BaseBundle(BaseBundle father)
   {
      fatherBundle = father;
      if (father != null)
      {
         father.addChild(this);
      }
      valid = true;
   }

   //-------------------------------------------------------------------
   // Getters and setters
   //-------------------------------------------------------------------
   /**
    * Checks if it is valid.
    *
    * @return true, if it is valid
    */
   public boolean isValid()
   {
      return valid;
   }

   /**
    * Sets the validity.
    *
    * @param valid the new valid
    */
   public void setValid(boolean valid)
   {
      this.valid = valid;
   }

   /**
    * Gets the father bundle.
    *
    * @return the father bundle
    */
   public BaseBundle getFatherBundle()
   {
      return fatherBundle;
   }

   /**
    * Sets the father bundle.
    *
    * @param fatherBundle the fatherBundle to set
    */
   public void setFatherBundle(BaseBundle fatherBundle)
   {
      this.fatherBundle = fatherBundle;
   }

   /**
    * Gets the bundle returned value.
    *
    * @param val the returned value
    */
   public void setValue(Object val)
   {
      value = val;
   }

   /**
    * Gets the bundle returned value.
    *
    * @return the returned value
    */
   public Object getValue()
   {
      return value;
   }

   /**
    * Gets the children.
    *
    * @return the children
    */
   public List<BaseBundle> getChildren()
   {
      return children;
   }

   /**
    * Sets the children.
    *
    * @param children the children to set
    */
   public void setChildren(List<BaseBundle> children)
   {
      this.children = children;
   }

   /**
    * Adds the child.
    *
    * @param child the child to add
    */
   public void addChild(BaseBundle child)
   {
      this.children.add(child);
   }

   //-------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------
   
   /**
    * Merge children returned values.
    *
    * @return the object
    */
   protected Object mergeChildrenReturnedValues()
   {
      if (children.size() == 1)
      {
         value = children.get(0).getValue();
      }
      else if (!children.isEmpty())
      {
         value = getParameters();
      }
      return value;
   }
   
   /**
    * Gets the parameters.
    *
    * @return the parameters
    */
   protected List<Object> getParameters()
   {
      List<Object> compound = new ArrayList<>();
      children.forEach((child) -> compound.add(child.getValue()));
      return compound;
   }
   
   /**
    * Action to be done during startElement part.
    * 
    * @param handler the handler
    * @param namespaceURI the namespace name
    * @param localName the local name
    * @param qName the qname
    * @param atts the attributes
    * @return the list of errors
    */
   public List<String> manageStartElement(Handler handler,
         String namespaceURI,
         String localName,
         String qName, 
         Attributes atts)
   {
      varName = atts.getValue("_put");
      return startElement(handler, namespaceURI, localName, qName, atts);
   }

   /**
    * Actions to do when ending an element.
    *
    * @param handler the handler
    * @param uri the uri
    * @param localName the local name
    * @param qName the q name
    * @return the list of errors
    */
   public List<String> manageEndElement(Handler handler, String uri, String localName,
         String qName)
   {
      List<String> ret = endElement(handler, uri, localName, qName);
      if (varName != null)
      {
         handler.getManager().setVariable(varName, getValue());
      }
      return ret;
   }
   
   /**
    * Gets the current object. The object is the value, or the current object of father if null.
    *
    * @return the current object
    */
   public Object getCurrentObject()
   {
      if (value != null)
      {
         return value;
      }
      else if (fatherBundle != null)
      {
         return fatherBundle.getCurrentObject();
      }
      else
      {
         // Bad luck !
         return null;
      }
   }
   
   /**
    * Action to be done during startElement part.
    * 
    * @param handler the handler
    * @param namespaceURI the namespace name
    * @param localName the local name
    * @param qName the qname
    * @param atts the attributes
    * @return the list of errors
    */
   protected abstract List<String> startElement(Handler handler,
         String namespaceURI,
         String localName,
         String qName, 
         Attributes atts);

   /**
    * Actions to do when ending an element.
    *
    * @param handler the handler
    * @param uri the uri
    * @param localName the local name
    * @param qName the q name
    * @return the list of errors
    */
   protected abstract List<String> endElement(Handler handler, String uri, String localName,
         String qName);

}
