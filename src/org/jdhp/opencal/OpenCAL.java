/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

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

import org.jdhp.opencal.card.lists.AllCardList;
import org.jdhp.opencal.card.lists.NewCardList;
import org.jdhp.opencal.card.lists.PlannedCardList;
import org.jdhp.opencal.card.lists.ReviewedCardList;
import org.jdhp.opencal.card.lists.SuspendedCardList;
import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.inspector.Inspector;
import org.jdhp.opencal.inspector.InspectorBrian;
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
	
	public final static String PKB_FILE_PATH = "/home/gremy/user_dev.pkb";
	
	// TODO : à supprimer...
	public final static String TMP_PKB_FILE = "/tmp/" + OpenCAL.PROGRAM_NAME + "_user.tmp.pkb";
	
	public final static String RIGHT_ANSWER_STRING = "good";
	
	public final static String WRONG_ANSWER_STRING = "bad";
	
	public final static String SUSPENDED_CARD_STRING = "Suspended";
	
	public static MainWindow mainWindow;
	
	public static Document domDocument;
	
	public static SimpleDateFormat iso8601Formatter;
	
	public static AllCardList allCardList;
	
	public static PlannedCardList plannedCardList;
	
	public static ReviewedCardList reviewedCardList;
	
	public static NewCardList newCardList;
	
	public static SuspendedCardList suspendedCardList;
	
	public static Inspector inspector;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OpenCAL.iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		// Build the XML DOM tree
		try {
			File pkbFile = new File(OpenCAL.PKB_FILE_PATH);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			OpenCAL.domDocument = db.parse(pkbFile);
		} catch(SAXException e) {
			OpenCAL.mainWindow.printError(OpenCAL.PKB_FILE_PATH + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.mainWindow.print(OpenCAL.PKB_FILE_PATH + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.mainWindow.printError(OpenCAL.PKB_FILE_PATH + " est illisible (IOException)");
			OpenCAL.exit(2);
		} catch(ParserConfigurationException e) {
			OpenCAL.mainWindow.printError("The XML parser was not configured (ParserConfigurationException)");
			OpenCAL.exit(2);
		}
		
		// Make inspector and lists
		OpenCAL.inspector = new InspectorBrian();
		
		OpenCAL.allCardList = new AllCardList();
		OpenCAL.plannedCardList = new PlannedCardList();
		OpenCAL.reviewedCardList = new ReviewedCardList();
		OpenCAL.newCardList = new NewCardList();
		OpenCAL.suspendedCardList = new SuspendedCardList();
		
		// Make and run GUI
		OpenCAL.mainWindow = new MainWindow();
		OpenCAL.mainWindow.run();
	}
	
	/**
	 * 
	 * @param document
	 */
	public static void updateXmlFile() {
		try {
			// Création de la source DOM
			Source source = new DOMSource(OpenCAL.domDocument);

			// Création du fichier de sortie
			Result resultat = new StreamResult(new File(OpenCAL.PKB_FILE_PATH));

			// Configuration du transformer
			TransformerFactory fabrique = TransformerFactory.newInstance();
			Transformer transformer = fabrique.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// Transformation
			transformer.transform(source, resultat);
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
