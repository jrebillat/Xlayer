package net.alantea.xlayer.test;

import java.util.List;

/**
 * The Class BigRoot.
 */
public class MethodsRoot
{
   public int intResult;
   public String strResult;

   void withOneParameter(int value)
   {
      intResult = value;
   }
   
   void withTwoParameters(int value1, int value2)
   {
      intResult = value1 + value2;
   }
   
   void withThreeParameters(String text1, String text2, String text3)
   {
      strResult = text1 + " " + text2 + " " + text3;
   }
   
   void withList(List<String> texts)
   {
      strResult = "";
      for (String text : texts)
      {
         strResult += text;
      }
   }
   
   void withIntAndList(int val, List<String> texts)
   {
      strResult = "" + val;
      for (String text : texts)
      {
         strResult += " " + text;
      }
   }
   
   int withReturn()
   {
      return 12;
   }
}
