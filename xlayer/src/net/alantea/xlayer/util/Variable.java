package net.alantea.xlayer.util;

/**
 * The Class Variable.
 */
public class Variable
{
   
   /** The name. */
   private String name;
   
   /** The object. */
   private Object object;
   
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

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }
}
