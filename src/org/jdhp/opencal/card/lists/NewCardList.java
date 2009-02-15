/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.jdhp.opencal.toolkit.CalendarToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class NewCardList extends CardList {

	public NewCardList() {
		super();
		
		for(int i=0 ; i<OpenCAL.allCardList.size() ; i++) {
			Card card = OpenCAL.allCardList.get(i);
			if(card.getCreationDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar()))) this.add(card);
		}
	}
}
