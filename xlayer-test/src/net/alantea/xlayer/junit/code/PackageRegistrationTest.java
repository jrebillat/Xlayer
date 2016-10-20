package net.alantea.xlayer.junit.code;

import java.awt.Container;
import java.util.List;

import javax.swing.JFrame;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;

public class PackageRegistrationTest
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
   public void addPackageTest()
   {
      // before add package
      List<String> errors = manager.parse(null, HEADER_XML
            + "<JFrame _put='packRoot'>"
            + "</JFrame>");
      Assert.assertFalse(errors.isEmpty());
      Object object = manager.getVariable("packRoot");
      Assert.assertNull(object);
      
      // add package
      manager.addPackage("javax.swing");
      
      // after adding package
      errors = manager.parse(null, HEADER_XML
            + "<JFrame _put='myJFrame'>"
            + "</JFrame>");
      Assert.assertTrue(errors.isEmpty());
      object = manager.getVariable("myJFrame");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), JFrame.class);
   }
   
   @Test
   public void addClassTest()
   {
      // add bad class
      try
      {
         manager.addClass("java.bad.package.className");
         Assert.fail("java.bad.package.className found. Why ?");
      }
      catch (ClassNotFoundException e)
      {
         // it is OK !
      }
      
      // before add class
      List<String> errors = manager.parse(null, HEADER_XML
            + "<Container _put='myComponent'>"
            + "</Container>");
      Assert.assertFalse(errors.isEmpty());
      Object object = manager.getVariable("myComponent");
      Assert.assertNull(object);
      
      // add class
      try
      {
         manager.addClass("java.awt.Container");
      }
      catch (ClassNotFoundException e)
      {
         Assert.fail("java.awt.Component not found");
      }
      
      // after adding class
      errors = manager.parse(null, HEADER_XML
            + "<Container _put='myComponent'>"
            + "</Container>");
      Assert.assertTrue(errors.isEmpty());
      object = manager.getVariable("myComponent");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Container.class);
   }
}
