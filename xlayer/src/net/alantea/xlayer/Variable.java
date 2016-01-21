package net.alantea.xlayer;

public class Variable
{
   String name;
   Object object;
   
   public void content(Object object)
   {
      this.object = object;
   }

   public Object getContent()
   {
      return object;
   }
}
