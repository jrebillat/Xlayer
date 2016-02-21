package net.alantea.fuzzy.comp;

import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.util.FuzzyObserver;
import net.alantea.fuzzy.val.FuzzyConstant;
import net.alantea.fuzzy.val.FuzzyValue;


public final class FuzzyRule extends FuzzyValue implements FuzzyObserver
{
   private FuzzyCondition cond;
   private FuzzyValue value;
   
   public FuzzyRule(FuzzyCondition cond, double value) throws FuzzyException
   { 
      this(cond, new FuzzyConstant(value));
   }
   
   public FuzzyRule(FuzzyCondition cond, FuzzyValue value) throws FuzzyException
   {
      if (cond == null) throw new FuzzyException("Null condition");
      if (value == null) throw new FuzzyException("Null value");
      this.cond = cond;
      cond.addObserver(this);
      this.value = value;
      value.addObserver(this);
      invalidate();
   }
   
   public double getConditionLevel() { return cond.getValue(); }

   @Override
   public void calculate()
   {
       updateValue( cond.getValue() * value.getValue());
   }
   
   @Override
   public void invalidate()
   {
      markAsDirty();
   }
}
