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
