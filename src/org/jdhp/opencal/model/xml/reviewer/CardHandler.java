/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.reviewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardHandler extends DefaultHandler {

	private Pile pile;
	
	private ArrayList<ReviewItem> reviewList;
	
	private String id;
	
	private String question;
	
	private String answer;
	
	private Date date;
	
	private String result;
	
	private boolean questionFlag;
	
	private boolean answerFlag;
	
	/**
	 * 
	 * @param pile
	 */
	public CardHandler(Pile pile) {
		super();
		
		this.pile = pile;
		this.reviewList = new ArrayList<ReviewItem>();
		this.questionFlag = false;
		this.answerFlag = false;
	}
	
	/**
	 * 
	 */
	public void startDocument() {
		
	}
	
	/**
	 * 
	 */
	public void endDocument() {
		
	}
	
	/**
	 * 
	 */
	public void startElement(String uri, String name, String qName, Attributes atts) {
		if(uri.equals("")) {
			if(qName.equals("card")) {
				this.id = atts.getValue("id");
				this.question = "";
				this.answer = "";
			}
			else if(qName.equals("question")) {
				this.questionFlag = true;
				this.question = "";
			}
			else if(qName.equals("answer")) {
				this.answerFlag = true;
				this.answer = "";
			}
			else if(qName.equals("review")) {
				// Les dates sont au format ISO 8601 (YYYY-MM-DD)
				String[] date = atts.getValue("rdate").split("-",3);
				// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
				this.date = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				this.result = atts.getValue("result");
				this.reviewList.add(new ReviewItem(this.date, this.result));
			}
		} else {
			if(name.equals("card")) {
				this.id = atts.getValue("id");
				this.question = "";
				this.answer = "";
			}
			else if(name.equals("question")) {
				this.questionFlag = true;
				this.question = "";
			}
			else if(name.equals("answer")) {
				this.answerFlag = true;
				this.answer = "";
			}
			else if(name.equals("review")) {
				// Les dates sont au format ISO 8601 (YYYY-MM-DD)
				String[] date = atts.getValue("rdate").split("-",3);
				// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
				this.date = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				this.result = atts.getValue("result");
				this.reviewList.add(new ReviewItem(this.date, this.result));
			}
		}
	}
	
	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		if(uri.equals("")) {
			if(qName.equals("card")) {
				this.pile.addCard(new Card(this.id, this.question, this.answer, this.calculateCardPriority()));
			}
			else if(qName.equals("question")) {
				this.questionFlag = false;
			}
			else if(qName.equals("answer")) {
				this.answerFlag = false;
			}
		} else {
			if(name.equals("card")) {
				this.pile.addCard(new Card(this.id, this.question, this.answer, this.calculateCardPriority()));
			}
			else if(name.equals("question")) {
				this.questionFlag = false;
			}
			else if(name.equals("answer")) {
				this.answerFlag = false;
			}
		}
	}
	
	/**
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		if(this.questionFlag == true) {
			for (int i=start ; i<start+length ; i++) this.question += ch[i];
		}
		else if(this.answerFlag == true) {
			for (int i=start ; i<start+length ; i++) this.answer += ch[i];
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int calculateCardPriority() {
		int priorityRank = 0;
		for(int i=0 ; i<this.reviewList.size() ; i++) {
			int oldestReviewIndex = 0;
			for(int reviewIndex=0 ; reviewIndex<this.reviewList.size() ; reviewIndex++) {
				if(((ReviewItem) this.reviewList.get(reviewIndex)).getReviewDate().before(((ReviewItem) this.reviewList.get(oldestReviewIndex)).getReviewDate())) {
					oldestReviewIndex = reviewIndex;
				}
			}
			if(((ReviewItem) this.reviewList.get(oldestReviewIndex)).getReviewResult().toUpperCase().equals("GOOD")) {
				priorityRank++;
			} else {
				priorityRank = 0;
			}
			this.reviewList.remove(oldestReviewIndex);
		}
		return priorityRank;
	}
}
