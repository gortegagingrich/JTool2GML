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
public class Item implements Comparable {
    private int x,y;
    private int[] xArr, yArr;
    private double xScale, yScale;
    private Color color1, color2;
    private int depth;
    
    /**
     * 
     * @param str {x,y,ID}
     */
    public Item(String[] str) {
        assert(str.length == 3);
        
        // set origin
        x = Integer.parseInt(str[0]);
        y = Integer.parseInt(str[1]);
        
        // initialize xScale, yScale, and depth
        xScale = 1;
        yScale = 1;
        depth = 0;
        
        // initialize data relevent to drawing the item
        // these should eventually be read from some sort of external file
        switch (str[2]) {
            case "objBlock":
                setData(new int[] {0, 31, 31, 0},
                        new int[] {0, 0, 31, 31},
                        new Color(0xBB, 0xBB, 0xBB, 100),
                        Color.BLACK,
                        100);
                break;
                
            case "objMovingPlatform":
                setData(new int[] {0, 31, 31, 0},
                        new int[] {0, 0, 15, 15},
                        new Color(139, 69, 19, 100), 
                        new Color(139, 69, 19, 255),
                        100);
                break;
                
            case "objWater2":
                setData(new int[] {0, 31, 31, 0},
                        new int[] {0, 0, 31, 31},
                        new Color(0, 255, 255, 100), 
                        new Color(0, 255, 255, 100),
                        -10);
                break;
                
            case "objMiniBlock":
                setData(new int[] {0, 15, 15, 0},
                        new int[] {0, 0, 15, 15},
                        new Color(0xBB, 0xBB, 0xBB, 100), 
                        Color.BLACK,
                        100);
                break;
                
            case "objSpikeUp":
                setData(new int[] {0, 15, 16, 31},
                        new int[] {31, 0, 0, 31},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objSpikeDown":
                setData(new int[] {0, 15, 16, 31},
                        new int[] {0, 31, 31, 0},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objSpikeRight":
                setData(new int[] {0, 31, 31, 0},
                        new int[] {0, 15, 16, 31},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objSpikeLeft":
                setData(new int[] {31, 0, 0, 31},
                        new int[] {0, 15, 16, 31},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objMiniUp":
                setData(new int[] {0, 7, 8, 15},
                        new int[] {15, 0, 0, 15},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objMiniDown":
                setData(new int[] {0, 7, 8, 15},
                        new int[] {0, 15, 15, 0},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objMiniRight":
                setData(new int[] {0, 15, 15, 0},
                        new int[] {0, 7, 8, 15},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objMiniLeft":
                setData(new int[] {15, 0, 0, 15},
                        new int[] {0, 7, 8, 15},
                        new Color(0, 0, 255, 100), 
                        new Color(0, 0, 255, 255),
                        50);
                break;
                
            case "objPlayerStart":
                setData(new int[] {12, 23, 23, 12},
                        new int[] {11, 11, 32, 32},
                        new Color(255, 0, 255, 200), 
                        new Color(255, 0, 255, 0),
                        98);
                break;
                
            case "objSave":
                setData(new int[] {4, 28, 28, 4},
                        new int[] {4, 4, 28, 28},
                        new Color(0, 255, 0, 100), 
                        new Color(0, 0, 0, 255),
                        99);
                break;
            
            default:
                // not a recognized object id
                /*
                * format for adding more:
                *    setData(new int[] {x1,x2,...},
                *        new int[] {y1,y2,...},
                *        new Color(r,g,b,alpha), 
                *        new Color(r,g,b,alpha),
                *        depth);
                * Eventually, these values should be read from some external
                * file.
                */
        }
    }
    
    /**
     * Draws the item
     * @param g Should be passed from Preview
     */
    public void draw(Graphics g) {
        int[] xx, yy;
        
        // copy over values so the originals do not get changed
        xx = Arrays.copyOf(xArr, xArr.length);
        yy = Arrays.copyOf(yArr, yArr.length);
        
        // adjust the coordinates based on x and y scale
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
        
        // fill interior with first color
        g.setColor(color1);
        g.fillPolygon(xx, yy, yArr.length);
        
        // draw outline with second color
        g.setColor(color2);
        g.drawPolygon(xx, yy, yArr.length);
    }
    
    @Override
    public int compareTo(Object i) {
        // depth should work like in GML where higher ones come first
        return ((Item)i).depth - depth;
    }
    
    private void setColors(Color c1, Color c2) {
        color1 = c1;
        color2 = c2;
    }
    
    /**
     * Updates the item's xScale
     * @param i 1 = default size, 2 = double, 0.5 = half
     */
    public void setXscale(double i) {
        xScale = i;
    }
    
    /**
     * Updates the item's yScale
     * @param i 1 = default size, 2 = double, 0.5 = half
     */
    public void setYscale(double i) {
        yScale = i;
    }
    
    private void setData(int[] xArr, int[] yArr, Color c1, Color c2, int d) {
        this.xArr = Arrays.copyOf(xArr, xArr.length);
        this.yArr = Arrays.copyOf(yArr, yArr.length);
        depth = d;
        setColors(c1,c2);
    }
}
