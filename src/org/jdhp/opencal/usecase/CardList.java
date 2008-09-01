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
