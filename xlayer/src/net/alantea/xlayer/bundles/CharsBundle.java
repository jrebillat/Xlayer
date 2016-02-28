package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Bundle class to manage characters input in xml file.
 */
public class CharsBundle extends BaseBundle
{

   /**
    * Instantiates a new chars bundle.
    *
    * @param father the father
    * @param content the characters content got from the file
    */
   public CharsBundle(BaseBundle father, String content)
   {
      super(father);
      setValue(content);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   protected List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      // all has been already done
      return new ArrayList<String>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // all has been already done
      return new ArrayList<String>();
   }

}
