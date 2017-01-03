/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Gabriel
 */
public class XMLWriter {

	/**
	 * Generates xml formatted room file that can be read by GMS. It uses an
	 * external xml file as a references and adds instances for each item.
	 *
	 * @param items array of items from preview
	 */
	public static void itemsToGMX(Item[] items, OutputStream os) {
		String itemPrefix = String.format("inst_%d", System.currentTimeMillis());

		SAXBuilder saxBuilder = new SAXBuilder();
		int itemCount = 0;

		try {
			Document doc = saxBuilder.build(new File("template.xml"));
			Element instances = doc.getRootElement().getChild("instances");

			for (Item item : items) {
				if (item != null) {
					instances.addContent(new Element("instance")
							  .setAttribute("objName", item.itemName)
							  .setAttribute("x", Integer.toString(item.x))
							  .setAttribute("y", Integer.toString(item.y))
							  .setAttribute("name", String.format("%s%d", itemPrefix,
										 itemCount++))
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
			out.output(doc.getRootElement(), os);

		} catch (JDOMException ex) {
		} catch (IOException ex) {
		}
	}

	private static void elementShortcut(XMLStreamWriter writer, String name,
			  String contents) throws XMLStreamException {
		writer.writeCharacters("\n");
		writer.writeStartElement(name);

		writer.writeCharacters(contents);

		writer.writeEndElement();
	}
}
