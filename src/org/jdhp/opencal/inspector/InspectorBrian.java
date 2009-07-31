/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.professor;

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
public class ProfessorBen implements Professor {

	public final String NAME = "Ben";
	
	/**
	 * Ben is a little more professional than Alan.
	 * He doesn't validate reviews when it's too early...
	 * but he doesn't care about late.
	 * 
	 * @return
	 */
	public float assess(Card card) {
		int grade = 0;
		
		GregorianCalendar cdate = CalendarToolKit.iso8601ToCalendar(card.getElement().getAttribute("cdate"));
		GregorianCalendar expectedRevisionDate = getExpectedRevisionDate(cdate, grade);
		
		// TODO : vérifier que les noeuds "review" sont bien classés par date croissante
		NodeList reviewList = card.getElement().getElementsByTagName("review");
		if(!isSorted(reviewList)) System.out.println("Unsorted card detected : " + card);
		
		for(int i=0 ; i < reviewList.getLength() ; i++) {
			GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(i)).getAttribute("rdate"));
			String result = ((Element) reviewList.item(i)).getAttribute("result"); 
			
			if(result.equals(OpenCAL.RIGHT_ANSWER_STRING)) {
				if(!rdate.before(expectedRevisionDate)) {
					grade++;
					expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
				}
			} else {
				grade = 0;
				expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
			}
		}
		
		GregorianCalendar today = new GregorianCalendar();
		if(today.before(expectedRevisionDate)) {
			// It's too early to review this card. The card will be hide.
			grade = -1;
		}
		
		return grade;
	}
	
	/**
	 * 
	 * @param reviewList
	 * @return
	 */
	public static boolean isSorted(NodeList reviewList) {
		boolean isSorted = true;
		
		if(reviewList.getLength()>1) {
			GregorianCalendar lastDate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(0)).getAttribute("rdate"));
			
			int i = 1;
			while(i < reviewList.getLength() && isSorted) {
				GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(i)).getAttribute("rdate"));
				
				if(rdate.before(lastDate)) isSorted = false;
				
				lastDate = rdate;
				i++;
			}
		}
		
		return isSorted;
	}
	
	/**
	 * Get the expected (next) revision date knowing the last revision date and the grade.
	 * 
	 * @param lastRevisionDate
	 * @param grade
	 * @return
	 */
	public static GregorianCalendar getExpectedRevisionDate(GregorianCalendar lastRevisionDate, int grade) {
		GregorianCalendar expectedRevisionDate = (GregorianCalendar) lastRevisionDate.clone();
		expectedRevisionDate.add(Calendar.DAY_OF_MONTH, deltaDays(grade));
		
		return expectedRevisionDate;
	}
	
	/**
	 * Return the delta day (time between expectedRevisionDate and rdate)
	 * knowing the grade : delta = 2^grade.
	 * 
	 * @param grade
	 * @return
	 */
	public static int deltaDays(int grade) {
		return (new Double(Math.pow(2, grade))).intValue();
	}
	
	
	/**
	 * 
	 */
	public String getName() {
		return this.NAME;
	}
	
}
