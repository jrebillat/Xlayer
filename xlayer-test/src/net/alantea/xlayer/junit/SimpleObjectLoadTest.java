/**
 * 
 */
package net.alantea.xlayer.junit;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.alantea.xlayer.Manager;
import net.alantea.xlayer.test.BigRoot;
import net.alantea.xlayer.test.MethodsRoot;
import net.alantea.xlayer.test.SimpleRoot;

/**
 * Simplest tests.
 *
 * @author Jean
 */
public class SimpleObjectLoadTest
{
   
   /** The Constant HEADER_XML. */
   static final String HEADER_XML = "<?xml version=\"1.0\"?>\n";

   /**
    * Sets the up before class.
    *
    * @throws Exception the exception
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
      Manager.addPackage("net.alantea.xlayer.test");
   }

   /**
    * Load simple objects, only available by its field, then its setter.
    */
   @Test
   public void loadSimpleWrapper()
   {
      BigRoot root = new BigRoot();
      
      // An integer wrapper with an integer 'value' attribute only available by its field
      Manager.parse(root, HEADER_XML + "<simpleIntegerWrapper value='69'></simpleIntegerWrapper>");
      Assert.assertEquals(root.getSimpleIntegerWrapper().value, 69);

      // An integer wrapper with an integer 'value' subnode only available by its field
      Manager.parse(root, HEADER_XML + "<simpleIntegerWrapper><value>666</value></simpleIntegerWrapper>");
      Assert.assertEquals(root.getSimpleIntegerWrapper().value, 666);

      // A string wrapper with an string 'value' subnode only available by its field
      String value = "Hello World !";
      Manager.parse(root, HEADER_XML + "<simpleStringWrapper><value>" + value + "</value></simpleStringWrapper>");
      Assert.assertEquals(root.simpleStringWrapper.value, value);

      // An integer wrapper with an integer 'value' attribute only available by its wrapper
      Manager.parse(root, HEADER_XML + "<setIntegerWrapper value='42'></setIntegerWrapper>");
      Assert.assertEquals(root.getSetIntegerWrapper().value, 42);

      // An integer wrapper with an integer 'value' subnode only available by its wrapper
      Manager.parse(root, HEADER_XML + "<setIntegerWrapper><value>33</value></setIntegerWrapper>");
      Assert.assertEquals(root.getSetIntegerWrapper().value, 33);
   }

   /**
    * Load simple objects of various types.
    */
   @Test
   public void loadSimpleObjects()
   {
	   SimpleRoot root = new SimpleRoot();

	   Manager.parse(root, HEADER_XML + "<myInteger>123</myInteger>");
	   Assert.assertEquals(root.myInteger, 123);

	   Manager.parse(root, HEADER_XML + "<myDouble>123.456</myDouble>");
	   Assert.assertEquals(root.myDouble, 123.456);

	   Manager.parse(root, HEADER_XML + "<myFloat>456.123</myFloat>");
	   Assert.assertEquals(root.myFloat, 456.123f);

	   Manager.parse(root, HEADER_XML + "<myShort>69</myShort>");
	   Assert.assertEquals(root.myShort, (short)69);

	   Manager.parse(root, HEADER_XML + "<myByte>123</myByte>");
	   Assert.assertEquals(root.myByte, (byte)123);

	   //Manager.parse(root, HEADER_XML + "<myChar>33</myChar>");
	   //Assert.assertEquals(root.myChar, (char)33);

	   Manager.parse(root, HEADER_XML + "<myLong>4567890123</myLong>");
	   Assert.assertEquals(root.myLong, 4567890123L);

	   Manager.parse(root, HEADER_XML + "<myString>Hello World</myString>");
	   Assert.assertEquals(root.myString, "Hello World");

      Manager.parse(root, HEADER_XML + "<myString><constant class='SimpleRoot' name='MESSAGE'/></myString>");
      Assert.assertEquals(root.myString, SimpleRoot.MESSAGE);
   }

   /**
    * Load list of objects, available by an 'add' method then by a getXXX().add method.
    */
   @Test
   public void methodsTest()
   {
      MethodsRoot root = new MethodsRoot();

      Manager.parse(root, HEADER_XML + "<withOneParameter>666</withOneParameter>");
      Assert.assertEquals(root.intResult, 666);
      
      Manager.parse(root, HEADER_XML + "<withTwoParameters><integer>37</integer><integer>5</integer></withTwoParameters>");
      Assert.assertEquals(root.intResult, 42);
      
      Manager.parse(root, HEADER_XML + "<withThreeParameters><String>Good bye </String><String>Crual</String><String>World</String></withThreeParameters>");
      Assert.assertEquals(root.strResult, "Good bye Crual World");
      
      Manager.parse(root, HEADER_XML + "<withList><String>Rami </String><String>na</String><String>grobis</String></withList>");
      Assert.assertEquals(root.strResult, "Raminagrobis");
      
      Manager.parse(root, HEADER_XML + "<withIntAndList><integer>36</integer><list><String>Quai</String><String>des</String><String>Orfevres</String></list></withIntAndList>");
      Assert.assertEquals(root.strResult, "36 Quai des Orfevres");

      Manager.parse(root, HEADER_XML + "<withOneParameter><withReturn/></withOneParameter>");
      Assert.assertEquals(root.intResult, 12);
   }

   /**
    * Load list of objects, available by an 'add' method then by a getXXX().add method.
    */
   @Test
   public void loadListedWrapper()
   {
      BigRoot root = new BigRoot();

      Manager.parse(root, HEADER_XML + "<integersWrapper>"
            + "<integer>1</integer>"
            + "<integer>2</integer>"
            + "<integer>3</integer>"
            + "<integer>4</integer>"
            + "</integersWrapper>");
      Assert.assertEquals(root.getIntegers().values1.size(), 4);
      Assert.assertEquals(root.getIntegers().values1.get(0).intValue(), 1);
      Assert.assertEquals(root.getIntegers().values1.get(1).intValue(), 2);
      Assert.assertEquals(root.getIntegers().values1.get(2).intValue(), 3);
      Assert.assertEquals(root.getIntegers().values1.get(3).intValue(), 4);

      Manager.parse(root, HEADER_XML + "<integersWrapper>"
            + "<value>11</value>"
            + "<value>12</value>"
            + "<value>13</value>"
            + "<value>14</value>"
            + "<value>15</value>"
            + "</integersWrapper>");
      Assert.assertEquals(root.getIntegers().values2.size(), 5);
      Assert.assertEquals(root.getIntegers().values2.get(0).intValue(), 11);
      Assert.assertEquals(root.getIntegers().values2.get(1).intValue(), 12);
      Assert.assertEquals(root.getIntegers().values2.get(2).intValue(), 13);
      Assert.assertEquals(root.getIntegers().values2.get(3).intValue(), 14);
      Assert.assertEquals(root.getIntegers().values2.get(4).intValue(), 15);
   }
}
