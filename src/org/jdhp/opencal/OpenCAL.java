/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */

package org.jdhp.opencal;

import java.net.URI;
import java.net.URISyntaxException;

import org.jdhp.opencal.data.pkb.PersonalKnowledgeBase;
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseException;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.data.properties.ApplicationPropertiesException;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.model.professor.ProfessorFactory;
import org.jdhp.opencal.model.professor.ProfessorFactoryException;
import org.jdhp.opencal.ui.swt.MainWindow;

/**
 * OpenCAL
 * 
 * @author Jérémie Decock
 * @version 3.0
 */
public class OpenCAL {

	public final static String PROGRAM_VERSION = "3.0.0";
	
	public final static String PROGRAM_NAME = "OpenCAL";
	
	public final static String COPYRIGHT = "Copyright © 2007,2008,2009,2010,2011,2012 Jérémie DECOCK";
	
	public final static String WEB_SITE = "http://www.jdhp.org";
	
	// TODO
	public static Professor professor = null; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Load UserProperties
		try {
			ApplicationProperties.loadApplicationProperties();
		} catch (ApplicationPropertiesException e) {
			MainWindow.getInstance().printError(e.getMessage());
			MainWindow.getInstance().close();
			System.exit(1);
		}
		
		// Create Professor
		try {
			OpenCAL.professor = ProfessorFactory.createProfessor(ApplicationProperties.getProfessorName());
		} catch(ProfessorFactoryException e) {
			MainWindow.getInstance().printError(e.getMessage());
			MainWindow.getInstance().close();
			System.exit(1);
		}
		
		// Open default PKB File and create card set
		try {
			URI uri = new URI(ApplicationProperties.getPkbPath());
			PersonalKnowledgeBase.load(uri);
		} catch(URISyntaxException e) {
			MainWindow.getInstance().printError(e.getMessage());
			MainWindow.getInstance().close();
			System.exit(1);
		} catch(PersonalKnowledgeBaseException e) {
			MainWindow.getInstance().printError(e.getMessage());
			MainWindow.getInstance().close();
			System.exit(1);
		}
        
		// Make and run GUI
		MainWindow.getInstance().run();
		
		// Save PKB file
		try {
			PersonalKnowledgeBase.save();
		} catch(PersonalKnowledgeBaseException e) {
			System.err.println(e.getMessage());
			//MainWindow.getInstance().printError(e.getMessage());  // TODO: le shell est fermé => pas d'affichage graphique... mettre les err dans des dialog (shell)  part ?
		}
		
		// Save UserProperties
		try {
			ApplicationProperties.saveApplicationProperties();
		} catch (ApplicationPropertiesException e) {
			System.err.println(e.getMessage());
			//MainWindow.getInstance().printError(e.getMessage());  // TODO: le shell est fermé => pas d'affichage graphique... mettre les err dans des dialog (shell)  part ?
			System.exit(1);
		}
	}

}
