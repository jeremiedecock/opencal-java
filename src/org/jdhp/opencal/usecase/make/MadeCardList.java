/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.make;

import java.util.Date;

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
public class MadeCardList extends CardList {

	public MadeCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.domDocument.getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			
			if(card.getCreationDate().equals(OpenCAL.iso8601Formatter.format(new Date()))) this.add(card);
		}
	}
}
