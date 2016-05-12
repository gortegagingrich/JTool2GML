/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
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
        setResizable(true);

        jta = new JTextArea(30, 40);
        readInputFile();
        
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setRowHeaderView(new TextLineNumber(jta));
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        
        JMenu display = new JMenu("Display");
        
        JMenuItem update = new JMenuItem("Update");
        
        update.addActionListener(ae -> {
            drawPanel.setItems(jta.getText().split("\n"));
        });
        
        display.add(update);
        
        menubar.add(display);

        // sets the menubar
        setJMenuBar(menubar);

        // add the text area to the window
        add(jsp);

        // initialize the preview panel
        drawPanel = new Preview();
        JScrollPane scrollPane = new JScrollPane(drawPanel);

        // add preview panel to the window
        scrollPane.setHorizontalScrollBar(scrollPane.createHorizontalScrollBar());
        scrollPane.setVerticalScrollBar(scrollPane.createVerticalScrollBar());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(scrollPane,1);

        // The windows 10 window border is around 8 pixels wide and 52 pixels
        // tall, but I didn't set it until here because I don't know the
        // dimensions of the menubar.
        // The height of the preview window needs to be 608 pixels high because
        // the rooms are generally made on a 32 or 16 pixel grid.
        setSize(1000, 480 + 52 + menubar.getHeight());
        setVisible(true);
        drawPanel.setItems(jta.getText().split("\n"));
    }
    
    private void readInputFile() {
        try {
            File f = new File("SampleScript");
            Scanner scan = new Scanner(f);
            
            while (scan.hasNext()) {
                jta.append(scan.nextLine() + "\n");
            }
        } catch (Exception e) {
            
        }
    }
}
