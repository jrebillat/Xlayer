/**
 * 
 */
package net.alantea.fenx.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.alantea.fenx.components.FnxBase;
import net.alantea.fenx.container.FnxContainer;

/**
 * The Class FnxProportionalLayout.
 *
 * @author Jean
 */
public class FnxProportionalLayout implements LayoutManager2
{
   /** the associated container */
   private FnxContainer container;
   
   /** The components map. */
   private Map<FnxBase, FnxProportionalConstraints> map;
   
   private List<FnxBase> validated;

   /**
    * Instantiates a new proportional layout.
    */
   private FnxProportionalLayout(FnxContainer cont)
   {
      container = cont;
      map = new HashMap<FnxBase, FnxProportionalConstraints>();
      validated = new ArrayList<FnxBase>();
   }
   
   public static void applyLayout(FnxContainer cont)
   {
      cont.setLayout(new FnxProportionalLayout(cont));
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
    */
   @Override
   public void layoutContainer(Container container)
   {
      if (!(container instanceof FnxContainer))
      {
         return;
      }
      validated.clear();
      
      for (FnxBase element : map.keySet())
      {
         validate((FnxContainer) container, element);
      }
   }
   
   private void validate(FnxContainer container, FnxBase element)
   {
      if (element == null)
      {
         return;
      }
      if (validated.contains(element))
      {
         return;
      }
      
      FnxProportionalConstraints constraints = map.get(element);
      validate(container, constraints.getYReference());
      validate(container, constraints.getWReference());
      validate(container, constraints.getHReference());
      
      Rectangle containerBounds = container.getBounds();
      int x = (int) (containerBounds.width * constraints.getXPercentage());
      int y = (int) (containerBounds.height * constraints.getYPercentage());
      int w = (int) (containerBounds.width * constraints.getWPercentage());
      int h = (int) (containerBounds.height * constraints.getHPercentage());

      FnxBase ref = constraints.getWReference();
      if (ref != null)
      {
         validate(container, ref);
         w += ref.getBounds().width;
      }
      ref = constraints.getHReference();
      if (ref != null)
      {
         validate(container, ref);
         h += ref.getBounds().height;
      }
      
      ref = constraints.getXReference();
      if (ref != null)
      {
         validate(container, ref);
         x += ref.getBounds().x;
         switch(constraints.getXPosition())
         {
            case FnxProportionalConstraints.BEFORE :
               x -= w;
               break;

            case FnxProportionalConstraints.AFTER :
               x += ref.getBounds().width;
               break;
         }
      }
      
      ref = constraints.getYReference();
      if (ref != null)
      {
         validate(container, ref);
         y += ref.getBounds().y;
         switch(constraints.getXPosition())
         {
            case FnxProportionalConstraints.BEFORE :
               y -= h;
               break;

            case FnxProportionalConstraints.AFTER :
               y += ref.getBounds().height;
               break;
         }
      }
      
      element.setBounds(x, y, w, h);
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
    */
   @Override
   public Dimension minimumLayoutSize(Container container)
   {
      return container.getBounds().getSize();
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
    */
   @Override
   public Dimension preferredLayoutSize(Container container)
   {
      return container.getBounds().getSize();
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
    */
   @Override
   public void removeLayoutComponent(Component component)
   {
      if (component instanceof FnxBase)
      {
         map.remove((FnxBase)component);
      }
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
    */
   @Override
   public void addLayoutComponent(String constraint, Component component)
   {
      addLayoutComponent((FnxBase)component, new FnxProportionalConstraints(constraint));
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
    */
   @Override
   public void addLayoutComponent(Component component, Object constraints)
   {
      if ((component instanceof FnxBase) &&(constraints instanceof FnxProportionalConstraints))
      {
         map.put((FnxBase)component, (FnxProportionalConstraints)constraints);
         ((FnxBase)component).setPalette(container.getPalette());
      }
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
    */
   @Override
   public float getLayoutAlignmentX(Container arg0)
   {
      return 0;
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
    */
   @Override
   public float getLayoutAlignmentY(Container arg0)
   {
      return 0;
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
    */
   @Override
   public void invalidateLayout(Container arg0)
   {
   }

   /* (non-Javadoc)
    * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
    */
   @Override
   public Dimension maximumLayoutSize(Container container)
   {
      return container.getBounds().getSize();
   }

}
