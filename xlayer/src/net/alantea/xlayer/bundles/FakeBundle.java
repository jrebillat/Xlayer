package net.alantea.xlayer.bundles;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import net.alantea.xlayer.Handler;

// TODO: Auto-generated Javadoc
/**
 * Fake Bundle. Does nothing and disappear at the end, giving all its children to its father.
 */
public class FakeBundle extends BaseBundle
{

   /**
    * Instantiates a new fake bundle.
    *
    * @param father the father
    */
   public FakeBundle(BaseBundle father)
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
      // nothing to do.
      return new ArrayList<String>();
   }

   /* (non-Javadoc)
    * @see net.alantea.xlayer.bundles.BaseBundle#endElement(net.alantea.xlayer.Handler, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public List<String> endElement(Handler handler, String uri, String localName, String qName)
   {
      // Remove me from the children of my father
      List<BaseBundle> brothers = getFatherBundle().getChildren();
      brothers.remove(this);
      
      // put all my children in father list
      getChildren().forEach((child) -> getFatherBundle().addChild(child));
      return new ArrayList<String>();
   }

}
