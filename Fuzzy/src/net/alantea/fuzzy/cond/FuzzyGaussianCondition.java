package net.alantea.fuzzy.cond;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyConstant;
import net.alantea.fuzzy.val.FuzzyValue;

public final class FuzzyGaussianCondition  extends FuzzyCondition
{
   private final FuzzyValue input;
   private FuzzyValue top;
   private FuzzyValue width;

   public FuzzyGaussianCondition(FuzzyValue input, double top, double width) throws FuzzyException
   {
      this(input, new FuzzyConstant(top), new FuzzyConstant(width));

   }

   public FuzzyGaussianCondition(FuzzyValue input, FuzzyValue top,
         FuzzyValue width)
         throws FuzzyException
   {
      if (input == null) throw new FuzzyException("Null input");
      if (top == null) throw new FuzzyException("Null top value");
      if (width == null) throw new FuzzyException("Null width value");
      
      this.input = input;
      input.addObserver(this);
      this.top = top;
      top.addObserver(this);
      this.width = width;
      width.addObserver(this);
      invalidate();
   }

   public void calculate()
   {
      double calc;
      double val = input.getValue();
      calc = Math.exp(-(val-top.getValue())*(val-top.getValue())/
                       (2.0*width.getValue()*width.getValue()));

      updateValue(calc);
   }
}
