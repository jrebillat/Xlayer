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
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   }
   
   @Test
   public void addPackageTest()
   {
      // after adding package
      Manager.parse(null, HEADER_XML
            + "<?package javax.swing?>"
            + "<JFrame _put='myJFrame'>"
            + "</JFrame>");
      Object object = Manager.getVariable("myJFrame");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), JFrame.class);
   }
   
   @Test
   public void addBadPackageTest()
   {
      // without adding package
      Manager.parse(null, HEADER_XML
            + "<Container _put='myComponent'>"
            + "</Container>");
      Object object = Manager.getVariable("myComponent");
      Assert.assertNull(object);
      
      // with adding package
      Manager.parse(null, HEADER_XML
            + "<?package java.awt?>"
            + "<Container _put='myComponent'>"
            + "</Container>");
      object = Manager.getVariable("myComponent");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), Container.class);
   }
}
