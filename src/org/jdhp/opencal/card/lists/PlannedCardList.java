/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.jdhp.opencal.gui.tabs.ReviewerTab;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class PlannedCardList extends CardList {

	public PlannedCardList() {
		super();
		
		for(int i=0 ; i<CardList.mainCardList.size() ; i++) {
            Card card = CardList.mainCardList.get(i);
			if(card.getGrade() >= 0. && !card.isHidden()) this.add(card);
		}
		
		ReviewerTab.sortCards(this);
	}

}
