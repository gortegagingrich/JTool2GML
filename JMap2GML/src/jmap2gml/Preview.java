/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import static jmap2gml.Preview.itemAttribute.YPOS;

/**
 * JPanel used to provide a visual preview of the room
 */
class Preview extends JPanel {

	public enum itemAttribute {
		XSCALE, YSCALE, XPOS, YPOS
	};

	protected ScriptGui parent;
	private boolean showGrid;
	protected final JPopupMenu rtClickMenu;
	protected int selected;
	private int gridX, gridY;

	// holds all the supported items in the room.
	protected Item[] items;

	/**
	 * currently initializes with a few items I used to test the draw methods
	 */
	protected Preview(ScriptGui parent) {
		this.parent = parent;
		
		gridX = 32;
		gridY = 32;

		addMouseListener(new PreviewMouseListener(this));

		items = new Item[]{};
		showGrid = true;
		selected = -1;

		setPreferredSize(new Dimension(800, 608));
		setVisible(true);

		rtClickMenu = new JPopupMenu();

		JMenuItem itemXScale = new JMenuItem("set xScale");
		itemXScale.addActionListener((ActionEvent ae) -> {
			double toXscale;

			try {
				toXscale = Double.parseDouble(JOptionPane.showInputDialog(
						  items[selected].itemName + ".xScale: ",
						  items[selected].xScale));
				modifyItem(itemAttribute.XSCALE, toXscale);
				parent.updateScriptFromItems();
			} catch (NumberFormatException | NullPointerException e) {
				// do nothing
			}
		});
		rtClickMenu.add(itemXScale);

		JMenuItem itemYScale = new JMenuItem("set yScale");
		itemYScale.addActionListener((ActionEvent ae) -> {
			double toYscale;

			try {
				toYscale = Double.parseDouble(JOptionPane.showInputDialog(
						  items[selected].itemName + ".yScale: ",
						  items[selected].yScale));
				modifyItem(itemAttribute.YSCALE, toYscale);
				parent.updateScriptFromItems();
			} catch (NumberFormatException | NullPointerException e) {
				// do nothing
			}
		});
		rtClickMenu.add(itemYScale);

		rtClickMenu.addSeparator();

		JMenuItem itemSetX = new JMenuItem("set x");
		itemSetX.addActionListener((ActionEvent ae) -> {
			if (items.length > 0 && selected != -1 && items[selected] != null) {
				int toX;

				try {
					toX = Integer.parseInt(JOptionPane.showInputDialog(
							  items[selected].itemName + ".x: ", items[selected].x));
					modifyItem(itemAttribute.XPOS, toX);
					parent.updateScriptFromItems();
				} catch (NumberFormatException | NullPointerException e) {
					// do nothing
				}
			}
		});
		rtClickMenu.add(itemSetX);

		JMenuItem itemSetY = new JMenuItem("set y");
		itemSetY.addActionListener((ActionEvent ae) -> {
			if (items.length > 0 && selected != -1 && items[selected] != null) {
				int toY;

				try {
					toY = Integer.parseInt(JOptionPane.showInputDialog(
							  items[selected].itemName + ".y: ", items[selected].y));
					modifyItem(itemAttribute.YPOS, toY);
					parent.updateScriptFromItems();
				} catch (NumberFormatException | NullPointerException e) {
					// do nothing
				}
			}
		});
		rtClickMenu.add(itemSetY);

		rtClickMenu.addSeparator();

		JMenuItem del = new JMenuItem("delete");
		del.addActionListener((ActionEvent ae) -> {
			removeItem();
			parent.updateScriptFromItems();
		});
		rtClickMenu.add(del);
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
			end = (s.length() > 2 && s.charAt(s.length() - 1) == ';') ? (s.length()
					  - 2) : (s.length() - 1);

			if (s.length() > 1) {
				if (s.charAt(0) == 'o' && s.length() > 5) {
					switch (s.charAt(1)) {
						case ' ': // o = insta...
							items[i] = new ItemImage(s.substring(20, end).split(","));
							prev = items[i];
							break;

						case '.': // o.image_...
							if (prev != null) {
								switch (s.substring(2, 14)) {
									case "image_xscale":
										d = Double.parseDouble(s.split(" = ")[1].
												  split(";")[0]);
										prev.setXscale(d);
										break;

									case "image_yscale":
										d = Double.parseDouble(s.split(" = ")[1].
												  split(";")[0]);
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
					items[i] = new ItemImage(s.substring(16, end).split(","));
					i += 1;
				}
			}
		}

		paintComponent(getGraphics());
	}

	protected void toggleGrid() {
		showGrid = !showGrid;
		repaint();
	}

	// todo: draw order should be based on depth
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Object[] temp;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 608);

		if (showGrid) {
			g.setColor(new Color(222, 222, 222, 255));
			g.drawLine(0, 0, 0, 608);
			g.drawLine(0, 0, 800, 0);
			g.drawLine(800, 0, 800, 608);
			g.drawLine(0, 608, 800, 608);

			for (int i = gridX-1; i < 799; i += gridX) {
				g.drawRect(i, 0, 1, 608);
			}

			for (int i = gridY-1; i < 607; i += gridY) {
				g.drawRect(0, i, 800, 1);
			}
		}

		temp = Arrays.copyOf(items, items.length);
		temp = cleanArray(temp);
		Arrays.sort(temp);

		for (Object i : temp) {
			if (i != null) {
				((Item) i).draw(g);
			}
		}
	}

	private Object[] cleanArray(Object[] arr) {
		Object[] temp = new Object[]{};

		for (Object o : arr) {
			if (o != null) {
				temp = Arrays.copyOf(temp, temp.length + 1);
				temp[temp.length - 1] = o;
			}
		}

		return temp;
	}

	/**
	 *
	 * @param atr attribute of the item to change
	 * @param value value to set it to (can get cast to int)
	 */
	protected void modifyItem(itemAttribute atr, double value) {
		switch (atr) {
			case XSCALE:
				items[selected].xScale = value;
				break;
			case YSCALE:
				items[selected].yScale = value;
				break;
			case XPOS:
				items[selected].x = (int) value;
				break;
			case YPOS:
				items[selected].y = (int) value;
				break;
		}

		selected = -1;
		repaint();
	}

	protected void removeItem() {
		if (items.length > 0 && selected != -1 && items[selected] != null) {
			items[selected] = null;
			cleanArray(items);
			selected = -1;
			this.repaint();
		}
	}
	
	public void modifyGrid() {
		// grid stuff
		JPanel gridOptions = new JPanel();
		JTextField gridXField = new JTextField(gridX);
		JTextField gridYField = new JTextField(gridY);
		
		gridOptions.setLayout(new FlowLayout());
		
		gridOptions.add(new JLabel("X: "));
		gridOptions.add(gridXField);
		gridOptions.add(new JLabel("Y: "));
		gridOptions.add(gridYField);
		
		JOptionPane.showConfirmDialog(null, gridOptions, 
               "Modify Grid", JOptionPane.PLAIN_MESSAGE);
		
		try {
			gridX = Integer.parseInt(gridXField.getText());
			gridY = Integer.parseInt(gridYField.getText());
		} catch (Exception e) {
			// do nothing
		} finally {
			repaint();
		}
	}
}
