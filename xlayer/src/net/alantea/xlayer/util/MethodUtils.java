package net.alantea.xlayer.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;

public final class MethodUtils
{

   private MethodUtils()
   {
   }
   
   /**
    * Search a method w/one arg. This will search for a method whose name is constructed from
    * arguments. If found, runs it.
    *
    * @param cl the class used to search for method
    * @param target the target object to apply method on
    * @param prefix the prefix for method (like 'set' or 'add') or null for no prefix. It key is
    *           null, the method name will be 'prefix()'.
    * @param key the key containing the core method name. If a prefix exists, the name will be
    *           'prefixKey()'. If prefix is null, the name is 'key()'.
    * @param value the value to give to the method.
    * @return true, if successful
    */
   public static boolean searchAndRunMethod(Class<?> cl, Object target, String prefix, String key, Object value)
   {
      try
      {
         // elaborate method name
         String methName = null;
         if ((prefix != null) && (key != null))
         {
            methName = prefix + key.substring(0, 1).toUpperCase() + key.substring(1);
         }
         else if (key != null)
         {
            methName = key;
         }
         else if (prefix != null)
         {
            methName = prefix;
         }
         else
         {
            // No name : can't find a method.
            return false;
         }

         Method[] methods = cl.getMethods();
         for (Method method : methods)
         {
            if ((method.getName().equals(methName)) && (method.getParameterCount() == 1))
            {
               method.setAccessible(true);
               Parameter parameter = method.getParameters()[0];
               Class<?> clazz = parameter.getType();
               if ((PrimitiveUtils.testPrimitive(clazz)) && (value != null))
               {
                  Object parm = PrimitiveUtils.getSimpleObject(clazz, value.toString());
                  method.invoke(target, parm);
                  return true;
               }
               else
               {
                  method.invoke(target, value);
                  return true;
               }
            }
         }
      }
      catch (SecurityException | IllegalAccessException | InvocationTargetException e)
      {
         // let's go to other tries...
      }
      catch (IllegalArgumentException e)
      {
         // let's go to other tries...
      }

      Class<?> sup = cl.getSuperclass();
      if ((sup != null) && (!sup.equals(Object.class)))
      {
         return searchAndRunMethod(sup, target, prefix, key, value);
      }
      return false;
   }

