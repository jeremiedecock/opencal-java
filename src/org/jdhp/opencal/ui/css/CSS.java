/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.css;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSS {
	
	final public static String REVIEW_CSS = CSS.loadCSS("review.css");
	
	final public static String EDITABLE_BROWSER_CSS = CSS.loadCSS("editable_browser.css");
	
	/**
	 * TODO : charger le fichier CSS depuis "source"
	 * 
	 * @param source
	 * @return
	 */
	private static String loadCSS(String source) {
		StringBuffer css = new StringBuffer();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(CSS.class.getResourceAsStream(source)));
			
			String line;
			do {
				line = reader.readLine();
				if(line != null) {
					css.append(line);
				}
			} while(line != null);
		} catch(FileNotFoundException e) {
			System.out.println(e);
		} catch(IOException e) {
			System.out.println(e);
		}
		
		return css.toString();
	}
}
