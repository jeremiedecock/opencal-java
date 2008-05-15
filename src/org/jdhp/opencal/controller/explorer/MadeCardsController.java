package org.jdhp.opencal.controller.explorer;

import org.jdhp.opencal.model.xml.explorer.Card;
import org.jdhp.opencal.model.xml.explorer.MadeCardsHandler;
import org.jdhp.opencal.model.xml.explorer.MadeCardsPile;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MadeCardsController {

	public static MadeCardsPile madeCardsPile;
	
	public static Card card;
	
	/**
	 * 
	 */
	public static void init() {
		MadeCardsController.madeCardsPile = new MadeCardsPile();
		MadeCardsHandler.initMadeCardsPile(MadeCardsController.madeCardsPile);
	}
	
	/**
	 * 
	 * @param card
	 */
	public static void add(Card card) {
		MadeCardsController.madeCardsPile.addCard(card);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getStrings() {
		return MadeCardsController.madeCardsPile.getStrings();
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public static String getQuestion(int cardIndex) {
		return MadeCardsController.madeCardsPile.getQuestion(cardIndex);
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public static String getAnswer(int cardIndex) {
		return MadeCardsController.madeCardsPile.getAnswer(cardIndex);
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public static String getTags(int cardIndex) {
		return MadeCardsController.madeCardsPile.getTags(cardIndex);
	}
}
