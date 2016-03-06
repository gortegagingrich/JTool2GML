/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

/**
 *
 * @author Gabriel
 */
public class JMap2GML {

    
    /**
     * @param args {inputfile}
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("No arguments passed.\nUsing GUI.");
            JMap2ScriptGui.main(args);
        }   else {
            new JMap2Script(args[0]);
        }
    }
    
}
