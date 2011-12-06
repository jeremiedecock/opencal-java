/*
 * OpenCAL version 3.0
 * Copyright (c) 2009 Jérémie Decock
 */

package org.jdhp.opencal.data.pkb;

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

import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.cardcollection.CardCollection;
import org.jdhp.opencal.swt.MainWindow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jérémie Decock
 */
public class PersonalKnowledgeBase {

	private static File pkbFile;
	
	private static Document domDocument;	// TODO : à supprimer

	
	/**
	 * TODO retourner un objet CardCollection
	 * 
	 * @param uri
	 */
	public static void load(URI uri) {
		PersonalKnowledgeBase.pkbFile = new File(uri);

		// Build the XML DOM tree
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			if(PersonalKnowledgeBase.pkbFile.exists()) {
				PersonalKnowledgeBase.domDocument = db.parse(PersonalKnowledgeBase.pkbFile);
			} else {
				PersonalKnowledgeBase.domDocument = db.newDocument();
				Element root = PersonalKnowledgeBase.domDocument.createElement("pkb");
				PersonalKnowledgeBase.domDocument.appendChild(root);
			}

			// Build the CardCollection
			NodeList nodeCards = PersonalKnowledgeBase.domDocument.getElementsByTagName("card");
            for(int i=0 ; i<nodeCards.getLength() ; i++) {
                Card card = new Card((Element) nodeCards.item(i));
                CardCollection.getInstance().add(card);
            }
            
            // Check the CardCollection
            boolean isDateConsistent = CardCollection.getInstance().isDateConsistent();
            if(!isDateConsistent) {
            	MainWindow.getInstance().printAlert("The system date is not consistent with some dates in the knowledge base.\n\nPlease check your system date !");
            }
            
		} catch(SAXException e) {
			/* TODO : une classe data n'a pas à appeller une classe gui => faire un throw à la place pour remonter l'exeption à l'appellant */
			MainWindow.getInstance().printError(ApplicationProperties.getPkbPath() + " n'est pas valide (SAXException)");
			MainWindow.getInstance().close();
		} catch(FileNotFoundException e) {
			/* TODO : une classe data n'a pas à appeller une classe gui => faire un throw à la place pour remonter l'exeption à l'appellant */
			MainWindow.getInstance().print(ApplicationProperties.getPkbPath() + " est introuvable (FileNotFoundException)");
			MainWindow.getInstance().close();
		} catch(IOException e) {
			/* TODO : une classe data n'a pas à appeller une classe gui => faire un throw à la place pour remonter l'exeption à l'appellant */
			MainWindow.getInstance().printError(ApplicationProperties.getPkbPath() + " est illisible (IOException)");
			MainWindow.getInstance().close();
		} catch(ParserConfigurationException e) {
			/* TODO : une classe data n'a pas à appeller une classe gui => faire un throw à la place pour remonter l'exeption à l'appellant */
			MainWindow.getInstance().printError("The XML parser was not configured (ParserConfigurationException)");
			MainWindow.getInstance().close();
		}
	}
	

	/**
	 * TODO : prendre un objet CardCollection
	 * 
	 */
	public static void save() {
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
	 * TODO : supprimer
	 * 
	 * @return
	 */
	public static Document getDomDocument() {
		return PersonalKnowledgeBase.domDocument;
	}
}
