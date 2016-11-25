/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Gabriel
 */
public class JMap2Script {

    private SimpleQueue queue;
    private NotDequeue blocks;
    private NotDequeue killers;
    private NotDequeue platforms;
    private NotDequeue other;
    private boolean toFile = true;
    private String str;
    private String fileName;

    /**
     * Main logic of the program is here.
     *
     * @param fName input file from args
     */
    public JMap2Script(String fName) {
        this(fName, true);
    }

    /**
     * Limited constructor that does not always write to file. Orders platforms
     * by X ASC, Y ASC TODO: rewrite this at some point
     *
     * @param fName
     * @param f
     */
    public JMap2Script(String fName, boolean f) {
        toFile = f;
        this.fileName = fName;
        queue = new SimpleQueue();
        blocks = new NotDequeue();
        killers = new NotDequeue();
        platforms = new NotDequeue();
        other = new NotDequeue();
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
                    out.println(blocks.pop());
                } else {
                    str += blocks.pop() + "\n";
                }
            }

            if (toFile) {
                out.println("\n// killers");
            }

            str += "\n// killers\n";

            while (killers.hasNext()) {
                if (toFile) {
                    out.println(killers.pop());
                }

                str += killers.pop() + "\n";

            }

            if (toFile) {
                out.println("// platforms");
            }

            str += "\n// platforms\n";

            while (platforms.hasNext()) {
                if (toFile) {
                    out.println(platforms.pop());
                }

                str += platforms.pop() + "\n";
            }

            if (toFile) {
                out.println("\n// other objects");
            }

            str += "\n// other objects\n";

            while (other.hasNext()) {
                if (toFile) {
                    out.println(other.pop());
                }

                str += other.pop() + "\n";
            }

            out.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Automatically orders platforms by X ASC and Y ASC
     *
     * @param fName input file from args
     * @param f output file
     */
    public JMap2Script(String fName, File f) {
        this(fName, f, false);
    }

    /**
     * Main logic of the program is here.
     *
     * @param fName input file from args
     * @param f output file
     * @param reversePlatforms false = pop, true = remove
     */
    public JMap2Script(String fName, File f, boolean reversePlatforms) {
        queue = new SimpleQueue();
        blocks = new NotDequeue();
        killers = new NotDequeue();
        platforms = new NotDequeue();
        other = new NotDequeue();

        try {
            readFile(fName);
            PrintWriter out = new PrintWriter(f);

            while (queue.hasNext()) {
                l2s(queue.dequeue());
            }

            out.println("// blocks");
            while (blocks.hasNext()) {
                out.println(blocks.pop());
            }

            out.println("\n// killers");
            while (killers.hasNext()) {
                out.println(killers.pop());
            }

            out.println("\n// platforms");
            while (platforms.hasNext()) {
                if (!reversePlatforms) {
                    out.println(platforms.pop());
                } else {
                    out.println(platforms.remove());
                }
            }

            out.println("\n// other objects");
            while (other.hasNext()) {
                out.println(other.pop());
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

        for (String s : str.split(" ")) {
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
     * Sorts tokens by object type, and generates each creation command for the
     * gml file.
     *
     * @param l {x, y, obj_id}
     */
    private void l2s(int[] l) {
       String add0, add1, out;
       
       if (l[0] < 99 && l[0] >= 0) {
          add0 = "0";
       } else {
          add0 = "";
       }
       
       if (l[1] < 99 && l[1] >= 0) {
          add1 = "0";
       } else {
          add1 = "";
       }
       
        out = "instance_create(" + add0 + Integer.toString(l[0]) + "," + add1 + Integer.toString(l[1]);
        int queueID = 0;

        // In the future, this should read from an external table to allow for 
        // easy substitutions.
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
                out = "//INVALID OBJECT ID: " + l[2] + out + ");";
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
        return fileName;
    }

}

/**
 * Barebones queue used to store symbols as int arrays
 *
 * @author Gabriel
 */
class SimpleQueue {

    // {{x, y, obj_ID},...}

    int[][] data = {};

    /**
     * Adds item to the end of the data array.
     *
     * @param l {x,y,obj_ID}
     */
    void enqueue(int[] l) {
        data = Arrays.copyOf(data, data.length + 1);
        data[data.length - 1] = Arrays.copyOf(l, l.length);
    }

    /**
     * Removes item at index 0.
     *
     * @return data[0]
     */
    int[] dequeue() {
        int[] out = Arrays.copyOf(data[0], data[0].length);
        int[][] temp = Arrays.copyOf(data, data.length - 1);
        int i = 0;

        for (int[] l : temp) {
            temp[i] = data[++i];
        }

        data = temp;

        return out;
    }

    /**
     * @return empty -> false else -> true
     */
    boolean hasNext() {
        return (data.length > 0);
    }
}

/**
 * Simple version of a double ended queue that sorts the contents when an item
 * is added. Some objects have different properties depending on the order
 * they're placed, so it would be useful to be able to reverse it if if needed.
 *
 * @author Gabriel
 */
class NotDequeue {

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
        String temp;

        for (int i = 0; i < data.length / 2; i++) {
            temp = data[i];
            data[i] = data[data.length - 1 - i];
            data[data.length - 1 - i] = temp;
        }
    }

    /**
     * Removes and returns the first item from data.
     *
     * @return first creation command in the structure.
     */
    String pop() {
        String out = data[0];
        String[] temp = Arrays.copyOf(data, data.length - 1);
        int i = 0;

        for (String l : temp) {
            temp[i] = data[++i];
        }

        data = temp;

        return out;
    }

    /**
     * Removes and returns final item from data
     *
     * @return final creation command in the structure.
     */
    String remove() {
        String out = data[data.length - 1];
        data = Arrays.copyOf(data, data.length - 1);
        return out;
    }

    /**
     * @return empty -> false else -> true
     */
    boolean hasNext() {
        return (data.length > 0);
    }
}
