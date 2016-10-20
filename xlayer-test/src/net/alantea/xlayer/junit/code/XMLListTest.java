package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingBaseClass;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLListTest
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

   @SuppressWarnings("unchecked")
   @Test
   public void SimpleValuesListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
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
      Assert.assertEquals(((List<Integer>)ints).size(), 4);
      for (int i = 0; i < ((List<Integer>)ints).size(); i++)
      {
         Assert.assertEquals(((List<Integer>)ints).get(i++), new Integer(i));
      }
   }

   @SuppressWarnings("unchecked")
   @Test
   public void SimpleValuesList2Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<myList><list>"
                  + "<integer>1</integer>"
                  + "<integer>2</integer>"
                  + "<integer>3</integer>"
                  + "<integer>4</integer>"
            + "</list></myList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyList();
      Assert.assertNotNull(ints);
      Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((List<Integer>)ints).size(), 4);
      for (int i = 0; i < ((List<Integer>)ints).size(); i++)
      {
         Assert.assertEquals(((List<Integer>)ints).get(i), new Integer(i+1));
      }
   }  

   @SuppressWarnings("unchecked")
   @Test
   public void ObjectValuesListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<myObjectList>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
            + "</myObjectList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyObjectList();
      Assert.assertNotNull(ints);
      Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((List<ParsingBaseClass>)ints).size(), 3);
   }  

   @SuppressWarnings("unchecked")
   @Test
   public void OneValueListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<myList>"
                  + "<integer>1</integer>"
            + "</myList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyList();
      Assert.assertNotNull(ints);
      Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((List<Integer>)ints).size(), 1);
      for (int i = 0; i < ((List<Integer>)ints).size(); i++)
      {
         Assert.assertEquals(((List<Integer>)ints).get(i++), new Integer(i));
      }
   }

   @SuppressWarnings("unchecked")
   @Test
   public void OneValueList2Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<myList><list>"
                  + "<integer>1</integer>"
            + "</list></myList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object ints = root.getMyList();
      Assert.assertNotNull(ints);
      Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
      Assert.assertEquals(((List<Integer>)ints).size(), 1);
      for (int i = 0; i < ((List<Integer>)ints).size(); i++)
      {
         Assert.assertEquals(((List<Integer>)ints).get(i), new Integer(i+1));
      }
   }  

   @SuppressWarnings("unchecked")
   @Test
   public void ListValuesListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<myListList>"
               + "<list>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
                  + "<ParsingBaseClass></ParsingBaseClass>"
               + "</list>"
               + "<list>"
               + "<ParsingBaseClass></ParsingBaseClass>"
               + "<ParsingBaseClass></ParsingBaseClass>"
            + "</list>"
            + "</myListList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object vals = root.getMyListList();
      Assert.assertNotNull(vals);
      Assert.assertTrue(List.class.isAssignableFrom(vals.getClass()));
      Assert.assertEquals(((List<List<ParsingBaseClass>>)vals).size(), 2);
   }

   @Test
   public void EmptyValuesListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<myList>"
               + "<list>"
               + "</list>"
            + "</myList>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object vals = root.getMyListList();
      Assert.assertNull(vals);
   }
}
