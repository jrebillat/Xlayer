package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;
import net.alantea.xlayer.MethodReturnedInformation;

/**
 * Bundle to handle methods.
 */
public class MethodBundle extends BaseBundle
{

   /**
    * Instantiates a new method bundle.
    *
    * @param father the father
    */
   public MethodBundle(BaseBundle father)
   {
      super(father);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      // nothing done at start : we need all parameters - the children - before calling the method
      return new ArrayList<>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<>();
      
      // search for method
      String methName = Manager.searchMethod(getCurrentObject(), localName, -1);
      
      // apply it
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
