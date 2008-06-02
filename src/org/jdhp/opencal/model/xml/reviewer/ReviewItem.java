/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.reviewer;

import java.util.Date;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewItem {

	private Date reviewDate;
	
	private String reviewResult;
	
	/**
	 * 
	 * @param reviewDate
	 * @param reviewResult
	 */
	public ReviewItem(Date reviewDate, String reviewResult) {
		this.reviewDate = reviewDate;
		this.reviewResult = reviewResult;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getReviewDate() {
		return this.reviewDate;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getReviewResult() {
		return this.reviewResult;
	}
}
