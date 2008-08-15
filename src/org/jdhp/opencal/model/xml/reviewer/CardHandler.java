/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.reviewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jdhp.opencal.usecase.review.inspector.Inspector;
import org.jdhp.opencal.usecase.review.inspector.InspectorAlan;
import org.jdhp.opencal.usecase.review.inspector.InspectorBrian;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardHandler extends DefaultHandler {

	private static Inspector inspector = new InspectorBrian();
	
	private RevisionPile revisionPile;
	
	private ArrayList<ReviewItem> revisionList;
	
	private String id;
	
	private Date cdate;
	
	private String question;
	
	private String answer;
	
	private Date rdate;
	
	private String result;
	
	private boolean questionFlag;
	
	private boolean answerFlag;
	
	/**
	 * 
	 * @param revisionPile
	 */
	public CardHandler(RevisionPile revisionPile) {
		super();
		
		this.revisionPile = revisionPile;
		this.revisionList = new ArrayList<ReviewItem>();
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
				// Les dates sont au format ISO 8601 (YYYY-MM-DD)
				String[] date = atts.getValue("cdate").split("-",3);
				// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
				this.cdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				
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
				this.rdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				this.result = atts.getValue("result");
				this.revisionList.add(new ReviewItem(this.rdate, this.result));
			}
		} else {
			if(name.equals("card")) {
				this.id = atts.getValue("id");
				// Les dates sont au format ISO 8601 (YYYY-MM-DD)
				String[] date = atts.getValue("cdate").split("-",3);
				// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
				this.cdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				
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
				this.rdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				this.result = atts.getValue("result");
				this.revisionList.add(new ReviewItem(this.rdate, this.result));
			}
		}
	}
	
	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		if(uri.equals("")) {
			if(qName.equals("card")) {
				this.revisionPile.addCard(new Card(this.id, this.question, this.answer, CardHandler.inspector.valueCardPriority(this.revisionList, this.cdate)));
			}
			else if(qName.equals("question")) {
				this.questionFlag = false;
			}
			else if(qName.equals("answer")) {
				this.answerFlag = false;
			}
		} else {
			if(name.equals("card")) {
				this.revisionPile.addCard(new Card(this.id, this.question, this.answer, CardHandler.inspector.valueCardPriority(this.revisionList, this.cdate)));
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

}
