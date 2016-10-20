package net.alantea.xlayer.junit.code;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;

public class VariableRegistrationTest
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
   public void addVariableTest()
   {
      // Before adding variable
      Object object = manager.getVariable("myVariable");
      Assert.assertNull(object);
      
      // add variable
      manager.addVariable("myVariable", "testMe");
      
      // after adding package
      object = manager.getVariable("myVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), String.class);
      Assert.assertEquals(object, "testMe");
   }
   
   @Test
   public void getVariableTest()
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
   public void useVariableTest()
   {
      // add variable
      manager.addVariable("firstVariable", "passed value");
      
      manager.parse(null, HEADER_XML
            + "<variable name='secondVariable'><string _variable='firstVariable'/></variable>");
      Object object = manager.getVariable("secondVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), String.class);
      Assert.assertEquals(object, "passed value");
   }
   
   @Test
   public void redefineVariableTest()
   {
      // add variable
      manager.addVariable("firstVariable", "passed value");
      
      manager.parse(null, HEADER_XML
            + "<xlayer>"
            + "<variable name='secondVariable'><string _variable='firstVariable'/></variable>"
            + "<variable name='secondVariable'>Redefined</variable>"
            + "</xlayer>");
      Object object = manager.getVariable("secondVariable");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), String.class);
      Assert.assertEquals(object, "Redefined");
   }
}
