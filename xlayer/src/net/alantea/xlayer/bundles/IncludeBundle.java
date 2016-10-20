package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Bundle to manage file inclusion. Behave mostly like a fake bundle
 */
public class IncludeBundle extends FakeBundle
{

   /**
    * Instantiates a new include bundle.
    *
    * @param father the father
    */
   public IncludeBundle(BaseBundle father)
   {
      super(father);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName, Attributes atts)
   {
      // get needed attributes
      String filePath = atts.getValue("path");
      String fileType = atts.getValue("type");
      
      List<String> errors = null;
      if ((fileType != null) && ("file".equals(fileType.toLowerCase())))
      {
         // load a file
         errors = handler.getManager().parseHandledFile(handler, filePath);
      }
      else
      {
         // load a resource file
         errors = handler.getManager().parseHandledResource(handler, filePath);
      }
      
      if (errors == null)
      {
         errors = new ArrayList<String>();
         errors.add("Bad include call");
      }
      return errors;
   }
   
   // Note : method endElement from FakeBundle is used.
}
