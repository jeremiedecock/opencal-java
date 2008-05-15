package org.jdhp.opencal.controller.explorer;

import org.jdhp.opencal.model.xml.explorer.Card;
import org.jdhp.opencal.model.xml.explorer.ReviewedCardsHandler;
import org.jdhp.opencal.model.xml.explorer.ReviewedCardsPile;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewedCardsController {

	public static ReviewedCardsPile reviewedCardsPile;
	
	public static Card card;
	
	/**
	 * 
	 */
	public static void init() {
		ReviewedCardsController.reviewedCardsPile = new ReviewedCardsPile();
		ReviewedCardsHandler.initReviewedCardsPile(ReviewedCardsController.reviewedCardsPile);
	}
	
	/**
	 * 
	 * @param card
	 */
	public static void add(Card card) {
		ReviewedCardsController.reviewedCardsPile.addCard(card);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[] getStrings() {
		return ReviewedCardsController.reviewedCardsPile.getStrings();
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public static String getQuestion(int cardIndex) {
		return ReviewedCardsController.reviewedCardsPile.getQuestion(cardIndex);
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public static String getAnswer(int cardIndex) {
		return ReviewedCardsController.reviewedCardsPile.getAnswer(cardIndex);
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public static String getResult(int cardIndex) {
		return ReviewedCardsController.reviewedCardsPile.getResult(cardIndex);
	}
}
