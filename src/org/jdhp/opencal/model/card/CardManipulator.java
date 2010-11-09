/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.card;

import java.util.List;


/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardManipulator {
	
	private final List<Card> cardList;
	
	private int index;
	
	/**
	 * 
	 * @param PKB_FILE_PATH
	 */
	public CardManipulator(List<Card> cardList) {
		this.index = 0;
		this.cardList = cardList;
	}
	
	/**
	 * 
	 * @return
	 */
	public Card pop() {
		if(!this.cardList.isEmpty()) {
			return this.cardList.get(this.index);
		} else {
			return null;
		}
	}

	/**
	 * 
	 */
	public void next() {
		if(this.hasNext()) this.index++;
	}
	
	/**
	 * 
	 */
	public void previous() {
		if(this.hasPrevious()) this.index--;
	}


	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {
		if(this.index < this.cardList.size() - 1) return true;
		else return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasPrevious() {
		if(this.index > 0) return true;
		else return false;
	}
	
	/**
	 * 
	 */
	public void first() {
		this.index = 0;
	}
	
	/**
	 * 
	 */
	public void last() {
		this.index = this.cardList.size() - 1;
	}

	/**
	 * 
     * @return 
	 */
	public int getIndex() {
        return this.index;
	}

	/**
	 * 
	 * @param index
     * @return 
	 */
	public boolean setIndex(int newIndex) {
        if(newIndex >= 0 && newIndex < this.cardList.size()) {
            this.index = newIndex;
            return true;
        } else {
            return false;
        }
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
		if(this.hasNext() && this.hasPrevious()) {
			this.cardList.remove(this.index);
		} else if(!this.hasNext() && this.hasPrevious()) {
			this.cardList.remove(this.index);
			this.index--;
		} else if(this.hasNext() && !this.hasPrevious()) {
			this.cardList.remove(this.index);
		} else if(!this.hasNext() && !this.hasPrevious()) {
			this.cardList.clear();
		}
	}
	
}
