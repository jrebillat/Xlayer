package net.alantea.fuzzy.cond;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyConstant;
import net.alantea.fuzzy.val.FuzzyValue;

public final class FuzzyCosinusCondition extends FuzzyCondition
{
   private final FuzzyValue input;
   private FuzzyValue top;
   private FuzzyValue width;

   public FuzzyCosinusCondition(FuzzyValue input, double top, double width) throws FuzzyException
   {
      this(input, new FuzzyConstant(top), new FuzzyConstant(width));

   }

   public FuzzyCosinusCondition(FuzzyValue input, FuzzyValue top,
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
      if (Math.abs(val - top.getValue()) > width.getValue()) calc = 0.;
      else calc = 0.5 * (1 + Math.cos(Math.PI * (val - top.getValue())
            / width.getValue()));
      updateValue(calc);
   }
}
