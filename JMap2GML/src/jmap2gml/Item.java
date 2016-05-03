/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

/**
 *
 * @author gabrielo
 */
class Item {

        private int x, y;
        private double image_xscale, image_yscale;
        private final String type;

        /**
         *
         * @param str {arg0, arg1, arg2}
         */
        protected Item(String[] str) {
            assert str.length == 3;
            try {
                x = Integer.parseInt(str[0]);
                y = Integer.parseInt(str[1]);
            } catch (Exception e) {
                System.err.println(Arrays.toString(str));
            }
            
            image_xscale = 1;
            image_yscale = 1;
            type = str[2];
        }
        
        protected void setXscale(double d) {
            image_xscale = d;
        }
        
        protected void setYscale(double d) {
            image_yscale = d;
        }

        public void draw(Graphics g) {
            switch (type) {
                case "objBlock":
                    g.setColor(new Color(0xBBBBBB));
                    g.fillRect(x, y, (int)(32 * image_xscale - 1), 31);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, (int)(32 * image_xscale - 1), 31);
                    g.setColor(new Color(0x111111));
                    g.drawLine(x, y, x + (int)(32 * image_xscale - 1), y + 31);
                    g.drawLine(x, y + 31, x + (int)(32 * image_xscale - 1), y);
                    break;

                case "objMiniBlock":
                    g.setColor(new Color(0xBBBBBB));
                    g.fillRect(x, y, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, 15, 15);
                    g.setColor(new Color(0x111111));
                    g.drawLine(x, y, x + 15, y + 15);
                    g.drawLine(x, y + 15, x + 15, y);
                    break;

                case "objSpikeUp":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x, x + 15, x + 16, x + 31}, new int[]{y + 31, y, y, y + 31}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x, x + 15, x + 16, x + 31}, new int[]{y + 31, y, y, y + 31}, 4);
                    break;

                case "objSpikeRight":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x, x + 31, x + 31, x}, new int[]{y, y + 15, y + 16, y + 31}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x, x + 31, x + 31, x}, new int[]{y, y + 15, y + 16, y + 31}, 4);
                    break;

                case "objSpikeLeft":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x + 31, x, x, x + 31}, new int[]{y, y + 15, y + 16, y + 31}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x + 31, x, x, x + 31}, new int[]{y, y + 15, y + 16, y + 31}, 4);
                    break;

                case "objSpikeDown":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x, x + 15, x + 16, x + 31}, new int[]{y, y + 31, y + 31, y}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x, x + 15, x + 16, x + 31}, new int[]{y, y + 31, y + 31, y}, 4);
                    break;

                case "objMovingPlatform":
                    g.setColor(new Color(139, 69, 19, 100));
                    g.fillRect(x, y, 31, 15);
                    g.setColor(new Color(139, 69, 19));
                    g.drawRect(x, y, 31, 15);
                    g.drawRect(x + 1, y + 1, 29, 13);
                    break;

                case "objWater2":
                    g.setColor(new Color(0, 255, 255, 100));
                    g.fillRect(x, y, 32, 32);
                    break;

                case "objMiniUp":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x, x + 7, x + 8, x + 15}, new int[]{y + 15, y, y, y + 15}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x, x + 7, x + 8, x + 15}, new int[]{y + 15, y, y, y + 15}, 4);
                    break;

                case "objMiniLeft":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x + 15, x, x, x + 15}, new int[]{y, y + 7, y + 8, y + 15}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x + 15, x, x, x + 15}, new int[]{y, y + 7, y + 8, y + 15}, 4);
                    break;

                case "objMiniRight":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x, x + 15, x + 15, x}, new int[]{y, y + 7, y + 8, y + 15}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x, x + 15, x + 15, x}, new int[]{y, y + 7, y + 8, y + 15}, 4);
                    break;

                case "objMiniDown":
                    g.setColor(new Color(0, 0, 255, 100));
                    g.fillPolygon(new int[]{x, x + 7, x + 8, x + 15}, new int[]{y, y + 15, y + 15, y}, 4);
                    g.setColor(Color.BLUE);
                    g.drawPolygon(new int[]{x, x + 7, x + 8, x + 15}, new int[]{y, y + 15, y + 15, y}, 4);
                    break;

                case "objSave":
                    g.setColor(Color.GREEN);
                    g.fillRoundRect(x + 4, y + 4, 24, 24, 6, 6);
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(x + 4, y + 4, 24, 24, 6, 6);
                    break;

                case "objWarpAutosaveNext":
                    g.setColor(Color.WHITE);
                    g.fillOval(x + 4, y + 4, 24, 24);
                    g.setColor(Color.BLACK);
                    g.drawOval(x + 4, y + 4, 24, 24);
                    break;

                case "objPlayerStart":
                    g.setColor(Color.BLUE);
                    g.fillRect(x + 12, y + 11, 11, 21);
                    break;

                default:
                // do nothing
                }
        }
}
