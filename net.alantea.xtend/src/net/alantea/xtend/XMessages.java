package net.alantea.xtend;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import net.alantea.xtend.Xception.Why;

/**
 * Class to manage messages from extensions.
 * 
 * @author Alantea
 * 
 */
public class XMessages
{
  private static Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

  private static Locale locale = Locale.getDefault();

  /** Private singleton constructor. */
  private XMessages()
  {
  }

  public static boolean addAssociatedBundle(Object object) throws Xception
  {
    try
    {
      return (manageBundle(object, true) != null);
    }
    catch (MissingResourceException e)
    {
        throw new Xception(Why.NO_BUNDLE, "Missing resource", e);
    }
  }

  public static ResourceBundle getBundle(Object object)
      throws MissingResourceException
  {
    return manageBundle(object, false);
  }

  private static ResourceBundle manageBundle(Object object, boolean storeIt)
      throws MissingResourceException
  {
    String name = object.getClass().getName();
    ResourceBundle bundle = null;

    bundle = bundles.get(name);
    if (bundle == null)
    {
      bundle = ResourceBundle.getBundle(name, locale, new UTF8Control());
      if (storeIt)
      {
         bundles.put(name, bundle);
      }
    }
    return bundle;
  }

  /**
   * Get a value as int from its key.
   * 
   * @param key to search
   * @return the int value or 0 if nothing is found
   */
  public static int getInteger(String key)
  {
    int ret = 0;
    String sVal = get(key);
    try
    {
       ret = Integer.parseInt(sVal);
    }
    catch (NumberFormatException e)
    {
      // nothing
    }
    
    return ret;
  }

  /**
   * Get a value as double from its key.
   * 
   * @param key to search
   * @return the double value or 0.0 if nothing is found
   */
  public static double getDouble(String key)
  {
    double ret = 0;
    String sVal = get(key);
    try
    {
       ret = Double.parseDouble(sVal);
    }
    catch (NumberFormatException e)
    {
      // nothing
    }
    
    return ret;
  }

  /**
   * Get a message from its key.
   * 
   * @param key
   *          to search
   * @return the message value or the key if nothing is found
   */
  public static String get(String key, String... args)
  {
    String ret = key;

    // search in registered bundles
    if ((ret == null) || (ret.equals(key)))
    {
      try
      {
        for (ResourceBundle bundle : bundles.values())
        {
          if ((ret == null) || (ret.equals(key)))
          {
            ret = bundle.getString(key);
          }
        }
      }
      catch (MissingResourceException e)
      {
        ret = key;
      }
    }

    // replace arguments
    for (int i = 0; i < args.length; i++)
    {
      String arg = args[i];
      if (arg == null)
      {
        arg = "'null'";
      }
      ret = ret.replaceAll("\\[" + (i + 1) + "\\]", arg);
    }
    return ret;
  }

}

/**
 * Control for getting bundle from an UTF-8 file. This work is got from the
 * article "How to use UTF-8 in resource properties with ResourceBundle" found
 * on internet at :
 * http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in
 * -resource-properties-with-resourcebundle
 *
 */
class UTF8Control extends Control
{
  /**
   * Bundle creation.
   * 
   * @throws IllegalAccessException
   *           when raised.
   * @throws InstantiationException
   *           when raised.
   * @throws IOException
   *           when raised.
   */
  public ResourceBundle new1Bundle(String baseName, Locale locale,
      String format, ClassLoader loader, boolean reload)
      throws IllegalAccessException, InstantiationException, IOException
  {
    // The below is a copy of the default implementation.
    String bundleName = toBundleName(baseName, locale);
    String resourceName = "/" + toResourceName(bundleName, "properties");
    ResourceBundle bundle = null;
    InputStream stream = null;

    stream = XMessages.class.getResourceAsStream(resourceName);

    if (stream == null)
    {
      resourceName = "/" + toResourceName(baseName, "properties");
      stream = XMessages.class.getResourceAsStream(resourceName);

    }

    if (stream != null)
    {
      try
      {
        // Only this line is changed to make it to read properties files as
        // UTF-8.
        bundle = new PropertyResourceBundle(new InputStreamReader(stream,
            "UTF-8"));
      }
      finally
      {
        stream.close();
      }
    }
    return bundle;
  }
}
