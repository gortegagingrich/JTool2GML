/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;

/**
 *
 * @author Gabriel
 */
public class Palette {
	private final int x;
	private int y;
	private final int width, height;
	
	public static enum position {UP, MOVING, DOWN};
	
	private position up = position.UP;
	private Timer timer;
	
	private final Preview parent;
	
	public Palette(Preview parent) {
		this.parent = parent;
		x = 0;
		y = -96;
		width = 256;
		height = 96;
	}
	
	public void draw(Graphics g) {
		try {
			
			g.setColor(Color.white);
			g.fillRect(x, (y < 0) ? 0 : y, width, (y < 0) ? height + y : height);
			
			g.setColor(Color.LIGHT_GRAY);
			
			for (int i = x; i < x + width; i += 32) {
				g.drawLine(i, 0, i, (y < 0) ? height + y : height);
				g.drawLine(i+31, 0, i+31, (y < 0) ? height + y : height);
			}
			
			for (int i = y; i < y + height; i += 32) {
				g.drawLine(0, (i < 0) ? 0 : i, width, (i < 0) ? 0 : i);
				g.drawLine(0, (i+31 < 0) ? 0 : i, width, (i+31 < 0) ? 0 : i);
			}
			
			g.setColor(Color.black);
			g.drawRect(x, (y < 0) ? 0 : y, width, (y < 0) ? height + y : height);
		} catch (Exception e) {
			// this is just so it doesn't break when it's off screen
		}
	}
	
	public void moveDown() {
		if (timer != null) {
			timer.stop();
		}
		
		up = position.MOVING;
		
		timer = new Timer(5, ae -> {
			y += 4;
			parent.repaint();
			
			
			if (y >= 0) {
				y = 0;
				timer.stop();
				up = position.DOWN;
			}
		});
		
		timer.start();
	}
	
	public void moveUp() {
		if (timer != null) {
			timer.stop();
		}
		
		up = position.MOVING;
		
		timer = new Timer(5,ae -> {
			y -= 4;
			parent.repaint();
			
			if (y <= -96) {
				y = -96;
				timer.stop();
				up = position.UP;
			}
		});
		
		timer.start();
	}
	
	public position isUp() {
		return up;
	}
}
