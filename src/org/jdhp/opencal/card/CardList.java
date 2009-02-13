/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
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

	/**
	 * 
	 */
	public CardList() {
		super();
	}
	
	/**
	 * 
	 */
	public CardManipulator manipulator() {
		return new CardManipulator(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getQuestionStrings() {
		ArrayList<String> questionStrings = new ArrayList<String>();
		
		for(int i=0 ; i<this.size() ; i++) {
			questionStrings.add(this.get(i).getQuestion());
		}
		
		return questionStrings.toArray(new String[0]);
	}
	
	/**
	 * 
	 */
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		
		for(int i=0 ; i<this.size() ; i++) {
			stringBuffer.append(this.get(i).toString());
		}
		
		return stringBuffer.toString();
	}
}
