package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Root Bundle.
 */
public class RootBundle extends BaseBundle
{
   
   /**
    * Instantiates a new root bundle.
    *
    * @param root the root object to set, or null
    */
   public RootBundle(Object root)
   {
      super(null);
      setValue(root);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName, Attributes atts)
   {
      // Should never been called
      return new ArrayList<String>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // Should never been called
      return new ArrayList<String>();
   }
}
