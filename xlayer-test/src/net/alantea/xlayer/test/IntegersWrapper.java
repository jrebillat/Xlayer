package net.alantea.xlayer.test;

import java.util.ArrayList;
import java.util.List;

public class IntegersWrapper
{
   public List<Integer> values1 = new ArrayList<>();
   public List<Integer> values2 = new ArrayList<>();
   
   public void addInteger(int value)
   {
      values1.add(value);
   }
   
   public List<Integer> getValues()
   {
      return values2;
   }
}
