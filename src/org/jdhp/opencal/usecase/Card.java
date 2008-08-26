/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Card {

	protected Element element; // TODO : node ou element ?
	
	public Card(Element element) {
		this.element = element;
	}
	
	public Element getElement() {
		return this.element;
	}
	
	public String getQuestion() {
		NodeList nodeCards = this.element.getElementsByTagName("question");
		Element questionElement = (Element) nodeCards.item(0);
		return questionElement.getTextContent(); // TODO : ???
	}
	
	public String getAnswer() {
		NodeList nodeCards = this.element.getElementsByTagName("answer");
		Element answerElement = (Element) nodeCards.item(0);
		return answerElement.getTextContent(); // TODO : ???
	}
	
}
