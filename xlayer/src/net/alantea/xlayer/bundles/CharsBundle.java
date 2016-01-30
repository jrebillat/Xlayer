package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

public class CharsBundle extends BaseBundle
{

   public CharsBundle(BaseBundle father, String content)
   {
      super(father);
      setValue(content);
   }

   @Override
   protected List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      // all has been already done
      return new ArrayList<String>();
   }

   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // all has been already done
      return new ArrayList<String>();
   }

}
