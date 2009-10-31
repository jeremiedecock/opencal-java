/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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
import org.jdhp.opencal.toolkit.Sensor;

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

	private static Properties userProperties;
	
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
	    //Sensor.setOutputMethod(Sensor.PRINT_METHOD);
	    Sensor.setOutputMethod(Sensor.CSV_METHOD);

        Sensor.measure("Before getting user properties");
		OpenCAL.userProperties = OpenCAL.getUserProperties();
        Sensor.measure("After getting user properties");
		
		// Create Professor
        Sensor.measure("Before setting professor");
		OpenCAL.setProfessor(OpenCAL.getProfessorName());
        Sensor.measure("After setting professor");
		
		// Open PKB File and create cards lists
        Sensor.measure("Before opening PKB file and creating lists");
		OpenCAL.openPkbFile(OpenCAL.getDefaultPkbFilePath());
        Sensor.measure("After opening PKB file and creating lists");
        Sensor.measure("Before creating main lists");
        Card.initCardList();
        Sensor.measure("After creating main lists");

		// Make and run GUI
        Sensor.measure("Before creating main window");
		OpenCAL.mainWindow = new MainWindow();
        Sensor.measure("After creating main window");
        Sensor.measure("Before running SWT");
		OpenCAL.mainWindow.run();
        Sensor.measure("After running SWT");
	}
	
	/**
	 * Get the user properties path (for example : "/home/foo/.opencal/opencal.properties").
	 * 
	 * @return
	 */
	public static String getUserPropertiesPath() {
		String userPropertiesPath = null;
		
		String userHome = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator"); // TODO : ça ne devrait pas être déclaré ici...
		
		if(userHome != null && fileSeparator != null) {
			userPropertiesPath = userHome + fileSeparator + ".opencal" + fileSeparator + "opencal.properties";
		} else {
			System.out.println("Unexpected error : your system or your JVM can't run this program.");
			System.exit(1);
		}
		
		return userPropertiesPath;
	}
	
	/**
	 * Set user properties object.
	 * 
	 * @return
	 */
	public static Properties getUserProperties() {
		Properties userProperties = new Properties();
		
		try {
			FileInputStream userPropertiesFile = new FileInputStream(OpenCAL.getUserPropertiesPath());
			userProperties.load(userPropertiesFile);
			userPropertiesFile.close();
		} catch(FileNotFoundException e) {
			OpenCAL.createUserPropertiesFile();
		} catch(IOException e) {
			// TODO : error...
		}
		
		return userProperties;
	}
	
	/**
	 * Create a user properties file when it doesn't exist.
	 */
	public static void createUserPropertiesFile() {
		System.out.println(OpenCAL.getUserPropertiesPath() + " is unreachable.");
		// TODO : call wizard (gui)...
		System.exit(1);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultPkbFilePath() {
		return OpenCAL.userProperties.getProperty("pkb.path");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getProfessorName() {
		return OpenCAL.userProperties.getProperty("professor.name");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getImgPath() {
		return OpenCAL.userProperties.getProperty("img.path");
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
			OpenCAL.mainWindow.printError(OpenCAL.getDefaultPkbFilePath() + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.mainWindow.print(OpenCAL.getDefaultPkbFilePath() + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.mainWindow.printError(OpenCAL.getDefaultPkbFilePath() + " est illisible (IOException)");
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
