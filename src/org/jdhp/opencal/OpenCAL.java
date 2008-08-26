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

import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.usecase.explore.MadeCardsController;
import org.jdhp.opencal.usecase.explore.ReviewedCardsController;
import org.jdhp.opencal.usecase.make.MakeController;
import org.jdhp.opencal.usecase.review.ReviewedCardList;
import org.jdhp.opencal.usecase.review.inspector.Inspector;
import org.jdhp.opencal.usecase.review.inspector.InspectorBrian;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class OpenCAL {

	public final static String programVersion = "3.0";
	
	public final static String programName = "OpenCAL";
	
	public final static String pkbFilePath = "/home/gremy/user_dev.pkb";
	
	public final static String tmpPkbFile = "/tmp/" + OpenCAL.programName + "_user.tmp.pkb";
	
	public static MainWindow mainWindow;
	
	public static Document domDocument;
	
	public static SimpleDateFormat iso8601Formatter;
	
	public static ReviewedCardList reviewedCardList;
	
	public static Inspector inspector;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OpenCAL.iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		// Build the XML DOM tree
		try {
			File pkbFile = new File(OpenCAL.pkbFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			OpenCAL.domDocument = db.parse(pkbFile);
		} catch(SAXException e) {
			OpenCAL.mainWindow.printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.mainWindow.print(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.mainWindow.printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			OpenCAL.exit(2);
		} catch(ParserConfigurationException e) {
			OpenCAL.mainWindow.printError("The XML parser was not configured (ParserConfigurationException)");
			OpenCAL.exit(2);
		}
		
		// Misc init //
		OpenCAL.init();
		
		OpenCAL.mainWindow = new MainWindow();
		OpenCAL.mainWindow.run();
	}

	/**
	 * Initialize all controllers
	 */
	public static void init() {
		OpenCAL.inspector = new InspectorBrian();
		
		MakeController.init();
		OpenCAL.reviewedCardList = new ReviewedCardList();
		MadeCardsController.init();
		ReviewedCardsController.init();
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
			Result resultat = new StreamResult(new File(OpenCAL.pkbFilePath));

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
