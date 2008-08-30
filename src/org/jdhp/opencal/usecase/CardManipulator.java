/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase;

import java.util.ArrayList;

import org.jdhp.opencal.OpenCAL;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardManipulator {
	
	private ArrayList<Card> cardList;
	
	private int cursor;
	
	/**
	 * 
	 * @param PKB_FILE_PATH
	 */
	public CardManipulator(ArrayList<Card> cardList) {
		this.cursor = 0;
		this.cardList = cardList;
	}
	
	/**
	 * 
	 * @return
	 */
	public Card pop() {
		if(!this.cardList.isEmpty()) {
			return (Card) this.cardList.get(this.cursor);
		} else {
			OpenCAL.mainWindow.printAlert("Review terminated");
			return null;
		}
	}

	/**
	 * 
	 */
	public void next() {
		if(this.hasNext()) this.cursor++;
	}
	
	/**
	 * 
	 */
	public void preview() {
		if(this.hasPreview()) this.cursor--;
	}


	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {
		if(this.cursor < this.cardList.size() - 1) return true;
		else return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasPreview() {
		if(this.cursor > 0) return true;
		else return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public void first() {
		this.cursor = 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public void last() {
		this.cursor = this.cardList.size() - 1;
	}
	
	/**
	 * 
	 * @param newCard
	 */
	public void insert(Card newCard) {
		this.cardList.add(newCard);
	}
	
	/**
	 * 
	 */
	public void remove() {
		if(this.hasNext() && this.hasPreview()) {
			this.cardList.remove(this.cursor);
		} else if(!this.hasNext() && this.hasPreview()) {
			this.cardList.remove(this.cursor);
			this.cursor--;
		} else if(this.hasNext() && !this.hasPreview()) {
			this.cardList.remove(this.cursor);
		} else if(!this.hasNext() && !this.hasPreview()) {
			this.cardList.remove(this.cursor);
		}
	}
	
}
