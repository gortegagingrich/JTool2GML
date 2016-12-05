/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.util.Arrays;

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

		System.out.printf("Enqueued: %s\n", l);
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
