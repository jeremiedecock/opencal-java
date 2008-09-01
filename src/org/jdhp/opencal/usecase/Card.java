/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase;

import java.util.ArrayList;
import java.util.Date;

import org.jdhp.opencal.OpenCAL;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Card {

	protected Element element;
	
	/**
	 * 
	 * @param element
	 */
	public Card(Element element) {
		this.element = element;
	}
	
	/**
	 * 
	 * @param questionText
	 * @param answerText
	 * @param tagsText
	 */
	public Card(String questionText, String answerText, String[] tagsText) {
		// Add the new "card" element to the DOM tree
		Element cardElement = OpenCAL.domDocument.createElement("card");
		cardElement.setAttribute("cdate", OpenCAL.iso8601Formatter.format(new Date()));
		cardElement.setAttribute("id", "c0"); // TODO : l'attribut id ne sert à rien... il faut le supprimer du model
		
		// TODO : CDATA !!!!!!!!!!!!!!
		Element questionElement = OpenCAL.domDocument.createElement("question");
		cardElement.appendChild(questionElement);
		questionElement.appendChild(OpenCAL.domDocument.createTextNode(questionText));
		
		// TODO : CDATA !!!!!!!!!!!!!!
		Element answerElement = OpenCAL.domDocument.createElement("answer");
		cardElement.appendChild(answerElement);
		answerElement.appendChild(OpenCAL.domDocument.createTextNode(answerText));
		
		for(int i=0 ; i<tagsText.length ; i++) {
			Element tagElement = OpenCAL.domDocument.createElement("tag");
			cardElement.appendChild(tagElement);
			tagElement.appendChild(OpenCAL.domDocument.createTextNode(tagsText[i]));
		}
		
		NodeList nodeList = OpenCAL.domDocument.getElementsByTagName("pkb");
		Element pkbElement = (Element) nodeList.item(0);
		pkbElement.appendChild(cardElement);
		
		// Add the new "card" to the XML file
		OpenCAL.updateXmlFile();
	}
	
	/**
	 * 
	 * @return
	 */
	public Element getElement() {
		return this.element;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return this.element.getAttribute("id");
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCreationDate() {
		return this.element.getAttribute("cdate");
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQuestion() {
		NodeList nodeCards = this.element.getElementsByTagName("question");
		Element questionElement = (Element) nodeCards.item(0);
		return questionElement.getTextContent(); // TODO : ???
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAnswer() {
		NodeList nodeCards = this.element.getElementsByTagName("answer");
		Element answerElement = (Element) nodeCards.item(0);
		return answerElement.getTextContent(); // TODO : ???
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getTags() {
		ArrayList<String> tags = new ArrayList<String>();
		
		NodeList nodeCards = this.element.getElementsByTagName("tag");
		Element tagElement;
		for(int i=0 ; i < nodeCards.getLength() ; i++) {
			tagElement = (Element) nodeCards.item(i);
			tags.add(tagElement.getTextContent());
		}
		
		return tags.toArray(new String[0]);
	}
	
	/**
	 * 
	 * @return
	 */
	public Review[] getReviews() {
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		NodeList nodeCards = this.element.getElementsByTagName("review");
		Element reviewElement;
		for(int i=0 ; i < nodeCards.getLength() ; i++) {
			reviewElement = (Element) nodeCards.item(i);
			reviews.add(new Review(reviewElement.getAttribute("rdate"), reviewElement.getAttribute("result")));
		}
		
		return reviews.toArray(new Review[0]);
	}
	
}
