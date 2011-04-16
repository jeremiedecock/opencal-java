package org.jdhp.opencal.model.cardcollection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TreeSet;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.util.CalendarToolKit;

//TODO : use an other collection ? (formerly HashSet -> it don't preserve chronology)
//public final static Collection<Card> cardCollection = new HashSet<Card>(); // TODO : use TreeSet instead ? (sort)

public class CardCollection extends ArrayList<Card> {
	
	// Singleton pattern
	private final static CardCollection uniqueInstance = new CardCollection();
	
	// Singleton pattern
	public static CardCollection getInstance() {
		return uniqueInstance;
	}
	
	// Singleton pattern (set all constructors to private)
	private CardCollection() {}
	private CardCollection(int initialCapacity) {}
	private CardCollection(Collection<? extends Card> c) {}

	/**
	 * 
	 * @return
	 */
	public String[] getTags(boolean ignoreHiddenCards) {
		TreeSet<String> tagSet = new TreeSet<String>();
		
        for(Card card : this) {
            if(!card.isHidden() || !ignoreHiddenCards) {
                String[] tags = card.getTags();
                
                for(String tag : tags) {
                	tagSet.add(tag);
                }
            }
        }
		
		return tagSet.toArray(new String[tagSet.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getCardCreationStats(int numberOfDays) {
		
		GregorianCalendar startDate = new GregorianCalendar();
		CalendarToolKit.floorGregorianCalendar(startDate);
		startDate.add(Calendar.DAY_OF_MONTH, -numberOfDays+1);

		int[] stats = new int[numberOfDays];
		
        for(Card card : this) {
        	
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
	public int[] getRevisionStats(int numberOfDays) {
		
		GregorianCalendar startDate = new GregorianCalendar();
		CalendarToolKit.floorGregorianCalendar(startDate);
		startDate.add(Calendar.DAY_OF_MONTH, -numberOfDays+1);

		int[] stats = new int[numberOfDays];
		
		for(Card card : this) {
            
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
