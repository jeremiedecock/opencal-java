/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase;

import java.util.ArrayList;

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
