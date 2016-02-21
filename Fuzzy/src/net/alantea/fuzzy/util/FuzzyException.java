package net.alantea.fuzzy.util;

public class FuzzyException extends Exception
{
   static final long serialVersionUID = 130361;

   @SuppressWarnings("unused")
  private FuzzyException()
   {
   }

   public FuzzyException(String message)
   {
      super(message);
   }

   public FuzzyException(Throwable cause)
   {
      super(cause);
   }

   public FuzzyException(String message, Throwable cause)
   {
      super(message, cause);
   }

}
