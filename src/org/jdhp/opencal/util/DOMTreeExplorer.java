/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMTreeExplorer {

	/**
	 * This method can be used to print a Node tree from a DOM document loaded in memory.
	 * 
	 * Example usage :
	 * DOMTreeExplorer.printNodeTree(
	 * 			this.element,
	 * 			"Before removing old 'tag' elements\n\n",
	 * 			"\n************************\n\n");
	 * 
	 * @param node The node to inspect
	 * @param header Some words printed before the tree
	 * @param footer Some words printed after the tree
	 */
	public static void printNodeTree(Node node, String header, String footer) {
		System.out.print(header);
		
		DOMTreeExplorer.printNodeTree(node, "");
		
		System.out.print(footer);
	}
	
	/**
	 * This method is used by printNodeTree to get a recursive call.
	 * 
	 * @param node The (sub)node to inspect
	 * @param header Used to indent the output
	 */
	private static void printNodeTree(Node node, String header) {
		System.out.println(header + node.getNodeName());
		
		NodeList nodes = node.getChildNodes();
		for(int i=0 ; i<nodes.getLength() ; i++) {
			DOMTreeExplorer.printNodeTree(nodes.item(i), header + "   ");
		}
	}
}
