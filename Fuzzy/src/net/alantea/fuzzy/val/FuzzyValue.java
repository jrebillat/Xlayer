package net.alantea.fuzzy.val;

import java.util.Vector;

import net.alantea.fuzzy.util.FuzzyObserver;

public abstract class FuzzyValue
{
   private double value = 0.;
   boolean upToDate = true;
   
   private Vector<FuzzyObserver> listens = new Vector<FuzzyObserver>();

   public final double getValue()
   {
      if (! upToDate)
      {
         calculate();
      }
      return value;
   }
   
   protected void updateValue(double v)
   {
      value = v;
      upToDate = true;
      invalidateObservers();
   }

   public final void addObserver(FuzzyObserver l)
   {
      if (! listens.contains(l))
      {
         listens.add(l);
      }
   }
   public final void removeObserver(FuzzyObserver l)
   {
      if (listens.contains(l))
      {
         listens.remove(l);
      }
   }
   
   protected void invalidateObservers()
   {
      for (FuzzyObserver e : listens)
      {
         e.invalidate();
      }
   }
   
   protected void markAsDirty()
   {
      upToDate = false;
      invalidateObservers();
   }
   
   public abstract void calculate();
}
