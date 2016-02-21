package net.alantea.fuzzy.cond;

import net.alantea.fuzzy.util.FuzzyObserver;
import net.alantea.fuzzy.val.FuzzyValue;

public abstract class FuzzyCondition extends FuzzyValue implements FuzzyObserver
{

   @Override
   public void invalidate()
   {
      markAsDirty();
   }
}


