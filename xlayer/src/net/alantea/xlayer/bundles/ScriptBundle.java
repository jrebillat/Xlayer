package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.proxy.ScriptedProxy;
import net.alantea.xlayer.util.ClassUtils;

/**
 * Bundle to handle scripts.
 */
public class ScriptBundle extends BaseBundle
{
   
   /** The 'as' interface name. */
   private String asInterface;

   /**
    * Instantiates a new script bundle.
    *
    * @param father the father
    */
   public ScriptBundle(BaseBundle father)
   {
      super(father);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      // get attribute, if any - to be used for proxy interface scripting
      asInterface = atts.getValue("as");
      return new ArrayList<>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // Construct script string.
      String script = (String) mergeChildrenReturnedValues();

      // Simple scripting
      if (asInterface == null)
      {
         Object obj = ScriptedProxy.launch(handler, script);
         setValue(obj);
      }
      // Proxy management
      else
      {
         Object obj = new ScriptedProxy(script).as(ClassUtils.searchClass("", asInterface));
         setValue(obj);
      }
      return null;
   }

}
