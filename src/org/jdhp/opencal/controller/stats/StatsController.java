/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.controller.stats;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.jdhp.opencal.controller.Controller;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class StatsController {

	/**
	 * 
	 */
	public static void init() {
		// void
	}
	
	/**
	 * 
	 * @return
	 */
	public static TreeMap<Date, Integer> getCardCreationStats() {
		TreeMap<Date, Integer> cardCreationStats = new TreeMap<Date, Integer>();
		
		NodeList nodeCards = Controller.getXmlDocument().getElementsByTagName("card");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Element card = (Element) nodeCards.item(i);
			
			// Les dates sont au format ISO 8601 (YYYY-MM-DD)
			String[] date = card.getAttribute("cdate").split("-",3);
			
			// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
			Date cdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
			
			if(cardCreationStats.containsKey(cdate)) {
				cardCreationStats.put(cdate, new Integer(cardCreationStats.get(cdate).intValue() + 1));
			} else {
				cardCreationStats.put(cdate, new Integer(1));
			}
		}
		
		// Ajoute à la liste tous les jours où aucune carte n'a été créée 
		if(!cardCreationStats.isEmpty()) {
			Date date = (Date) cardCreationStats.firstKey().clone();
			Date today = new Date();
			
			while(date.before(today)) {
				if(!cardCreationStats.containsKey(date)) cardCreationStats.put((Date) date.clone(), new Integer(0));
				date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
			}
		}
		
		return cardCreationStats;
	}
	
	/**
	 * 
	 * @return
	 */
	public static TreeMap<Date, Integer> getRevisionStats() {
		TreeMap<Date, Integer> revisionStats = new TreeMap<Date, Integer>();
		
		NodeList nodeCards = Controller.getXmlDocument().getElementsByTagName("review");
		for(int i=0 ; i<nodeCards.getLength() ; i++) {
			Element review = (Element) nodeCards.item(i);
			
			// Les dates sont au format ISO 8601 (YYYY-MM-DD)
			String[] date = review.getAttribute("rdate").split("-",3);
			
			// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
			Date rdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
			
			if(revisionStats.containsKey(rdate)) {
				revisionStats.put(rdate, new Integer(revisionStats.get(rdate).intValue() + 1));
			} else {
				revisionStats.put(rdate, new Integer(1));
			}
		}
		
		// Ajoute à la liste tous les jours où aucune carte n'a été révisée 
		if(!revisionStats.isEmpty()) {
			Date date = (Date) revisionStats.firstKey().clone();
			Date today = new Date();
			
			while(date.before(today)) {
				if(!revisionStats.containsKey(date)) revisionStats.put((Date) date.clone(), new Integer(0));
				date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
			}
		}
		
		return revisionStats;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getNumberOfCardsMadeToday() {
		return 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getNumberOfCardsReviewedToday() {
		return 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getNumberOfCardsScheduledForToday() {
		return 0;
	}
}
