package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingBaseClass;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLMethodCallingTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";

   private static Manager manager;
   
   @BeforeClass
   public void beforeClass()
   {
      manager = new Manager();
      manager.clearAll();
   }

   @Test
   public void NoArgsCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<value><loadValue/></value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, root.loadValue());
   }  

   @Test
   public void SimpleCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<changeValue>33</changeValue>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, 33);
   }

   @Test
   public void ObjectCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<changeTestObject><ParsingBaseClass/></changeTestObject>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = root.getTestObject();
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingBaseClass.class);
   }

   @Test
   public void TwoParamsCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<changeBoth><integer>22</integer><ParsingBaseClass/></changeBoth>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = root.getTestObject();
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingBaseClass.class);
      int val = root.getValue();
      Assert.assertEquals(val, 22);
   }  

   @Test
   public void AutoListCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<value><sum>"
            + "<integer>1</integer>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</sum></value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, 10);
   } 

   @Test
   public void DefinedListCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<value><sum>"
                  + "<list>"
            + "<integer>1</integer>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</list>"
            + "</sum></value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, 10);
   } 

   @Test
   public void ObjectAndListCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<value><setSum>"
            + "<ParsingBaseClass/>"
            + "<list>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</list>"
            + "</setSum></value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = root.getTestObject();
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingBaseClass.class);
      int val = root.getValue();
      Assert.assertEquals(val, 9);
   }

   @Test
   public void BadCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<badMethodName>33</badMethodName>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }

   @Test
   public void BadparametersCallTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<value><setSum>"
            + "<list>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</list>"
            + "<ParsingBaseClass/>"
            + "</setSum></value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
}
