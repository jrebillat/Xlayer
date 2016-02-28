package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.ParsingRoot;

public class XMLStaticTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   }
   
   @Test
   public void callStaticTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<xlayer>"
            + "<static class='ParsingRoot' method='putValue'>32</static>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = ParsingRoot.getStaticValue();
      Assert.assertEquals(val, 32);
   }
   
   @Test
   public void callBadClassStaticTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<xlayer>"
            + "<static class='UnknownClass' method='putValue'>32</static>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
   
   @Test
   public void callBadMethodStaticTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<xlayer>"
            + "<static class='ParsingRoot' method='unknownMethod'>32</static>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
   
   @Test
   public void callBadValueTypeStaticTest()
   {
      List<String> errors = Manager.parse(null, HEADER_XML
            + "<xlayer>"
            + "<static class='ParsingRoot' method='putValue'>Test me</static>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertFalse(errors.isEmpty());
   }
}
