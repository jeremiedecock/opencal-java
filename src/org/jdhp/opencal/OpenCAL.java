/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010 Jérémie Decock
 */

package org.jdhp.opencal;

//import org.jdhp.opencal.card.Card;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;

import org.jdhp.opencal.data.PersonalKnowledgeBase;
import org.jdhp.opencal.data.UserProperties;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.model.professor.ProfessorAlan;
import org.jdhp.opencal.model.professor.ProfessorBen;
import org.jdhp.opencal.model.professor.ProfessorCharlie;
import org.jdhp.opencal.swt.MainWindow;

/**
 * OpenCAL
 * 
 * @author Jérémie Decock
 * @version 3.0
 */
public class OpenCAL {

	public final static String PROGRAM_VERSION = "3.0";
	
	public final static String PROGRAM_NAME = "OpenCAL";
	
	public final static String RIGHT_ANSWER_STRING = "good";
	
	public final static String WRONG_ANSWER_STRING = "bad";
	
	public final static Collection<Card> cardCollection = new HashSet<Card>();
	
	public static MainWindow mainWindow;
	
	private static Professor professor;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserProperties.loadUserProperties();
		
		// Create Professor
		OpenCAL.setProfessor(UserProperties.getProfessorName());
		
		// Open default PKB File and create card set
		try {
			URI uri = new URI(UserProperties.getDefaultPkbFilePath());
			PersonalKnowledgeBase.load(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
		// Make and run GUI
		OpenCAL.mainWindow = new MainWindow();
		OpenCAL.mainWindow.run();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Professor getProfessor() {
		return OpenCAL.professor;
	}
	
	/**
	 * 
	 * @return
	 */
	public static void setProfessor(String professorName) {
		if(professorName == null) {
			// TODO : set a default professor and manage errors
			System.out.println("No professor set.");
			OpenCAL.exit(1);
		} else if(professorName.equals("Alan")) OpenCAL.professor = new ProfessorAlan();
		else if(professorName.equals("Ben")) OpenCAL.professor = new ProfessorBen();
		else if(professorName.equals("Charlie")) OpenCAL.professor = new ProfessorCharlie();
		else {
			// TODO : set a default professor and manage errors
			System.out.println("No professor set.");
			OpenCAL.exit(1);
		}
	}
		
	/**
	 * Quit the program
	 * 
	 * @param status
	 */
	public static void exit(int status) {
		System.exit(status);
	}

}
