/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.util.Arrays;
import java.util.Scanner;
import java.io.*;

/**
 *
 * @author Gabriel
 */
public class JMap2Script {
    
    private GhettoQueue queue;
    private NotQueue blocks;
    private NotQueue killers;
    private NotQueue platforms;
    private NotQueue other;
    private boolean toFile = true;
    private String str;
    private String fileName;
    
    /**
     * Main logic of the program is here.
     * @param fName input file from args
     */
    public JMap2Script(String fName) {
        this(fName, true);
    }
    
    public JMap2Script(String fName, boolean f) {
        toFile = f;
        this.fileName = fName;
        queue = new GhettoQueue();
        blocks = new NotQueue();
        killers = new NotQueue();
        platforms = new NotQueue();
        other = new NotQueue();
        str = "";
        
        try {
            readFile(fName);
            File file = new File((fName.substring(0, fName.lastIndexOf(".jmap")) + ".gml"));
            PrintWriter out = new PrintWriter(file);
            
            while (queue.hasNext()) {
                l2s(queue.dequeue());
            }
            if (toFile) {
                out.println("// blocks");
            }
               
            str += "\n// blocks\n";
            
            while (blocks.hasNext()) {
                if (toFile) {
                    out.println(blocks.dequeue());
                } else {
                    str += blocks.dequeue() + "\n";
                }
            }
            
            if (toFile) {
                out.println("\n// killers");
            }
                
            str += "\n// killers\n";
            
            while (killers.hasNext()) {
                if (toFile) {
                    out.println(killers.dequeue());
                }
                    
                str += killers.dequeue() + "\n";
                
            }
            
            if (toFile) {
                out.println("// platforms");
            }
                
            str += "\n// platforms\n";
            
            while (platforms.hasNext()) {
                if (toFile) {
                    out.println(platforms.dequeue());
                }
                    
                str += platforms.dequeue() + "\n";
            }
            
            if (toFile) {
                out.println("\n// other objects");
            }
            
            str += "\n// other objects\n";
            
            while (other.hasNext()) {
                if (toFile) {
                    out.println(other.dequeue());
                }
                   
                str += other.dequeue() + "\n";
            }
            
            out.close();
            
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Main logic of the program is here.
     * @param fName input file from args
     * @param f output file
     */
    public JMap2Script(String fName, File f) {
        queue = new GhettoQueue();
        blocks = new NotQueue();
        killers = new NotQueue();
        platforms = new NotQueue();
        other = new NotQueue();
        
        try {
            readFile(fName);
            PrintWriter out = new PrintWriter(f);
            
            while (queue.hasNext()) {
                l2s(queue.dequeue());
            }
            
           out.println("// blocks");
            while (blocks.hasNext()) {
                out.println(blocks.dequeue());
            }
            
            out.println("\n// killers");
            while (killers.hasNext()) {
                out.println(killers.dequeue());
            }
            
            out.println("\n// platforms");
            while (platforms.hasNext()) {
                out.println(platforms.dequeue());
            }
            
            out.println("\n// other objects");
            while (other.hasNext()) {
                out.println(other.dequeue());
            }
            
            out.close();
            
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public String toString() {
        return str;
    }
    
    
    /**
     * Parses input file
     * 
     * @param fName input file
     * @throws Exception
     */
    private void readFile(String fName) throws Exception {
        File file = new File(fName);
        Scanner in = new Scanner(file);
        String str = "";
        int[] temp = null;
        
        for (int i = 0; i < 5; i++) {
            str = in.nextLine();
        }
        
        int i = 0;
        
        for (String s: str.split(" ")) {
            if (i % 3 == 0) {
                if (temp != null) {
                    queue.enqueue(temp);
                }
                temp = new int[3];
                i = 0;
            }
            
            temp[i++] = Integer.parseInt(s);
        }
        queue.enqueue(temp);
        
        in.close();
    }
    
    /**
     * Sorts tokens by object type, and generates each creation command
     * for the gml file.
     * @param l {x, y, obj_id}
     */
    private void l2s(int[] l) {
        String add0 = l[0] >99 ? "" : "0";
        String add1 = l[1] >99 ? "" : "0";
        String out = "instance_create(" + add0 + Integer.toString(l[0]) + "," + add1 + Integer.toString(l[1]);
        int queueID = 0;
        
        switch (l[2]) {
            
            case 1:
                out += ",objBlock)";
                queueID = 1;
                break;
            case 2:
                out += ",objMiniBlock)";
                queueID = 1;
                break;
                
            case 3:
                out += ",objSpikeUp)";
                queueID = 2;
                break;
            case 4:
                out += ",objSpikeRight)";
                queueID = 2;
                break;
            case 5:
                out += ",objSpikeLeft)";
                queueID = 2;
                break;
            case 6:
                out += ",objSpikeDown)";
                queueID = 2;
                break;
            case 7:
                out += ",objMiniUp)";
                queueID = 2;
                break;
            case 8:
                out += ",objMiniRight)";
                queueID = 2;
                break;
            case 9:
                out += ",objMiniLeft)";
                queueID = 2;
                break;
            case 10:
                out += ",objMiniDown)";
                queueID = 2;
                break;
            case 11:
                out += ",objCherry)";
                queueID = 2;
                break;
                
            case 15:
                out += ",objWater2)";
                break;
                
            case 12:
                out += ",objSave)";
                break;
            
            case 13:
                out += ",objMovingPlatform)";
                queueID = 3;
                break;
                
            case 16:
                out += ",objWalljumpL)";
                break;
            case 17:
                out += ",objWalljumpR)";
                break;
                
            case 20:
                out += ",objPlayerStart)";
                break;
                
            case 21:
                out += ",objWarpAutosaveNext)";
                break;
            
            default:
                out = "//INVALID OBJECT ID: "+ l[2] + out + ");";
        }
        
        switch (queueID) {
            case 1:
                // for blocks
                blocks.enqueue(out);
                break;
            case 2:
                // for killers
                killers.enqueue(out);
                break;
            case 3:
                platforms.enqueue(out);
                // for platforms
                break;
            default:
                // for other objects (saves, warps, ...)
                other.enqueue(out);
                break;
        }
    }

    protected String getFileName() {
        return fileName; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

/**
 * Barebones queue used to store symbols as int arrays
 * @author Gabriel
 */
class GhettoQueue {
    // {{x, y, obj_ID},...}
    int[][] data = {};
    
    /**
     * Adds item to the end of the data array.
     * @param l {x,y,obj_ID}
     */
    void enqueue(int[] l) {
        data = Arrays.copyOf(data, data.length + 1);
        data[data.length - 1] = Arrays.copyOf(l,l.length);
    }
    
    /**
     * Removes item at index 0.
     * @return data[0]
     */
    int[] dequeue() {
        int[] out = Arrays.copyOf(data[0],data[0].length);
        int[][] temp = Arrays.copyOf(data,data.length - 1);
        int i = 0;
        
        for (int[] l: temp) {
            temp[i] = data[++i];
        }
        
        data = temp;
        
        return out;
    }
    
    /**
     * @return 
     *          empty -> false
     *          else -> true
     */
    boolean hasNext() {
        return (data.length > 0);
    }
}

/**
 * Alternate implementation of GhettoQueue that handles strings and is
 * no longer FIFO due to the data being sorted when an item is added.
 * This is not actually a queue.
 * 
 * @author Gabriel
 */
class NotQueue {
    String[] data = {};
    
    /**
     * Adds an item to data and sorts data.
     * 
     * @param l Creation command for gml file
     */
    void enqueue(String l) {
        data = Arrays.copyOf(data, data.length + 1);
        data[data.length - 1] = l;
        Arrays.sort(data);
        String temp = "";
        
        for (int i = 0; i < data.length / 2; i++) {
            temp = data[i];
            data[i] = data[data.length - 1 - i];
            data[data.length - 1 - i] = temp;
        }
    }
    
    /**
     * Removes the first item from data.
     * 
     * @return creation command for gml file
     */
    String dequeue() {
        String out = data[0];
        String[] temp = Arrays.copyOf(data,data.length - 1);
        int i = 0;
        
        for (String l: temp) {
            temp[i] = data[++i];
        }
        
        data = temp;
        
        return out;
    }
    
     /**
     * @return 
     *          empty -> false
     *          else -> true
     */
    boolean hasNext() {
        return (data.length > 0);
    }
}