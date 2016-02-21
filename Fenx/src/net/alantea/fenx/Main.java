package net.alantea.fenx;

import net.alantea.xtend.XManager;
import net.alantea.xtend.Xception;

public class Main
{
  public static void main(String[] args)
  {
	    try
	    {
	      FnxApplication appli = XManager.loadAbstractExtension(FnxApplication.class, true);
	      appli.start();
	    }
	    catch (Xception e)
	    {
	      e.printStackTrace();
	    }
  }
}
