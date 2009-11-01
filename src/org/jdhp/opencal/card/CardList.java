/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.card;

import java.util.ArrayList;

import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.PersonalKnowledgeBase;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * TODO : implementer le patern Observateur ?
 *
 * @author Jérémie Decock
 */
public class CardList extends ArrayList<Card> {

    // mainCardList est volontairement pas de type CardList (...)
    public static ArrayList<Card> mainCardList = new ArrayList<Card>();

    /**
     *
     */
    public static void initMainCardList() {
        try {
            NodeList nodeCards = PersonalKnowledgeBase.getDomDocument().getElementsByTagName("card");
            for(int i=0 ; i<nodeCards.getLength() ; i++) {
                Card card = new Card((Element) nodeCards.item(i));
                CardList.mainCardList.add(card);
            }
        } catch(NullPointerException e) {
            System.out.println("Error : DOM tree called but not yet loaded.");
            System.exit(1);
        }
    }
	
	/**
	 * 
	 */
	public CardList() {
		super();
	}
}
