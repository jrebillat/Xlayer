package net.alantea.xlayer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.alantea.xlayer.util.PrimitiveUtils;

/**
 * The Class InterfaceBridgeProxy, used to make a bridge proxy between an interface and a script.
 */
public class InterfaceBridgeProxy implements InvocationHandler
{

   /** The proxee. */
   private Object proxee = null;

   /**
    * Instantiates a new interface bridge proxy.
    *
    * @param proxee the proxee
    */
   public InterfaceBridgeProxy(Object proxee)
   {
      this.proxee = proxee;
   }
   
   /**
    * Create Proxy instance to behave 'as' the given interface.
    *
    * @param <T> the generic type
    * @param asInterface the interface to behave as
    * @param bean the bean
    * @return the proxy instance
    */
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

   /* (non-Javadoc)
    * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
    */
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      // launch script
      Object ret = ((ScriptedProxy) (proxee)).doIt(method, args);
      if (ret != null)
      {
         if (method.getReturnType().isAssignableFrom(ret.getClass()))
         {
            return ret;
         }
         if (!PrimitiveUtils.verifyNotReservedContainer(ret.getClass().getSimpleName()))
         {
            return ret;
         }
      }
      return null;
   }
}