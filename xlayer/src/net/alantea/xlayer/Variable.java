package net.alantea.xlayer;

/**
 * The Class Variable.
 */
public class Variable
{
   
   /** The name. */
   String name;
   
   /** The object. */
   Object object;
   
   /**
    * Content.
    *
    * @param object the content
    */
   public void content(Object object)
   {
      this.object = object;
   }

   /**
    * Gets the content.
    *
    * @return the content
    */
   public Object getContent()
   {
      return object;
   }
}
