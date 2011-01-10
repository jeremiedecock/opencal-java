/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.model.professor;

import org.jdhp.opencal.model.card.Card;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface Professor {
	
	public float assess(Card card);
	
}
