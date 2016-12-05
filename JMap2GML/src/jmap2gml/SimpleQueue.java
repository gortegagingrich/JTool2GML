/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.util.Arrays;

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
