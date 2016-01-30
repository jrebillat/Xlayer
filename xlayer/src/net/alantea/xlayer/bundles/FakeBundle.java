package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

public class FakeBundle extends BaseBundle
{

   public FakeBundle(BaseBundle father)
   {
      super(father);
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
      setValue(mergeChildrenReturnedValues());
      return new ArrayList<String>();
   }

}
