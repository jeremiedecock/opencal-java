/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.reviewer;

public class Card {
	
	private String id;
	private String question;
	private String answer;
	private int priority;
	
	public Card(String id, String question, String answer, int priority) {
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.priority = priority;
	}
	
	public String getId() {
		return this.id;
	}

	public String getQuestion() {
		return this.question;
	}

	public String getAnswer() {
		return this.answer;
	}
	
	public int getPriority() {
		return this.priority;
	}
}
