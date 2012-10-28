/*
 * OpenCAL version 3.0
 * Copyright (c) 2009 Jérémie Decock
 */

package org.jdhp.opencal.data.pkb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.cardcollection.CardCollection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jérémie Decock
 */
public class DOMPersonalKnowledgeBase implements PersonalKnowledgeBase {

	public static final String NAME = "DOM";
	
	/**
	 * 
	 */
	public CardCollection load(URI uri) throws PersonalKnowledgeBaseException {
		CardCollection cardCollection = new CardCollection();
		
		File pkbFile = new File(uri);
		
		// Build the XML DOM tree
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document domDocument = null;
			
			if(pkbFile.exists()) {
				domDocument = db.parse(pkbFile);
			} else {
				domDocument = db.newDocument();
				Element root = domDocument.createElement("pkb");
				domDocument.appendChild(root);
			}

			// Build the CardCollection
			NodeList cardNodes = domDocument.getElementsByTagName("card");
            for(int i=0 ; i<cardNodes.getLength() ; i++) {
            	Element cardNode = (Element) cardNodes.item(i);
            	
            	// Question
            	String question = "";
        		NodeList questionNodes = cardNode.getElementsByTagName("question");
        		if(questionNodes.getLength() != 0) {
        			Element questionElement = (Element) questionNodes.item(0);
        			question = questionElement.getTextContent();
        		} else {
        			// TODO: throw exception
        		}
            	
            	// Answer
            	String answer = "";
        		NodeList answerNodes = cardNode.getElementsByTagName("answer");
        		if(answerNodes.getLength() != 0) {
        			Element answerElement = (Element) answerNodes.item(0);
        			answer = answerElement.getTextContent();
        		}
            	
            	// Tags
            	List<String> tags = new ArrayList<String>();
        		NodeList tagNodes = cardNode.getElementsByTagName("tag");
        		for(int j=0 ; j < tagNodes.getLength() ; j++) {
        			Element tagElement = (Element) tagNodes.item(j);
        			tags.add(tagElement.getTextContent());
        		}
            	
            	// Reviews
            	List<Review> reviews = new ArrayList<Review>();
        		NodeList reviewNodes = cardNode.getElementsByTagName("review");
        		for(int j=0 ; j < reviewNodes.getLength() ; j++) {
        			Element reviewElement = (Element) reviewNodes.item(j);
        			reviews.add(new Review(reviewElement.getAttribute("rdate"), reviewElement.getAttribute("result")));
        		}
        		
            	// Creation Date
            	String creationDate = cardNode.getAttribute("cdate");
            	
            	// Is hidden
            	String isHiddenString = cardNode.getAttribute("hidden");
            	boolean isHidden = isHiddenString.equals("true") ? true : false;
            	
                Card card = new Card(question, answer, tags, reviews, creationDate, isHidden);
                cardCollection.add(card);
            }
            
            // Check the CardCollection
            boolean isDateConsistent = cardCollection.isDateConsistent();
            if(!isDateConsistent) {
            	throw new PersonalKnowledgeBaseException("The system date is not consistent with some dates in the knowledge base.\n\nPlease check your system date !");
            }
            
		} catch(SAXException e) {
			throw new PersonalKnowledgeBaseException(uri + " n'est pas valide (SAXException)", e);
		} catch(FileNotFoundException e) {
			throw new PersonalKnowledgeBaseException(uri + " est introuvable (FileNotFoundException)", e);
		} catch(IOException e) {
			throw new PersonalKnowledgeBaseException(uri + " est illisible (IOException)", e);
		} catch(ParserConfigurationException e) {
			throw new PersonalKnowledgeBaseException("The XML parser was not configured (ParserConfigurationException)", e);
		}
		
		return cardCollection;
	}
	

	/**
	 * 
	 */
	public void save(CardCollection cardCollection, URI uri) throws PersonalKnowledgeBaseException {
		File pkbFile = new File(uri);
		
		try {
			// Make DOM document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document domDocument = db.newDocument();
			
			// Add root element (pkb)
			Element rootElement = domDocument.createElement("pkb");
			domDocument.appendChild(rootElement);
			
			// Add each card in root
			for(Card card : cardCollection) {
				
				// Add the new "card" element to the DOM tree
				Element cardElement = domDocument.createElement("card");

				// Creation date
				cardElement.setAttribute("cdate", card.getCreationDate());
				
				// Is hidden
				cardElement.setAttribute("hidden", card.isHidden() ? "true" : "false");
				
				// Question
				Element questionElement = domDocument.createElement("question");
				cardElement.appendChild(questionElement);
				questionElement.appendChild(domDocument.createCDATASection(card.getQuestion()));
				
				// Answer
				Element answerElement = domDocument.createElement("answer");
				cardElement.appendChild(answerElement);
				answerElement.appendChild(domDocument.createCDATASection(card.getAnswer()));
				
				// Tags
				for(String tag : card.getTags()) {
					if(!tag.equals("")) {
						Element tagElement = domDocument.createElement("tag");
						tagElement.appendChild(domDocument.createTextNode(tag));
						
						cardElement.appendChild(tagElement);
					}
				}
				
				// Reviews
				for(Review review : card.getReviews()) {
					Element reviewElement = domDocument.createElement("review");
					reviewElement.setAttribute("rdate", review.getReviewDate());
					reviewElement.setAttribute("result", review.getResult());
					
					cardElement.appendChild(reviewElement);
				}
				
				// Append the card element to the root
				rootElement.appendChild(cardElement);
				
			}

			// Make output file
			Result streamResult = new StreamResult(pkbFile);

			// Setup transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// Make the "DOM source" object
			Source domSource = new DOMSource(domDocument);
			
			// Transformation
			transformer.transform(domSource, streamResult);
		} catch (TransformerConfigurationException e) {
			throw new PersonalKnowledgeBaseException("Can't write the PKB file (TransformerConfigurationException)", e);
		} catch (TransformerException e) {
			throw new PersonalKnowledgeBaseException("Can't write the PKB file (TransformerException)", e);
		} catch (ParserConfigurationException e) {
			throw new PersonalKnowledgeBaseException("Can't write the PKB file (ParserConfigurationException)", e);
		}
	}
	
}
