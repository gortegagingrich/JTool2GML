/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.util.Arrays;

/**
 *
 * @author Gabriel
 */
public class ScriptFromItem {

	private NotDequeue blocks;
	private NotDequeue killers;
	private NotDequeue platforms;
	private NotDequeue other;

	private String scriptContents;

	public ScriptFromItem(Item[] itemList) {
		blocks = new NotDequeue();
		killers = new NotDequeue();
		platforms = new NotDequeue();
		other = new NotDequeue();

		System.out.println(Arrays.toString(itemList));

		for (Item item : itemList) {
			if (item != null) {

				switch (item.itemName) {
					case "objBlock":
					case "objMiniBlock":
						blocks.enqueue(item.toString());
						break;

					case "objSpikeUp":
					case "objSpikeDown":
					case "objSpikeRight":
					case "objSpikeLeft":
					case "objMiniUp":
					case "objMiniDown":
					case "objMiniLeft":
					case "objMiniRight":
						killers.enqueue(item.toString());
						break;

					case "objMovingPlatform":
						platforms.enqueue(item.toString());
						break;

					default:
						other.enqueue(item.toString());
				}
			}
		}
	}

	@Override
	public String toString() {
		String out = "var o;\n\n//blocks\n";

		while (blocks.hasNext()) {
			out += blocks.pop();
		}

		out += "\n//killers\n";

		while (killers.hasNext()) {
			out += killers.pop();
		}

		out += "\n//platforms\n";

		while (platforms.hasNext()) {
			out += platforms.pop();
		}

		out += "\n//other objects\n";

		while (other.hasNext()) {
			out += other.pop();
		}

		return out;
	}
}
