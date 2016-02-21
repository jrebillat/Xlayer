package net.alantea.fuzzy.equation.functs;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.alantea.fuzzy.equation.FuzzyEquation;
import net.alantea.fuzzy.equation.FuzzySubEquation;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.util.FuzzyObserver;
import net.alantea.fuzzy.val.FuzzyValue;

public abstract class FuzzyFunction extends FuzzyValue implements FuzzyObserver
{
   Vector<FuzzySubEquation> parms = new Vector<FuzzySubEquation>();

   protected FuzzyFunction(FuzzyEquation eqt, String str, int nbParams) throws FuzzyException
   {
      int dpar = str.indexOf('[');
      int epar = str.lastIndexOf(']');
      String parStr = str.substring(dpar + 1, epar);

      List<String> ps = parseParameters(parStr);
      if (nbParams != ps.size())
      {
         throw new FuzzyException("Bad parameters number in " + str);
      }
      
      for (String s : ps)
      {
         FuzzySubEquation seqt = new FuzzySubEquation(eqt, s);
         seqt.addObserver(this);
         parms.add(seqt);
      }
   }
   
   protected Vector<FuzzySubEquation> getParms()
   {
      return parms;
   }
   
   @Override
   public void invalidate()
   {
      markAsDirty();
   }
   
   public abstract void calculate();
   
   private List<String> parseParameters(String parStr)
   {
      List<String> ret = new ArrayList<String>();
      int nbseps = 0;
      int start = 0;

      byte[] buf = parStr.getBytes();
      for (int i = 0; i < buf.length; i++)
      {
         if ((buf[i] == '[') || (buf[i] == '('))
         {
            nbseps++;
         }
         else if ((buf[i] == ']') || (buf[i] == ')'))
         {
            nbseps--;
         }
         else if ((buf[i] == ',') && (nbseps == 0))
         {
            String s = parStr.substring(start, i);
            ret.add(s);
            start = i+1;
         }
      }
      String s = parStr.substring(start, buf.length);
      ret.add(s);
      return ret;
   }
}
