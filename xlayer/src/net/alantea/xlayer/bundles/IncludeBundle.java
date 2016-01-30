package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

public class IncludeBundle extends BaseBundle
{

   public IncludeBundle(BaseBundle father)
   {
      super(father);
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName, Attributes atts)
   {
      String filePath = atts.getValue("path");
      String fileType = atts.getValue("type");
      List<String> errors = null;
      if ((fileType != null) && ("file".equals(fileType.toLowerCase())))
      {
         errors = Manager.parseHandledFile(handler, filePath);
      }
      else
      {
         errors = Manager.parseHandledResource(handler, filePath);
      }
      
      if (errors == null)
      {
         errors = new ArrayList<String>();
         errors.add("Bad include call");
      }
      return errors;
   }

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // nothing to be done
      return null;

   }
}
