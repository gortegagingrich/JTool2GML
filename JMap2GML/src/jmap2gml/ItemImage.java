/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author Gabriel
 */
public class ItemImage extends ItemFromFile {

	private Image img;

	public ItemImage(String[] str) {
		super(str);

		parseFile();
	}

	@Override
	public void draw(Graphics g) {
		if (img != null) {
			int width, height;

			width = (int) (img.getWidth(null) * xScale);
			height = (int) (img.getHeight(null) * yScale);

			g.drawImage(img, x, y, width, height, null);
		} else {
			super.draw(g);
		}
	}
	
	private void parseFile() {
		Scanner scan;
		File file;
		String[] line;
		
		file = new File("ImageItemConfig");
		
		try {
			scan = new Scanner(file);
			
			while (scan.hasNext()) {
				line = scan.nextLine().split(";");
				
				if (line.length == 2 && line[0].equals(itemName)) {
					img = (new ImageIcon(line[1])).getImage();
					break;
				}
			}
			
			scan.close();
		} catch (Exception e) {
			// For exceptions stemming from files not existing or improper format
		}
	}
}
