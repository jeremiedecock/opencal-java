/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import java.util.Date;

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
public class NewCardList extends CardList {

	public NewCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.getDomDocument().getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			
			if(card.getCreationDate().equals(OpenCAL.iso8601Formatter.format(new Date()))) this.add(card);
		}
	}
}
