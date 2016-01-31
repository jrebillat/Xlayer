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
   
   /** The array. */
   Integer[] myArray;
   
   /** The object array. */
   ParsingBaseClass[] myObjectArray;
   
   /** The list list. */
   List<List<ParsingBaseClass>> myListList;
   
   /** The array list. */
   List<ParsingBaseClass[]> myArrayList;
   
   /** The array array. */
   ParsingBaseClass[][] myArrayArray;
   
   /** The list array. */
   List<ParsingBaseClass>[] myListArray;
   
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
    * Gets the array.
    *
    * @return the list
    */
   public Integer[] getMyArray()
   {
      return myArray;
   }

   /**
    * Sets the array.
    *
    * @param arr the new array
    */
   public void setMyArray(Integer[] arr)
   {
      this.myArray = arr;
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
    * @return the myObjectArray
    */
   public ParsingBaseClass[] getMyObjectArray()
   {
      return myObjectArray;
   }

   /**
    * @param myObjectArray the myObjectArray to set
    */
   public void setMyObjectArray(ParsingBaseClass[] myObjectArray)
   {
      this.myObjectArray = myObjectArray;
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

   /**
    * @return the myArrayArray
    */
   public ParsingBaseClass[][] getMyArrayArray()
   {
      return myArrayArray;
   }

   /**
    * @param myArrayArray the myArrayArray to set
    */
   public void setMyArrayArray(ParsingBaseClass[][] myArrayArray)
   {
      this.myArrayArray = myArrayArray;
   }

   /**
    * @return the myArrayList
    */
   public List<ParsingBaseClass[]> getMyArrayList()
   {
      return myArrayList;
   }

   /**
    * @param myArrayList the myArrayList to set
    */
   public void setMyArrayList(List<ParsingBaseClass[]> myArrayList)
   {
      this.myArrayList = myArrayList;
   }

   /**
    * @return the myListArray
    */
   public List<ParsingBaseClass>[] getMyListArray()
   {
      return myListArray;
   }

   /**
    * @param myListArray the myListArray to set
    */
   public void setMyListArray(List<ParsingBaseClass>[] myListArray)
   {
      this.myListArray = myListArray;
   }
}
