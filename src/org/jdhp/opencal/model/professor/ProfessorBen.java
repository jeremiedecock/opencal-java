/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2017 Jérémie Decock
 */

package org.jdhp.opencal.model.professor;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.util.CalendarToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ProfessorBen implements Professor {
	
	public static final String NAME = "Ben";
	
	public String getName() {
		return ProfessorBen.NAME;
	}

	/**
	 * Ben is a little more professional than Alan.
	 * He doesn't validate reviews when it's too early...
	 * but he doesn't care about late.
	 * 
	 * @return
	 */
	public float assess(Card card) {
		int grade = 0;
		
		List<Review> reviewList = card.getReviews();
		
		if(reviewList.isEmpty()) {
			grade = Professor.HAS_NEVER_BEEN_REVIEWED;
		} else {
			GregorianCalendar cdate = CalendarToolKit.iso8601ToCalendar(card.getCreationDate());
			GregorianCalendar expectedRevisionDate = getExpectedRevisionDate(cdate, grade);
			
			// TODO : vérifier que les noeuds "review" sont bien classés par date croissante
			if(!isSorted(reviewList)) System.out.println("Unsorted card detected : " + card);
			
			for(Review review : reviewList) {
				GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(review.getReviewDate());
				String result = review.getResult(); 
				
				if(result.equals(Review.RIGHT_ANSWER_STRING)) {
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
				grade = Professor.DONT_REVIEW_THIS_TODAY;
			}
		}
		
		return grade;
	}
	
	/**
	 * 
	 * @param reviewList
	 * @return
	 */
	public static boolean isSorted(List<Review> reviewList) {
		boolean isSorted = true;
		
		if(reviewList.size() > 1) {
			GregorianCalendar lastDate = CalendarToolKit.iso8601ToCalendar(reviewList.get(0).getReviewDate());
			
			int i = 1;
			while(i < reviewList.size() && isSorted) {
				GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(reviewList.get(0).getReviewDate());
				
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
	
}
