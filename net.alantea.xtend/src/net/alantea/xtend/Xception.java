package net.alantea.xtend;

public class Xception extends Exception
{

  private static final long serialVersionUID = 1L;

  public enum Why
  {
    NO_EXTENSION,
    MULTIPLE_EXTENSION,
    BAD_CONSTRUCTOR,
    NOT_IMPLEMENTED,
    BAD_EXTENSION,
    NO_BUNDLE,
    EXTENSION_ERROR
  }

  private Why why;

  public Xception(Why type)
  {
    super();
    why = type;
  }

  public Xception(Why type, String message)
  {
    this(type, message, null);
  }

  public Xception(String message)
  {
    this(Why.EXTENSION_ERROR, message, null);
  }

  public Xception(String message, Exception root)
  {
    this(Why.EXTENSION_ERROR, message, root);
  }

  public Xception(Why type, String message, Exception root)
  {
    super(message, root);
    why = type;
  }

  Why getWhy()
  {
    return why;
  }
}
