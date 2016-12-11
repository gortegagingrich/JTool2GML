/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Gabriel
 */
public class ItemImage extends ItemFromFile {

	private Image img;

	public ItemImage(String[] str) {
		super(str);

		switch (itemName) {
			case "objSpikeUp":
				img = (new ImageIcon("spikeup.png")).getImage();
				break;
			default:
				img = null;
		}
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
}
