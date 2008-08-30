/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.review.inspector;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.usecase.Card;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorAlan implements Inspector {

	/**
	 * Alan is a lazy guy. He doesn't care about too late or too early reviews.
	 * 
	 * @return
	 */
	public int assess(Card card) {
		int grade = 0;
		
		// TODO : vérifier que les noeuds "review" sont bien classés par date croissante
		NodeList reviewList = card.getElement().getElementsByTagName("review");
		for(int i=0 ; i < reviewList.getLength() ; i++) {
			if(((Element) reviewList.item(i)).getAttribute("result").equals(OpenCAL.RIGHT_ANSWER_STRING)) {
				grade++;
			} else {
				grade = 0;
			}
		}
		
		return grade;
	}
	
}
