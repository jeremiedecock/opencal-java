/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

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
    
//  // Singleton pattern
//  private final static CardCollection uniqueInstance = new CardCollection();
//  
//  // Singleton pattern
//  public static CardCollection getInstance() {
//      return uniqueInstance;
//  }
//  
//  // Singleton pattern (set all constructors to private)
    public CardCollection() {
        super();
    }
    
    public CardCollection(int initialCapacity) {
        super(initialCapacity);
    }
    
    public CardCollection(Collection<? extends Card> c) {
        super(c);
    }

    /**
     * 
     * @return
     */
    public String[] getTags(boolean ignoreHiddenCards) {
        TreeSet<String> tagSet = new TreeSet<String>();
        
        for(Card card : this) {
            if(!card.isHidden() || !ignoreHiddenCards) {
                String[] tags = card.getTags().toArray(new String[0]);
                
                for(String tag : tags) {
                    tagSet.add(tag);
                }
            }
        }
        
        return tagSet.toArray(new String[tagSet.size()]);
    }
    
    
    /**
     * Vérifi que toutes les dates de toutes les cartes sont bien antérieurs (ou égales) à aujourd'hui.
     * Dans le cas contraire, c'est qu'il y a un problème avec l'heure du système... 
     */
    public boolean isDateConsistent() {
        GregorianCalendar todayDate = new GregorianCalendar();
        boolean isConsistent = true;
        
        loop1: for(Card card : this) {
            // Check creationDate
            GregorianCalendar creationDate = CalendarToolKit.iso8601ToCalendar(card.getCreationDate());
            if(creationDate.after(todayDate)) {
                isConsistent = false;
                break loop1;
            }
            
            // Check reviewDate
            for(Review review : card.getReviews()) {
                GregorianCalendar reviewDate = CalendarToolKit.iso8601ToCalendar(review.getReviewDate());
                if(reviewDate.after(todayDate)) {
                    isConsistent = false;
                    break loop1;
                }
            }
        }
        
        return isConsistent;
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
            
            Review[] reviews = card.getReviews().toArray(new Review[0]);
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
