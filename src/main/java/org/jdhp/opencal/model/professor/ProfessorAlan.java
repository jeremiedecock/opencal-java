/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.model.professor;

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
public class ProfessorAlan implements Professor {
    
    public static final String NAME = "Alan";
    
    /**
     * 
     */
    public String getName() {
        return ProfessorAlan.NAME;
    }
    
    /**
     * Alan is a lazy guy. He doesn't care about too late or too early reviews.
     * 
     * @return
     */
    public float assess(Card card) {
        int grade = 0;
        
        List<Review> reviewList = card.getReviews();
        
        if(reviewList.isEmpty()) {
            grade = Professor.HAS_NEVER_BEEN_REVIEWED;
        } else {
            // TODO : vérifier que les noeuds "review" sont bien classés par date croissante
            if(!isSorted(reviewList)) System.out.println("Unsorted card detected : " + card);
            
            for(Review review : reviewList) {
                if(review.getResult().equals(Review.RIGHT_ANSWER_STRING)) {
                    grade++;
                } else {
                    grade = 0;
                }
            }
        }
        
        return grade;
    }
    
    /**
     * TODO: use directly a sorted list/set on the date
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
    
}
