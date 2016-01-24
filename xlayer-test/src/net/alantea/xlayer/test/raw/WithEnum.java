package net.alantea.xlayer.test.raw;

public class WithEnum
{
   public enum State { ON, OFF};
   
   public WithEnum()
   {
      System.out.println("creating WithEnum");
   }
   
   public void setState(State state)
   {
      System.out.println("Setting state :" + state.name());
   }
   
}
