/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.model.professor;

import org.jdhp.opencal.model.card.Card;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface Professor {
    
    /**
     * Special grade for cards which has never been reviewed.
     */
    public static final int HAS_NEVER_BEEN_REVIEWED = -2;
    
    /**
     * Special grade for cards which doesn't need to be reviewed today.
     */
    public static final int DONT_REVIEW_THIS_TODAY = -1;
    
    /**
     * Return the name of the Professor instance.
     * 
     * @return
     */
    public String getName();
    
    /**
     * Asses a card.
     * 
     * @param card the card to assess.
     * @return the grade of the assessed card.
     */
    public float assess(Card card);
    
}
