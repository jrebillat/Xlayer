package net.alantea.fuzzy.op;

import java.util.Vector;

import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.util.FuzzyException;

public final class FuzzyAnd extends FuzzyCondition
{
  public static enum FuzzyAndMethod { MINIMUM, PRODUCT };

  private Vector<FuzzyCondition> conds;
  private FuzzyAndMethod meth;
   
  public FuzzyAnd()
  {
     this(FuzzyAndMethod.PRODUCT);
  }
  
   public FuzzyAnd(FuzzyAndMethod meth)
   {
      this.meth = meth;
      conds = new Vector<FuzzyCondition>();
   }
   
   public FuzzyAnd(FuzzyCondition first, FuzzyCondition second ) throws FuzzyException
   {
      this(first, second, FuzzyAndMethod.PRODUCT);
   }
   
    public FuzzyAnd(FuzzyCondition first, FuzzyCondition second, FuzzyAndMethod meth)  throws FuzzyException
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

   private double getMinimumLevel()
   {
      double ret = 1.;

      for (FuzzyCondition e : conds)
      {
         double v = e.getValue();
         if (v < ret) ret = v;
      }

      return ret;
   }

   private double getProductLevel()
   {
      double ret = 1.;

      for (FuzzyCondition e : conds)
      {
         ret *= e.getValue();
      }
      return ret;
   }

   @Override
   public void calculate()
   {
      switch(meth)
      {
         case MINIMUM:
            updateValue(getMinimumLevel());
         default:
            updateValue(getProductLevel());
      }
   }
}
