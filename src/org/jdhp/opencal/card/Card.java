/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card;

import java.util.ArrayList;
import java.util.Date;

import org.jdhp.opencal.OpenCAL;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Card {

	protected Element element;
	
	private int grade;
	
	/**
	 * 
	 * @param element
	 */
	public Card(Element element) {
		this.element = element;
		this.grade = OpenCAL.inspector.assess(this);
	}
	
	/**
	 * 
	 * @param questionString
	 * @param answerString
	 * @param tagStrings
	 */
	public Card(String questionString, String answerString, String[] tagStrings) {
		if(questionString != null && !questionString.equals("")) {
			// Add the new "card" element to the DOM tree
			this.element = OpenCAL.domDocument.createElement("card");
			this.element.setAttribute("cdate", OpenCAL.iso8601Formatter.format(new Date()));
			this.element.setAttribute("id", "c0"); // TODO : l'attribut id ne sert à rien... il faut le supprimer du model
			
			Element questionElement = OpenCAL.domDocument.createElement("question");
			this.element.appendChild(questionElement);
			questionElement.appendChild(OpenCAL.domDocument.createCDATASection(questionString));
			
			Element answerElement = OpenCAL.domDocument.createElement("answer");
			this.element.appendChild(answerElement);
			answerElement.appendChild(OpenCAL.domDocument.createCDATASection(answerString));
			
			for(int i=0 ; i<tagStrings.length ; i++) {
				if(!tagStrings[i].equals("")) {
					Element tagElement = OpenCAL.domDocument.createElement("tag");
					this.element.appendChild(tagElement);
					tagElement.appendChild(OpenCAL.domDocument.createTextNode(tagStrings[i]));
				}
			}
			
			NodeList nodeList = OpenCAL.domDocument.getElementsByTagName("pkb");
			Element pkbElement = (Element) nodeList.item(0);
			pkbElement.appendChild(this.element);
			
			// Add the new "card" to the XML file
			OpenCAL.updateXmlFile();
			
			this.grade = OpenCAL.inspector.assess(this);
		}
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
	public String getTagsString() {
		StringBuffer tags = new StringBuffer();
		
		NodeList nodeCards = this.element.getElementsByTagName("tag");
		Element tagElement;
		for(int i=0 ; i < nodeCards.getLength() ; i++) {
			tagElement = (Element) nodeCards.item(i);
			if(tags.length() != 0) tags.append("\n");
			tags.append(tagElement.getTextContent());
		}
		
		return tags.toString();
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
	
	/**
	 * 
	 * @return
	 */
	public int getGrade() {
		return this.grade;
	}
	
	/**
	 * 
	 * @param result
	 */
	public void putReview(String result) {
		// Add the new "review" element to the DOM tree
		Element reviewElement = OpenCAL.domDocument.createElement("review");
		reviewElement.setAttribute("rdate", OpenCAL.iso8601Formatter.format(new Date()));
		reviewElement.setAttribute("result", result);
		this.element.appendChild(reviewElement);
		
		// Update grade
		this.grade = OpenCAL.inspector.assess(this);
		
		// Serialize DOM tree
		OpenCAL.updateXmlFile();
		
//		OpenCAL.plannedCardList.remove(this);  // TODO : pb, le manipulator n'est pas au courrant...
		OpenCAL.reviewedCardList.add(this);
	}
	
	/**
	 * 
	 * @param newQuestion
	 */
	public void setQuestion(String newQuestion) {
		NodeList nodeCards = this.element.getElementsByTagName("question");
		Element questionElement = (Element) nodeCards.item(0);
		((CDATASection) questionElement.getFirstChild()).setTextContent(newQuestion);
		
		// Serialize DOM tree
		OpenCAL.updateXmlFile();
	}
	
	/**
	 * 
	 * @param newAnswer
	 */
	public void setAnswer(String newAnswer) {
		NodeList nodeCards = this.element.getElementsByTagName("answer");
		Element answerElement = (Element) nodeCards.item(0);
		((CDATASection) answerElement.getFirstChild()).setTextContent(newAnswer);
		
		// Serialize DOM tree
		OpenCAL.updateXmlFile();
	}
	
	/**
	 * 
	 * @param newTags
	 */
	public void setTags(String[] newTags) {
		// Remove old tags
		NodeList nodeCards = this.element.getElementsByTagName("tag");
		while(nodeCards.getLength() > 0) {
			// Remove the "#Text" node following the "Tag" node
			Node nextSibling = nodeCards.item(0).getNextSibling();
			if(nextSibling != null && nextSibling.getNodeType() == Node.TEXT_NODE) this.element.removeChild(nextSibling);
			
			// Remove the "Tag" node
			this.element.removeChild(nodeCards.item(0));
		}
		
		// Insert new tags
		Element tagElement;
		for(int i=0 ; i<newTags.length ; i++) {
			if(!newTags[i].equals("")) {
				tagElement = OpenCAL.domDocument.createElement("tag");
				this.element.appendChild(tagElement);
				tagElement.appendChild(OpenCAL.domDocument.createTextNode(newTags[i]));
			}
		}
		
		// Serialize DOM tree
		OpenCAL.updateXmlFile();
		
		// Update plannedCardList and suspendedCardList if necessary
		boolean isSuspended = false;
		int i = 0;
		while(!isSuspended && i<newTags.length) { 
			if(newTags[i].equals(OpenCAL.SUSPENDED_CARD_STRING)) isSuspended = true;
			i++;
		}
		
		if(isSuspended) {
			OpenCAL.plannedCardList.remove(this);  // TODO : pb, le manipulator n'est pas au courrant...
			OpenCAL.suspendedCardList.add(this);
		}
	}
	
}
