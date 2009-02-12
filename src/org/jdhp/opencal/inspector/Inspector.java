/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.inspector;

import org.jdhp.opencal.card.Card;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface Inspector {
	
	public String getName();
	
	public float assess(Card card);
	
}
