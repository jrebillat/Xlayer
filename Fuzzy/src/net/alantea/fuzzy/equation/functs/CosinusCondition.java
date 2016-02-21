package net.alantea.fuzzy.equation.functs;

import net.alantea.fuzzy.cond.FuzzyCosinusCondition;
import net.alantea.fuzzy.equation.FuzzyEquation;
import net.alantea.fuzzy.util.FuzzyException;

public class CosinusCondition extends FuzzyFunction
{
   FuzzyCosinusCondition cond;
   
   public CosinusCondition( FuzzyEquation eqt, String str) throws FuzzyException
   {
      super(eqt, str, 3);
      cond = new FuzzyCosinusCondition(getParms().get(0), getParms().get(1), getParms().get(2));
      cond.addObserver(this);
   }

   public void calculate()
   {
      updateValue(cond.getValue());
   }
}
