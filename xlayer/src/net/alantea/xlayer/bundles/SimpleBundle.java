package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

public class SimpleBundle extends BaseBundle
{

   public SimpleBundle(BaseBundle father, Object val)
   {
      super(father);
      setValue(val);
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      return new ArrayList<String>();
   }

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      return new ArrayList<String>();
   }

}
