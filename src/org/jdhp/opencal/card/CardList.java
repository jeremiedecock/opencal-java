/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card;

import java.util.ArrayList;

import org.jdhp.opencal.card.CardManipulator;
import org.jdhp.opencal.card.Manipulable;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardList extends ArrayList<Card> implements Manipulable {

	public CardList() {
		super();
	}
	
	public CardManipulator manipulator() {
		return new CardManipulator(this);
	}
	
	public String[] getQuestionStrings() {
		ArrayList<String> questionStrings = new ArrayList<String>();
		
		for(int i=0 ; i<this.size() ; i++) {
			questionStrings.add(this.get(i).getQuestion());
		}
		
		return questionStrings.toArray(new String[0]);
	}
	
}
