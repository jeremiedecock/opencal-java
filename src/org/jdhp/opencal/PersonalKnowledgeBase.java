/*
 * OpenCAL version 3.0
 * Copyright (c) 2009 Jérémie Decock
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

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Jérémie Decock
 */
public class PersonalKnowledgeBase {

	private static File pkbFile;
	
	private static Document domDocument;

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
		PersonalKnowledgeBase.pkbFile = new File(pkbFilePath);

		// Build the XML DOM tree
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			PersonalKnowledgeBase.domDocument = db.parse(PersonalKnowledgeBase.pkbFile);
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
//		// PersonalKnowledgeBase.updatePkbFile()
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
		return PersonalKnowledgeBase.pkbFile;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Document getDomDocument() {
		return PersonalKnowledgeBase.domDocument;
	}

	/**
	 * 
	 * @param document
	 */
	public static void updatePkbFile() {
		try {
			// Make DOM source
			Source domSource = new DOMSource(PersonalKnowledgeBase.domDocument);

			// Make output file
			Result streamResult = new StreamResult(PersonalKnowledgeBase.pkbFile);

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
}
