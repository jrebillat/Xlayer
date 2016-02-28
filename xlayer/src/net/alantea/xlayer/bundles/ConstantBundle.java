package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.util.ClassUtils;

// TODO: Auto-generated Javadoc
/**
 * Bundle to handle constants or Enum values access in classes.
 */
public class ConstantBundle extends BaseBundle
{

   /**
    * Instantiates a new constant bundle.
    *
    * @param father the father
    */
   public ConstantBundle(BaseBundle father)
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
      List<String> errors = new ArrayList<String>();

      // Get constant name
      String cstName = atts.getValue("name");
      if (cstName == null)
      {
         setValid(false);
         errors.add("Invalid constant definition : no constant name given.");
      }
      else
      {
         // get constant class
         String cstClass = atts.getValue("class");
         if (cstClass == null)
         {
            setValid(false);
            errors.add("Invalid constant definition : no class name given.");
         }
         else
         {
            // Search for class
            Class<?> cl = ClassUtils.searchClass("", cstClass);
            if (cl == null)
            {
               setValid(false);
               errors.add("Invalid constant definition : class  " + cstClass + " not found.");
            }
            else
            {
               // Get correct value
               try
               {
                  setValue(cl.getField(cstName).get(null));
               }
               catch (NoSuchFieldException e)
               {
                  setValid(false);
                  errors.add("Invalid constant name : " + cstName + " in " + cstClass);
               }
               catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
               {
                  setValid(false);
                  errors.add("Invalid constant definition : class " + cstClass + " generates exception.");
               }
            }
         }
      }
      return errors;
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // all has been already done
      return new ArrayList<String>();
   }

}
