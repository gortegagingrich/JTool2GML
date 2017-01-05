/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Gabriel
 */
public class GMX {

	/**
	 * Generates xml formatted room file that can be read by GMS. Uses
	 * external xml file as a references and adds instances for each item.
	 *
	 * @param items array of items from preview
	 * @param os stream to write xml string to
	 */
	public static void itemsToGMX(Item[] items, OutputStream os) {
		String prefix = String.format("inst_%d", System.currentTimeMillis());
		
		SAXBuilder saxBuilder = new SAXBuilder();
		int count = 0;
		
		try {
			Element room = saxBuilder.build(new File("template.xml")).
					  getRootElement();
			Element instances = room.getChild("instances");
			
			for (Item item : items) {
				if (item != null) {
					instances.addContent(new Element("instance")
							  .setAttribute("objName", item.itemName)
							  .setAttribute("x", Integer.toString(item.x))
							  .setAttribute("y", Integer.toString(item.y))
							  .setAttribute("name", String.format("%s%d", prefix,
										 count++))
							  .setAttribute("locked", "0")
							  .setAttribute("code", item.creationCode)
							  .setAttribute("scaleX", Double.toString(item.xScale))
							  .setAttribute("scaleY", Double.toString(item.yScale))
							  .setAttribute("colour", "4294967295")
							  .setAttribute("rotation", "0")
					);
					instances.addContent("\n");
				}
			}
			
			XMLOutputter out = new XMLOutputter();
			out.output(room, os);
			
		} catch (JDOMException | IOException ex) {
		}
	}
	
	/**
	 * Creates array of Items from given gmx file.
	 * Will be used from importing gms maps
	 * 
	 * @param filename .room.gmx file
	 * @return array of items for each instance in the room
	 */
	public static Item[] gmxToItems(String filename) {
		int index = 0;
		Item[] out = null;
		String[] args;
		
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Element room = builder.build(new File(filename)).getRootElement();
			
			out = new Item[room.getChild("instances").getChildren().size()];
			System.out.println(out.length);
			
			for (Element child : room.getChild("instances").getChildren()) {
				args = new String[]{child.getAttributeValue("x"), child.
					getAttributeValue("y"), child.getAttributeValue("objName")};
				
				out[index] = new ItemImage(args);
				out[index].xScale = Double.parseDouble(child.getAttributeValue(
						  "scaleX"));
				out[index].yScale = Double.parseDouble(child.getAttributeValue("scaleY"));
				out[index].creationCode = child.getAttributeValue("code");
				
				index++;
			}
			
		} catch (JDOMException | IOException ex) {
		}
		
		return out;
	}
}
