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

    private boolean showGrid;

    // holds all the supported items in the room.
    protected Item[] items;

    /**
     * currently initializes with a few items I used to test the draw methods
     */
    protected Preview() {

        items = new Item[]{};
        showGrid = true;

        setPreferredSize(new Dimension(800, 608));
        setVisible(true);
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
            end = (s.charAt(s.length() - 1) == ';') ? (s.length() - 2) : (s.length() - 1);

            if (s.length() > 1) {
                if (s.charAt(0) == 'o' && s.length() > 5) {
                    switch (s.charAt(1)) {
                        case ' ': // o = insta...
                            items[i] = new Item(s.substring(20, end).split(","));
                            prev = items[i];
                            break;

                        case '.': // o.image_...
                            if (prev != null) {
                                switch (s.substring(2, 14)) {
                                    case "image_xscale":
                                        d = Double.parseDouble(s.split(" = ")[1].split(";")[0]);
                                        prev.setXscale(d);
                                        break;

                                    case "image_yscale":
                                        d = Double.parseDouble(s.split(" = ")[1].split(";")[0]);
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
                    items[i] = new Item(s.substring(16, end).split(","));
                    i += 1;
                }
            }
        }

        paintComponent(getGraphics());
    }

    protected void toggleGrid() {
        showGrid = !showGrid;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 608);

        if (showGrid) {
            g.setColor(new Color(244, 244, 244, 255));
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

        for (Item i : items) {
            if (i != null) {
                i.draw(g);
            }
        }
    }

}
