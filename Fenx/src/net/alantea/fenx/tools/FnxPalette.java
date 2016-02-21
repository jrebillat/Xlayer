package net.alantea.fenx.tools;

import java.awt.Color;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FnxPalette extends SimpleObjectProperty<Color>
{
   private ObjectProperty<Color> complementaryColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> lighterColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> darkerColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> leftColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> rightColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> triLeftColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> triRightColor = new SimpleObjectProperty<Color>();
   private ObjectProperty<Color> grayColor = new SimpleObjectProperty<Color>();
   
   public FnxPalette(FnxPalette origin)
   {
      this(origin.get());
      origin.addListener((v, o, n) -> set(n));
   }
   
   public FnxPalette()
   {
      this(0.5, 0.5, 0.5, 1.0);
   }
   
   public FnxPalette(double r, double g, double b, double a)
   {
      this(new Color((float)r, (float)g, (float)b, (float)a));
   }
   
   public FnxPalette(String model)
   {
      this(Color.decode(model));
   }
   
   public FnxPalette(Color baseColor)
   {
      super(baseColor);
      addListener((o, oldV, newV) -> {
         calculateColors();
      });
      calculateColors();
   }

   public final ReadOnlyObjectProperty<Color> complementaryColorProperty()
   {
      return this.complementaryColor;
   }

   public final ReadOnlyObjectProperty<Color> lighterColorProperty()
   {
      return this.lighterColor;
   }

   public final ReadOnlyObjectProperty<Color> darkerColorProperty()
   {
      return this.darkerColor;
   }

   public final ReadOnlyObjectProperty<Color> leftColorProperty()
   {
      return this.leftColor;
   }

   public final ReadOnlyObjectProperty<Color> rightColorProperty()
   {
      return this.rightColor;
   }

   public final ReadOnlyObjectProperty<Color> triLeftColorProperty()
   {
      return this.triLeftColor;
   }

   public final ReadOnlyObjectProperty<Color> triRightColorProperty()
   {
      return this.triRightColor;
   }

   public final ReadOnlyObjectProperty<Color> grayColorProperty()
   {
      return this.grayColor;
   }

   private void calculateColors()
   {
      Color refColor = get();
      complementaryColor.set(deriveColor(0.5, 0.0, 0.0, 0.0));
      leftColor.set(deriveColor(-48/256.0, 0.0, 0.0, 0.0));
      rightColor.set(deriveColor(48/256.0, 0.0, 0.0, 0.0));
      triLeftColor.set(deriveColor(-96/256.0, 0.0, 0.0, 0.0));
      triRightColor.set(deriveColor(96/256.0, 0.0, 0.0, 0.0));
      grayColor.set(deriveColor(0.0, -1.0, 0.0, 0.0));
      darkerColor.set(refColor.darker());
      lighterColor.set(refColor.brighter());
   }
   
   public Color deriveColor(double dH, double dS, double dB, double dA) {
      float hsb[] = Color.RGBtoHSB(
              get().getRed(), get().getGreen(), get().getBlue(), null);

      hsb[0] += dH;
      hsb[1] += dS;
      hsb[2] += dB;
      
      while(hsb[0] > 1.0)
      {
         hsb[0] -= 1.0;
      }
      
      while(hsb[0] < 0.0)
      {
         hsb[0] += 1.0;
      }
      Color tempColor = Color.getHSBColor(
              hsb[0] < 0? 0 : (hsb[0] > 1? 1 : hsb[0]),
              hsb[1] < 0? 0 : (hsb[1] > 1? 1 : hsb[1]),
              hsb[2] < 0? 0 : (hsb[2] > 1? 1 : hsb[2]));
      
      float[] values = tempColor.getRGBComponents(null);
      values[3] = (float) (get().getAlpha() / 256.0 + dA);
      
      return new Color(values[0], values[1], values[2],
            values[3] < 0? 0 : (values[3] > 1? 1 : values[3]));
  }
}
