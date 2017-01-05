/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import javax.swing.SwingUtilities;

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
			Runnable r = () -> new ScriptGui();
			SwingUtilities.invokeLater(r);
		} else {
			new ScriptFromJmap(args[0]);
		}
	}

}
