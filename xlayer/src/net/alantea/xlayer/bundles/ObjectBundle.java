package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

public class ObjectBundle extends BaseBundle
{
   private boolean reserved;
   
   private Object content;
   
   public ObjectBundle(BaseBundle father)
   {
      super(father);
   }

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
         Class<?> baseClass = Manager.searchClass(namespace, localName);
         Class<?> derivedClass = Manager.searchClass(namespace, classAttr);
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
         else if (!baseClass.isAssignableFrom(derivedClass))
         {
            this.setValid(false);
            errors.add("Invalid object definition : class " + localName + " is not assignable from " + classAttr);
            return errors;
         }
         className = classAttr;
      }
      
      content = null;
      if ((Manager.verifyNotReserved(className)) && (Manager.verifyNotReservedContainer(className)))
      {
         content = Manager.getInstance(namespace, className, atts);
      }
      else
      {
         reserved = true;
         content = Manager.createReserved(className);
      }
      if (content == null)
      {
         errors.add("Invalid object definition : class " + localName);
      }
      else
      {
         // apply attributes
         for (int i = 0; i < atts.getLength(); i++)
         {
            String key = atts.getLocalName(i);
            String val = atts.getValue(i);
            Manager.setOrAddAttribute(content.getClass(), content, key, val);
         }
      }
      return errors;
   }

   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<>();
      if (reserved)
      {
         Object o = mergeChildrenReturnedValues();
         if (o == null)
         {
            errors.add("Null value for " + localName);
            return errors;
         }
         
         Object val = Manager.getSimpleObject(content.getClass(), o.toString());

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
      else if (getClass().equals(ObjectBundle.class) && (getChildren().size() >= 1))
      {
         for (BaseBundle bundle : getChildren())
         {
            if (bundle.getClass().equals(ObjectBundle.class))
            {
               Manager.addObjectInObject(content, ((ObjectBundle)bundle).content);
            }
         }
         setValue(content);
      }
      else
      {
         setValue(content);
      }
      return errors;
   }
   
   @Override
   public Object getCurrentObject()
   {
      if (Manager.verifyNotReservedContainer(content.getClass().getSimpleName()))
      {
         return content;
      }
      else
      {
         return getFatherBundle().getCurrentObject(); 
      }
   }
   
   protected boolean isReserved()
   {
      return reserved;
   }

}
