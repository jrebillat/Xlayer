package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;
import net.alantea.xlayer.MethodReturnedInformation;

public class MethodBundle extends BaseBundle
{
   
   public MethodBundle(BaseBundle father)
   {
      super(father);
      // TODO Auto-generated constructor stub
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      return new ArrayList<>();
   }

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<>();
      String methName = Manager.searchMethod(getCurrentObject(), localName, -1);
      MethodReturnedInformation ret = Manager.applyMethod(getCurrentObject(), methName, getParameters());
      if (!ret.isSuccess())
      {
         errors.add(ret.getErrorMessage());
      }
      else
      {
         // store result for upper usage
         if (ret.isNonvoid())
         {
            if ( ret.getValue() != null)
            {
               setValue(ret.getValue());
            }
         }
      }
      return errors;
   }

}
