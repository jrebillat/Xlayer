package net.alantea.fuzzy.equation.functs;

import net.alantea.fuzzy.equation.FuzzyEquation;
import net.alantea.fuzzy.util.FuzzyException;

public class Test extends FuzzyFunction
{
   
   public Test( FuzzyEquation eqt, String str) throws FuzzyException
   {
      super(eqt, str, 2);
   }

   public void calculate()
   {
      double v = getParms().get(0).getValue() + getParms().get(1).getValue();
      System.out.println("Test : " + (getParms().get(0).getValue()) + " + " + (getParms().get(1).getValue()));
      updateValue(v);
   }
}
