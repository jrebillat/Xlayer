package net.alantea.fuzzy.cond;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyConstant;
import net.alantea.fuzzy.val.FuzzyValue;

public final class FuzzyLinearCondition extends FuzzyCondition
{
   private final FuzzyValue input;
   private FuzzyValue start;
   private FuzzyValue upstart;
   private FuzzyValue upend;
   private FuzzyValue end;

   public FuzzyLinearCondition(FuzzyValue input, double start, double upstart,
         double upend, double end) throws FuzzyException
   {
      this(input, new FuzzyConstant(start), new FuzzyConstant(upstart),
            new FuzzyConstant(upend), new FuzzyConstant(end));

   }

   public FuzzyLinearCondition(FuzzyValue input, FuzzyValue start,
         FuzzyValue upstart, FuzzyValue upend, FuzzyValue end)
         throws FuzzyException
   {
      if (input == null) throw new FuzzyException("Null input");
      if (start == null) throw new FuzzyException("Null start value");
      if (upstart == null) throw new FuzzyException("Null upstart value");
      if (upend == null) throw new FuzzyException("Null upend value");
      if (end == null) throw new FuzzyException("Null end value");
      
      this.input = input;
      input.addObserver(this);
      this.start = start;
      start.addObserver(this);
      this.upstart = upstart;
      upstart.addObserver(this);
      this.upend = upend;
      upend.addObserver(this);
      this.end = end;
      end.addObserver(this);

      invalidate();
   }

   public FuzzyLinearCondition(FuzzyValue input, double start, double middle,
         double end) throws FuzzyException
   {
      this(input, start, middle, middle, end);
   }

   public FuzzyLinearCondition(FuzzyValue input, double start, double end)
         throws FuzzyException
   {
      this(input, start, start, end, end);
   }

   public void calculate()
   {
      double calc;
      double val = input.getValue();
      double upst = upstart.getValue();
      double st = start.getValue();
      double upen = upend.getValue();
      double en = end.getValue();
      double slopestart;
      double slopeend;

      if (upst > st)
         slopestart = 1. / (upst - st);
      else slopestart = 0.;

      if (upen < en)
         slopeend = 1. / (upen - en);
      else slopeend = 0.;

      if (val < start.getValue())
         calc = 0.;
      else if (val < upstart.getValue())
         calc = (val - start.getValue()) * slopestart;
      else if (val <= upend.getValue())
         calc = 1.;
      else if (val <= end.getValue())
         calc = (val - end.getValue()) * slopeend;
      else calc = 0.;

      if (calc < 0.) calc = 0.;

      updateValue(calc);
   }
}
