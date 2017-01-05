/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONException;
import org.json.JSONObject;

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
		setTitle("jmap to gml script converter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());

		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent we) {
			}

			@Override
			public void windowClosing(WindowEvent we) {
				saveConfig();
			}

			@Override
			public void windowClosed(WindowEvent we) {
			}

			@Override
			public void windowIconified(WindowEvent we) {
			}

			@Override
			public void windowDeiconified(WindowEvent we) {
			}

			@Override
			public void windowActivated(WindowEvent we) {
			}

			@Override
			public void windowDeactivated(WindowEvent we) {
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		setResizable(true);
		setIconImage((new ImageIcon("spikeup.png")).getImage());

		jta = new JTextArea(38, 30);

		loadConfig();

		JScrollPane jsp = new JScrollPane(jta);
		jsp.setRowHeaderView(new TextLineNumber(jta));
		jsp.setHorizontalScrollBarPolicy(
				  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setSize(jsp.getWidth(), 608);

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

				jm2s = new ScriptFromJmap(selectedFile.getPath(), false);

				jta.setText("");
				jta.append(jm2s.toString());
				jta.setCaretPosition(0);

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
		
		JMenuItem gmx = new JMenuItem("Export as gmx");
		gmx.addActionListener(ae -> {
			String fn = String.format("%s.room.gmx",prevDirectory);
			
			JFileChooser fc = new JFileChooser(prevDirectory);
			fc.setSelectedFile(new File(fn));
			fc.setFileFilter(new FileNameExtensionFilter("Game Maker XML",
					  "gmx", "gmx"));
			fc.showDialog(null,"Save");
			File f = fc.getSelectedFile();
			
			if (f != null) {
				try {
					GMX.itemsToGMX(drawPanel.items, new FileOutputStream(f));
				} catch (FileNotFoundException ex) {
					Logger.getLogger(ScriptGui.class.getName()).
							  log(Level.SEVERE, null, ex);
				}
			}
		});

		// add to file menu
		file.add(writeFile);
		file.add(gmx);

		// add file menu to the menubar
		menubar.add(file);
		
		// Edit menu
		
		
		// display menu
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

		JMenuItem gridOptions = new JMenuItem("Modify Grid");
		gridOptions.addActionListener(ae -> {
			drawPanel.modifyGrid();
		});
		display.add(gridOptions);

		menubar.add(display);

		// sets the menubar
		setJMenuBar(menubar);

		// add the text area to the window
		c.gridx = 0;
		c.gridy = 0;
		add(jsp, c);

		// initialize the preview panel
		drawPanel = new Preview(this);
		JScrollPane scrollPane = new JScrollPane(drawPanel);

		// add preview panel to the window
		c.gridx = 1;
		c.gridwidth = 2;
		add(scrollPane, c);

		pack();
		setMinimumSize(this.getSize());
		setLocationRelativeTo(null);
		setVisible(true);
		drawPanel.setItems(jta.getText().split("\n"));
	}

	public void updateScriptFromItems() {
		jta.setText((new ScriptFromItem(drawPanel.items)).toString());
		jta.setCaretPosition(0);
	}

	private void saveConfig() {
		JSONObject config = new JSONObject();

		config.put("WindowX", this.getLocationOnScreen().x);
		config.put("WindowY", this.getLocationOnScreen().y);
		config.put("PrevFile", prevDirectory);
		config.put("PrevScript", jta.getText());

		try {
			try (PrintWriter out = new PrintWriter(new File("config"))) {
				out.write(config.toString());
			}
		} catch (FileNotFoundException ex) {
			//
		}
	}

	private void loadConfig() {
		try {
			Scanner scan = new Scanner(new File("config"));

			JSONObject config = new JSONObject(scan.nextLine());
			this.setLocation(config.getInt("WindowX"), config.getInt("WindowY"));
			prevDirectory = config.getString("PrevFile");
			jta.setText(config.getString("PrevScript"));
			jta.setCaretPosition(0);

		} catch (FileNotFoundException | JSONException ex) {
		}
	}
}
