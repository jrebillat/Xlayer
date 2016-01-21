package net.alantea.xlayer.test;

/**
 * The Class BigRoot.
 */
public class BigRoot
{
   
   /** The simple integer wrapper. */
   private SimpleIntegerWrapper simpleIntegerWrapper;
   
   /** The masqued integer wrapper. */
   private SetIntegerWrapper masquedIntegerWrapper;
   
   /** The simple string wrapper. */
   public SimpleStringWrapper simpleStringWrapper;
   
   /** The integers wrapper. */
   private IntegersWrapper integersWrapper;

   /**
    * Gets the simple integer wrapper.
    *
    * @return the simple integer wrapper
    */
   public SimpleIntegerWrapper getSimpleIntegerWrapper()
   {
      return simpleIntegerWrapper;
   }

   /**
    * Sets the sets the integer wrapper.
    *
    * @param wrapper the new sets the integer wrapper
    */
   public void setSetIntegerWrapper(SetIntegerWrapper wrapper)
   {
      masquedIntegerWrapper = wrapper;
   }

   /**
    * Gets the sets the integer wrapper.
    *
    * @return the sets the integer wrapper
    */
   public SetIntegerWrapper getSetIntegerWrapper()
   {
      return masquedIntegerWrapper;
   }

   /**
    * Gets the integers.
    *
    * @return the integers
    */
   public IntegersWrapper getIntegers()
   {
      return integersWrapper;
   }

}
