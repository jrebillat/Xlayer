package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingBaseClass;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLArrayTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   } 
   @Test
   public void SimpleValuesArrayTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myArray>"
                  + "<integer>1</integer>"
                  + "<integer>2</integer>"
                  + "<integer>3</integer>"
                  + "<integer>4</integer>"
            + "</myArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyArray();
      Assert.assertNotNull(ints);
      Assert.assertTrue(Integer[].class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((Integer[])ints).length, 4);
      for (int i = 0; i < ((Integer[])ints).length; i++)
      {
         Assert.assertEquals(((Integer[])ints)[i], new Integer(i+1));
      }
   }

   @Test
   public void SimpleValuesArray2Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myArray><array>"
                  + "<integer>1</integer>"
                  + "<integer>2</integer>"
                  + "<integer>3</integer>"
                  + "<integer>4</integer>"
            + "</array></myArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyArray();
      Assert.assertNotNull(ints);
      Assert.assertTrue(Integer[].class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((Integer[])ints).length, 4);
      for (int i = 0; i < ((Integer[])ints).length; i++)
      {
         Assert.assertEquals(((Integer[])ints)[i], new Integer(i+1));
      }
   }
   
   @Test
   public void OneValueArrayTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myArray>"
                  + "<integer>1</integer>"
            + "</myArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyArray();
      Assert.assertNotNull(ints);
      Assert.assertTrue(Integer[].class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((Integer[])ints).length, 1);
      for (int i = 0; i < ((Integer[])ints).length; i++)
      {
         Assert.assertEquals(((Integer[])ints)[i], new Integer(i+1));
      }
   }

   @Test
   public void OneValueArray2Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myArray><array>"
                  + "<integer>1</integer>"
            + "</array></myArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyArray();
      Assert.assertNotNull(ints);
      Assert.assertTrue(Integer[].class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((Integer[])ints).length, 1);
      for (int i = 0; i < ((Integer[])ints).length; i++)
      {
         Assert.assertEquals(((Integer[])ints)[i], new Integer(i+1));
      }
   }

   @Test
   public void ObjectValuesArrayTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myObjectArray>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
            + "</myObjectArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyObjectArray();
      Assert.assertNotNull(ints);
      Assert.assertTrue(ParsingBaseClass[].class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((ParsingBaseClass[])ints).length, 3);
   }  

   @Test
   public void ArrayValuesArrayTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myArrayArray>"
               + "<array>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
               + "</array>"
               + "<array>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
            + "</array>"
            + "</myArrayArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object vals = root.getMyArrayArray();
      Assert.assertNotNull(vals);
      Assert.assertTrue(ParsingBaseClass[][].class.isAssignableFrom(vals.getClass()));
      Assert.assertEquals(((ParsingBaseClass[][])vals).length, 2);
   }

   @Test
   public void EmptyValuesArrayTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<myArray>"
               + "<list>"
               + "</list>"
            + "</myArray>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object vals = root.getMyListList();
      Assert.assertNull(vals);
   }
}
