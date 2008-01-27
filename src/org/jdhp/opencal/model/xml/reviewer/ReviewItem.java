/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.reviewer;

import java.util.Date;

public class ReviewItem {

	private Date reviewDate;
	private String reviewResult;
	
	public ReviewItem(Date reviewDate, String reviewResult) {
		this.reviewDate = reviewDate;
		this.reviewResult = reviewResult;
	}
	
	public Date getReviewDate() {
		return this.reviewDate;
	}
	
	public String getReviewResult() {
		return this.reviewResult;
	}
}
