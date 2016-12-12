/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author Gabriel
 */
public class ItemImage extends ItemFromFile {

	private static final HashMap<String, Image> images = new HashMap<>();

	public ItemImage(String[] str) {
		super(str);

		parseFile();
	}

	@Override
	public void draw(Graphics g) {
		Image img = images.get(itemName);
		
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
		Image img;
		
		file = new File("ImageItemConfig");
		
		try {
			scan = new Scanner(file);
			
			while (scan.hasNext()) {
				line = scan.nextLine().split(";");
				
				if (line.length == 2 && line[0].equals(itemName)) {
					img = (new ImageIcon(line[1])).getImage();
					if (!images.containsKey(itemName)) {
						images.put(itemName, img);
					}
					break;
				}
			}
			
			scan.close();
		} catch (Exception e) {
			// For exceptions stemming from files not existing or improper format
		}
	}
}
