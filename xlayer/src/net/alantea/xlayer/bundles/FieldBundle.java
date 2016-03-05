package net.alantea.xlayer.bundles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

/**
 * Bundle to manage fields. It inherits from ObjectBundle.
 */
public class FieldBundle extends ObjectBundle
{
   
   /** The field. */
   Field field;
   
   /** The class name. */
   String className;
   
   /**
    * Instantiates a new field bundle.
    *
    * @param father the father
    */
   public FieldBundle(BaseBundle father)
   {
      super(father);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.ObjectBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      List<String> ret = new ArrayList<>();
      
      // search for field
      field = Manager.searchField(getFatherBundle().getCurrentObject(), localName);
      if (field == null)
      {
         ret.add("Unknown field : " + localName);
      }
      else
      {
         className = field.getGenericType().getTypeName();
         ret.addAll(super.startElement(handler, namespaceURI, className, qName, atts));
      }
      return ret;
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.ObjectBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> ret = new ArrayList<>();

	  // nothing already set
      if (getValue() == null)
      {
    	  // We have one constant child : it contains the value to set
    	  if ((getChildren().size() == 1) && (ConstantBundle.class.isAssignableFrom(getChildren().get(0).getClass())))
    	  {
    		  setValue(((ConstantBundle)getChildren().get(0)).getValue());
    	  }
    	  // One element or nothing given : set or create an object from the class
    	  else if (getChildren().size() <= 1)
    	  {
    		  ret.addAll(super.endElement(handler, uri, className, qName));
    	  }
    	  else
    	  {
    		  ret.add("Bad number of elements : " + getChildren().size() + ", should be 0 or 1 for " + localName);
    		  return ret;
    	  }
      }
      
      // set value in field
      Object val = getValue();
      try
      {
         field.setAccessible(true);
         Manager.setOrAddAttribute(getFatherBundle().getCurrentObject().getClass(), getFatherBundle().getCurrentObject(), field.getName(), val);
      }
      catch (IllegalArgumentException e)
      {
         ret.add("Bad field value class for field " + field.getName() + " in " + getFatherBundle().getCurrentObject().getClass().getSimpleName() );
      }
      return ret;
   }
}
