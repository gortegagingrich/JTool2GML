/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * JFrame that serves as a GUI for the parser
 *
 * @author Gabriel
 */
public class ScriptGui extends JFrame {

	private ScriptFromJmap jm2s;
	private JTextArea jta;
	private JMenuItem writeFile;
	private Preview drawPanel;
	private String prevDirectory;

	/**
	 * Formats the window, initializes the JMap2Script object, and sets up all
	 * the necessary events.
	 */
	public ScriptGui() {

		try {
			Scanner configReader = new Scanner(new File("MiscSettings"));

			prevDirectory = configReader.nextLine();

			configReader.close();
		} catch (Exception ex) {
			Logger.getLogger(ScriptGui.class.getName()).
					  log(Level.SEVERE, null, ex);
			prevDirectory = "";
		}

		setTitle("jmap to gml script converter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(1, 2));
		setResizable(true);

		jta = new JTextArea(30, 40);
		readInputFile();

		JScrollPane jsp = new JScrollPane(jta);
		jsp.setRowHeaderView(new TextLineNumber(jta));
		jsp.setHorizontalScrollBarPolicy(
				  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// menu bar
		JMenuBar menubar = new JMenuBar();

		// file menu
		JMenu file = new JMenu("File");

		// load button
		JMenuItem load = new JMenuItem("Load jmap");
		load.addActionListener(ae -> {
			JFileChooser fileChooser = new JFileChooser(prevDirectory);
			fileChooser.setFileFilter(new FileNameExtensionFilter("jmap file",
					  "jmap", "jmap"));

			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();

				prevDirectory = selectedFile.getAbsolutePath();

				PrintWriter writer;
				try {
					writer = new PrintWriter(new File("MiscSettings"));
					writer.println(prevDirectory);
					writer.close();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(ScriptGui.class.getName()).
							  log(Level.SEVERE, null, ex);
				}

				jm2s = new ScriptFromJmap(selectedFile.getPath(), false);

				jta.setText("");
				jta.append(jm2s.toString());

				writeFile.setEnabled(true);

				drawPanel.setItems(jta.getText().split("\n"));
			}
		});

		// add load to file menu
		file.add(load);

		// button to save script to file
		writeFile = new JMenuItem("Write file");
		writeFile.addActionListener(ae -> {
			if (jm2s != null) {
				PrintWriter out;
				try {
					File f = new File(jm2s.getFileName().substring(0, jm2s.
							  getFileName().lastIndexOf(".jmap")) + ".gml");
					System.out.println(f.getPath());
					out = new PrintWriter(f);
					out.append(jm2s.toString());
					out.close();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(ScriptGui.class.getName()).
							  log(Level.SEVERE, null, ex);
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

		JMenuItem gridToggle = new JMenuItem("Toggle Grid");
		gridToggle.addActionListener(ae -> {
			drawPanel.toggleGrid();
		});
		display.add(gridToggle);

		menubar.add(display);

		// sets the menubar
		setJMenuBar(menubar);

		// add the text area to the window
		add(jsp);

		// initialize the preview panel
		drawPanel = new Preview(this);
		JScrollPane scrollPane = new JScrollPane(drawPanel);

		// add preview panel to the window
		//scrollPane.setHorizontalScrollBar(scrollPane.createHorizontalScrollBar());
		//scrollPane.setVerticalScrollBar(scrollPane.createVerticalScrollBar());
		//scrollPane.setHorizontalScrollBarPolicy(
		//        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPane.setVerticalScrollBarPolicy(
		//        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, 1);

		pack();
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

	public void updateScriptFromItems() {
		jta.setText((new ScriptFromItem(drawPanel.items)).toString());
	}
}
