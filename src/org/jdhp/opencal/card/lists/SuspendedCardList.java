/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class SuspendedCardList extends CardList {

	public SuspendedCardList() {
		super();
		
		for(int i=0 ; i<OpenCAL.allCardList.size() ; i++) {
            Card card = OpenCAL.allCardList.get(i);
			if(card.isSuspended()) this.add(card);
		}
	}
}
