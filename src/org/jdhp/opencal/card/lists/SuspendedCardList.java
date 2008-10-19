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
public class SuspendedCardList extends CardList {

	public SuspendedCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.getDomDocument().getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			
			boolean isSuspended = false;
			String[] tags = card.getTags();
			for(int j=0 ; j < tags.length ; j++) {
				if(tags[j].equals(OpenCAL.SUSPENDED_CARD_STRING)) isSuspended = true;
			}
			
			if(isSuspended) this.add(card);
		}
	}
}
