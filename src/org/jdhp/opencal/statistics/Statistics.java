/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.statistics;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.Review;
import org.jdhp.opencal.util.CalendarToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Statistics {
	
	/**
	 * 
	 * @return
	 */
	public static int[] getCardCreationStats(int numberOfDays) {
		
		GregorianCalendar startDate = new GregorianCalendar();
		CalendarToolKit.floorGregorianCalendar(startDate);
		startDate.add(Calendar.DAY_OF_MONTH, -numberOfDays+1);

		int[] stats = new int[numberOfDays];
		
		Iterator<Card> it;
		it = OpenCAL.cardCollection.iterator();
		
        while(it.hasNext()) {
            Card card = it.next();
			
			GregorianCalendar cdate = CalendarToolKit.iso8601ToCalendar(card.getCreationDate());
			
			boolean found = false;
			int i = -1;
			GregorianCalendar date = (GregorianCalendar) startDate.clone();
			while(!found && i < stats.length) {
				if(cdate.after(date)) {
					i++;
					date.add(Calendar.DAY_OF_MONTH, 1);
				} else {
					found = true;
				}
			}
			
			if(i > 0)
				stats[i]++;
        }
		
		return stats;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int[] getRevisionStats(int numberOfDays) {
		
		GregorianCalendar startDate = new GregorianCalendar();
		CalendarToolKit.floorGregorianCalendar(startDate);
		startDate.add(Calendar.DAY_OF_MONTH, -numberOfDays+1);

		int[] stats = new int[numberOfDays];
		
		Iterator<Card> it;
		it = OpenCAL.cardCollection.iterator();
		
        while(it.hasNext()) {
            Card card = it.next();
            
            Review[] reviews = card.getReviews();
            for(int j=0 ; j < reviews.length ; j++) {
            	
				GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(reviews[j].getReviewDate());
				
				boolean found = false;
				int i = -1;
				GregorianCalendar date = (GregorianCalendar) startDate.clone();
				while(!found && i < stats.length) {
					if(rdate.after(date)) {
						i++;
						date.add(Calendar.DAY_OF_MONTH, 1);
					} else {
						found = true;
					}
				}
				
				if(i > 0)
					stats[i]++;
            }
		}
		
		return stats;
	}
	
}
