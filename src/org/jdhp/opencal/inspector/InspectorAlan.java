/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.inspector;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorAlan implements Inspector {
	
	public final String NAME = "Alan";

	/**
	 * Alan is a lazy guy. He doesn't care about too late or too early reviews.
	 * 
	 * @return
	 */
	public float assess(Card card) {
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
	
	/**
	 * 
	 */
	public String getName() {
		return this.NAME;
	}
	
}
