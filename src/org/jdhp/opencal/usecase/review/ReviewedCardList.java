/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.review;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.usecase.Card;
import org.jdhp.opencal.usecase.CardList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewedCardList extends CardList {

	private int reviewedCards;
	
	public ReviewedCardList() {
		super();
		
		this.reviewedCards = 0;
		
		NodeList nodeCards = OpenCAL.domDocument.getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			ReviewedCard card = new ReviewedCard((Element) nodeCards.item(i));
			if(card.getGrade() >= 0) this.cardList.add(card);
		}
		
		this.sortCards();
	}
	
	/**
	 * Tri le tableau par "grade" décroissant
	 */
	private void sortCards() {
		// Tri bulle
		for(int i=this.cardList.size()-1 ; i>0 ; i--) {
			for(int j=0 ; j<i ; j++) {
				if(((ReviewedCard) this.cardList.get(j+1)).getGrade() < ((ReviewedCard) this.cardList.get(j)).getGrade()) {
					Card tmp = this.cardList.get(j+1);
					this.cardList.set(j+1, this.cardList.get(j));
					this.cardList.set(j, tmp);
				}
			}
		}
	}
	
	public int getReviewedCards() {
		return this.reviewedCards;
	}
	
	public int getRemainingCards() {
		return this.cardList.size();
	}
	
	public void incrementReviewedCards() {
		this.reviewedCards++;
	}
}
