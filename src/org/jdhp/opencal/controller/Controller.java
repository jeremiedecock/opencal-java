/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.controller.explorer.MadeCardsController;
import org.jdhp.opencal.controller.explorer.ReviewedCardsController;
import org.jdhp.opencal.controller.maker.MakeController;
import org.jdhp.opencal.controller.reviewer.ReviewController;
import org.jdhp.opencal.controller.stats.StatsController;
import org.jdhp.opencal.view.UserInterface;
import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Controller {
	
	private static UserInterface ui;
	
	private static Document xmlDocument;
	
	/**
	 * Initialize all controllers
	 */
	public static void init() {
		// Make the XML DOM tree
		try {
			File pkbFile = new File(OpenCAL.pkbFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Controller.xmlDocument = db.parse(pkbFile);
			
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
			Controller.getUserInterface().printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			Controller.exit(2);
		} catch(FileNotFoundException e) {
			Controller.getUserInterface().print(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			Controller.exit(2);
		} catch(IOException e) {
			Controller.getUserInterface().printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			Controller.exit(2);
		} catch(ParserConfigurationException e) {
			Controller.getUserInterface().printError("The XML parser was not configured (ParserConfigurationException)");
			Controller.exit(2);
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
		return Controller.xmlDocument;
	}
	
	/**
	 * Quit the program
	 * 
	 * @param status
	 */
	public static void exit(int status) {
		System.exit(status);
	}
	
	/**
	 * 
	 * @return
	 */
	public static UserInterface getUserInterface() {
		return Controller.ui;
	}

	/**
	 * 
	 * @param ui
	 */
	public static void setUserInterface(UserInterface ui) {
		Controller.ui = ui;
	}
}
