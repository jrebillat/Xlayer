package net.alantea.fuzzy.comp;

import java.util.Vector;
import java.util.Enumeration;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.util.FuzzyObserver;
import net.alantea.fuzzy.val.FuzzyValue;


public class FuzzySet extends FuzzyValue implements FuzzyObserver
{
   public static enum FuzzySetMethod
   {
      MAXIMUM, GRAVITY
   };

   private Vector<FuzzyRule> rules;
   private FuzzySetMethod    meth;

   public FuzzySet(FuzzySetMethod meth)
   {
      rules = new Vector<FuzzyRule>();
      this.meth = meth;
      invalidate();
   }
   
   public FuzzySet()
   {
      this(FuzzySetMethod.GRAVITY);
   }

   public void addRule(FuzzyRule rule) throws FuzzyException
   {
      if (rule == null) throw new FuzzyException("Null rule");
      rules.add(rule);
      rule.addObserver(this);
      invalidate();
   }

   private double getMaximumValue()
   {
      double ret = 0.;

      for (Enumeration<FuzzyRule> e = rules.elements(); e
            .hasMoreElements();)
      {
         double v = e.nextElement().getValue();
         if (v > ret) ret = v;
      }

      return ret;
   }

   private double getGravityValue()
   {
      double sigmamu = 0.;
      double ret = 0.;

      for (FuzzyRule e : rules)
      {
         double mu = e.getConditionLevel();
         double va = e.getValue();
         ret += va * mu;
         sigmamu += mu;
      }

      if (sigmamu > 0.) ret /= sigmamu;

      return ret;
   }
   
   @Override
   public void calculate()
   {
      switch (meth)
      {
         case MAXIMUM:
            updateValue(getMaximumValue());
         default:
            updateValue(getGravityValue());
      }
   }

   @Override
   public void invalidate()
   {
      markAsDirty();
   }
}
