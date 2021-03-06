package net.alantea.xlayer.bundles;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

/**
 * Bundle to handle arrays.
 */
public class ArrayBundle extends BaseBundle
{

   /**
    * Instantiates a new list bundle.
    *
    * @param father the father
    */
   public ArrayBundle(BaseBundle father)
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
      return new ArrayList<>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // put all children values into an array and set it as value
      List<Object> ret = new ArrayList<>();
      children.forEach((child) -> ret.add(child.getValue()));
      // TODO change this if possible
      // we consider that all elements are the same class as first
      // Should instead calculate the common class for all elements
      Object[] arr = new Object[0];
      if (!ret.isEmpty())
      {
         arr = (Object[]) Array.newInstance(ret.get(0).getClass(), 0);
      }
      setValue(ret.toArray(arr));
      return new ArrayList<>();
   }


}
