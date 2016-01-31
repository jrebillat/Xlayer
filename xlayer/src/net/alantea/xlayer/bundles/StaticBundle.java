package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.util.ClassUtils;
import net.alantea.xlayer.util.MethodReturnedInformation;
import net.alantea.xlayer.util.MethodUtils;

/**
 * Bundle to handle static methods in classes.
 */
public class StaticBundle extends BaseBundle
{
   
   private String methName;
   
   private String className;

   /**
    * Instantiates a new bundle.
    *
    * @param father the father
    */
   public StaticBundle(BaseBundle father)
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

      // Get method name
      methName = atts.getValue("name");
      if (methName == null)
      {
         setValid(false);
         errors.add("Invalid static method definition : no method name given.");
      }
      else
      {
         // get class
         className = atts.getValue("class");
         if (className == null)
         {
            setValid(false);
            errors.add("Invalid static method definition : no class name given.");
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
      List<String> errors = new ArrayList<String>();

         // Search for class
         Class<?> cl = ClassUtils.searchClass("", className);
         if (cl == null)
         {
            setValid(false);
            errors.add("Invalid static method definition : class  " + className + " not found.");
         }
         else
         {
            List<Object> objects = getParameters();
            MethodReturnedInformation retInfo = MethodUtils.applyMethod(cl, methName, objects);
            if (retInfo.getErrorMessage() == null)
            {
               setValue(retInfo.getValue());
            }
            else
            {
               errors.add(retInfo.getErrorMessage());
            }
         }
         return errors;
   }

}
