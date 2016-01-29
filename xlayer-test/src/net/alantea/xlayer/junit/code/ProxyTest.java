package net.alantea.xlayer.junit.code;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.junit.internal.TestMain;

public class ProxyTest
{
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";
   
   @BeforeClass
   public void beforeClass()
   {
      Manager.clearAll();
   } 

   @Test
   public void simpleVariableTest()
   {
      TestMain test = new TestMain();
      List<String> errors = Manager.parse(test, HEADER_XML
            + "<xlayer>"
            + "<?package path='net.alantea.xlayer.junit.internal' type='resource'?>"
            + " <setTestItf>"
            + "  <script as='TestInterface'>var a; a = methodArguments[0];</script>"
            + " </setTestItf>"
            + "</xlayer>");
      for (String err : errors)
      {
         System.out.println(err);
      }
      Assert.assertTrue(errors.isEmpty());
      int val = test.exec(128);
      Assert.assertEquals(val, 128);
   }
}
