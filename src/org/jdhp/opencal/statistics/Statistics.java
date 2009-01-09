/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.statistics;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.toolkit.CalendarToolKit;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Statistics {
	
	/**
	 * 
	 * @return
	 */
	public static TreeMap<GregorianCalendar, Integer> getCardCreationStats() {
		TreeMap<GregorianCalendar, Integer> cardCreationStats = new TreeMap<GregorianCalendar, Integer>();
		
		NodeList nodeCards = OpenCAL.getDomDocument().getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Element card = (Element) nodeCards.item(i);
			
			GregorianCalendar cdate = CalendarToolKit.iso8601ToCalendar(card.getAttribute("cdate"));
			
			if(cardCreationStats.containsKey(cdate)) {
				cardCreationStats.put(cdate, new Integer(cardCreationStats.get(cdate).intValue() + 1));
			} else {
				cardCreationStats.put(cdate, new Integer(1));
			}
		}
		
		// Ajoute à la liste tous les jours où aucune carte n'a été créée 
		if(!cardCreationStats.isEmpty()) {
			GregorianCalendar date = (GregorianCalendar) cardCreationStats.firstKey().clone();
			GregorianCalendar today = new GregorianCalendar();
			
			while(date.before(today)) {
				if(!cardCreationStats.containsKey(date)) cardCreationStats.put((GregorianCalendar) date.clone(), new Integer(0));
				date.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		
		return cardCreationStats;
	}
	
	/**
	 * 
	 * @return
	 */
	public static TreeMap<GregorianCalendar, Integer> getRevisionStats() {
		TreeMap<GregorianCalendar, Integer> revisionStats = new TreeMap<GregorianCalendar, Integer>();
		
		NodeList nodeCards = OpenCAL.getDomDocument().getElementsByTagName("review");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Element review = (Element) nodeCards.item(i);
			
			GregorianCalendar rdate = CalendarToolKit.iso8601ToCalendar(review.getAttribute("rdate"));
			
			if(revisionStats.containsKey(rdate)) {
				revisionStats.put(rdate, new Integer(revisionStats.get(rdate).intValue() + 1));
			} else {
				revisionStats.put(rdate, new Integer(1));
			}
		}
		
		// Ajoute à la liste tous les jours où aucune carte n'a été révisée 
		if(!revisionStats.isEmpty()) {
			GregorianCalendar date = (GregorianCalendar) revisionStats.firstKey().clone();
			GregorianCalendar today = new GregorianCalendar();
			
			while(date.before(today)) {
				if(!revisionStats.containsKey(date)) revisionStats.put((GregorianCalendar) date.clone(), new Integer(0));
				date.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		
		return revisionStats;
	}
}
