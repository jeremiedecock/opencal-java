/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.toolkit.CalendarToolKit;
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
		this.grade = OpenCAL.getInspector().assess(this);
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
			this.element = OpenCAL.getDomDocument().createElement("card");
			this.element.setAttribute("cdate", CalendarToolKit.calendarToIso8601(new GregorianCalendar()));
			
			Element questionElement = OpenCAL.getDomDocument().createElement("question");
			this.element.appendChild(questionElement);
			questionElement.appendChild(OpenCAL.getDomDocument().createCDATASection(questionString));
			
			Element answerElement = OpenCAL.getDomDocument().createElement("answer");
			this.element.appendChild(answerElement);
			answerElement.appendChild(OpenCAL.getDomDocument().createCDATASection(answerString));
			
			for(int i=0 ; i<tagStrings.length ; i++) {
				if(!tagStrings[i].equals("")) {
					Element tagElement = OpenCAL.getDomDocument().createElement("tag");
					this.element.appendChild(tagElement);
					tagElement.appendChild(OpenCAL.getDomDocument().createTextNode(tagStrings[i]));
				}
			}
			
			NodeList nodeList = OpenCAL.getDomDocument().getElementsByTagName("pkb");
			Element pkbElement = (Element) nodeList.item(0);
			pkbElement.appendChild(this.element);
			
			// Add the new "card" to the XML file
			OpenCAL.updatePkbFile();
			
			this.grade = OpenCAL.getInspector().assess(this);
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
		String question = "";
		
		NodeList nodeCards = this.element.getElementsByTagName("question");
		if(nodeCards.getLength() != 0) {
			Element questionElement = (Element) nodeCards.item(0);
			question = questionElement.getTextContent();
		}
		
		return question;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAnswer() {
		String answer = "";
		
		NodeList nodeCards = this.element.getElementsByTagName("answer");
		if(nodeCards.getLength() != 0) {
			Element answerElement = (Element) nodeCards.item(0);
			answer = answerElement.getTextContent();
		}
		
		return answer;
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
		Element reviewElement = OpenCAL.getDomDocument().createElement("review");
		reviewElement.setAttribute("rdate", CalendarToolKit.calendarToIso8601(new GregorianCalendar()));
		reviewElement.setAttribute("result", result);
		this.element.appendChild(reviewElement);
		
		// Update grade
		this.grade = OpenCAL.getInspector().assess(this);
		
		// Serialize DOM tree
		OpenCAL.updatePkbFile();
		
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
		OpenCAL.updatePkbFile();
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
		OpenCAL.updatePkbFile();
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
				tagElement = OpenCAL.getDomDocument().createElement("tag");
				this.element.appendChild(tagElement);
				tagElement.appendChild(OpenCAL.getDomDocument().createTextNode(newTags[i]));
			}
		}
		
		// Serialize DOM tree
		OpenCAL.updatePkbFile();
		
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
