package net.alantea.fuzzy.op;

import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.util.FuzzyException;


public final class FuzzyNot extends FuzzyCondition
{
   private FuzzyCondition cond;

   @SuppressWarnings("unused")
  private FuzzyNot()
   {
   }

   public FuzzyNot(FuzzyCondition cond) throws FuzzyException
   {
      if (cond == null) throw new FuzzyException("Null condition");
      this.cond = cond;
      cond.addObserver(this);
      invalidate();
   }

   public void calculate()
   {
      double v = cond.getValue();
      updateValue(1. - v);
   }

}
