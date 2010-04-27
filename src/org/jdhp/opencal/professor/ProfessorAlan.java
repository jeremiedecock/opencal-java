/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.professor;

import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.util.CalendarToolKit;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ProfessorAlan implements Professor {
	
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
		if(!isSorted(reviewList)) System.out.println("Unsorted card detected : " + card);
		
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
	 * @param reviewList
	 * @return
	 */
	public static boolean isSorted(NodeList reviewList) {
		boolean isSorted = true;
		
		if(reviewList.getLength()>1) {
			GregorianCalendar lastDate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(0)).getAttribute("rdate"));
			
			int i = 1;
			while(i < reviewList.getLength() && isSorted) {
				GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(((Element) reviewList.item(i)).getAttribute("rdate"));
				
				if(rdate.before(lastDate)) isSorted = false;
				
				lastDate = rdate;
				i++;
			}
		}
		
		return isSorted;
	}
	
	/**
	 * 
	 */
	public String getName() {
		return this.NAME;
	}
	
}
