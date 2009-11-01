/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.lists.AllCardList;
import org.jdhp.opencal.card.lists.CardByTagList;
import org.jdhp.opencal.card.lists.NewCardList;
import org.jdhp.opencal.card.lists.PlannedCardList;
import org.jdhp.opencal.card.lists.ReviewedCardList;
import org.jdhp.opencal.card.lists.HiddenCardList;
import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.professor.Professor;
import org.jdhp.opencal.professor.ProfessorAlan;
import org.jdhp.opencal.professor.ProfessorBen;
import org.jdhp.opencal.professor.ProfessorCharlie;
import org.jdhp.opencal.UserProperties;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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

	private static File pkbFile;
	
	private static Document domDocument;
	
	public static MainWindow mainWindow;
	
	public static AllCardList allCardList;
	
	public static PlannedCardList plannedCardList;
	
	public static ReviewedCardList reviewedCardList;
	
	public static NewCardList newCardList;
	
	public static HiddenCardList hiddenCardList;
	
	public static CardByTagList cardByTagList;
	
	private static Professor professor;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserProperties.loadUserProperties();
		
		// Create Professor
		OpenCAL.setProfessor(UserProperties.getProfessorName());
		
		// Open PKB File and create cards lists
		OpenCAL.openPkbFile(UserProperties.getDefaultPkbFilePath());
        Card.initCardList();

		// Make and run GUI
		OpenCAL.mainWindow = new MainWindow();
		OpenCAL.mainWindow.run();
	}
	
	/**
	 * 
	 * @param pkbFilePath
	 */
	public static void createPkbFile(String pkbFilePath) {
		// TODO ...
	}
	
	/**
	 * 
	 * @param pkbFilePath
	 */
	public static void openPkbFile(String pkbFilePath) {
		OpenCAL.pkbFile = new File(pkbFilePath);

		// Build the XML DOM tree
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			OpenCAL.domDocument = db.parse(OpenCAL.pkbFile);
			
			// Make lists
			OpenCAL.allCardList = new AllCardList();
			OpenCAL.plannedCardList = new PlannedCardList();
			OpenCAL.reviewedCardList = new ReviewedCardList();
			OpenCAL.newCardList = new NewCardList();
			OpenCAL.hiddenCardList = new HiddenCardList();
			OpenCAL.cardByTagList = new CardByTagList();

		} catch(SAXException e) {
			OpenCAL.mainWindow.printError(UserProperties.getDefaultPkbFilePath() + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.mainWindow.print(UserProperties.getDefaultPkbFilePath() + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.mainWindow.printError(UserProperties.getDefaultPkbFilePath() + " est illisible (IOException)");
			OpenCAL.exit(2);
		} catch(ParserConfigurationException e) {
			OpenCAL.mainWindow.printError("The XML parser was not configured (ParserConfigurationException)");
			OpenCAL.exit(2);
		}
	}
	
	/**
	 * 
	 * @param pkbFilePath
	 */
	public static void closePkbFile() {
		// TODO : save data and close streams.
//		// OpenCAL.updatePkbFile()
//		
//		OpenCAL.allCardList = null;
//		OpenCAL.plannedCardList = null;
//		OpenCAL.reviewedCardList = null;
//		OpenCAL.newCardList = null;
//		OpenCAL.hiddenCardList = null;
//		
//		OpenCAL.domDocument = null;
//		OpenCAL.pkbFile = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static File getPkbFile() {
		return OpenCAL.pkbFile;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Document getDomDocument() {
		return OpenCAL.domDocument;
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
	 * 
	 * @param document
	 */
	public static void updatePkbFile() {
		try {
			// Make DOM source
			Source domSource = new DOMSource(OpenCAL.domDocument);

			// Make output file
			Result streamResult = new StreamResult(OpenCAL.pkbFile);

			// Setup transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// Transformation
			transformer.transform(domSource, streamResult);
		} catch (Exception e) {
			e.printStackTrace();
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
