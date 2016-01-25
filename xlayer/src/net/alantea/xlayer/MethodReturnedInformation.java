package net.alantea.xlayer;

/**
 * The Class MethodReturnedInformation.
 */
public class MethodReturnedInformation
{
   
   /** The success. */
   private boolean success;
   
   /** The nonvoid. */
   private boolean nonvoid;
   
   /** The value. */
   private Object value;
   
   /** The error message. */
   private String errorMessage;
   
   /**
    * Checks if is success.
    *
    * @return the success
    */
   public boolean isSuccess()
   {
      return success;
   }
   
   /**
    * Sets the success.
    *
    * @param success the success to set
    */
   public void setSuccess(boolean success)
   {
      this.success = success;
   }
   
   /**
    * Checks if is nonvoid.
    *
    * @return the nonvoid
    */
   public boolean isNonvoid()
   {
      return nonvoid;
   }
   
   /**
    * Sets the nonvoid.
    *
    * @param nonvoid the nonvoid to set
    */
   public void setNonvoid(boolean nonvoid)
   {
      this.nonvoid = nonvoid;
   }
   
   /**
    * Gets the value.
    *
    * @return the value
    */
   public Object getValue()
   {
      return value;
   }
   
   /**
    * Sets the value.
    *
    * @param value the value to set
    */
   public void setValue(Object value)
   {
      this.value = value;
   }
   
   /**
    * Gets the error message.
    *
    * @return the error message
    */
   public String getErrorMessage()
   {
      return errorMessage;
   }
   
   /**
    * Sets the error message.
    *
    * @param errorMessage the new error message
    */
   public void setErrorMessage(String errorMessage)
   {
      this.errorMessage = errorMessage;
   }
}
