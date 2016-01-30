package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

public class EqualsVariableBundle extends BaseBundle
{
   private String varAttr;

   public EqualsVariableBundle(BaseBundle father)
   {
      super(father);
   }

   @Override
   public List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      varAttr = atts.getValue("_variable");
      return new ArrayList<String>();
   }

   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<String>();
      if (varAttr != null)
      {
         setValue(Manager.getVariable(varAttr));
      }
      else
      {
         setValid(false);
         errors.add("Invalid variable definition : no name given.");
      }
      return errors;
   }

}
