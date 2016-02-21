package net.alantea.xtend;

public interface IExtension
{
  public Class<?> getExtendedInterface();
  
  public void addImplementation(Object object) throws Xception;

}
