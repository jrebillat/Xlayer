package net.alantea.xlayer.junit.code;

import java.util.List;

import javax.swing.JFrame;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingBaseClass;
import net.alantea.xlayer.junit.internal.ParsingBaseClass.State;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLConstantTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   }
   
   @Test
   public void loadConstant1Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<value>"
            + "<constant class='javax.swing.JFrame' name='EXIT_ON_CLOSE' />"
            + "</value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, JFrame.EXIT_ON_CLOSE);
   }
   
   @Test
   public void loadConstant2Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<value>"
            + "<constant class='ParsingBaseClass' name='TEST_VALUE' />"
            + "</value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, ParsingBaseClass.TEST_VALUE);
   }
   
   @Test
   public void loadConstant3Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<value>"
            + "<constant class='ParsingBaseClass.InnerClass' name='TEST_VALUE_2' />"
            + "</value>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = root.getValue();
      Assert.assertEquals(val, ParsingBaseClass.InnerClass.TEST_VALUE_2);
   }
   
   @Test
   public void loadConstant4Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<stateValue>"
            + "<constant class='net.alantea.xlayer.junit.internal.ParsingBaseClass.State' name='OFF' />"
            + "</stateValue>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      State val = root.getStateValue();
      Assert.assertEquals(val, ParsingBaseClass.State.OFF);
   }
   
   @Test
   public void loadInvalidConstant1Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<stateValue>"
            + "<constant class='Completely.unknown.class' name='OFF' />"
            + "</stateValue>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
   
   @Test
   public void loadInvalidConstant2Test()
   {
      ParsingRoot root = new ParsingRoot();
      List<String> errors = Manager.parse(root, HEADER_XML
            + "<stateValue>"
            + "<constant class='net.alantea.xlayer.junit.internal.ParsingBaseClass.State' name='UNKNOWN_CONSTANT' />"
            + "</stateValue>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
}
