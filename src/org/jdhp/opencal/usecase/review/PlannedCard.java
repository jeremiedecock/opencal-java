/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.review;

import java.util.Date;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.usecase.Card;
import org.w3c.dom.Element;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class PlannedCard extends Card {

	private int grade;
	
	public PlannedCard(Element element) {
		super(element);
		
		this.grade = OpenCAL.inspector.assess(this);
	}
	
	public int getGrade() {
		return this.grade;
	}
	
	public void putReview(String result) {
		// Add the new "review" element to the DOM tree
		Element reviewElement = OpenCAL.domDocument.createElement("review");
		reviewElement.setAttribute("rdate", OpenCAL.iso8601Formatter.format(new Date()));
		reviewElement.setAttribute("result", result);
		this.element.appendChild(reviewElement);
		
		// Serialize DOM tree
		OpenCAL.updateXmlFile();
		
		OpenCAL.plannedCardList.incrementReviewedCards();
	}
	
}
