/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.model.card;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdhp.opencal.util.CalendarToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Card {
	
	private String question;
	
	private String answer;
	
	private List<String> tags;
	
	private List<Review> reviews;
	
	private GregorianCalendar creationDate;  // TODO: use Date instead ?
	
	private boolean isHidden;

	private float grade;

	/**
	 * Constructor called by PKB.load().
	 * 
	 * @param question
	 * @param answer
	 * @param tags
	 * @param reviews
	 * @param cdate
	 * @param hidden
	 */
	public Card(String question, String answer, List<String> tags, List<Review> reviews, String creationDate, boolean isHidden) {
		this.setQuestion(question);
		this.setAnswer(answer);
		this.setTags(tags);
		this.setReviews(reviews);
		this.setCreationDate(creationDate);
		this.setHidden(isHidden);
		
//		System.out.println(this);
	}
	
	/**
	 * Constructor called by Window.
	 * 
	 * @param question
	 * @param answer
	 * @param tags
	 */
	public Card(String question, String answer, List<String> tags) {
		this(question, answer, tags, new ArrayList<Review>(), CalendarToolKit.calendarToIso8601(new GregorianCalendar()), false);
	}

	/**
	 * 
	 * @return
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * 
	 * @param question
	 */
	public void setQuestion(String question) {
		if(question == null || question.isEmpty()) {
			// TODO: throw exception or warning ?...
		} else {
			this.question = question;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * 
	 * @param answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * 
	 * @param tags
	 */
	public void setTags(List<String> tags) {
		
		for(String tag : tags) {
			// TODO : n'authoriser que l'alphanum et [ _-] ?
			// TODO : interdire les balises xml [<>...] car les tags ne sont pas dans des CDATA
			//tag = tag.trim().toLowerCase(Locale.ENGLISH);  // TODO : i18n and L10n issues ???
			tag = tag.trim().toLowerCase();  // TODO : i18n and L10n issues ???
		}
		
		this.tags = tags;
	}

	/**
	 * 
	 * @return
	 */
	public List<Review> getReviews() {
		return reviews;
	}

	/**
	 * 
	 * @param reviews
	 */
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	/**
	 * 
	 * @return
	 */
	public String getCreationDate() {
		return CalendarToolKit.calendarToIso8601(creationDate);
	}

	/**
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = CalendarToolKit.iso8601ToCalendar(creationDate);  // TODO: throw exception if non valid date
	}

	/**
	 * 
	 * @return
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 * 
	 * @param isHidden
	 */
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * 
	 * @return
	 */
	public float getGrade() {
		return grade;
	}

	/**
	 * 
	 * @param grade
	 */
	public void setGrade(float grade) {
		this.grade = grade;
	}
	
	/**
	 * Retourne vrai si question ou answer contiennent le motif "pattern".
	 * 
	 * @param pattern
	 * @param caseSensitive
	 * @return
	 */
	public boolean contains(String pattern, boolean caseSensitive) {
		boolean contains;
		String question = this.getQuestion();
		String answer = this.getAnswer();
		
		if(caseSensitive) {
			contains = question.contains(pattern) || answer.contains(pattern);
		} else {
			pattern = pattern.toLowerCase();
			question = question.toLowerCase();
			answer = answer.toLowerCase();
			contains = question.contains(pattern) || answer.contains(pattern);
		}
		
		return contains;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Card [creationDate=");
		builder.append(this.getCreationDate());
		
		builder.append(", isHidden=");
		builder.append(this.isHidden());
		
		builder.append(", grade=");
		builder.append(this.getGrade());
		
		builder.append(", question=");
		builder.append(this.getQuestion());
		
		builder.append(", answer=");
		builder.append(this.getAnswer());
		
		builder.append(", reviews=[ ");
		for(Review review : this.getReviews()) {
			builder.append("<");
			builder.append(review.getReviewDate());
			builder.append(",");
			builder.append(review.getResult());
			builder.append("> ");
		}
		builder.append("]");
		
		builder.append(", tags=[ ");
		for(String tag : this.getTags()) {
			builder.append("<");
			builder.append(tag);
			builder.append("> ");
		}
		builder.append("]");
		
		builder.append("]");
		return builder.toString();
	}
	
}
