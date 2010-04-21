/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal;

//import org.jdhp.opencal.card.Card;
import java.net.URI;
import java.net.URISyntaxException;

import org.jdhp.opencal.card.CardList;
//import org.jdhp.opencal.card.lists.AllCardList;
//import org.jdhp.opencal.card.lists.CardByTagList;
//import org.jdhp.opencal.card.lists.NewCardList;
import org.jdhp.opencal.card.lists.PlannedCardList;
//import org.jdhp.opencal.card.lists.ReviewedCardList;
//import org.jdhp.opencal.card.lists.HiddenCardList;
import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.PersonalKnowledgeBase;
import org.jdhp.opencal.professor.Professor;
import org.jdhp.opencal.professor.ProfessorAlan;
import org.jdhp.opencal.professor.ProfessorBen;
import org.jdhp.opencal.professor.ProfessorCharlie;
import org.jdhp.opencal.UserProperties;

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
	
	public static MainWindow mainWindow;
	
	//public static AllCardList allCardList;
	
	public static PlannedCardList plannedCardList;
	
	//public static ReviewedCardList reviewedCardList;
	
	//public static NewCardList newCardList;
	
	//public static HiddenCardList hiddenCardList;
	
	//public static CardByTagList cardByTagList;
	
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
        
        CardList.initMainCardList();
        //OpenCAL.allCardList = new AllCardList();
        OpenCAL.plannedCardList = new PlannedCardList();
        //OpenCAL.reviewedCardList = new ReviewedCardList();
        //OpenCAL.newCardList = new NewCardList();
        //OpenCAL.hiddenCardList = new HiddenCardList();
        //OpenCAL.cardByTagList = new CardByTagList();

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
