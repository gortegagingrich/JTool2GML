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
    private JPanel drawPanel;
    
    public JMap2ScriptGui() {
        setTitle("jmap to gml script converter");
        setSize(1600,608);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,2));
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
        
        add(jsp);
        
        drawPanel = new Preview();
        add(drawPanel,1);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        Runnable r = () -> {
            new JMap2ScriptGui();
        };
        
        javax.swing.SwingUtilities.invokeLater(r);
    }
    
    private class Preview extends JPanel {
        
        private Item[] items;
        
        private Preview() {
            Item item1 = new Item(new String[] {"384","352","objSpikeUp"});
            Item item2 = new Item(new String[] {"416","352","objSpikeLeft"});
            Item item3 = new Item(new String[] {"384","384","objSpikeDown"});
            Item item4 = new Item(new String[] {"416","416","objSpikeRight"});
            
            items = new Item[] {new Item(new String[] {"384","416","objBlock"}),item1,item2,item3,item4};
            
            setSize(800,608);
            setVisible(true);
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            for (Item i: items) {
                i.draw(g);
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
                
                x = Integer.parseInt(str[0]);
                y = Integer.parseInt(str[1]);
                type = str[2];
                
                System.out.println("made");
            }
            
            public void draw(Graphics g) {
                switch (type) {
                    case "objBlock":
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, 31, 31);
                        break;
                        
                    case "objSpikeUp":
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x,x+15,x+16,x+31}, new int[] {y+31,y,y,y+31}, 4);
                        break;
                        
                    case "objSpikeRight":
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x,x+31,x+31,x}, new int[] {y,y+15,y+16,y+31}, 4);
                        break;
                        
                    case "objSpikeLeft":
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x+31,x,x,x+31}, new int[] {y,y+15,y+16,y+31}, 4);
                        break;
                        
                    case "objSpikeDown":
                        g.setColor(Color.BLUE);
                        g.drawPolygon(new int[] {x,x+15,x+16,x+31}, new int[] {y,y+31,y+31,y}, 4);
                        break;
                        
                    default:
                        // do nothing
                }
            }
        }
    }
}
