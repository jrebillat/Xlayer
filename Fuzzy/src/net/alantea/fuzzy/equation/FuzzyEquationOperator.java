package net.alantea.fuzzy.equation;

import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyValue;

class FuzzyEquationOperator extends FuzzyEquationToken
{
   private static enum OpType
   {
      MULT, DIV, ADD, SUBST
   };

   private FuzzyValue operator;
   private FuzzyValue operand;
   private OpType     optype;

   public FuzzyEquationOperator(FuzzyEquation eqt, String s, FuzzyValue op1,
         FuzzyValue op2) throws FuzzyException
   {
      super(eqt, s);
      operator = op1;
      op1.addObserver(this);
      operand = op2;
      op2.addObserver(this);
   }

   protected void analyse()
   {
      if (str.equals("*"))
      {
         optype = OpType.MULT;
      }
      else if (str.equals("/"))
      {
         optype = OpType.DIV;
      }
      else if (str.equals("+"))
      {
         optype = OpType.ADD;
      }
      else if (str.equals("-"))
      {
         optype = OpType.SUBST;
      }
   }

   @Override
   public void calculate()
   {
      System.out.println("updating op. "+optype+" "+operator.getValue()+" "+ operand.getValue());
      switch (optype)
      {
         case MULT:
            updateValue(operator.getValue() * operand.getValue());
            break;
         case DIV:
            updateValue(operator.getValue() / operand.getValue());
            break;
         case ADD:
            updateValue(operator.getValue() + operand.getValue());
            break;
         case SUBST:
            updateValue(operator.getValue() - operand.getValue());
            break;
      }
      System.out.println("value = " + getValue());
   }
}