/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.card;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.PersonalKnowledgeBase;
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

	private Element element;
	
	private float grade;

	/**
	 * Constructeur utilisé lors de la création des cartes contenues dans le fichier XML.
	 * 
	 * @param element
	 */
	public Card(Element element) {
		this.element = element;
		this.grade = OpenCAL.getProfessor().assess(this);
	}
	
	/**
	 * Constructeur utilisé lors de la création de cartes depuis l'interface utilisateur.
	 * 
	 * @param questionString
	 * @param answerString
	 * @param tagStrings
	 */
	public Card(String questionString, String answerString, String[] tagStrings) {
		if(questionString != null && !questionString.equals("")) {
			// Add the new "card" element to the DOM tree
			this.element = PersonalKnowledgeBase.getDomDocument().createElement("card");
			this.element.setAttribute("cdate", CalendarToolKit.calendarToIso8601(new GregorianCalendar()));
			
			Element questionElement = PersonalKnowledgeBase.getDomDocument().createElement("question");
			this.element.appendChild(questionElement);
			questionElement.appendChild(PersonalKnowledgeBase.getDomDocument().createCDATASection(questionString));
			
			Element answerElement = PersonalKnowledgeBase.getDomDocument().createElement("answer");
			this.element.appendChild(answerElement);
			answerElement.appendChild(PersonalKnowledgeBase.getDomDocument().createCDATASection(answerString));
			
			for(int i=0 ; i<tagStrings.length ; i++) {
				if(!tagStrings[i].equals("")) {
					Element tagElement = PersonalKnowledgeBase.getDomDocument().createElement("tag");
					this.element.appendChild(tagElement);
					tagElement.appendChild(PersonalKnowledgeBase.getDomDocument().createTextNode(tagStrings[i]));
				}
			}
			
			NodeList nodeList = PersonalKnowledgeBase.getDomDocument().getElementsByTagName("pkb");
			Element pkbElement = (Element) nodeList.item(0);
			pkbElement.appendChild(this.element);
			
			// Add the new "card" to the XML file
			PersonalKnowledgeBase.save(null);
			
			this.grade = OpenCAL.getProfessor().assess(this);
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
	public float getGrade() {
		return this.grade;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isHidden() {
        String attrString = this.element.getAttribute("hidden");
		return attrString.equals("true") ? true : false;
    }
	
	/**
	 * 
	 * @param result
	 */
	public void putReview(String result) {
		// Add the new "review" element to the DOM tree
		Element reviewElement = PersonalKnowledgeBase.getDomDocument().createElement("review");
		reviewElement.setAttribute("rdate", CalendarToolKit.calendarToIso8601(new GregorianCalendar()));
		reviewElement.setAttribute("result", result);
		this.element.appendChild(reviewElement);
		
		// Update grade
		this.grade = OpenCAL.getProfessor().assess(this);
		
		// Serialize DOM tree
		PersonalKnowledgeBase.save(null);
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
		PersonalKnowledgeBase.save(null);
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
		PersonalKnowledgeBase.save(null);
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
				tagElement = PersonalKnowledgeBase.getDomDocument().createElement("tag");
				this.element.appendChild(tagElement);
				tagElement.appendChild(PersonalKnowledgeBase.getDomDocument().createTextNode(newTags[i]));
			}
		}
		
		// Serialize DOM tree
		PersonalKnowledgeBase.save(null);
	}

	/**
	 * 
	 * @param isHidden
	 */
	public void setHidden(boolean isHidden) {
        // Update XML element
        this.element.setAttribute("hidden", isHidden ? "true" : "false");
		
		// Serialize DOM tree
		PersonalKnowledgeBase.save(null);
    }
	
	/**
	 * 
	 * @param grade
	 */
	public void setGrade(float grade) {
		this.grade = grade;
	}
	
	/**
	 * Return the XML value
	 */
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		
		try {
			// Make DOM source
			Source domSource = new DOMSource(this.element);
		
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
