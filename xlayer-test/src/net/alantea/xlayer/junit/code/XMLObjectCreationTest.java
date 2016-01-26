package net.alantea.xlayer.junit.code;

import java.util.List;

import javax.swing.JFrame;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingDerivedClass;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLObjectCreationTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   }
   
   @Test
   public void defaultPackageCreationTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myparsingRoot'>"
            + "</ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myparsingRoot");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingRoot.class);
   }
   
   @Test
   public void registeredPackageCreationTest()
   {
      // after adding package
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<?package javax.swing?>"
            + "<JFrame _put='myJFrame'>"
            + "</JFrame>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myJFrame");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), JFrame.class);
   }
   
   @Test
   public void withClassCreationTest()
   {
      // after adding package
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myparsingRoot'>"
            + "<testObject _class='ParsingDerivedClass' _put='myParsingObject1'></testObject>"
            + "</ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      Object object = Manager.getVariable("myParsingObject1");
      Assert.assertNotNull(object);
      Assert.assertEquals(object.getClass(), ParsingDerivedClass.class);
   }
   
   @Test
   public void withNonExistingClassCreationTest()
   {
      // after adding package
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myparsingRoot'>"
            + "<testObject _class='NonExistingClass' _put='myParsingObject2'></testObject>"
            + "</ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
   
   @Test
   public void withInvalidClassCreationTest()
   {
      // after adding package
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<ParsingRoot _put='myparsingRoot'>"
            + "<testObject _class='ParsingRoot' _put='myParsingObject3'></testObject>"
            + "</ParsingRoot>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
}
