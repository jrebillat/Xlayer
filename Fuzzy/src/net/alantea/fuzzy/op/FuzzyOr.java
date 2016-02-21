package net.alantea.fuzzy.op;

import java.util.Vector;
import java.util.Enumeration;

import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.util.FuzzyException;

public final class FuzzyOr extends FuzzyCondition
{
   public static enum FuzzyOrMethod
   {
      MAXIMUM, AVERAGE
   };

   private Vector<FuzzyCondition> conds;
   private FuzzyOrMethod          meth;

   public FuzzyOr()
   {
      this(FuzzyOrMethod.AVERAGE);
   }

   public FuzzyOr(FuzzyOrMethod meth)
   {
      this.conds = new Vector<FuzzyCondition>();
      this.meth = meth;
   }

   public FuzzyOr(FuzzyCondition first, FuzzyCondition second) throws FuzzyException
   {
      this(first, second, FuzzyOrMethod.AVERAGE);
   }

   public FuzzyOr(FuzzyCondition first, FuzzyCondition second,
         FuzzyOrMethod meth) throws FuzzyException
   {
      this(meth);
      addCondition(first);
      addCondition(second);
   }

   public void addCondition(FuzzyCondition cond) throws FuzzyException
   {
      if (cond == null) throw new FuzzyException("Null condition");
      conds.add(cond);
      cond.addObserver(this);
      invalidate();
   }

   private double getMaximumLevel()
   {
      double ret = 0.;

      for (Enumeration<FuzzyCondition> e = conds.elements(); e
            .hasMoreElements();)
      {
         double v = e.nextElement().getValue();
         if (v > ret) ret = v;
      }

      return ret;
   }

   private double getAverageLevel()
   {
      int n = 0;
      double ret = 0.;

      for (Enumeration<FuzzyCondition> e = conds.elements(); e
            .hasMoreElements();)
      {
         ret += e.nextElement().getValue();
         n++;
      }

      if (n > 0) ret /= n;

      return ret;
   }

   @Override
   public void calculate()
   {
      switch (meth)
      {
         case MAXIMUM:
            updateValue(getMaximumLevel());
         default:
            updateValue(getAverageLevel());
      }
   }
}
