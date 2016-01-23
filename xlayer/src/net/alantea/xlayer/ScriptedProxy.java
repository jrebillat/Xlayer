package net.alantea.xlayer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptedProxy
{

   private static ScriptEngineManager manager = new ScriptEngineManager();
   private static ScriptEngine engine = manager.getEngineByName("JavaScript");
   
   private String script;
   
   public ScriptedProxy(String script)
   {
      this.script = script;
   }

   public void doIt(Object object)
   {
      try
      {
         engine.eval(script);
      }
      catch (ScriptException e)
      {
         throw new RuntimeException(e);
      }
   }

   public <T> T as( Class<T> interfaceMask ){
       return InterfaceBridgeProxy.as( interfaceMask, this );
   }
}

class InterfaceBridgeProxy implements InvocationHandler {

   // factory method
   @SuppressWarnings("unchecked")
   public static <T> T as( Class<T> asInterface, Object bean ){
       return
           (T) Proxy.newProxyInstance(
                   bean.getClass().getClassLoader(),
                   new Class[]{asInterface},
                   new InterfaceBridgeProxy( bean ) );
   }

   private Object proxee = null;

   public InterfaceBridgeProxy( Object proxee ){
       this.proxee = proxee;
   }

   public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
      ((ScriptedProxy)(proxee)).doIt(null);
      return null;
   }
}