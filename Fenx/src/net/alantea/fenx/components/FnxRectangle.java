package net.alantea.fenx.components;

import java.awt.Graphics;

public class FnxRectangle extends FnxBase
{
   private static final long serialVersionUID = 1L;

   public FnxRectangle()
   {
   }

   protected void setColors()
   {
      setBackgroundColor(getPalette().get());
   }
   
   @Override
   public void paint(Graphics g)
   {
      g.setColor(getBackgroundColor());
      g.fillRect(0, 0, getBounds().width, getBounds().height);
   }
   
}
