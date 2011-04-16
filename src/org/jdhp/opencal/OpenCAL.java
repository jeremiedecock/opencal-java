/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */

package org.jdhp.opencal;

import java.net.URI;
import java.net.URISyntaxException;

import org.jdhp.opencal.data.pkb.PersonalKnowledgeBase;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.model.professor.Professors;
import org.jdhp.opencal.swt.MainWindow;

/**
 * OpenCAL
 * 
 * @author Jérémie Decock
 * @version 3.0
 */
public class OpenCAL {

	public final static String PROGRAM_VERSION = "3.0.0";
	
	public final static String PROGRAM_NAME = "OpenCAL";
	
	public final static String COPYRIGHT = "Copyright © 2007,2008,2009,2010,2011 Jérémie DECOCK";
	
	public final static String WEB_SITE = "http://www.jdhp.org";
	
	public static MainWindow mainWindow;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Load UserProperties
		ApplicationProperties.loadApplicationProperties();
		
		// Create Professor
		Professors.setProfessorName(ApplicationProperties.getProfessorName());
		
		// Open default PKB File and create card set
		try {
			URI uri = new URI(ApplicationProperties.getPkbPath());
			PersonalKnowledgeBase.load(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
		// Make and run GUI
		OpenCAL.mainWindow = new MainWindow();
		OpenCAL.mainWindow.run();
		
		// Save UserProperties
		ApplicationProperties.saveApplicationProperties();
	}

}
