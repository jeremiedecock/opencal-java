/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdhp.opencal.controller.explorer.MadeCardsController;
import org.jdhp.opencal.controller.explorer.ReviewedCardsController;
import org.jdhp.opencal.controller.maker.MakeController;
import org.jdhp.opencal.controller.reviewer.ReviewController;
import org.jdhp.opencal.controller.stats.StatsController;
import org.jdhp.opencal.gui.MainWindow;
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
	
	public static MainWindow MainWindow;
	
	private static Document xmlDocument;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OpenCAL.init();
		OpenCAL.MainWindow = new MainWindow();
		OpenCAL.MainWindow.run();
	}

	/**
	 * Initialize all controllers
	 */
	public static void init() {
		// Make the XML DOM tree
		try {
			File pkbFile = new File(OpenCAL.pkbFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			OpenCAL.xmlDocument = db.parse(pkbFile);
			
//			Element rootElement = doc.getDocumentElement();
//			System.out.println("Nom root element : " + rootElement.getNodeName());
//			
//			NodeList nodeCards = doc.getElementsByTagName("card");
//			for(int i=0 ; i<nodeCards.getLength() ; i++) {
//				Element card = (Element) nodeCards.item(i);
//				Element question = (Element) card.getElementsByTagName("question").item(0);
//				System.out.println(card.getAttribute("id") + " : " + ((Text) question.getFirstChild()).getData());
//			}
		} catch(SAXException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.MainWindow.print(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			OpenCAL.exit(2);
		} catch(ParserConfigurationException e) {
			OpenCAL.MainWindow.printError("The XML parser was not configured (ParserConfigurationException)");
			OpenCAL.exit(2);
		}
		
		MakeController.init();
		ReviewController.init();
		StatsController.init();
		MadeCardsController.init();
		ReviewedCardsController.init();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Document getXmlDocument() {
		return OpenCAL.xmlDocument;
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
