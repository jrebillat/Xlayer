package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Simple Bundle, to encapsulate a value.
 */
public class SimpleBundle extends BaseBundle
{

   /**
    * Instantiates a new simple bundle. Used for avriable setting
    *
    * @param father the father
    * @param val the value to set
    */
   public SimpleBundle(BaseBundle father, Object val)
   {
      super(father);
      setValue(val);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      // Nothing to do
      return new ArrayList<String>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // Nothing to do
      return new ArrayList<String>();
   }

}
