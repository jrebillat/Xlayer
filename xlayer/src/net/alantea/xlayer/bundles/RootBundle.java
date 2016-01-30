package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

public class RootBundle extends BaseBundle
{
   public RootBundle(Object root)
   {
      super(null);
      setValue(root);
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName, Attributes atts)
   {
      // Should never been called
      return new ArrayList<String>();
   }

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // Should never been called
      return new ArrayList<String>();
   }
}
