package net.alantea.fuzzy.equation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.alantea.fuzzy.comp.FuzzyNetwork;
import net.alantea.fuzzy.equation.functs.FuzzyFunction;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.util.FuzzyObserver;
import net.alantea.fuzzy.val.FuzzyValue;

public final class FuzzyEquation extends FuzzyValue implements FuzzyObserver
{
   private FuzzyNetwork     net;
   private String           eqt;
   private FuzzySubEquation subeqt;

   public FuzzyEquation(FuzzyNetwork net, String eqstr) throws FuzzyException
   {
      this.net = net;
      
      // clean string for simpler understanding
      String st = new String(eqstr);
      // remove all blanks
      st = st.replaceAll(" *", "");
      // separate operators
      st = st.replaceAll("\\*", " * ");
      st = st.replaceAll("\\+", " + ");
      st = st.replaceAll("-", " - ");
      st = st.replaceAll("/", " / ");
      
      this.eqt = st;
      this.subeqt = new FuzzySubEquation(this, eqt);
      this.subeqt.addObserver(this);
      invalidate();
   }

   @Override
   public void calculate()
   {
      subeqt.calculate();
      updateValue(subeqt.getValue());
   }

   FuzzyValue getNetReference(String label) throws FuzzyException
   {
      return net.getValue(label);
   }

   public String GetEquationString()
   {
      return eqt;
   }

   @Override
   public void invalidate()
   {
      markAsDirty();
   }

   public Object getFunctionReference(String s) throws FuzzyException
   {
      String name = s.substring(0, s.indexOf('['));
      name = name.substring(0, 1).toUpperCase() + name.substring(1);
      String clname = "net.alantea.fuzzy.equation.functs." + name;
      try
      {
         Class<?> cl = getClass().getClassLoader().loadClass(clname);
         @SuppressWarnings("unchecked")
         Constructor<FuzzyFunction> cons = (Constructor<FuzzyFunction>) cl.getConstructor(FuzzyEquation.class, String.class);
         FuzzyFunction func = cons.newInstance(this, s);
         return func;
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
      catch (NoSuchMethodException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
      catch (SecurityException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
      catch (InstantiationException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
      catch (IllegalAccessException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
      catch (IllegalArgumentException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
      catch (InvocationTargetException e)
      {
         e.printStackTrace();
         throw new FuzzyException(e);
      }
   }
}



