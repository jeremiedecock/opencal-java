/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class PlannedCardList extends CardList {

	public PlannedCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.getDomDocument().getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			
			boolean isSuspended = false;
			String[] tags = card.getTags();
			for(int j=0 ; j < tags.length ; j++) {
				if(tags[j].equals(OpenCAL.SUSPENDED_CARD_STRING)) isSuspended = true;
			}
			
			if(card.getGrade() >= 0 && !isSuspended) this.add(card);
		}
		
		this.sortCards();
	}
	
	/**
	 * Tri le tableau par "grade" décroissant
	 */
	private void sortCards() {
		// Tri bulle
		for(int i=this.size()-1 ; i>0 ; i--) {
			for(int j=0 ; j<i ; j++) {
				if((this.get(j+1)).getGrade() < (this.get(j)).getGrade()) {
					Card tmp = this.get(j+1);
					this.set(j+1, this.get(j));
					this.set(j, tmp);
				}
			}
		}
	}
}
