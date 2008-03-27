package org.jdhp.opencal.controller.stats;

import org.jdhp.opencal.model.xml.stats.CardCreationStatsHandler;

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
		CardCreationStatsHandler.getCardCreationStats();
		return null;
	}
}
