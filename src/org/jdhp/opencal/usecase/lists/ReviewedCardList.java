/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.lists;

import java.util.Date;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.usecase.Card;
import org.jdhp.opencal.usecase.CardList;
import org.jdhp.opencal.usecase.Review;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewedCardList extends CardList {

	public ReviewedCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.domDocument.getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			
			boolean hasBeenReviewed = false;
			Review[] reviews = card.getReviews();
			for(int j=0 ; j < reviews.length ; j++) {
				if(reviews[j].getReviewDate().equals(OpenCAL.iso8601Formatter.format(new Date()))) hasBeenReviewed = true;
			}
			
			if(hasBeenReviewed) this.add(card);
		}
	}
}
