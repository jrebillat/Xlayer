package net.alantea.fenx.components;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Timer;

import net.alantea.fenx.container.FnxContainer;

public class FnxSmoothScrollPane extends FnxContainer implements ActionListener, MouseListener, MouseMotionListener
{
   private static final long serialVersionUID = 1L;

   private static final int FINGER_TOUCH_POINT = 5;
   private static final int DELAY = 20;
   
   // flags used to turn on/off draggable scrolling directions
   public static final int DRAGGABLE_HORIZONTAL_SCROLL_BAR = 1;
   public static final int DRAGGABLE_VERTICAL_SCROLL_BAR = 2;

   // the draggable component
   private final Component draggableComponent;
   
   private Point startPoint;
   private Point endPoint;
   private Point startLocation = new Point();
   private Point endLocation = new Point();
   private int dx;
   private int dy;
   private Timer timer = new Timer(DELAY, this);

   // flag which defines the draggable scroll directions
   private int scrollBarMask = DRAGGABLE_HORIZONTAL_SCROLL_BAR | DRAGGABLE_VERTICAL_SCROLL_BAR;

   public FnxSmoothScrollPane(FnxBase component)
   {
      setLayout(new FlowLayout());
      draggableComponent = component;
      add(component);
      addMouseListener(this);
      addMouseMotionListener(this);
   }

   /**
    * 
    * Sets the Draggable elements - the Horizontal or Vertical Direction. One
    * can use a bitmasked 'or' (HORIZONTAL_SCROLL_BAR | VERTICAL_SCROLL_BAR ).
    * @param mask One of HORIZONTAL_SCROLL_BAR, VERTICAL_SCROLL_BAR, or HORIZONTAL_SCROLL_BAR |
    *           VERTICAL_SCROLL_BAR
    */
   public void setDraggableDirections(int mask)
   {
      scrollBarMask = mask;
   }

   private void scrollSmoothly(MouseEvent e)
   {
      int i = getMaximumDraggedPosition(startLocation, e.getLocationOnScreen());
      if (i > FINGER_TOUCH_POINT)
      {
         System.out.println("start" + draggableComponent.getBounds().x + " " + draggableComponent.getBounds().y + " / " + endLocation.x + " " + endLocation.y);
         timer.start();
      }
   }

   private int getMaximumDraggedPosition(Point startPoint, Point endPoint)
   {
      return getMax(getMax(startPoint.x, endPoint.x), getMax(startPoint.y, endPoint.y));
   }

   private int getMax(int start, int end)
   {
      return Math.abs(start - end);
   }

   @Override
   protected void processMouseMotionEvent(MouseEvent e)
   {
      if (e.getID() == MouseEvent.MOUSE_DRAGGED)
      {
         Rectangle r = draggableComponent.getBounds();
         endPoint = e.getPoint();
         
         // scroll the x axis
         if ((scrollBarMask & DRAGGABLE_HORIZONTAL_SCROLL_BAR) != 0)
         {
            dx = (endPoint.x - startPoint.x);
            endLocation.x = r.x + dx;
         }
         
         // scroll the y axis
         if ((scrollBarMask & DRAGGABLE_VERTICAL_SCROLL_BAR) != 0)
         {
            dy = (endPoint.y - startPoint.y);
            endLocation.y = r.y + dy;
         }
         
         verifyLimits();
         
         System.out.println(r.x + dx + " " + (r.y + dy) + " / " + endLocation.x + " " + endLocation.y);
         Rectangle aRect = new Rectangle(r.x + dx, r.y + dy, r.width, r.height);
         draggableComponent.setBounds(aRect);
         startPoint.x = endPoint.x;
         startPoint.y = endPoint.y;
      }
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      Rectangle r = draggableComponent.getBounds();
      int deltaX = endLocation.x - r.x;
      int deltaY = endLocation.y - r.y;
      int dbldx = (int) Math.ceil(deltaX / 5.0);
      int dbldy = (int) Math.ceil(deltaY / 5.0);
      
      int newX = r.x + dbldx;
      int newY = r.y + dbldy;

      System.out.println("-> " + newX + " " + newY + " / " + endLocation.x + " " + endLocation.y);
      Rectangle newRect = new Rectangle(newX, newY, r.width, r.height);
      draggableComponent.setBounds(newRect);
      if (r.equals(newRect))
      {
         timer.stop();
      }
   }

   @Override
   public void mouseDragged(MouseEvent e)
   {
      Rectangle r = draggableComponent.getBounds();
      endPoint = e.getPoint();
      
      // scroll the x axis
      if ((scrollBarMask & DRAGGABLE_HORIZONTAL_SCROLL_BAR) != 0)
      {
         dx = (endPoint.x - startPoint.x);
         endLocation.x = r.x + dx;
      }
      
      // scroll the y axis
      if ((scrollBarMask & DRAGGABLE_VERTICAL_SCROLL_BAR) != 0)
      {
         dy = (endPoint.y - startPoint.y);
         endLocation.y = r.y + dy;
      }
      
      verifyLimits();
      
      Rectangle aRect = new Rectangle(r.x + dx, r.y + dy, r.width, r.height);
      draggableComponent.setBounds(aRect);
      startPoint.x = endPoint.x;
      startPoint.y = endPoint.y;
   }

   @Override
   public void mouseMoved(MouseEvent e)
   {      
   }

   @Override
   public void mouseClicked(MouseEvent e)
   {      
   }

   @Override
   public void mouseEntered(MouseEvent e)
   {      
   }

   @Override
   public void mouseExited(MouseEvent e)
   {      
   }

   @Override
   public void mousePressed(MouseEvent e)
   {
      timer.stop();
      startPoint = e.getPoint();
      Rectangle r = draggableComponent.getBounds();
      startLocation.x = r.x;
      startLocation.y = r.y;
      endLocation.x = r.x;
      endLocation.y = r.y;
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
      endLocation.x = endLocation.x +20 * dx;
      endLocation.y = endLocation.y + 20 * dy;
      System.out.println("delta " + ((endLocation.x - startLocation.x)) + " " + ((endLocation.y - startLocation.y)));
      verifyLimits();
      scrollSmoothly(e);
   }
   
   private void verifyLimits()
   {
      Rectangle r = draggableComponent.getBounds();

      if (endLocation.x > 0)
      {
         endLocation.x = 0;
      }
      if (endLocation.x < getBounds().width - r.width)
      {
         endLocation.x =  getBounds().width - r.width;
      }
      if (endLocation.y > 0)
      {
         endLocation.y = 0;
      }
      if (endLocation.y < getBounds().height - r.height)
      {
         endLocation.y = getBounds().height - r.height;
      }
   }
}