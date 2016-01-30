package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

public class VariableBundle extends BaseBundle
{
   private String name;
   
   public VariableBundle(BaseBundle father)
   {
      super(father);
      // TODO Auto-generated constructor stub
   }

   @Override
   protected List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      name = atts.getValue("name");
      return new ArrayList<String>();
   }

   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<String>();
      if (name != null)
      {
         Manager.setVariable(name, mergeChildrenReturnedValues());
      }
      else
      {
         setValid(false);
         errors.add("Invalid variable definition : no name given.");
      }
      return errors;
   }

}
