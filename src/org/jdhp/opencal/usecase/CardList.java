/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase;

import java.util.ArrayList;

import org.jdhp.opencal.usecase.CardManipulator;
import org.jdhp.opencal.usecase.Manipulable;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardList implements Manipulable {

	protected ArrayList<Card> cardList;
	
	public CardList() {
		this.cardList = new ArrayList<Card>();
		// TODO : ex-init...
	}
	
	public CardManipulator manipulator() {
		return new CardManipulator(this.cardList);
	}
	
}
