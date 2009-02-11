/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.jdhp.opencal.card.Review;
import org.jdhp.opencal.toolkit.CalendarToolKit;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewedCardList extends CardList {

	/**
	 * 
	 */
	public ReviewedCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.getDomDocument().getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			
			boolean hasBeenReviewed = false;
			Review[] reviews = card.getReviews();
			for(int j=0 ; j < reviews.length ; j++) {
				if(reviews[j].getReviewDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar()))) hasBeenReviewed = true;
			}
			
			if(hasBeenReviewed) this.add(card);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getQuestionStrings() {
		ArrayList<String> questionStrings = new ArrayList<String>();
		
		boolean wrongAnswer;
		Review[] reviews;
		for(int i=0 ; i<this.size() ; i++) {
			wrongAnswer = false;
			reviews = this.get(i).getReviews();
			
			for(int j=0 ; j<reviews.length ; j++) {
				if(reviews[j].getReviewDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar())) && reviews[j].getResult().equals(OpenCAL.WRONG_ANSWER_STRING)) wrongAnswer = true;
			}
			
			if(wrongAnswer) questionStrings.add("▶ " + this.get(i).getQuestion());
			else questionStrings.add(this.get(i).getQuestion());
		}
		
		return questionStrings.toArray(new String[0]);
	}
}
