package fuzzytest;

import net.alantea.fuzzy.comp.FuzzyRule;
import net.alantea.fuzzy.comp.FuzzySet;
import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.cond.FuzzyGaussianCondition;
import net.alantea.fuzzy.op.FuzzyAnd;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyVariable;

public class Test2
{
   // Basic test for Fuzzy classes
   public static void main(String[] args)
   {
      try
      {
         FuzzyVariable i1 = new FuzzyVariable();
         //FuzzyCondition bas1 = new FuzzyLinearCondition(i1, 0., 0., 1., 3.);
         //FuzzyCondition haut1 = new FuzzyLinearCondition(i1, 1., 3., 4., 4.);
         FuzzyCondition bas1 = new FuzzyGaussianCondition(i1, 0.5, 0.2);
         FuzzyCondition haut1 = new FuzzyGaussianCondition(i1, 3.5, 0.4);
         FuzzyVariable i2 = new FuzzyVariable();
         //FuzzyCondition bas2 = new FuzzyLinearCondition(i2, 0., 0., 1., 3.);
         //FuzzyCondition haut2 = new FuzzyLinearCondition(i2, 1., 3., 4., 4.);
         FuzzyCondition bas2 = new FuzzyGaussianCondition(i2, 0.5, 2.);
         FuzzyCondition haut2 = new FuzzyGaussianCondition(i2, 3.5, 4.);
         FuzzySet set = new FuzzySet();
         set.addRule(new FuzzyRule(new FuzzyAnd(bas1, bas2), 0.));
         set.addRule(new FuzzyRule(new FuzzyAnd(haut1, bas2), 10.));
         set.addRule(new FuzzyRule(new FuzzyAnd(bas1, haut2), 20.));
         set.addRule(new FuzzyRule(new FuzzyAnd(haut1, haut2), 100.));

         for (double v = 0.; v <= 4.; v += 0.5)
         {
            i1.setValue(v);
            for (double w = 0.; w <= 4.; w += 0.5)
            {
               i2.setValue(w);
               System.out.println("v=" + v + ", w=" + w + ", out="
                     + set.getValue());
            }
         }
      }
      catch (FuzzyException e)
      {
         e.printStackTrace();
      }
   }
}
