/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.explorer;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Card {
	
	private String question;
	
	private String answer;
	
	private String tags;
	
	/**
	 * 
	 * @param id
	 * @param question
	 * @param answer
	 * @param priorityRank
	 */
	public Card(String question, String answer, String tags) {
		this.question = question;
		this.answer = answer;
		this.tags = tags;
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
	public String getTags() {
		return this.tags;
	}
}
