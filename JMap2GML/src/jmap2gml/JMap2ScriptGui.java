/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import jmap2gml.JMap2Script;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JFrame that serves as a GUI for the parser
 *
 * @author Gabriel
 */
public class JMap2ScriptGui extends JFrame {

    private JMap2Script jm2s;
    private JTextArea jta;
    private JMenuItem writeFile;
    private Preview drawPanel;

    /**
     * Formats the window, initializes the JMap2Script object, and sets up all
     * the necessary events.
     */
    public JMap2ScriptGui() {
        setTitle("jmap to gml script converter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1, 2));
        setResizable(false);

        jta = new JTextArea(30, 40);
        jta.setEditable(false);
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // menu bar
        JMenuBar menubar = new JMenuBar();

        // file menu
        JMenu file = new JMenu("File");

        // load button
        JMenuItem load = new JMenuItem("Load jmap");
        load.addActionListener(ae -> {
            JFileChooser fileChooser = new JFileChooser();

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                jm2s = new JMap2Script(selectedFile.getPath(), false);

                jta.setText("");
                jta.append(jm2s.toString());
                drawPanel.setItems(jm2s.toString().split("\n"));

                writeFile.setEnabled(true);
            }
        });

        // add load to file menu
        file.add(load);

        // button to save script to file
        writeFile = new JMenuItem("Write file");
        writeFile.addActionListener(ae -> {
            if (jm2s != null) {
                PrintWriter out = null;
                try {
                    File f = new File(jm2s.getFileName().substring(0, jm2s.getFileName().lastIndexOf(".jmap")) + ".gml");
                    System.out.println(f.getPath());
                    out = new PrintWriter(f);
                    out.append(jm2s.toString());
                    out.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(JMap2ScriptGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        writeFile.setEnabled(false);

        // add to file menu
        file.add(writeFile);

        // add file menu to the menubar
        menubar.add(file);

        // sets the menubar
        setJMenuBar(menubar);

        // add the text area to the window
        add(jsp);

        // initialize the preview panel
        drawPanel = new Preview();

        // add preview panel to the window
        add(drawPanel, 1);

        // The windows 10 window border is around 8 pixels wide and 52 pixels
        // tall, but I didn't set it until here because I don't know the
        // dimensions of the menubar.
        // The height of the preview window needs to be 608 pixels high because
        // the rooms are generally made on a 32 or 16 pixel grid.
        setSize(1608, 608 + 52 + menubar.getHeight());
        setVisible(true);
    }

    /**
     * JPanel used to provide a visual preview of the room
     */
    protected class Preview extends JPanel {

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

            setSize(800, 608);
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

            for (String s : str) {
                if (s.length() >= 20) {
                    //try {
                    items[i] = new Item(s.substring(16, s.length() - 1).split(","));
                    i += 1;
                }
            }

            this.paintComponent(this.getGraphics());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (Item i : items) {
                if (i != null) {
                    i.draw(g);
                }
            }
        }

    }

    private class Item {

        private int x, y;
        private final String type;

        /**
         *
         * @param str {arg0, arg1, arg2}
         */
        private Item(String[] str) {
            assert str.length == 3;
            try {
                x = Integer.parseInt(str[0]);
                y = Integer.parseInt(str[1]);
            } catch (Exception e) {
                System.err.println(Arrays.toString(str));
            }
            type = str[2];
        }

        public void draw(Graphics g) {
            switch (type) {
                case "objBlock":
                    g.setColor(new Color(0xBBBBBB));
                    g.fillRect(x, y, 31, 31);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, 31, 31);
                    g.setColor(new Color(0x111111));
                    g.drawLine(x, y, x + 31, y + 31);
                    g.drawLine(x, y + 31, x + 31, y);
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
}
