/****************************************************************************************************/
//
//
// Copyright Alantea
/****************************************************************************************************/
package net.alantea.fenx;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

import net.alantea.fenx.tools.FnxPalette;

// TODO: Auto-generated Javadoc
/**
 * The Class FApplication.
 */
public abstract class FnxApplication extends JFrame
{
	private static final long serialVersionUID = 1L;

/** The Constant FULL_SCREEN. */
  public static final int FULL_SCREEN = -1;
  
  /** The instance. */
  private static FnxApplication instance;
  
  private static FnxPalette defaultPalette = new FnxPalette(0.5, 0.5, 0.5, 1.0);

  /**
   * Instantiates a new f application.
   */
  protected FnxApplication()
  {
    instance = this;
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
  
  /**
   * Get the required width for application. If size is too big for screen, it
   * will be reduced to FULL_SCREEN.
   * 
   * @return the size or FULL_SCREEN for full screen window.
   */
  protected int getRequiredWidth()
  {
	  return FULL_SCREEN;
  }

  /**
   * Get the required height for application. If size is too big for screen, it
   * will be reduced to FULL_SCREEN.
   * 
   * @return the size or FULL_SCREEN for full screen window.
   */
  protected int getRequiredHeight()
  {
	  return FULL_SCREEN;
  }
  
  /**
   * Generate content.
   */
  protected abstract void generateContent();
  
  /**
   * Gets the application name.
   *
   * @return the name
   */
  public String getName()
  {
	  return null;
  }
  
  /**
   * Gets the default palette.
   *
   * @return the palette
   */
  public static FnxPalette getDefaultPalette()
  {
     return defaultPalette;
  }

  /**
   * Gets the application.
   *
   * @return the application
   */
  public static FnxApplication getApplication() {return instance; }

  /**
   * Start.
   *
   * @param stage the stage
   */
  public final void start()
  {
    initializeSize();
    setTitleAndDecorations();
    generateContent();
    this.setVisible(true);
  }

  /**
   * Sets the title and decorations.
   *
   * @param stage the new title and decorations
   */
  private void setTitleAndDecorations()
  {
    String title = getName();
    if (title == null)
    {
    	setUndecorated(true);
    }
    else
    {
      setTitle(title);
    }
  }

  /**
   * Initialize size.
   *
   * @param stage the stage
   */
  private void initializeSize()
  {
	  GraphicsEnvironment
      .getLocalGraphicsEnvironment()
      .getDefaultScreenDevice()
      .getDefaultConfiguration()
      .getBounds();

    Rectangle effectiveScreenArea = GraphicsEnvironment
    	      .getLocalGraphicsEnvironment()
    	      .getDefaultScreenDevice()
    	      .getDefaultConfiguration()
    	      .getBounds();

    int x = effectiveScreenArea.x;
    int y = effectiveScreenArea.y;
    int w = getRequiredWidth();
    if ((w == FnxApplication.FULL_SCREEN) || (w > effectiveScreenArea.width))
    {
      w = effectiveScreenArea.width;
    }
    else
    {
      x = (effectiveScreenArea.width - effectiveScreenArea.x - w) / 2;
    }
    int h = getRequiredHeight();
    if ((h == FnxApplication.FULL_SCREEN) || (h > effectiveScreenArea.height))
    {
      h = effectiveScreenArea.height;
    }
    else
    {
      y = (effectiveScreenArea.height - effectiveScreenArea.y - h) / 2;
    }

    setBounds(x, y, w, h);
  }
}
