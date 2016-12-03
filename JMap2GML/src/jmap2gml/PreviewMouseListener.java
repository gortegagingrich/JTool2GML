/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import javax.swing.SwingUtilities;

/**
 *
 * @author Gabriel
 */
public class PreviewMouseListener implements MouseListener {

   private final Preview prev;

   public PreviewMouseListener(Preview prev) {
      super();

      this.prev = prev;
   }

   @Override
   public void mouseReleased(MouseEvent e) {

      int xx, yy, index;
      int[] xArr, yArr;
      Polygon hitbox;
      Item item;

      if (SwingUtilities.isRightMouseButton(e)) {
         for (index = 0; index < prev.items.length; index++) {
            item = prev.items[index];

            if (item != null) {
               xArr = Arrays.copyOf(item.xArr, item.xArr.length);
               yArr = Arrays.copyOf(item.yArr, item.yArr.length);

               for (int i = 0; i < xArr.length; i++) {
                  xArr[i] = (int) ((double) xArr[i] * item.xScale) + item.x;
                  yArr[i] = (int) ((double) yArr[i] * item.yScale) + item.y;
               }

               hitbox = new Polygon(xArr, yArr, item.xArr.length);

               if (hitbox.contains(e.getX(), e.getY())) {
                  prev.selected = index;
                  prev.rtClickMenu.show(prev, e.getX(), e.getY());
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
