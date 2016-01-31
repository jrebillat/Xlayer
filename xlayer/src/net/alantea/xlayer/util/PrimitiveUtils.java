package net.alantea.xlayer.util;

/**
 * Reflection Utilities.
 */
public final class PrimitiveUtils
{
   
   /**
    * Do not instantiates.
    */
   private PrimitiveUtils()
   {
   }

   /**
    * Test if class is convertible in a primitive type.
    *
    * @param clazz the class to analyze
    * @return true, if successful
    */
   public static boolean testPrimitive(Class<?> clazz)
   {
      if (clazz.isPrimitive())
      {
         return true;
      }
      else if (Number.class.isAssignableFrom(clazz))
      {
         return true;
      }
      else if (Boolean.class.isAssignableFrom(clazz))
      {
         return true;
      }
      else if (String.class.isAssignableFrom(clazz))
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   /**
    * Creates a reserved class instance.
    *
    * @param className the class name
    * @return the object
    */
   public static Object createReserved(String className)
   {
      switch (className)
      {
         case "int":
         case "integer":
            return new Integer(0);
         case "boolean":
            return new Boolean(true);
         case "long":
            return new Long(0);
         case "float":
            return new Float(0);
         case "byte":
            return new Byte((byte) 0);
         case "short":
            return new Short((short) 0);
         case "String":
            return new String("");
         case "double":
            return new Double(0);
         default:
            return null;
      }
   }

   /**
    * Verify that the class is not reserved.
    *
    * @param className the class name
    * @return true, if successful
    */
   public static boolean verifyNotReserved(String className)
   {
      switch (className)
      {
         case "int":
         case "integer":
         case "boolean":
         case "long":
         case "float":
         case "byte":
         case "short":
         case "String":
         case "double":
            return false;

         default:
            return true;
      }
   }

   /**
    * Verify the class is not a reserved container.
    *
    * @param className the class name
    * @return true, if successful
    */
   public static boolean verifyNotReservedContainer(String className)
   {
      switch (className)
      {
         case "Integer":
         case "Boolean":
         case "Long":
         case "Float":
         case "Byte":
         case "Short":
         case "String":
         case "Double":
            return false;

         default:
            return true;
      }
   }
   
   /**
    * Gets a simple object from a String.
    *
    * @param clazz the class for the returned object
    * @param value the value as a String
    * @return the simple object parsed from the string, or null
    */
   public static Object getSimpleObject(Class<?> clazz, String value)
   {
      Object parm = null;
   try
   {
      if (clazz == Long.class || clazz == long.class)
      {
         parm = Long.parseLong(value);
      }
      else if (clazz == Float.class || clazz == float.class)
      {
         parm = Float.parseFloat(value);
      }
      else if (clazz == Integer.class || clazz == int.class)
      {
         parm = Integer.parseInt(value);
      }
      else if (clazz == Double.class || clazz == double.class)
      {
         parm = Double.parseDouble(value);
      }
      else if (clazz == Boolean.class || clazz == boolean.class)
      {
         parm = Boolean.parseBoolean(value);
      }
      else if (clazz == Byte.class || clazz == byte.class)
      {
         parm = Byte.parseByte(value);
      }
      else if (clazz == Short.class || clazz == short.class)
      {
         parm = Short.parseShort(value);
      }
      else if (clazz == Boolean.class || clazz == boolean.class)
      {
         parm = Boolean.parseBoolean(value);
      }
      else if (clazz == String.class)
      {
         parm = value;
      }
   }
   catch (NumberFormatException ex)
   {
      // bad format. Return null.
   }
      return parm;
   }
}
