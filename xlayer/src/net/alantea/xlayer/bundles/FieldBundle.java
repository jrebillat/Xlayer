package net.alantea.xlayer.bundles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

public class FieldBundle extends ObjectBundle
{
   Field field;
   String className;
   
   public FieldBundle(BaseBundle father, String fieldName)
   {
      super(father);
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      List<String> ret = new ArrayList<>();
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

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> ret = new ArrayList<>();

      if ((getChildren().size() == 1) && (ConstantBundle.class.isAssignableFrom(getChildren().get(0).getClass())))
      {
         setValue(((ConstantBundle)getChildren().get(0)).getValue());
      }
      else
      {
         ret.addAll(super.endElement(handler, uri, className, qName));
      }
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
