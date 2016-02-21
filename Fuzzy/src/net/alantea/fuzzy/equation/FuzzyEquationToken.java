package net.alantea.fuzzy.equation;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.util.FuzzyObserver;
import net.alantea.fuzzy.val.FuzzyValue;


abstract class FuzzyEquationToken extends FuzzyValue implements FuzzyObserver
{
   protected FuzzyEquation eqt;
   protected final String  str;

   FuzzyEquationToken(FuzzyEquation eqt, String str) throws FuzzyException
   {
      this.eqt = eqt;
      this.str = str;
      analyse();
   }

   @Override
   public void invalidate()
   {
      markAsDirty();
   }

   protected abstract void analyse() throws FuzzyException;
}