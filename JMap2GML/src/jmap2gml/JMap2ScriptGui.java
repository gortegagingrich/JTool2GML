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
 *
 * @author Gabriel
 */
public class JMap2ScriptGui extends JFrame {
    private JMap2Script jm2s;
    private JTextArea jta;
    private JMenuItem writeFile;
    private Preview drawPanel;
    
    public JMap2ScriptGui() {
        setTitle("jmap to gml script converter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1,2));
        setResizable(false);
        
        jta = new JTextArea(30,40);
        jta.setEditable(false);
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JMenuBar menubar = new JMenuBar();
        
        JMenu file = new JMenu("File");
        
        JMenuItem load = new JMenuItem("Load jmap");
        load.addActionListener(ae -> {
            JFileChooser fileChooser = new JFileChooser();
            
            int returnValue = fileChooser.showOpenDialog(null);
            
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                jm2s = new JMap2Script(selectedFile.getPath(),false);
                
                jta.setText("");
                jta.append(jm2s.toString());
                drawPanel.setItems(jm2s.toString().split("\n"));
                
                writeFile.setEnabled(true);
            }
        });
        file.add(load);
        
        writeFile = new JMenuItem("Write file");
        writeFile.addActionListener(ae -> {
            if (jm2s != null) {
                PrintWriter out = null;
                try {
                    File f = new File(jm2s.getFileName().substring(0,jm2s.getFileName().lastIndexOf(".jmap")) + ".gml");
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
        file.add(writeFile);
        
        menubar.add(file);
        setJMenuBar(menubar);
        
        getContentPane().add(jsp);
        
        drawPanel = new Preview();
        getContentPane().add(drawPanel,1);
        
        //pack();
        
        setSize(1608,608+52+menubar.getHeight());
        setVisible(true);
    }
    
    public static void main(String[] args) {
        Runnable r = () -> {
            new JMap2ScriptGui();
        };
        
        javax.swing.SwingUtilities.invokeLater(r);
    }
    
    protected class Preview extends JPanel {
        
        protected Item[] items;
        
        protected Preview() {
            Item item1 = new Item(new String[] {"384","352","objSpikeUp"});
            Item item2 = new Item(new String[] {"416","352","objSpikeLeft"});
            Item item3 = new Item(new String[] {"384","384","objSpikeDown"});
            Item item4 = new Item(new String[] {"416","416","objSpikeRight"});
            Item item5 = new Item(new String[] {"384","448","objMovingPlatform"});
            Item item6 = new Item(new String[] {"384","384","objWater2"});
            
            
            items = new Item[] {new Item(new String[] {"384","416","objBlock"}),item1,item2,item3,item4,item5,item6};
            
            setSize(800,608);
            setVisible(true);
        }
        
        protected void setItems(String[] str) {
            items = new Item[str.length];
            int i = 0;
            
            for (String s: str) {
                if (s.length() >= 20) {
                    //try {
                        items[i] = new Item(s.substring(16,s.length()-1).split(","));
                        i += 1;
                }
            }
            
            this.paintComponent(this.getGraphics());
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            for (Item i: items) {
                if (i != null) {
                    i.draw(g);
                }
            }
        }
        
        private class Item {
            private int x,y;
            private String type;
            
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
                        g.drawLine(x, y, x+31, y+31);
                        g.drawLine(x,y+31,x+31,y);
                        break;
                        
                    case "objSpikeUp":
                        g.setColor(new Color(0,0,255,100));
                        g.fillPolygon(new int[] {x,x+15,x+16,x+31}, new int[] {y+31,y,y,y+31}, 4);
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x,x+15,x+16,x+31}, new int[] {y+31,y,y,y+31}, 4);
                        break;
                        
                    case "objSpikeRight":
                        g.setColor(new Color(0,0,255,100));
                        g.fillPolygon(new int[] {x,x+31,x+31,x}, new int[] {y,y+15,y+16,y+31}, 4);
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x,x+31,x+31,x}, new int[] {y,y+15,y+16,y+31}, 4);
                        break;
                        
                    case "objSpikeLeft":
                        g.setColor(new Color(0,0,255,100));
                        g.fillPolygon(new int[] {x+31,x,x,x+31}, new int[] {y,y+15,y+16,y+31}, 4);
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x+31,x,x,x+31}, new int[] {y,y+15,y+16,y+31}, 4);
                        break;
                        
                    case "objSpikeDown":
                        g.setColor(new Color(0,0,255,100));
                        g.fillPolygon(new int[] {x,x+15,x+16,x+31}, new int[] {y,y+31,y+31,y}, 4);
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x,x+15,x+16,x+31}, new int[] {y,y+31,y+31,y}, 4);
                        break;
                        
                    case "objMovingPlatform":
                        g.setColor(new Color(139,69,19,100));
                        g.fillRect(x,y,31,15);
                        g.setColor(new Color(139,69,19));
                        g.drawRect(x,y,31,15);
                        g.drawRect(x+1,y+1,29,13);
                        break;
                        
                    case "objWater2":
                        g.setColor(new Color(0,255,255,100));
                        g.fillRect(x,y,32,32);
                        break;
                        
                        
                    default:
                        // do nothing
                }
            }
        }
    }
}
