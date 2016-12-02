package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Bundle to manage variables.
 */
public class VariableBundle extends BaseBundle
{
   
   /** The variable name. */
   private String name;
   
   /**
    * Instantiates a new variable bundle.
    *
    * @param father the father
    */
   public VariableBundle(BaseBundle father)
   {
      super(father);
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#startElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
   @Override
   protected List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      // store name
      name = atts.getValue("name");
      return new ArrayList<String>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<String>();
      
      // Store variable
      if (name != null)
      {
         handler.getManager().setVariable(name, mergeChildrenReturnedValues());
      }
      else
      {
         setValid(false);
         errors.add("Invalid variable definition : no name given.");
      }
      return errors;
   }

}
