/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.inspector;

import org.jdhp.opencal.usecase.Card;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface Inspector {
	
	public int assess(Card card);
	
}
