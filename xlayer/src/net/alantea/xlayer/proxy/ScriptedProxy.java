package net.alantea.xlayer.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.alantea.xlayer.Handler;

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
   
   /** The Handler. */
   private Handler handler;

   /**
    * Instantiates a new scripted proxy.
    *
    * @param handler the handler
    * @param script the script
    */
   public ScriptedProxy(Handler handler, String script)
   {
      this.script = script;
      this.handler = handler;
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
            return null;
         }
      }
      try
      {
         Map<String, Object> variables = handler.getManager().getVariables();
         for (String key : variables.keySet())
         {
            engine.put(key, variables.get(key));
         }
         engine.put("handler", handler);
         engine.put("manager", handler.getManager());
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
    * @param handler the handler
    * @param script the script to launch
    * @return the object
    */
   public static Object launch(Handler handler, String script)
   {
      try
      {
         Map<String, Object> variables = handler.getManager().getVariables();
         for (String key : variables.keySet())
         {
            engine.put(key, variables.get(key));
         }
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