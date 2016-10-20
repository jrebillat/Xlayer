package net.alantea.xlayer;

import java.util.List;

/**
 * Run clas. Used to run a Xlayer file.
 */
public class Run
{
   
   /**
    * The main method.
    *
    * @param args the arguments
    */
   public static void main(String[] args) {
      if (args.length == 1)
      {
         List<String> errors = new Manager().parseFile(args[0]);
         if (!errors.isEmpty())
         {
            for (String error : errors)
            {
               System.out.println(error);
            }
         }
      }
      else
      {
         System.out.println("Bad parameter count. Usage : Run <Xlayer file path>");
      }
   }
}
