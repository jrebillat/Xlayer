package fuzzytest;

import net.alantea.fuzzy.comp.FuzzyRule;
import net.alantea.fuzzy.comp.FuzzySet;
import net.alantea.fuzzy.cond.FuzzyCondition;
import net.alantea.fuzzy.cond.FuzzyLinearCondition;
import net.alantea.fuzzy.util.FuzzyException;
import net.alantea.fuzzy.val.FuzzyVariable;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BasicTest1 {
   
   FuzzyVariable input;
   FuzzySet set;
   
   @BeforeClass
   public void setup()
   {
      try
      {
         input = new FuzzyVariable();
         FuzzyCondition nulle = new FuzzyLinearCondition(input, 0.,1.,5.,7.);
         set = new FuzzySet();
         set.addRule(new FuzzyRule(nulle, 5.));
      }
      catch (FuzzyException e)
      {
         e.printStackTrace();
      }
   }
   
   @Test(dataProvider = "test1")
  public void test1(Double in, Double out)
  {
     input.setValue(in);
     Assert.assertEquals(set.getValue(), out.doubleValue());
  }
  
  @DataProvider(name = "test1")
  public Object[][] createData1() {
   return new Object[][] {
     { new Double(0.0), new Double(0.0) },
     { new Double(0.5), new Double(2.5) },
     { new Double(1.0), new Double(5.0) },
     { new Double(3.0), new Double(5.0) },
     { new Double(5.0), new Double(5.0) },
     { new Double(6.0), new Double(2.5) },
     { new Double(7.0), new Double(0.0) },
     { new Double(8.0), new Double(0.0) }
   };
  }

}
