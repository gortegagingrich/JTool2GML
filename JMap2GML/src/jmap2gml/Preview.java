/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
     * JPanel used to provide a visual preview of the room
     */
    class Preview extends JPanel {

        // holds all the supported items in the room.
        protected Item[] items;

        /**
         * currently initializes with a few items I used to test the draw
         * methods
         */
        protected Preview() {
            Item item1 = new Item(new String[]{"384", "352", "objSpikeUp"});
            Item item2 = new Item(new String[]{"416", "352", "objSpikeLeft"});
            Item item3 = new Item(new String[]{"384", "384", "objSpikeDown"});
            Item item4 = new Item(new String[]{"416", "416", "objSpikeRight"});
            Item item5 = new Item(new String[]{"384", "448", "objMovingPlatform"});
            Item item6 = new Item(new String[]{"384", "384", "objWater2"});

            items = new Item[]{new Item(new String[]{"384", "416", "objBlock"}), item1, item2, item3, item4, item5, item6};

            setPreferredSize(new Dimension(800, 608));
            setVisible(true);
        }

        /**
         * Recreates the items array and adds each valid item that gets created
         * by the script.
         *
         * @param str {{x,y,objID},...}
         */
        protected void setItems(String[] str) {
            items = new Item[str.length];
            int i = 0;
            int end;

            for (String s : str) {
                if (s.length() >= 20) {
                    //try {
                    end = (s.charAt(s.length() - 1) == ';') ? (s.length() - 2) : (s.length() - 1);
                    items[i] = new Item(s.substring(16, end).split(","));
                    i += 1;
                }
            }

            this.paintComponent(this.getGraphics());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 800, 608);
            
            g.setColor(new Color(244, 244, 244, 255));
            g.drawLine(0,0,0,608);
            g.drawLine(0,0,800,0);
            g.drawLine(800,0,800,608);
            g.drawLine(0,608,800,608);
            
            for (int i = 31; i < 799; i += 32) {
                g.drawRect(i, 0, 1, 608);
            }
            
            for (int i = 31; i < 607; i+= 32) {
                g.drawRect(0,i,800,1);
            }

            for (Item i : items) {
                if (i != null) {
                    i.draw(g);
                }
            }
        }

    }