   /**
    * Apply getKEYs().yyy() method chain.
    *
    * @param cl the class used to search for method
    * @param target the target object to apply the getKEYs() method on
    * @param key the key containing the KEY method name part. it will be converted with first
    *           character in uppercase
    * @param value the value to give to the method.
    * @return true, if successful
    */
   public static boolean applyGetAndAddMethod(Class<?> cl, Object target, String key, Object value)
   {
      // Search for a getXXXs() method
      try
      {
         String methName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "s";
         Method meth = cl.getMethod(methName);

         Object pack = meth.invoke(target);
         // analyse for parametrized class information
         Class<?> clazz = parametrizedReturnType(meth);
         Method method = null;
         Class<?> cl0 = clazz;
         while (method == null)
         {
            // search for add() method in returned type
            try
            {
               method = meth.getReturnType().getMethod("add", cl0);
            }
            catch (NoSuchMethodException e)
            {
               if (Object.class.equals(cl0))
               {
                  return false;
               }
               cl0 = cl0.getSuperclass();
            }
         }
         method.setAccessible(true);

         if (PrimitiveUtils.testPrimitive(clazz))
         {
            // apply method on a primitive argument
            Object parm = PrimitiveUtils.getSimpleObject(clazz, value.toString());
            method.invoke(pack, parm);
            return true;
         }
         else
         {
            // apply method on a complex argument
            method.invoke(target, value);
            return true;
         }
      }
      catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
            | InvocationTargetException e)
      {
         // nothing to do here...
      }
      return false;
   }

   /**
    * Adds the child object in the parent object.
    *
    * @param parent the parent
    * @param child the child
    * @return true, if successful
    */
   public static boolean addObjectInObject(Object parent, Object child)
   {
      // try using adding the object (ex: for use for adding AWT elements in a Container)
      @SuppressWarnings("unchecked")
      Set<Method> meths = ReflectionUtils.getAllMethods(parent.getClass(), ReflectionUtils.withName("add"),
            ReflectionUtils.withParametersCount(1));
      if (!meths.isEmpty())
      {
         for (Method method : meths)
         {
            Class<?>[] pcls = method.getParameterTypes();
            Class<?> pcl = pcls[0];
            if (pcl.isAssignableFrom(child.getClass()))
            {
               try
               {
                  method.invoke(parent, child);
               }
               catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
               {
                  return false;
               }
               break;
            }
         }
         return true;
      }
      return false;
   }

   /**
    * Search for a method.
    *
    * @param target the target object
    * @param methName the method name
    * @return true, if successful
    */
   @SuppressWarnings("unchecked")
   public
   static String searchMethod(Object target, String methName, int nbParms)
   {
      String root = methName.substring(0, 1).toUpperCase() + methName.substring(1);
      String realName = methName;
      Set<Method> methods = null;
      if (nbParms > -1)
      {
         methods = ReflectionUtils.getAllMethods(target.getClass(), ReflectionUtils.withName(methName),
               ReflectionUtils.withParametersCount(nbParms));
      }
      else
      {
         methods = ReflectionUtils.getAllMethods(target.getClass(), ReflectionUtils.withName(methName));
      }
      if ((methods.isEmpty()))
      {
         realName = "set" + root;
         if (nbParms > -1)
         {
            methods = ReflectionUtils.getAllMethods(target.getClass(), ReflectionUtils.withName(realName),
                  ReflectionUtils.withParametersCount(nbParms));
         }
         else
         {
            methods = ReflectionUtils.getAllMethods(target.getClass(), ReflectionUtils.withName(realName));
         }
      }
      if ((methods.isEmpty()))
      {
         realName = "add" + root;
         if (nbParms > -1)
         {
            methods = ReflectionUtils.getAllMethods(target.getClass(), ReflectionUtils.withName(realName),
                  ReflectionUtils.withParametersCount(nbParms));
         }
         else
         {
            methods = ReflectionUtils.getAllMethods(target.getClass(), ReflectionUtils.withName(realName));
         }
      }

      if (methods.isEmpty())
      {
         realName = null;
      }

      return realName;
   }
   
   /**
    * Apply method.
   *
   * @param target the target object
   * @param methName the method name
   * @param objects the objects to push to method
   * @return the information
   */
  @SuppressWarnings("unchecked")
  public
  static MethodReturnedInformation applyMethod(Object target, String methName, List<Object> objects)
  {
     boolean oneArg = false;
     boolean useArray = false;
     Class<?> targetClass = null;
     Object targetObject = target;
     if (target instanceof Class)
     {
        targetClass = (Class<?>) target;
        targetObject = null;
     }
     else
     {
        targetClass = target.getClass();
     }
     List<Class<?>> parmClasses = new ArrayList<>();
     for (Object object : objects)
     {
        if (object != null)
        {
           parmClasses.add(object.getClass());
        }
        else
        {
           parmClasses.add(Object.class);
        }
     }
     Set<Method> methods = ReflectionUtils.getAllMethods(targetClass, ReflectionUtils.withName(methName),
           ReflectionUtils.withParametersCount(objects.size()));

     Method meth = null;
     Class<?>[] pcls = null;
     if (!methods.isEmpty())
     {
        for (Method method : methods)
        {
           boolean found = false;
           pcls = method.getParameterTypes();
           if (pcls.length == objects.size())
           {
              found = true;
              
              for (int i = 0; i < pcls.length; i++)
              {
                 Class<?> pcl = pcls[i];
                 if ((!pcl.isAssignableFrom(parmClasses.get(i)))
                       && (!PrimitiveUtils.comparePrimitive(pcl, parmClasses.get(i)))
                       && (!(pcl.isArray() && (List.class.isAssignableFrom(parmClasses.get(i)) || pcls.length == 1)))
                       && (!((parmClasses.get(i).isArray() || pcls.length == 1) && List.class.isAssignableFrom(pcl)))
                    )
                 {
                    found = false;
                    break;
                 }
              }
              
              if (found)
              {
                 meth = method;
                 if ((meth.getParameterCount() == 1) && (meth.getParameterTypes()[0].isArray()))
                 {
                    oneArg = true;
                    useArray = true;
                 }
                 else if ((meth.getParameterCount() == 1)
                       && (List.class.isAssignableFrom(meth.getParameterTypes()[0])))
                 {
                    oneArg = true;
                    useArray = false;
                 }
                 break;
              }
           }
        }
     }
     else
     {
        // search for one List or Array parameter
        oneArg = true;
        methods = ReflectionUtils.getAllMethods(targetClass, ReflectionUtils.withName(methName),
              ReflectionUtils.withParametersCount(1));
        if (!methods.isEmpty())
        {
           meth = methods.toArray(new Method[0])[0];
           if ((meth.getParameterCount() == 1) && (meth.getParameterTypes()[0].isArray()))
           {
              useArray = true;
           }
           else if ((meth.getParameterCount() == 1)
                 && (List.class.isAssignableFrom(meth.getParameterTypes()[0])))
           {
              useArray = false;
           }
        }
     }
     return launchMethod(meth, methName, targetObject, pcls, objects, oneArg, useArray);
  }
  
  @SuppressWarnings("unchecked")
