package org.jdhp.opencal.controller.reviewer.inspector;

import java.util.ArrayList;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface Inspector {
	
	public int valueCardPriority(ArrayList<ReviewItem> revisionList);
	
}
