/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * JPanel used to provide a visual preview of the room
 */
class Preview extends JPanel {

   private boolean showGrid;
   private JPopupMenu rtClickMenu;
   private int selected;

   // holds all the supported items in the room.
   protected Item[] items;

   /**
    * currently initializes with a few items I used to test the draw methods
    */
   protected Preview() {
      addMouseListener(new PreviewMouseListener(this));

      items = new Item[]{};
      showGrid = true;
      selected = -1;

      setPreferredSize(new Dimension(800, 608));
      setVisible(true);

      rtClickMenu = new JPopupMenu();

      JMenuItem del = new JMenuItem("delete");
      del.addActionListener((ActionEvent ae) -> {
         if (items.length > 0 && selected != -1 && items[selected] != null) {
            items[selected] = null;
            cleanArray(items);
            selected = -1;
            this.repaint();
         }
      });
      rtClickMenu.add(del);
   }

   /**
    * Recreates the items array and adds each valid item that gets created by
    * the script. Also supports simple use of variables to set xscale and
    * yscale.
    *
    * @param str {{x,y,objID},...}
    */
   protected void setItems(String[] str) {
      items = new Item[str.length];
      int i = 0;
      double d;
      int end;
      Item prev = null;

      for (String s : str) {
         end = (s.length() > 2 && s.charAt(s.length() - 1) == ';') ? (s.length()
                 - 2) : (s.length() - 1);

         if (s.length() > 1) {
            if (s.charAt(0) == 'o' && s.length() > 5) {
               switch (s.charAt(1)) {
                  case ' ': // o = insta...
                     items[i] = new ItemFromFile(s.substring(20, end).split(","));
                     prev = items[i];
                     break;

                  case '.': // o.image_...
                     if (prev != null) {
                        switch (s.substring(2, 14)) {
                           case "image_xscale":
                              d = Double.parseDouble(s.split(" = ")[1].
                                      split(";")[0]);
                              prev.setXscale(d);
                              break;

                           case "image_yscale":
                              d = Double.parseDouble(s.split(" = ")[1].
                                      split(";")[0]);
                              prev.setYscale(d);
                              break;

                           default:
                           // do nothing
                        }
                     }
                     break;

                  default:
                  // do nothing
               }

               i++;
            } else if (s.charAt(0) == 'i' && s.length() >= 20) {
               System.out.println(s);
               items[i] = new ItemFromFile(s.substring(16, end).split(","));
               i += 1;
            }
         }
      }

      paintComponent(getGraphics());
   }

   protected void toggleGrid() {
      showGrid = !showGrid;
   }

   // todo: draw order should be based on depth
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Object[] temp;

      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 800, 608);

      if (showGrid) {
         g.setColor(new Color(222, 222, 222, 255));
         g.drawLine(0, 0, 0, 608);
         g.drawLine(0, 0, 800, 0);
         g.drawLine(800, 0, 800, 608);
         g.drawLine(0, 608, 800, 608);

         for (int i = 31; i < 799; i += 32) {
            g.drawRect(i, 0, 1, 608);
         }

         for (int i = 31; i < 607; i += 32) {
            g.drawRect(0, i, 800, 1);
         }
      }

      temp = Arrays.copyOf(items, items.length);
      temp = cleanArray(temp);
      Arrays.sort(temp);

      for (Object i : temp) {
         if (i != null) {
            ((Item) i).draw(g);
         }
      }
   }

   private Object[] cleanArray(Object[] arr) {
      Object[] temp = new Object[]{};

      for (Object o : arr) {
         if (o != null) {
            temp = Arrays.copyOf(temp, temp.length + 1);
            temp[temp.length - 1] = o;
         }
      }

      return temp;
   }

   private class PreviewMouseListener implements MouseListener {

      private final Preview prev;

      public PreviewMouseListener(Preview prev) {
         super();

         this.prev = prev;
      }

      public void mouseReleased(MouseEvent e) {

         int xx, yy, index;
         int[] xArr, yArr;
         Polygon hitbox;
         Item item;

         if (SwingUtilities.isRightMouseButton(e)) {
            for (index = 0; index < items.length; index++) {
               item = items[index];

               if (item != null) {
                  xArr = Arrays.copyOf(item.xArr, item.xArr.length);
                  yArr = Arrays.copyOf(item.yArr, item.yArr.length);

                  for (int i = 0; i < xArr.length; i++) {
                     xArr[i] = (int)((double)xArr[i] * item.xScale) + item.x;
                     yArr[i] = (int)((double)yArr[i] * item.yScale) + item.y;;
                  }

                  hitbox = new Polygon(xArr, yArr, item.xArr.length);

                  if (hitbox.contains(e.getX(), e.getY())) {
                     selected = index;
                     rtClickMenu.show(prev, e.getX(), e.getY());
                     break;
                  }
               }
            }
         }
      }

      @Override
      public void mousePressed(MouseEvent me) {
      }

      @Override
      public void mouseClicked(MouseEvent me) {
      }

      @Override
      public void mouseEntered(MouseEvent me) {
      }

      @Override
      public void mouseExited(MouseEvent me) {
      }
   }
}
