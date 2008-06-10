/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.explorer;

import java.util.ArrayList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MadeCardsPile {
	
	private ArrayList<Card> cardList;
	
	/**
	 * 
	 * @param pkbFilePath
	 */
	public MadeCardsPile() {
		this.cardList = new ArrayList<Card>();
	}
	
	/**
	 * 
	 * @param newCard
	 */
	public void addCard(Card newCard) {
		this.cardList.add(newCard);
	}
	
	/**
	 * 
	 * @return strings
	 */
	public String[] getStrings() {
		String[] stringArray = new String[this.cardList.size()];
		
		for(int i=0 ; i<this.cardList.size() ; i++) {
			stringArray[i] = this.cardList.get(i).getQuestion();
			if(stringArray[i].length() > 14) stringArray[i] = stringArray[i].substring(0, 11) + "...";
		}
		
		return stringArray;
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public String getQuestion(int cardIndex) {
		return this.cardList.get(cardIndex).getQuestion();
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public String getAnswer(int cardIndex) {
		return this.cardList.get(cardIndex).getAnswer();
	}
	
	/**
	 * 
	 * @param int
	 * @return
	 */
	public String getTags(int cardIndex) {
		return this.cardList.get(cardIndex).getTags();
	}
}
