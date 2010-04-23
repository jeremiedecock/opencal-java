/*
 * OpenCAL version 3.0
 * Copyright (c) 2009 Jérémie Decock
 */

package org.jdhp.opencal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jérémie Decock
 */
public class PersonalKnowledgeBase {

	private static File pkbFile;			// TODO : à supprimer
	
	private static Document domDocument;	// TODO : à supprimer

	
	/**
	 * 
	 * @param uri
	 */
	public static void load(URI uri) {
		PersonalKnowledgeBase.pkbFile = new File(uri);

		// Build the XML DOM tree
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			PersonalKnowledgeBase.domDocument = db.parse(PersonalKnowledgeBase.pkbFile);
			
			NodeList nodeCards = PersonalKnowledgeBase.domDocument.getElementsByTagName("card");
            for(int i=0 ; i<nodeCards.getLength() ; i++) {
                Card card = new Card((Element) nodeCards.item(i));
                OpenCAL.cardCollection.add(card);
            }
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
	 * @param document
	 */
	public static void save(URI uri) {
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
	
	
	/**
	 * 
	 * @return
	 */
	public static Document getDomDocument() {
		return PersonalKnowledgeBase.domDocument;
	}
}
