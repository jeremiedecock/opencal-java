/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.model.professor;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.util.CalendarToolKit;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ProfessorCharlie implements Professor {

	public static final String NAME = "Charlie";
	
	public String getName() {
		return ProfessorCharlie.NAME;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public float assess(Card card) {
		float grade = 0;
		
		NodeList reviewList = card.getElement().getElementsByTagName("review");
		
		if(reviewList.getLength() == 0) {
			grade = Professor.HAS_NEVER_BEEN_REVIEWED;
		} else {
			GregorianCalendar cdate = CalendarToolKit.iso8601ToCalendar(card.getElement().getAttribute("cdate"));
			GregorianCalendar expectedRevisionDate = getExpectedRevisionDate(cdate, grade);
			
			// TODO : vérifier que les noeuds "review" sont bien classés par date croissante
			for(int i=0 ; i < reviewList.getLength() ; i++) {
				GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(i)).getAttribute("rdate"));
				String result = ((Element) reviewList.item(i)).getAttribute("result"); 
				
				if(result.equals(Review.RIGHT_ANSWER_STRING)) {
					if(!rdate.before(expectedRevisionDate)) {
						// TODO : vérifier qu'il n'y a pas de bug sur les conversions heure d'été / heure d'hivers dans le calcul suivant
						// TODO : log(n) -> vérifier que n>=1
						grade = new Double(Math.log(((long) 2)*expectedRevisionDate.getTimeInMillis()-rdate.getTimeInMillis()+1)/Math.log(2)).floatValue();
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
				grade = Professor.DONT_REVIEW_THIS_TODAY;
			}
		}
		
		return grade;
	}
	
	/**
	 * Get the expected (next) revision date knowing the last revision date and the grade.
	 * 
	 * @param previousRevisionDate
	 * @param grade
	 * @return
	 */
	private GregorianCalendar getExpectedRevisionDate(GregorianCalendar previousRevisionDate, float grade) {
		GregorianCalendar expectedRevisionDate = (GregorianCalendar) previousRevisionDate.clone();
		expectedRevisionDate.add(Calendar.DAY_OF_MONTH, deltaDays(grade));
		
		return expectedRevisionDate;
	}
	
	/**
	 * Return the delta days (time between expectedRevisionDate and rdate)
	 * knowing the grade : delta = 2^grade.
	 * 
	 * @param grade
	 * @return
	 */
	private int deltaDays(float grade) {
		return (new Double(Math.pow(2, grade))).intValue(); // TODO comment est arrondi le double ???!!!
	}
	
}
