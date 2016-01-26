package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingDerivedClass;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLFieldSettingTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   } 

   @Test
   public void SimpleFieldSettingTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<value>24</value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, 24);
   } 

   @Test
   public void objectFieldSettingTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<testObject _class='ParsingDerivedClass'></testObject>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object val = root.getTestObject();
      Assert.assertNotNull(val);
      Assert.assertEquals(val.getClass(), ParsingDerivedClass.class);
   }
   
   @Test
   public void ListFieldSetting1Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myList>"
               + "<list>"
                  + "<integer>1</integer>"
                  + "<integer>2</integer>"
                  + "<integer>3</integer>"
                  + "<integer>4</integer>"
                  + "</list>"
            + "</myList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyList();
      Assert.assertNotNull(ints);
      Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
      int i = 0;
      for (String err : errors)
      {
         Assert.assertEquals(((List<Integer>)ints).get(i++), new Integer(i));
      }
   }  
   
 @Test
 public void ListFieldSetting2Test()
 {
    ParsingRoot root = new ParsingRoot();
    List<String> errors = Manager.parse(root, HEADER_XML
          + "<myList>"
                + "<integer>1</integer>"
                + "<integer>2</integer>"
                + "<integer>3</integer>"
                + "<integer>4</integer>"
          + "</myList>");
    for (String err : errors)
    {
       System.out.println(err);
    }
    Assert.assertTrue(errors.isEmpty());
    Object ints = root.getMyList();
    Assert.assertNotNull(ints);
    Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
    int i = 0;
    for (String err : errors)
    {
       Assert.assertEquals(((List<Integer>)ints).get(i++), new Integer(i));
    }
 }  

   @Test
   public void invalidNameFieldSettingTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<nontestObject _class='ParsingDerivedClass'></nontestObject>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }

   @Test
   public void invalidValueFieldSettingTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<value>bad value</value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
}
