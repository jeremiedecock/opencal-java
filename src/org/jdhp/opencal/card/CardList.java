/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.card;

import java.io.StringWriter;
import java.util.ArrayList;

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

import org.jdhp.opencal.card.CardManipulator;
import org.jdhp.opencal.card.Manipulable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardList extends ArrayList<Card> implements Manipulable {

	/**
	 * 
	 */
	public CardList() {
		super();
	}
	
	/**
	 * 
	 */
	public CardManipulator manipulator() {
		return new CardManipulator(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getQuestionStrings() {
		ArrayList<String> questionStrings = new ArrayList<String>();
		
		for(int i=0 ; i<this.size() ; i++) {
			questionStrings.add(this.get(i).getQuestion());
		}
		
		return questionStrings.toArray(new String[0]);
	}
	
	/**
	 * 
	 */
	public String toString() {
		// Créer un document
		Document document = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			document = db.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		// Ajouter un noeud root au document
		Element rootElement = document.createElement("root");
		document.appendChild(rootElement);
		
		// Ajouter les elements de chaques cartes dans le rootElement
		for(int i=0 ; i<this.size() ; i++) {
			rootElement.appendChild(document.importNode(this.get(i).getElement(),true));
		}
		
		// Convert the document
		StringWriter stringWriter = new StringWriter();
		
		try {
			// Make DOM source
			Source domSource = new DOMSource(document);
		
			// Make output file
			Result streamResult = new StreamResult(stringWriter);
		
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
		
		return stringWriter.toString();
	}
}
