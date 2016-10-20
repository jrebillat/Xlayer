package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class ParsingTest
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
   public void parseStringWithNullRootTest()
   {
      List<String> errors = manager.parse(null, HEADER_XML
            + "<integer _put='myTestVariable'>"
            + "24"
            + "</integer>");
      Assert.assertTrue(errors.isEmpty());
      Object object = manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Integer.class);
      Assert.assertEquals(object, new Integer(24));
   }
   
   @Test
   public void parseStringWithRootTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<value>24</value>");
      Assert.assertTrue(errors.isEmpty());
      Assert.assertEquals(root.getValue(), 24);
   }
   
   @Test
   public void parseNullStringTest()
   {
      List<String> errors = manager.parse(null, (String)null);
      Assert.assertTrue(!errors.isEmpty());
   }
   
   @Test
   public void parseResourceWithNullRootTest()
   {
      List<String> errors = manager.parseResource(null, "/data/parsingTest1.xml");
      Assert.assertTrue(errors.isEmpty());
      Object object = manager.getVariable("parsingTest1Variable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Integer.class);
      Assert.assertEquals(object, new Integer(24));
   }
   
   @Test
   public void parseResourceWithRootTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parseResource(root, "/data/parsingTest2.xml");
      Assert.assertTrue(errors.isEmpty());
      Assert.assertEquals(root.getValue(), 24);
   }
   
   @Test
   public void parseNullResourceTest()
   {
      List<String> errors = manager.parseResource(null, (String)null);
      Assert.assertTrue(!errors.isEmpty());
   }
   
   @Test
   public void parseFileWithNullRootTest()
   {
      List<String> errors = manager.parseFile(null, "resources/data/parsingTest1.xml");
      Assert.assertTrue(errors.isEmpty());
      Object object = manager.getVariable("parsingTest1Variable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Integer.class);
      Assert.assertEquals(object, new Integer(24));
   }
   
   @Test
   public void parseFileWithRootTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parseFile(root, "resources/data/parsingTest2.xml");
      Assert.assertTrue(errors.isEmpty());
      Assert.assertEquals(root.getValue(), 24);
   }
   
   @Test
   public void parseNullFileTest()
   {
      List<String> errors = manager.parseFile(null, (String)null);
      Assert.assertTrue(!errors.isEmpty());
   }
}
