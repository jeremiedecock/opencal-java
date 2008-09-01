/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.lists;

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
public class AllCardList extends CardList {

	public AllCardList() {
		super();
		
		NodeList nodeCards = OpenCAL.domDocument.getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Card card = new Card((Element) nodeCards.item(i));
			this.add(card);
		}
	}
}
