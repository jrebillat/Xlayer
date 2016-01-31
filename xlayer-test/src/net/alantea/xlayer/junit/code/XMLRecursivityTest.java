package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingBaseClass;
import net.alantea.xlayer.junit.internal.ParsingDerivedClass;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLRecursivityTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   } 

   @Test
   public void objectInObjectTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><testObject _class='ParsingDerivedClass'></testObject></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      Object subObject = ((ParsingRoot)object).getTestObject();
      Assert.assertNotNull(subObject);
      Assert.assertEquals(subObject.getClass(), ParsingDerivedClass.class);
   } 

   @Test
   public void simpleInObjectTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><value>24</value></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 24);
   }

   @Test
   public void methodInObjectTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><changeValue>33</changeValue></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 33);
   }

   @SuppressWarnings("unchecked")
   @Test
   public void listInObjectTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'>"
            + "<myList>"
                  + "<integer>1</integer>"
                  + "<integer>2</integer>"
                  + "<integer>3</integer>"
                  + "<integer>4</integer>"
            + "</myList>"
            + "</ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      Object ints = ((ParsingRoot)object).getMyList();
      Assert.assertNotNull(ints);
      Assert.assertTrue(List.class.isAssignableFrom(ints.getClass()));
      for (int i = 0; i < ((List<Integer>)ints).size(); i++)
      {
         Assert.assertEquals(((List<Integer>)ints).get(i++), new Integer(i));
      }
   }

   @Test
   public void scriptInObjectTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><value><script>var a; a = 99;</script></value></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 99);
   }

   @Test
   public void simpleInMethodTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><changeValue><integer>33</integer></changeValue></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 33);
   }

   @Test
   public void methodInMethodTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><changeValue><loadValue/></changeValue></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 42);
   }

   @Test
   public void objectInMethodTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><changeTestObject><ParsingBaseClass/></changeTestObject></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      Object subObject = ((ParsingRoot)object).getTestObject();
      Assert.assertNotNull(subObject);
      Assert.assertEquals(subObject.getClass(), ParsingBaseClass.class);
   }

   @Test
   public void listInMethodTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><value><sum>"
            + "<integer>1</integer>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</sum></value></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 10);
   }

   @Test
   public void scriptInMethodTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><changeValue><script>var a; a = 99;</script></changeValue></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 99);
   }
   
   @Test
   public void simpleInListTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><value><sum><list>"
            + "<integer>1</integer>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</list></sum></value></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 10);
   }  

   @SuppressWarnings("unchecked")
   @Test
   public void ObjectInListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
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
   
   @Test
   public void methodInListTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><value><sum><list>"
            + "<integer>1</integer>"
            + "<loadValue/>"
            + "</list></sum></value></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 43);
   }
   
   @Test
   public void scriptInListTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'><value><sum><list>"
            + "<integer>1</integer>"
            + "<script>var a; a = 99;</script>"
            + "</list></sum></value></ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
      int val = ((ParsingRoot)object).getValue();
      Assert.assertEquals(val, 100);
   } 

   @SuppressWarnings("unchecked")
   @Test
   public void ListInListTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
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
}
