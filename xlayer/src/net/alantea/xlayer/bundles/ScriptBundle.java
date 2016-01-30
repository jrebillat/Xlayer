package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;
import net.alantea.xlayer.ScriptedProxy;

public class ScriptBundle extends BaseBundle
{
   private String asInterface;

   public ScriptBundle(BaseBundle father)
   {
      super(father);
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      asInterface = atts.getValue("as");
      return new ArrayList<>();
   }

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      String script = (String) mergeChildrenReturnedValues();
      if (asInterface == null)
      {
         Object obj = ScriptedProxy.launch(handler, script);
         setValue(obj);
      }
      else
      {
         Object obj = new ScriptedProxy(script).as(Manager.searchClass("", asInterface));
         setValue(obj);
      }
      return null;
   }

}
