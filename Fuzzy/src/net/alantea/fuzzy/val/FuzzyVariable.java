package net.alantea.fuzzy.val;



public final class FuzzyVariable extends FuzzyValue
{
   public void setValue(double value)
   {
      updateValue(value);
   }

   @Override
   public void calculate()
   {
   }
}
