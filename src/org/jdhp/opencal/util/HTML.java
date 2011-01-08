/*
 * Copyright (c) 2011 Jérémie Decock
 */

package org.jdhp.opencal.util;

public class HTML {
	
	/**
	 * Replace the five reserved characters with entities
	 * to avoid HTML interpretation of src.
	 * 
	 * @param src the string to process
	 * @return a string without HTML tags
	 */
	public static String replaceSpecialChars(String src) {
		src = src.replaceAll("&", "&amp;");
		src = src.replaceAll("<", "&lt;");
		src = src.replaceAll(">", "&gt;");
		src = src.replaceAll("\"", "&quot;");
		src = src.replaceAll("\'", "&apos;");
		
		return src;
	}
}
