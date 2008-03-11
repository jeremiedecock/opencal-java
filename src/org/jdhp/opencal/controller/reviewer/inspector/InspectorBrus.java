package org.jdhp.opencal.controller.reviewer.inspector;

import java.util.ArrayList;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorBrus implements Inspector {

	/**
	 * 
	 * @return
	 */
	public int valueCardPriority(ArrayList<ReviewItem> revisionList) {
		return 0;
	}
	
}
