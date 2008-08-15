/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.review.inspector;

import java.util.ArrayList;
import java.util.Date;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface Inspector {
	
	public int valueCardPriority(ArrayList<ReviewItem> revisionList, Date cardCreationDate);
	
}
