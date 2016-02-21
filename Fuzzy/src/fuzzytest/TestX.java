package fuzzytest;

import net.alantea.fuzzy.comp.FuzzyNetwork;
import net.alantea.fuzzy.equation.FuzzyEquation;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyVariable;

public class TestX
{
   // Basic test for Fuzzy classes
   public static void main(String[] args)
   {
      try
      {
         /*
         FuzzyVariable input = new FuzzyVariable();
         FuzzyNetwork net = new FuzzyNetwork();
         net.addValue("input", input);
         net.readFile("ess01.fzy");

         for (double v = 0.; v <= 20.; v += 0.5)
         {
            input.setValue(v);
            System.out.println("v=" + v + ", out="
                  + net.getDoubleValue("myset"));
         }
         System.out.println("==========================================");
         
         net = new FuzzyNetwork();
         FuzzyVariable i1 = new FuzzyVariable();
         FuzzyVariable i2 = new FuzzyVariable();
         net.addValue("i1", i1);
         net.addValue("i2", i2);
         net.readFile("ess02.fzy");
         for (double v = 0.; v <= 4.; v += 0.5)
         {
            i1.setValue(v);
            for (double w = 0.; w <= 4.; w += 0.5)
            {
               i2.setValue(w);
               System.out.println("v=" + v + ", w=" + w + ", out="
                     + net.getDoubleValue("myset"));
            }
         }
         System.out.println("==========================================");
         */
         FuzzyNetwork net = new FuzzyNetwork();
         FuzzyVariable i01 = new FuzzyVariable();
         net.addValue("i1", i01);
         i01.setValue(1);
         //net.addValue("i2", new FuzzyEquation(net, "test[i1, 1.0]"));
         net.addValue("i2", new FuzzyEquation(net, "cosinusCondition[i1, 3.0, 2.0]"));
         net.readFile("ess02.fzy");
         for (double v = 0.1; v <= 6; v += 0.5)
         {
            i01.setValue(v);
            System.out.println("v=" + v + ", w=" + net.getDoubleValue("i2") + ", out="
                     + net.getDoubleValue("myset"));
         }
      }
      catch (FuzzyException e)
      {
         e.printStackTrace();
      }
   }
}
