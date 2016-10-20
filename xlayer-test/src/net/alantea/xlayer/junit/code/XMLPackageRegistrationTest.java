package net.alantea.xlayer.junit.code;

import java.awt.Container;

import javax.swing.JFrame;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;

public class XMLPackageRegistrationTest
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
      // after adding package
      manager.parse(null, HEADER_XML
            + "<?package javax.swing?>"
            + "<JFrame _put='myJFrame'>"
            + "</JFrame>");
      Object object = manager.getVariable("myJFrame");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), JFrame.class);
   }
   
   @Test
   public void addBadPackageTest()
   {
      // without adding package
      manager.parse(null, HEADER_XML
            + "<Container _put='myComponent'>"
            + "</Container>");
      Object object = manager.getVariable("myComponent");
      Assert.assertNull(object);
      
      // with adding package
      manager.parse(null, HEADER_XML
            + "<?package java.awt?>"
            + "<Container _put='myComponent'>"
            + "</Container>");
      object = manager.getVariable("myComponent");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Container.class);
   }
}
