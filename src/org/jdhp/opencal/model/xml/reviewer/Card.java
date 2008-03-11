/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.reviewer;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Card {
	
	private String id;
	
	private String question;
	
	private String answer;
	
	private int priorityRank;
	
	/**
	 * 
	 * @param id
	 * @param question
	 * @param answer
	 * @param priorityRank
	 */
	public Card(String id, String question, String answer, int priorityRank) {
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.priorityRank = priorityRank;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 
	 * @return
	 */
	public String getQuestion() {
		return this.question;
	}

	/**
	 * 
	 * @return
	 */
	public String getAnswer() {
		return this.answer;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPriorityRank() {
		return this.priorityRank;
	}
}
