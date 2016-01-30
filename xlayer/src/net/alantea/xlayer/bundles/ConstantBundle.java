package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;
import net.alantea.xlayer.Manager;

public class ConstantBundle extends BaseBundle
{

   public ConstantBundle(BaseBundle father)
   {
      super(father);
   }

   @Override
   protected List<String> startElement(Handler handler, String namespaceURI, String localName, String qName,
         Attributes atts)
   {
      List<String> errors = new ArrayList<String>();

      String cstName = atts.getValue("name");
      if (cstName == null)
      {
         setValid(false);
         errors.add("Invalid constant definition : no constant name given.");
      }
      else
      {
         String cstClass = atts.getValue("class");
         if (cstClass == null)
         {
            setValid(false);
            errors.add("Invalid constant definition : no class name given.");
         }
         else
         {
            Class<?> cl = Manager.searchClass("", cstClass);
            if (cl == null)
            {
               setValid(false);
               errors.add("Invalid constant definition : class  " + cstClass + " not found.");
            }
            else
            {
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

   @Override
   protected List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // all has been already done
      return new ArrayList<String>();
   }

}
