package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLIncludeTest
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
   public void SimpleIncludeResourceTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<xlayer><include path='/data/subfile1.xml' type='resource'/></xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object var2 = manager.getVariable("myTestVariable2");
      Assert.assertNotNull(var2);
      Assert.assertTrue(Integer.class.isAssignableFrom(var2.getClass()));
      Assert.assertEquals(((Integer)var2), new Integer(36));
   }

   @Test
   public void SimpleIncludeFileTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<list><include path='resources/data/subfile1.xml' type='file'/></list>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object var2 = manager.getVariable("myTestVariable2");
      Assert.assertNotNull(var2);
      Assert.assertTrue(Integer.class.isAssignableFrom(var2.getClass()));
      Assert.assertEquals(((Integer)var2), new Integer(36));
   }
   
   @Test
   public void ParserIncludeTest()
   {
      List<String> errors = manager.parseResource((Object)null, "/data/master1.xml");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object var1 = manager.getVariable("myTestVariable1");
      Assert.assertNotNull(var1);
      Assert.assertTrue(Integer.class.isAssignableFrom(var1.getClass()));
      Assert.assertEquals(((Integer)var1), new Integer(24));
      Object var2 = manager.getVariable("myTestVariable2");
      Assert.assertNotNull(var2);
      Assert.assertTrue(Integer.class.isAssignableFrom(var2.getClass()));
      Assert.assertEquals(((Integer)var2), new Integer(36));
      Object var3 = manager.getVariable("myTestVariable3");
      Assert.assertNotNull(var3);
      Assert.assertTrue(Integer.class.isAssignableFrom(var3.getClass()));
      Assert.assertEquals(((Integer)var3), new Integer(42));
   } 

   @Test
   public void MultipleIncludeResourceTest()
   {
      List<String> errors = manager.parse(null, HEADER_XML
            + "<xlayer>"
                  + "<include path='/data/subfile1.xml' type='resource'/>"
                  + "<include path='/data/subfile2.xml' type='resource'/>"
                  + "<include path='/data/subfile3.xml' type='resource'/>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object var2 = manager.getVariable("myTestVariable2");
      Assert.assertNotNull(var2);
      Assert.assertTrue(Integer.class.isAssignableFrom(var2.getClass()));
      Assert.assertEquals(((Integer)var2), new Integer(36));
      Object var3 = manager.getVariable("myTestVariable3");
      Assert.assertNotNull(var3);
      Assert.assertTrue(Integer.class.isAssignableFrom(var3.getClass()));
      Assert.assertEquals(((Integer)var3), new Integer(42));
      Object var4 = manager.getVariable("myTestVariable4");
      Assert.assertNotNull(var4);
      Assert.assertTrue(Integer.class.isAssignableFrom(var4.getClass()));
      Assert.assertEquals(((Integer)var4), new Integer(55));
   }
   
   @Test
   public void ParserRecurseIncludeTest()
   {
      List<String> errors = manager.parseResource((Object)null, "/data/master2.xml");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object var1 = manager.getVariable("myTestVariable1");
      Assert.assertNotNull(var1);
      Assert.assertTrue(Integer.class.isAssignableFrom(var1.getClass()));
      Assert.assertEquals(((Integer)var1), new Integer(24));
      Object var3 = manager.getVariable("myTestVariable3");
      Assert.assertNotNull(var3);
      Assert.assertTrue(Integer.class.isAssignableFrom(var3.getClass()));
      Assert.assertEquals(((Integer)var3), new Integer(42));
      Object var5 = manager.getVariable("myTestVariable5");
      Assert.assertNotNull(var5);
      Assert.assertTrue(Integer.class.isAssignableFrom(var5.getClass()));
      Assert.assertEquals(((Integer)var5), new Integer(66));
      Object var6 = manager.getVariable("myTestVariable6");
      Assert.assertNotNull(var6);
      Assert.assertTrue(Integer.class.isAssignableFrom(var6.getClass()));
      Assert.assertEquals(((Integer)var6), new Integer(77));
   } 

   @Test
   public void ErrorIncludeFileTest()
   {
      List<String> errors = manager.parse(null, HEADER_XML
            + "<xlayer>"
                  + "<include path='unknown-file' type='resource'/>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
}
