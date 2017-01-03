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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.json.JSONObject;

/**
 *
 * @author Gabriel
 */
public class ItemImage extends ItemFromFile {

	public static final HashMap<String, Image> IMAGES = readConfig();
	private static String JSONTEXT;
	private static JSONObject config;

	public ItemImage(String[] str) {
		super(str);
	}

	@Override
	public void draw(Graphics g) {
		Image img = IMAGES.get(itemName);

		int xOffset, yOffset;

		if (config.has(itemName + "XOFFSET")) {
			xOffset = config.getInt(itemName + "XOFFSET");
		} else {
			xOffset = 0;
		}

		if (config.has(itemName + "YOFFSET")) {
			yOffset = config.getInt(itemName + "YOFFSET");
		} else {
			yOffset = 0;
		}

		if (img != null) {
			int width, height;

			width = (int) (img.getWidth(null) * xScale);
			height = (int) (img.getHeight(null) * yScale);

			g.drawImage(img, x + xOffset, y + yOffset, width, height, null);
		} else {
			super.draw(g);
		}
	}

	private static HashMap<String, Image> readConfig() {
		HashMap<String, Image> out = new HashMap<>();
		JSONTEXT = "";
		Image img;

		Scanner scan;
		try {
			scan = new Scanner(new File("ImageItemConfig"));

			while (scan.hasNext()) {
				JSONTEXT += scan.nextLine();
			}

			config = new JSONObject(JSONTEXT);

			for (String str : config.keySet()) {
				if (!str.contains("XOFFSET") && !str.contains("YOFFSET")) {
					img = (new ImageIcon(config.getString(str))).getImage();
					out.put(str, img);
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(ItemImage.class.getName()).log(Level.SEVERE, null, ex);
		}

		return out;
	}

}
