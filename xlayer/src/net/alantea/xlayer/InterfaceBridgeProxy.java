package net.alantea.xlayer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InterfaceBridgeProxy implements InvocationHandler
{
   // factory method
   @SuppressWarnings("unchecked")
   public static <T> T as(Class<T> asInterface, Object bean)
   {
      if (!asInterface.isInterface())
      {
         return null;
      }
      return (T) Proxy.newProxyInstance(bean.getClass().getClassLoader(), new Class[] { asInterface },
            new InterfaceBridgeProxy(bean));
   }

   private Object proxee = null;

   public InterfaceBridgeProxy(Object proxee)
   {
      this.proxee = proxee;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      Object ret = ((ScriptedProxy) (proxee)).doIt(method, args);
      if (ret != null)
      {
         if (method.getReturnType().isAssignableFrom(ret.getClass()))
         {
            return ret;
         }
         if (!Manager.verifyNotReservedContainer(ret.getClass().getSimpleName()))
         {
            return ret;
         }
      }
      return null;
   }
}