package net.alantea.xlayer.junit.internal;


public class TestMain
{
   TestInterface action;
   
   void setTestItf(TestInterface act)
   {
      action = act;
   }
   
   public int exec(int value)
   {
      return action.execute(value);
   }
}
