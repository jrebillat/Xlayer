package fuzzytest;

import net.alantea.fuzzy.comp.FuzzyRule;
import net.alantea.fuzzy.comp.FuzzySet;
import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.cond.FuzzyLinearCondition;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyVariable;

public class Test1
{
// Basic test for Fuzzy classes
   public static void main(String[] args)
   {
      try
      {
      FuzzyVariable input = new FuzzyVariable();
      FuzzyCondition nulle = new FuzzyLinearCondition(input, 0.,0.,5.,7.);
      FuzzyCondition bas   = new FuzzyLinearCondition(input, 5.,7.,9.,10.);
      FuzzyCondition moyen = new FuzzyLinearCondition(input, 9.,10.,15.,17.);
      FuzzyCondition haut  = new FuzzyLinearCondition(input, 15.,17.,20.,20.);
      FuzzySet set = new FuzzySet();
      set.addRule(new FuzzyRule(nulle, 5.));
      set.addRule(new FuzzyRule(bas, 10.));
      set.addRule(new FuzzyRule(moyen, 0.));
      set.addRule(new FuzzyRule(haut, 20.));
      
      for ( double v = 0.; v <= 20.; v+=0.5)
      {
         input.setValue(v);
         System.out.println("v="+v+", out="+set.getValue());
      }
      }
      catch (FuzzyException e)
      {
         e.printStackTrace();
      }
   }

}
