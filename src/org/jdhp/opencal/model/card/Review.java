/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.card;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Review {

	// TODO : use an enum
	final public static int NO_ANSWER = 0;
	
	// TODO : use an enum
	final public static int RIGHT_ANSWER = 1;
	
	// TODO : use an enum
	final public static int WRONG_ANSWER = -1;
	
	// TODO : faire une map (dico) pour relier les clés {RIGHT_ANSWER, WRONG_ANSWER} aux valeures {RIGHT_ANSWER_STRING, WRONG_ANSWER_STRING}
	public final static String RIGHT_ANSWER_STRING = "good";
	
	public final static String WRONG_ANSWER_STRING = "bad";
	
	
	private String rdate;
	
	private String result;
	
	public Review(String rdate, String result) {
		this.rdate = rdate;
		this.result = result;
	}
	
	public String getReviewDate() {
		return this.rdate;
	}
	
	public String getResult() {
		return this.result;
	}
	
}
