package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Bundle to handle variable setting in the file.
 */
public class EqualsVariableBundle extends BaseBundle
{
   
   /** The variable name. */
   private String varName;

   /**
    * Instantiates a new bundle.
    *
    * @param father the father
    */
   public EqualsVariableBundle(BaseBundle father)
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
      // store variable value (it may be define later, but must exist before ending element).
      varName = atts.getValue("_variable");
      return new ArrayList<String>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      List<String> errors = new ArrayList<String>();
      
      // set value
      if (varName != null)
      {
         setValue(handler.getManager().getVariable(varName));
      }
      else
      {
         setValid(false);
         errors.add("Invalid variable definition : no name given.");
      }
      return errors;
   }

}
