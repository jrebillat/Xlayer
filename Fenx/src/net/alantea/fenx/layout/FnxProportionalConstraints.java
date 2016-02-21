package net.alantea.fenx.layout;

import net.alantea.fenx.components.FnxBase;

public class FnxProportionalConstraints
{
   public static final int SAME = 1;
   public static final int AFTER = 2;
   public static final int BEFORE = 3;
   
   private double xPercentage;
   private double yPercentage;
   private double wPercentage;
   private double hPercentage;
   
   private FnxBase xReference;
   private FnxBase yReference;
   private FnxBase wReference;
   private FnxBase hReference;
   
   private int xPosition;
   private int yPosition;

   public FnxProportionalConstraints()
   {
      xPercentage = 0.0;
      yPercentage = 0.0;
      wPercentage = 1.0;
      hPercentage = 1.0;
   }
   
   public FnxProportionalConstraints(double x, double y, double w, double h)
   {
      xPercentage = x;
      yPercentage = y;
      wPercentage = w;
      hPercentage = h;
   }

   public FnxProportionalConstraints(String constraints)
   {
      // TODO Auto-generated constructor stub
   }
   
   public void setXReference(FnxBase x, int position)
   {
      xReference = x;
      xPosition = position;
   }
   
   public void setYReference(FnxBase y, int position)
   {
      yReference = y;
      yPosition = position;
   }
   
   public void setWReference(FnxBase w)
   {
      wReference = w;
   }
   
   public void setHReference(FnxBase h)
   {
      hReference = h;
   }
   
   public double getXPercentage()
   {
      return xPercentage;
   }
   
   public double getYPercentage()
   {
      return yPercentage;
   }
   
   public double getWPercentage()
   {
      return wPercentage;
   }
   
   public double getHPercentage()
   {
      return hPercentage;
   }
   
   public FnxBase getXReference()
   {
      return xReference;
   }
   
   public FnxBase getYReference()
   {
      return yReference;
   }
   
   public FnxBase getWReference()
   {
      return wReference;
   }
   
   public FnxBase getHReference()
   {
      return hReference;
   }
   
   public int getXPosition()
   {
      return xPosition;
   }
   
   public int getYPosition()
   {
      return yPosition;
   }
}
