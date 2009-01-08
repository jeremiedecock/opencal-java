/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.inspector;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.toolkit.CalendarToolKit;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorBrian implements Inspector {

	private final int[] revisionDates = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
	
	/**
	 * Brian is a little more professional than Alan.
	 * He doesn't validate reviews when it's too early...
	 * but he doesn't care about late.
	 * 
	 * @return
	 */
	public int assess(Card card) {
		int grade = 0;
		
		GregorianCalendar lastRevisionDate = CalendarToolKit.iso8601ToCalendar(card.getElement().getAttribute("cdate"));
		GregorianCalendar expectedRevisionDate = getExpectedRevisionDate(lastRevisionDate, grade);
		
		// TODO : vérifier que les noeuds "review" sont bien classés par date croissante
		NodeList reviewList = card.getElement().getElementsByTagName("review");
		for(int i=0 ; i < reviewList.getLength() ; i++) {
			GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(i)).getAttribute("rdate"));
			
			if(((Element) reviewList.item(i)).getAttribute("result").equals(OpenCAL.RIGHT_ANSWER_STRING)) {
				grade++;
				lastRevisionDate = rdate;
				expectedRevisionDate = getExpectedRevisionDate(lastRevisionDate, grade);
			} else {
				grade = 0;
				lastRevisionDate = rdate;
				expectedRevisionDate = getExpectedRevisionDate(lastRevisionDate, grade);
			}
		}
		
		if(isTooEarly(expectedRevisionDate, new GregorianCalendar())) {
			// It's too early to review this card. The card will be hide.
			grade = -1;
		}
		
		return grade;
	}
	
	/**
	 * 
	 * @param oldestRevisionItem
	 * @return
	 */
	private boolean isTooEarly(GregorianCalendar expectedRevisionDate, GregorianCalendar revisionDate) {
		boolean isTooEarly;
		
		if(expectedRevisionDate == null) {
			isTooEarly = false;
		} else {
			if(revisionDate.before(expectedRevisionDate)) {
				isTooEarly = true;
			} else {
				isTooEarly = false;
			}
		}
		
		return isTooEarly;
	}
	
	/**
	 * 
	 * @param previousRevisionDate
	 * @param grade
	 * @return
	 */
	private GregorianCalendar getExpectedRevisionDate(GregorianCalendar previousRevisionDate, int grade) {
		GregorianCalendar expectedRevisionDate = null;
		
		if(previousRevisionDate != null) {
			expectedRevisionDate = (GregorianCalendar) previousRevisionDate.clone();
			expectedRevisionDate.add(Calendar.DAY_OF_MONTH, this.revisionDates[grade]);
		}
		
		return expectedRevisionDate;
	}
}
