package net.alantea.xlayer.junit.internal;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ParsingRoot.
 */
public class ParsingRoot
{
   /** The value. */
   int value;

   /** The test object. */
   ParsingBaseClass testObject;
   
   /** The state. */
   ParsingBaseClass.State stateValue;
   
   /** The list. */
   List<Integer> myList;
   
   /** The object list. */
   List<ParsingBaseClass> myObjectList;
   
   /** The list list. */
   List<List<ParsingBaseClass>> myListList;
   
   /**
    * Gets the value.
    *
    * @return the value
    */
   public int getValue()
   {
      return value;
   }

   /**
    * Sets the value.
    *
    * @param value the new value
    */
   public void setValue(int value)
   {
      this.value = value;
   }

   public ParsingBaseClass getTestObject()
   {
      return testObject;
   }

   public void setTestObject(ParsingBaseClass object)
   {
      this.testObject = object;
   }
   
   public int loadValue()
   {
      return 42;
   }
   
   public void changeValue(int val)
   {
      value = val;
   }
   
   public void changeTestObject(ParsingBaseClass val)
   {
      testObject = val;
   }
   
   public void changeBoth(int val1, ParsingBaseClass val2)
   {
      value = val1;
      testObject = val2;
   }
   
   public int sum(List<Integer> values)
   {
      int ret = 0;
      for (Integer val : values)
      {
         ret += val;
      }
      return ret;
   }
   public int setSum(ParsingBaseClass val1, List<Integer> values)
   {
      testObject = val1;
      int ret = 0;
      for (Integer val : values)
      {
         ret += val;
      }
      return ret;
   }

   public ParsingBaseClass.State getStateValue()
   {
      return stateValue;
   }

   public void setStateValue(ParsingBaseClass.State state)
   {
      this.stateValue = state;
   }

   /**
    * Gets the list.
    *
    * @return the list
    */
   public List<Integer> getMyList()
   {
      return myList;
   }

   /**
    * Sets the list.
    *
    * @param list the new list
    */
   public void setMyList(List<Integer> list)
   {
      this.myList = list;
   }

   /**
    * @return the myObjectList
    */
   public List<ParsingBaseClass> getMyObjectList()
   {
      return myObjectList;
   }

   /**
    * @param myObjectList the myObjectList to set
    */
   public void setMyObjectList(List<ParsingBaseClass> myObjectList)
   {
      this.myObjectList = myObjectList;
   }

   /**
    * @return the myListList
    */
   public List<List<ParsingBaseClass>> getMyListList()
   {
      return myListList;
   }

   /**
    * @param myListList the myListList to set
    */
   public void setMyListList(List<List<ParsingBaseClass>> myListList)
   {
      this.myListList = myListList;
   }
}
