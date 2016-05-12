/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

/**
 *
 * @author gabrielo
 */
public class Item {
    private int x,y;
    private int[] xArr, yArr;
    private double xScale, yScale;
    private Color color1, color2;
    
    /**
     * 
     * @param str {x,y,ID}
     */
    public Item(String[] str) {
        assert(str.length == 3);
        
        x = Integer.parseInt(str[0]);
        y = Integer.parseInt(str[1]);
        
        xScale = 1;
        yScale = 1;
        
        switch (str[2]) {
            case "objBlock":
                xArr = new int[] {0, 31, 31, 0};
                yArr = new int[] {0, 0, 31, 31};
                setColors(new Color(0xBBBBBB), Color.BLACK);
                break;
                
            case "objMovingPlatform":
                xArr = new int[] {0, 31, 31, 0};
                yArr = new int[] {0, 0, 15, 15};
                setColors(new Color(139, 69, 19, 100), 
                        new Color(139, 69, 19, 255));
                break;
                
            case "objWater2":
                xArr = new int[] {0, 31, 31, 0};
                yArr = new int[] {0, 0, 31, 31};
                setColors(new Color(0, 255, 255, 100), 
                        new Color(0, 255, 255, 100));
                break;
                
            case "objMiniBlock":
                xArr = new int[] {0, 15, 15, 0};
                yArr = new int[] {0, 0, 15, 15};
                setColors(new Color(0xBBBBBB), Color.BLACK);
                break;
                
            case "objSpikeUp":
                xArr = new int[] {0, 15, 16, 31};
                yArr = new int[] {31, 0, 0, 31};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objSpikeDown":
                xArr = new int[] {0, 15, 16, 31};
                yArr = new int[] {0, 31, 31, 0};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objSpikeRight":
                xArr = new int[] {0, 31, 31, 0};
                yArr = new int[] {0, 15, 16, 31};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objSpikeLeft":
                xArr = new int[] {31, 0, 0, 31};
                yArr = new int[] {0, 15, 16, 31};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objMiniUp":
                xArr = new int[] {0, 7, 8, 15};
                yArr = new int[] {15, 0, 0, 15};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objMiniDown":
                xArr = new int[] {0, 7, 8, 15};
                yArr = new int[] {0, 15, 15, 0};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objMiniRight":
                xArr = new int[] {0, 15, 15, 0};
                yArr = new int[] {0, 7, 8, 15};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
                
            case "objMiniLeft":
                xArr = new int[] {15, 0, 0, 15};
                yArr = new int[] {0, 7, 8, 15};
                setColors(new Color(0, 0, 255, 100), Color.BLUE);
                break;
            
            default:
                // not a recognized object id
        }
    }
    
    void draw(Graphics g) {
        int[] xx, yy;
        xx = Arrays.copyOf(xArr, xArr.length);
        yy = Arrays.copyOf(yArr, yArr.length);
        
        for (int i = 0; i < yArr.length; i++) {
            if (xScale != 1 && xx[i] != 0) {
                xx[i] += 1;
                xx[i] = x + (int)(xScale * xx[i] - 1);
            } else {
                xx[i] = x + xx[i];
            }
            
            if (yScale != 1 && yy[i] != 0) {
                yy[i] += 1;
                yy[i] = y + (int)(yScale * yy[i] - 1);
            } else {
                yy[i] = y + (int)(yScale * yy[i]);
            }
        }
        
        g.setColor(color1);
        g.fillPolygon(xx, yy, yArr.length);
        
        g.setColor(color2);
        g.drawPolygon(xx, yy, yArr.length);
    }
    
    private void setColors(Color c1, Color c2) {
        color1 = c1;
        color2 = c2;
    }
    
    void setXscale(double i) {
        xScale = i;
    }
    
    void setYscale(double i) {
        yScale = i;
    }
}
