package org.jdhp.opencal.controller.stats;

import java.util.Date;
import java.util.HashMap;

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
	public static int[] getCardCreationStats() {
		HashMap<Date, Integer> cardCreationStats = CardCreationStatsHandler.getCardCreationStats();
		System.out.println(cardCreationStats);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int[] getRevisionStats() {
		HashMap<Date, Integer> revisionStats = RevisionStatsHandler.getRevisionStats();
		System.out.println(revisionStats);
		return null;
	}
}
