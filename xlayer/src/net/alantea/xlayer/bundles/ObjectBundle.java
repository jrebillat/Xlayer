package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.util.MethodUtils;
import net.alantea.xlayer.util.PrimitiveUtils;

/**
 * The Class ObjectBundle.
 */
public class ObjectBundle extends BaseBundle
{
   
   /** Indicate whether we have a reserved object (base type or derived). */
   private boolean reserved;
   
   /** The content. */
   private Object content;
   
   /**
    * Instantiates a new object bundle.
    *
    * @param father the father
    */
   public ObjectBundle(BaseBundle father)
   {
      super(father);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   protected List<String> startElement(Handler handler, String namespace, String localName, String qName,
         Attributes atts)
   {
      List<String> errors = new ArrayList<>();
      
      // Search for object class
      String className = localName;
      String classAttr = atts.getValue("_class");
      if (classAttr != null)
      {
         // search for nase and derived classes
         Class<?> baseClass = handler.getClassUtils().searchClass(namespace, localName);
         Class<?> derivedClass = handler.getClassUtils().searchClass(namespace, classAttr);
         if (baseClass == null)
         {
            this.setValid(false);
            errors.add("Invalid object definition : class " + localName + " not reachable");
            return errors;
         }
         else if (derivedClass == null)
         {
            this.setValid(false);
            errors.add("Invalid object definition : class " + classAttr + " not reachable");
            return errors;
         }
         // verify that the derived class is correct
         else if (!baseClass.isAssignableFrom(derivedClass))
         {
            this.setValid(false);
            errors.add("Invalid object definition : class " + localName + " is not assignable from " + classAttr);
            return errors;
         }
         // substitute class name
         className = classAttr;
      }
      
      content = null;
      // If not reserved, we can already instantiate the content
      if ((PrimitiveUtils.verifyNotReserved(className)) && (PrimitiveUtils.verifyNotReservedContainer(className)))
      {
         content = handler.getManager().getInstance(namespace, className, atts);
      }
      else
      {
         // create value, just not to have null for children if they need their father class
         reserved = true;
         content = PrimitiveUtils.createReserved(className);
      }
      if (content == null)
      {
         errors.add("Invalid object definition : class " + localName);
      }
      else
      {
         // apply attributes as fields
         for (int i = 0; i < atts.getLength(); i++)
         {
            String key = atts.getLocalName(i);
            String val = atts.getValue(i);
            handler.getManager().setOrAddAttribute(content.getClass(), content, key, val);
         }
      }
      return errors;
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<>();
      
      // Reserved class : we need to recreate one.
      if (reserved)
      {
         Object o = mergeChildrenReturnedValues();
         if (o == null)
         {
            errors.add("Null value for " + localName);
            return errors;
         }
         
         // Create new value
         Object val = PrimitiveUtils.getSimpleObject(content.getClass(), o.toString());

         if (val == null)
         {
            errors.add("Bad value '" 
               + mergeChildrenReturnedValues().toString() 
               + "', impossible to convert to " 
               + content.getClass().getSimpleName());
         }
         else
         {
            content = val;
            setValue(val);
         }
      }
      else if (content != null)
      {
         // just set content as value
         setValue(content);
      }
      else if (this.children.size() == 1)
      {
         // set first child as value
         setValue(children.get(0).getCurrentObject());
      }
      
      // add it in father if they both are objects
      if (getClass().equals(ObjectBundle.class) && (getFatherBundle().getClass().equals(ObjectBundle.class)))
      {
         MethodUtils.addObjectInObject(((ObjectBundle)getFatherBundle()).getCurrentObject(),
        		 (content == null) ? getValue() : content);
      }
      // Add it in father if it is a non-null root
      else if (getClass().equals(ObjectBundle.class) 
            && (getFatherBundle().getClass().equals(RootBundle.class))
            && (getFatherBundle().getValue() != null))
      {
         MethodUtils.addObjectInObject(getFatherBundle().getValue(), content);
      }
      return errors;
   }
   
   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#getCurrentObject()
    */
   @Override
   public Object getCurrentObject()
   {
      if ((content != null) && (PrimitiveUtils.verifyNotReservedContainer(content.getClass().getSimpleName())))
      {
         return content;
      }
      else
      {
         return getFatherBundle().getCurrentObject(); 
      }
   }
   
   /**
    * Checks if is reserved.
    *
    * @return true, if is reserved
    */
   protected boolean isReserved()
   {
      return reserved;
   }

}
