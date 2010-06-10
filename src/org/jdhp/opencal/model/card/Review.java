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
