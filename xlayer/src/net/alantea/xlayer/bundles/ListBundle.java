package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

public class ListBundle extends BaseBundle
{

   public ListBundle(BaseBundle father)
   {
      super(father);
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
      List<Object> ret = new ArrayList<>();
      children.forEach((child) -> ret.add(child.getValue()));
      setValue(ret);
      return new ArrayList<>();
   }


}
