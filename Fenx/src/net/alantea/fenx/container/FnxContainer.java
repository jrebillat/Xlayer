package net.alantea.fenx.container;

import java.awt.Graphics;

import net.alantea.fenx.components.FnxBase;

public class FnxContainer extends FnxBase
{
   private static final long serialVersionUID = 1L;

   public FnxContainer()
   {
      // TODO Auto-generated constructor stub
   }

   @Override
   protected void setColors()
   {
   }

   @Override
   public final void paint(Graphics g)
   {
      paintBackground(g);
      super.paint(g);
      paintOverlay(g);
   }

   protected void paintOverlay(Graphics g)
   {
   }

   protected void paintBackground(Graphics g)
   { 
   }
}
