package net.alantea.xlayer;

import java.util.List;

public class Run
{
   public static void main(String[] args) {
      if (args.length == 1)
      {
         List<String> errors = Manager.parseFile(args[0]);
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
