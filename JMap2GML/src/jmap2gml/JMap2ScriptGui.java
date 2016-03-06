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
    
    public JMap2ScriptGui() {
        setTitle("jmap to gml script converter");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        
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
        pack();
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        Runnable r = () -> {
            new JMap2ScriptGui();
        };
        
        javax.swing.SwingUtilities.invokeLater(r);
    }
}
