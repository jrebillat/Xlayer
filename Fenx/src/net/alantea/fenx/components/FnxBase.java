package net.alantea.fenx.components;

import java.awt.Color;

import javax.swing.JComponent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.alantea.fenx.FnxApplication;
import net.alantea.fenx.tools.FnxPalette;

public abstract class FnxBase extends JComponent implements ChangeListener<Color>
{
   private static final long serialVersionUID = 1L;
   
   private FnxPalette palette = new FnxPalette(FnxApplication.getDefaultPalette());
   
   private ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<Color>();
   
   private ObjectProperty<Color> foregroundColor = new SimpleObjectProperty<Color>();

   public FnxBase()
   {
      super();
      palette.addListener(this);
      backgroundColor.addListener((v, o, n) -> repaint());
      foregroundColor.addListener((v, o, n) -> repaint());
   }
   
   public final void setPalette(FnxPalette newPalette)
   {
      if (newPalette != null)
      {
         palette.removeListener(this);
         palette.unbind();
         palette = new FnxPalette(newPalette);
         palette.addListener(this);
         setColors();
      }
   }
   
   public final FnxPalette getPalette()
   {
      return palette;
   }
   
   protected abstract void setColors();
   
   protected final void setBackgroundColor(Color color)
   {
      backgroundColor.unbind();
      backgroundColor.set(color);
   }
   
   protected final void setForegroundColor(Color color)
   {
      foregroundColor.unbind();
      foregroundColor.set(color);
   }
   
   protected final Color getBackgroundColor()
   {
      return backgroundColor.get();
   }
   
   protected final Color getForegroundColor()
   {
      return foregroundColor.get();
   }

   @Override
   public final void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue)
   {
      setColors();
   }

}
