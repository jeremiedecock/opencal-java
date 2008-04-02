package org.jdhp.opencal.controller.stats;

import java.util.Date;
import java.util.TreeMap;

import org.jdhp.opencal.model.xml.stats.CardCreationStatsHandler;
import org.jdhp.opencal.model.xml.stats.RevisionStatsHandler;

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
		TreeMap<Date, Integer> cardCreationStats = CardCreationStatsHandler.getCardCreationStats();
		Date date = (Date) cardCreationStats.firstKey().clone();
		Date today = new Date();
		
		while(date.before(today)) {
			if(cardCreationStats.get(date) == null) cardCreationStats.put((Date) date.clone(), new Integer(0));
			date.setTime(date.getTime() + 86400000);
		}
		
		return cardCreationStats;
	}
	
	/**
	 * 
	 * @return
	 */
	public static TreeMap<Date, Integer> getRevisionStats() {
		TreeMap<Date, Integer> revisionStats = RevisionStatsHandler.getRevisionStats();
		Date date = (Date) revisionStats.firstKey().clone();
		Date today = new Date();
		
		while(date.before(today)) {
			if(revisionStats.get(date) == null) revisionStats.put((Date) date.clone(), new Integer(0));
			date.setTime(date.getTime() + 86400000);
		}
		
		return revisionStats;
	}
}
