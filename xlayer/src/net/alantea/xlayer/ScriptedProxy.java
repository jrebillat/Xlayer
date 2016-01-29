package net.alantea.xlayer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * The Class ScriptedProxy.
 */
public class ScriptedProxy
{

   /** The manager. */
   private static ScriptEngineManager manager = new ScriptEngineManager();
   
   /** The engine. */
   private static ScriptEngine engine = manager.getEngineByName("JavaScript");

   /** The script. */
   private String script;

   /**
    * Instantiates a new scripted proxy.
    *
    * @param script the script
    */
   public ScriptedProxy(String script)
   {
      this.script = script;
   }

   /**
    * Do it.
    *
    * @param method the method
    * @param objects the objects
    * @return the object
    */
   public Object doIt(Method method, Object[] objects)
   {
      Method[] objectMeths = Object.class.getDeclaredMethods();
      for (Method objectMeth : objectMeths)
      {
         if (objectMeth.equals(method))
         {
            System.out.println(objectMeth.getName());
            return null;
         }
      }
      try
      {
         Map<String, Object> variables = Manager.getVariables();
         for (String key : variables.keySet())
         {
            engine.put(key, variables.get(key));
         }
         engine.put("methodName", method.getName());
         engine.put("methodArguments", objects);
         return engine.eval(script);
      }
      catch (ScriptException e)
      {
         throw new RuntimeException(e);
      }
   }

   /**
    * Do it.
    *
    * @param script the script to launch
    * @param objects the objects
    * @return the object
    */
   public static Object launch(Handler handler, String script, List<Object> objects)
   {
      try
      {
         Map<String, Object> variables = Manager.getVariables();
         for (String key : variables.keySet())
         {
            engine.put(key, variables.get(key));
         }
         engine.put("arguments", objects.toArray());
         return engine.eval(script);
      }
      catch (ScriptException e)
      {
         handler.addError("Bad script : " + e.getMessage());
         return null;
      }
   }

   /**
    * As.
    *
    * @param <T> the generic type
    * @param interfaceMask the interface mask
    * @return the t
    */
   public <T> T as(Class<T> interfaceMask)
   {
      return InterfaceBridgeProxy.as(interfaceMask, this);
   }
}