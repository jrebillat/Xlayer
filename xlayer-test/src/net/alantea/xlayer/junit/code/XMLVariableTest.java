package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLVariableTest
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
   public void simpleVariableTest()
   {
      manager.parse(null, HEADER_XML
            + "<integer _put='myTestVariable'>"
            + "24"
            + "</integer>");
      Object object = manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Integer.class);
      Assert.assertEquals(object, new Integer(24));
   }

   @Test
   public void objectVariableTest()
   {
      manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myTestVariable'>"
            + "</ParsingRoot>");
      Object object = manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
   }

   @Test
   public void ListVariableTest()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = manager.parse(root, HEADER_XML
            + "<list _put='myTestVariable'>"
                  + "<integer>1</integer>"
                  + "<integer>2</integer>"
                  + "<integer>3</integer>"
                  + "<integer>4</integer>"
            + "</list>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Object object = manager.getVariable("myTestVariable");
      Assert.assertNotNull(object);
      Assert.assertTrue(List.class.isAssignableFrom(object.getClass()));
   }

   @Test
   public void useVariableTest()
   {
      // add variable
      manager.addVariable("firstVariable", "passed value");
      
      List<String> errors = manager.parse(null, HEADER_XML
            + "<variable name='secondVariable'><string _variable='firstVariable'/></variable>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Object object = manager.getVariable("secondVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), String.class);
      Assert.assertEquals(object, "passed value");
   }
}
