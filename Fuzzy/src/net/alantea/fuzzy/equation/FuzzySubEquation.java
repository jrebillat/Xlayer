package net.alantea.fuzzy.equation;

import java.util.Vector;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyConstant;
import net.alantea.fuzzy.val.FuzzyValue;

public class FuzzySubEquation extends FuzzyEquationToken
{
   FuzzyValue token;

   public FuzzySubEquation(FuzzyEquation eqt, String str) throws FuzzyException
   {
      super(eqt, str);
   }

   protected void analyse() throws FuzzyException
   {
      Vector<Object> v = new Vector<Object>();
      int nbpars = 0;
      int nbaccs = 0;
      int starttok = -1;
      
      byte[] buf = str.getBytes();

      System.out.println("reading '"+str+"'");
      // parse for rough tokens
      for (int i = 0; i < buf.length; i++)
      {
         System.out.println(":"+buf[i]);
         if (buf[i] == '(')
         {
            if (nbpars == 0)
            {
               starttok = i + 1;
            }
            nbpars++;
         }
         else if (buf[i] == '[')
         {
            nbaccs++;
         }
         else if (buf[i] == ']')
         {
            nbaccs--;
         }
         else if (buf[i] == ')')
         {
            nbpars--;
            if (nbpars == 0)
            {
               v.add(new FuzzySubEquation(eqt, str.substring(starttok, i)));
               starttok = -1;
            }
         }
         else if ((nbpars == 0) && (buf[i] != ' ') && (starttok == -1))
         {
            starttok = i;
         }
         if ((nbpars == 0) && (nbaccs == 0) && ((buf[i] == ' ')||i == (buf.length-1)))
         {
            String s;
            if ( starttok == -1) starttok = i;
            if ((buf[i] == ' ')) s = str.substring(starttok, i);
            else s = str.substring(starttok, i+1);
            
            System.out.println("found token : '"+s+"'");
            if (s.matches("^[0-9\\.]+$"))
            {
               System.out.println("found const : "+s);
               v.add(new FuzzyConstant(Double.parseDouble(s)));
            }
            else if (s.matches("^[a-zA-Z_][a-zA-Z_0-9]*$"))
            {
               System.out.println("found reference : "+s);
               v.add(eqt.getNetReference(s));
            }
            else if (s.matches("^[a-zA-Z_][a-zA-Z_0-9]*\\[.*\\]$"))
            {
               System.out.println("found function : "+s);
               v.add(eqt.getFunctionReference(s));
            }
            else
            {
               // this is not a Value !
               System.out.println("found op : "+s);
               v.add(s);
            }
            starttok = -1;
         }
      }

      FuzzyValue prev = null;
      String opstr = null;
      for (Object e : v)
      {
         if (e instanceof String)
         {
            opstr = (String) e;
            System.out.println("parse op :"+opstr);
         }
         else
         {
            if (opstr != null)
            {
               FuzzyValue nt = new FuzzyEquationOperator(eqt, opstr, prev,
                     (FuzzyValue) e);
               prev = nt;
               opstr = null;
            }
            else
            {
               prev = (FuzzyValue) e;
            }
         }
      }
      token = prev;
      token.addObserver(this);
      updateValue(token.getValue());
      if ( token == null) System.out.println("final token is null");
   }

   @Override
   public void calculate()
   {
      token.calculate();
      updateValue(token.getValue());
   }
}
