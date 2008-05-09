/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.maker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jdhp.opencal.controller.Controller;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardMakerHandler extends DefaultHandler {
	
	private PrintWriter tmpFile;
	
	private int id;
	
	private String question;
	
	private String answer;
	
	private String[] tags;
	
	private boolean questionFlag;
	
	private boolean answerFlag;
	
	private boolean tagFlag;
	
	private static String lastCardRecordedId = "";
	
	/**
	 * 
	 * @param tmpFileName
	 * @param question
	 * @param answer
	 * @param tags
	 */
	public CardMakerHandler(String tmpFileName, String question, String answer, String[] tags) {
		super();
		
		if(tmpFileName.equals("")) Controller.getUserInterface().printError("La variable tmpFileName n'est pas définie...");
		
		try {
			this.tmpFile = new PrintWriter(new FileWriter(tmpFileName));
		} catch(IOException e) {
			Controller.getUserInterface().printError("Impossible d'écrire dans le fichier " + tmpFileName);
			Controller.exit(32);
		}
		
		this.id = 0;
		this.question = question;
		this.answer = answer;
		this.tags = tags;
		this.questionFlag = false;
		this.answerFlag = false;
		this.tagFlag = false;
	}

	/**
	 * 
	 */
	public void startDocument() {
		this.tmpFile.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.tmpFile.println("<!DOCTYPE pkb [");
		this.tmpFile.println("    <!--- Knowledge Base -->");
		this.tmpFile.println("    <!ELEMENT pkb (card)*>");
		this.tmpFile.println("");
		this.tmpFile.println("    <!--- Card -->");
		this.tmpFile.println("    <!--- cdate (Format ISO 8601 : YYYY-MM-DD) -->");
		this.tmpFile.println("    <!ELEMENT card (question,answer?,(review|tag)*)>");
		this.tmpFile.println("    <!ATTLIST card id ID #REQUIRED>");
		this.tmpFile.println("    <!ATTLIST card cdate CDATA #REQUIRED>");
		this.tmpFile.println("");
		this.tmpFile.println("    <!--- Question -->");
		this.tmpFile.println("    <!ELEMENT question (#PCDATA)>");
		this.tmpFile.println("");
		this.tmpFile.println("    <!--- Answer -->");
		this.tmpFile.println("    <!ELEMENT answer (#PCDATA)>");
		this.tmpFile.println("");
		this.tmpFile.println("    <!--- Review -->");
		this.tmpFile.println("    <!--- rdate (Format ISO 8601 : YYYY-MM-DD) -->");
		this.tmpFile.println("    <!ELEMENT review EMPTY>");
		this.tmpFile.println("    <!ATTLIST review rdate CDATA #REQUIRED>");
		this.tmpFile.println("    <!ATTLIST review result (GOOD|BAD) #REQUIRED>");
		this.tmpFile.println("");
		this.tmpFile.println("    <!--- Tag -->");
		this.tmpFile.println("    <!ELEMENT tag (#PCDATA)>");
		this.tmpFile.println("]>");
//		this.tmpFile.println("<?xml-stylesheet type=\"text/css\" href=\"pkb.css\" ?>");
		this.tmpFile.println("");
		this.tmpFile.println("<!--");
		this.tmpFile.println("    Personal Knowledge Base version 1.0");
		this.tmpFile.println("    Copyright (c) 2007,2008 Jérémie DECOCK");
//		this.tmpFile.println("    Generator  : " + Controller.programName + " " + Controller.version + " (Java - DTD v3)");
		this.tmpFile.println("-->");
		this.tmpFile.println("");
	}

	/**
	 * 
	 */
	public void endDocument() {
		this.tmpFile.close();
	}

	/**
	 * 
	 */
	public void startElement(String uri, String name, String qName, Attributes atts) {
		if(uri.equals("")) {
			if(qName.equals("pkb")) {
				this.tmpFile.println("<pkb>");
			}
			else if(qName.equals("card")) {
				this.tmpFile.println("	<card id=\"" + atts.getValue("id") + "\" cdate=\"" + atts.getValue("cdate") + "\">");
				int cId = Integer.parseInt(atts.getValue("id").substring(1));
				if(cId > this.id) this.id = cId;
			}
			else if(qName.equals("question")) {
				this.questionFlag = true;
				this.tmpFile.print("		<question><![CDATA[");
			}
			else if(qName.equals("answer")) {
				this.answerFlag = true;
				this.tmpFile.print("		<answer><![CDATA[");
			}
			else if(qName.equals("review")) {
				this.tmpFile.println("		<review rdate=\"" + atts.getValue("rdate") + "\" result=\"" + atts.getValue("result") + "\" />");
			}
			else if(qName.equals("tag")) {
				this.tagFlag = true;
				this.tmpFile.print("		<tag>");
			}
		} else {
			if(name.equals("pkb")) {
				this.tmpFile.println("<pkb>");
			}
			else if(name.equals("card")) {
				this.tmpFile.println("	<card id=\"" + atts.getValue("id") + "\" cdate=\"" + atts.getValue("cdate") + "\">");
				int cId = Integer.parseInt(atts.getValue("id").substring(1));
				if(cId > this.id) this.id = cId;
			}
			else if(name.equals("question")) {
				this.questionFlag = true;
				this.tmpFile.print("		<question><![CDATA[");
			}
			else if(name.equals("answer")) {
				this.answerFlag = true;
				this.tmpFile.print("		<answer><![CDATA[");
			}
			else if(name.equals("review")) {
				this.tmpFile.println("		<review rdate=\"" + atts.getValue("rdate") + "\" result=\"" + atts.getValue("result") + "\" />");
			}
			else if(name.equals("tag")) {
				this.tagFlag = true;
				this.tmpFile.print("		<tag>");
			}
		}
	}

	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		if(uri.equals("")) {
			if(qName.equals("pkb")) {
				if(this.question.equals("")) {
					Controller.getUserInterface().printAlert("La question ne doit pas être vide");
				} else {
					GregorianCalendar gc = new GregorianCalendar();
					this.tmpFile.println("	<card id=\"c" + (this.id + 1) + "\" cdate=\"" + gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH) + "\">");
					this.tmpFile.println("		<question><![CDATA[" + this.question + "]]></question>");
					if(!this.answer.equals("")) this.tmpFile.println("		<answer><![CDATA[" + this.answer + "]]></answer>");
					for(int i=0 ; i < this.tags.length ; i++) {
						this.tmpFile.println("		<tag>" + this.tags[i] + "</tag>");
					}
					this.tmpFile.println("	</card>");
					
					// Set lastCardRecordedId (used by views)
					CardMakerHandler.lastCardRecordedId = "" + (this.id + 1);
				}
				this.tmpFile.println("</pkb>");
			}
			else if(qName.equals("card")) {
				this.tmpFile.println("	</card>");
			}
			else if(qName.equals("question")) {
				this.tmpFile.println("]]></question>");
				this.questionFlag = false;
			}
			else if(qName.equals("answer")) {
				this.tmpFile.println("]]></answer>");
				this.answerFlag = false;
			}
			else if(qName.equals("tag")) {
				this.tmpFile.println("</tag>");
				this.tagFlag = false;
			}
		} else {
			if(name.equals("pkb")) {
				if(this.question.equals("")) {
					Controller.getUserInterface().printAlert("La question ne doit pas être vide");
				} else {
					GregorianCalendar gc = new GregorianCalendar();
					this.tmpFile.println("	<card id=\"c" + (this.id + 1) + "\" cdate=\"" + gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH) + "\">");
					this.tmpFile.println("		<question><![CDATA[" + this.question + "]]></question>");
					if(!this.answer.equals("")) this.tmpFile.println("		<answer><![CDATA[" + this.answer + "]]></answer>");
					for(int i=0 ; i < this.tags.length ; i++) {
						this.tmpFile.println("		<tag>" + this.tags[i] + "</tag>");
					}
					this.tmpFile.println("	</card>");

					// Set lastCardRecordedId (used by views)
					CardMakerHandler.lastCardRecordedId = "" + (this.id + 1);
				}
				this.tmpFile.println("</pkb>");
			}
			else if(name.equals("card")) {
				this.tmpFile.println("	</card>");
			}
			else if(name.equals("question")) {
				this.tmpFile.println("]]></question>");
				this.questionFlag = false;
			}
			else if(name.equals("answer")) {
				this.tmpFile.println("]]></answer>");
				this.answerFlag = false;
			}
			else if(name.equals("tag")) {
				this.tmpFile.println("</tag>");
				this.tagFlag = false;
			}
		}
	}

	/**
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		if(this.questionFlag == true || this.answerFlag == true || this.tagFlag == true) {
			for (int i=start ; i<start+length ; i++) this.tmpFile.print(ch[i]);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLastCardRecordedId() {
		return CardMakerHandler.lastCardRecordedId;
	}
}