private static MethodReturnedInformation launchMethod(Method meth, String methName, Object target, Class<?>[] pcls,
        List<Object> objects, boolean oneArg, boolean useArray)
  {
     MethodReturnedInformation info = new MethodReturnedInformation();
     String parmInfo = "";
     if (meth != null)
     {
        try
        {
           if (oneArg)
           {
              Object ret = null;
              meth.setAccessible(true);
              
              // get rid of 'intermediate' unneeded List layer
              List<Object> objs = objects;
              if ((objects.size() == 1) && (List.class.isAssignableFrom(objects.get(0).getClass())))
              {
                 objs = (List<Object>) objects.get(0);
              }
              else if ((objects.size() == 1) && (objects.get(0).getClass().isArray()))
              {
                 objs = new ArrayList<>();
                 Object[] objArr = (Object[])objects.get(0);
                 for (int i = 0; i < objArr.length; i++)
                 {
                    objs.add(objArr[i]);
                 }
              }
              
              // Convert to array, then call
              if (useArray)
              {
                 Class<?> arrayComponentType = meth.getParameterTypes()[0].getComponentType();
                 Object[] arr = (Object[]) Array.newInstance(arrayComponentType, objs.size());
                 for (int i = 0; i < objs.size(); i++)
                 {
                    arr[i] = objs.get(i);
                 }
                 ret = meth.invoke(target, new Object[]{arr});
              }
              // call directly
              else
              {
                 ret = meth.invoke(target, objs);
              }
              info.setValue(ret);
              info.setNonvoid(!meth.getReturnType().equals(Void.class));
           }
           else
           {
              meth.setAccessible(true);
              Object[] parms = new Object[objects.size()];
              for (int i = 0; i < pcls.length; i++)
              {
                 Class<?> pcl = pcls[i];
                 parmInfo += " " + objects.get(i).getClass().getSimpleName() + " (" + pcl.getSimpleName() + ")";
                 Object obj = PrimitiveUtils.getSimpleObject(pcl, objects.get(i).toString());
                 if (obj != null)
                 {
                    parms[i] = obj;
                 }
                 else
                 {
                    parms[i] = objects.get(i);
                 }
              }
              Object ret = null;
              if (parms.length == 0)
              {
                 ret = meth.invoke(target);
              }
              else
              {
                 ret = meth.invoke(target, parms);
              }
              info.setValue(ret);
              info.setNonvoid(!meth.getReturnType().equals(Void.class));
           }
           info.setSuccess(true);
           return info;
        }
        catch (NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
           info.setSuccess(false);
           info.setErrorMessage("Error applying method " + methName + " :" + parmInfo);
           return info;
        }
     }
     info.setSuccess(false);
     info.setErrorMessage("Impossible to find method " + methName + " with correct arguments :" + parmInfo);
     return info;
  }

  /**
   * Get parametrized return type if any.
   *
   * @param method the method to analyze
   * @return the class for parametrized (or not) return type
   */
  private static Class<?> parametrizedReturnType(Method method)
  {
     Type returnType = method.getGenericReturnType();
     Class<?> clazz = method.getReturnType();
     if (returnType instanceof ParameterizedType)
     {
        ParameterizedType type = (ParameterizedType) returnType;
        Type[] typeArguments = type.getActualTypeArguments();
        if (typeArguments.length > 0)
        {
           clazz = (Class<?>) typeArguments[0];
        }
     }
     return clazz;
  }
}
