package net.alantea.fuzzy.val;

public final class FuzzyConstant extends FuzzyValue
{

   public FuzzyConstant(double value)
   {
      updateValue(value);
   }
   
   @Override
   public void calculate()
   {
      // Nothing to do
   }
}