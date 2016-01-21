package net.alantea.xlayer;

public class MethodReturnedInformation
{
   private boolean success;
   private boolean nonvoid;
   private Object value;
   
   /**
    * @return the success
    */
   public boolean isSuccess()
   {
      return success;
   }
   /**
    * @param success the success to set
    */
   public void setSuccess(boolean success)
   {
      this.success = success;
   }
   /**
    * @return the nonvoid
    */
   public boolean isNonvoid()
   {
      return nonvoid;
   }
   /**
    * @param nonvoid the nonvoid to set
    */
   public void setNonvoid(boolean nonvoid)
   {
      this.nonvoid = nonvoid;
   }
   /**
    * @return the value
    */
   public Object getValue()
   {
      return value;
   }
   /**
    * @param value the value to set
    */
   public void setValue(Object value)
   {
      this.value = value;
   }
}
