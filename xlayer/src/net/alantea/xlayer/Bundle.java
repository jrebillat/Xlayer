package net.alantea.xlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Bundle to store information for a parsing level.
 */
public class Bundle
{
   
   /** The current object. */
   private Object object;
   
   /** The method name. */
   private String methName;
   
   /** If we are in a method. */
   private String inMethod;
   
   /** The additionnal info. */
   private String addition;
   
   /** The parameters for method. */
   private List<Object> parms = new ArrayList<>();
   
   /** Whether the bundle concerns a valid information */
   private boolean valid;
   
   /**
    * Instantiates a new bundle.
    *
    * @param object the current object
    * @param methName the method name
    * @param inMethod name of a method
    * @param parms The parameters for method
    */
   Bundle()
   {
      setValid(true);
   }

   /**
    * Checks if we are in a method.
    *
    * @return the inMethod
    */
   public String isInMethod()
   {
      return inMethod;
   }

   /**
    * Gets the parameters.
    *
    * @return the parameters
    */
   public List<Object> getParms()
   {
      return parms;
   }

   /**
    * Adds the parameters.
    *
    * @param parm the parameter to add
    */
   public void addParm(Object parm)
   {
      this.parms.add(parm);
   }

   /**
    * Clears the parameters.
    *
    * @param parm the parameter to add
    */
   public void clearParms()
   {
      this.parms.clear();
   }

   /**
    * Gets the current object.
    *
    * @return the current object
    */
   public Object getObject()
   {
      return object;
   }

   /**
    * Gets the method name.
    *
    * @return the method name
    */
   public String getMethodName()
   {
      return methName;
   }

   /**
    * @param methName the methName to set
    */
   public void setMethodName(String methName)
   {
      this.methName = methName;
   }

   /**
    * @return the inMethod
    */
   public String getInMethod()
   {
      return inMethod;
   }

   /**
    * @param inMethod the inMethod to set
    */
   public void setInMethod(String inMethod)
   {
      this.inMethod = inMethod;
   }

   /**
    * @param object the object to set
    */
   public void setObject(Object object)
   {
      this.object = object;
   }

   /**
    * Checks if is valid.
    *
    * @return true, if is valid
    */
   public boolean isValid()
   {
      return valid;
   }

   /**
    * Sets the validity.
    *
    * @param valid the new validity
    */
   public void setValid(boolean valid)
   {
      this.valid = valid;
   }

   /**
    * Gets the addition.
    *
    * @return the addition
    */
   public String getAddition()
   {
      return addition;
   }

   /**
    * Sets the addition.
    *
    * @param addition the new addition
    */
   public void setAddition(String addition)
   {
      this.addition = addition;
   }
}