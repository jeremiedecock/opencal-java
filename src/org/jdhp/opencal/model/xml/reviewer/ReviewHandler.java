/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.reviewer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdhp.opencal.OpenCAL;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewHandler extends DefaultHandler {
	
	private PrintWriter newFile;
	
	private String idCard;
	
	private String result;
	
	private boolean questionFlag;
	
	private boolean answerFlag;
	
	private boolean tagFlag;
	
	private boolean reviewedCardFlag;
	
	private SimpleDateFormat iso8601Formatter;
	
	/**
	 * 
	 * @param file
	 * @param idCard
	 * @param result
	 */
	public ReviewHandler(String file, String idCard, String result) {
		super();
		
		if(file.equals("")) OpenCAL.MainWindow.printError("La variable file n'est pas définie...");
		
		try {
			this.newFile = new PrintWriter(new FileWriter(file));
		} catch(IOException e) {
			OpenCAL.MainWindow.printError("Impossible d'écrire dans le fichier " + file);
			OpenCAL.exit(32);
		}

		this.idCard = idCard;
		this.result = result;

		this.questionFlag = false;
		this.answerFlag = false;
		this.tagFlag = false;
		this.reviewedCardFlag = false;
		
		this.iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	/**
	 * 
	 */
	public void startDocument() {
		this.newFile.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.newFile.println("<!DOCTYPE pkb [");
		this.newFile.println("    <!--- Knowledge Base -->");
		this.newFile.println("    <!ELEMENT pkb (card)*>");
		this.newFile.println("");
		this.newFile.println("    <!--- Card -->");
		this.newFile.println("    <!--- cdate (Format ISO 8601 : YYYY-MM-DD) -->");
		this.newFile.println("    <!ELEMENT card (question,answer?,(review|tag)*)>");
		this.newFile.println("    <!ATTLIST card id ID #REQUIRED>");
		this.newFile.println("    <!ATTLIST card cdate CDATA #REQUIRED>");
		this.newFile.println("");
		this.newFile.println("    <!--- Question -->");
		this.newFile.println("    <!ELEMENT question (#PCDATA)>");
		this.newFile.println("");
		this.newFile.println("    <!--- Answer -->");
		this.newFile.println("    <!ELEMENT answer (#PCDATA)>");
		this.newFile.println("");
		this.newFile.println("    <!--- Review -->");
		this.newFile.println("    <!--- rdate (Format ISO 8601 : YYYY-MM-DD) -->");
		this.newFile.println("    <!ELEMENT review EMPTY>");
		this.newFile.println("    <!ATTLIST review rdate CDATA #REQUIRED>");
		this.newFile.println("    <!ATTLIST review result (good|bad) #REQUIRED>");
		this.newFile.println("");
		this.newFile.println("    <!--- Tag -->");
		this.newFile.println("    <!ELEMENT tag (#PCDATA)>");
		this.newFile.println("]>");
//		this.newFile.println("<?xml-stylesheet type=\"text/css\" href=\"pkb.css\" ?>");
		this.newFile.println("");
		this.newFile.println("<!--");
		this.newFile.println("    Personal Knowledge Base version 1.0");
		this.newFile.println("    Copyright (c) 2007,2008 Jérémie DECOCK");
//		this.newFile.println("    Generator  : " + OpenCAL.programName + " " + OpenCAL.version + " (Java - DTD v3)");
		this.newFile.println("-->");
		this.newFile.println("");
	}
	
	/**
	 * 
	 */
	public void startElement(String uri, String name, String qName, Attributes atts) {
		if(uri.equals("")) {
			if(qName.equals("pkb")) {
				this.newFile.println("<pkb>");
			}
			else if(qName.equals("card")) {
				this.newFile.println("	<card id=\"" + atts.getValue("id") + "\" cdate=\"" + atts.getValue("cdate") + "\">");
				if(atts.getValue("id").equals(this.idCard)) this.reviewedCardFlag = true;
			}
			else if(qName.equals("question")) {
				this.questionFlag = true;
				this.newFile.print("		<question><![CDATA[");
			}
			else if(qName.equals("answer")) {
				this.answerFlag = true;
				this.newFile.print("		<answer><![CDATA[");
			}
			else if(qName.equals("review")) {
				this.newFile.println("		<review rdate=\"" + atts.getValue("rdate") + "\" result=\"" + atts.getValue("result") + "\" />");
			}
			else if(qName.equals("tag")) {
				this.tagFlag = true;
				this.newFile.print("		<tag>");
			}
		} else {
			if(name.equals("pkb")) {
				this.newFile.println("<pkb>");
			}
			else if(name.equals("card")) {
				this.newFile.println("	<card id=\"" + atts.getValue("id") + "\" cdate=\"" + atts.getValue("cdate") + "\">");
				if(atts.getValue("id").equals(this.idCard)) this.reviewedCardFlag = true;
			}
			else if(name.equals("question")) {
				this.questionFlag = true;
				this.newFile.print("		<question><![CDATA[");
			}
			else if(name.equals("answer")) {
				this.answerFlag = true;
				this.newFile.print("		<answer><![CDATA[");
			}
			else if(name.equals("review")) {
				this.newFile.println("		<review rdate=\"" + atts.getValue("rdate") + "\" result=\"" + atts.getValue("result") + "\" />");
			}
			else if(name.equals("tag")) {
				this.tagFlag = true;
				this.newFile.print("		<tag>");
			}
		}
	}

	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		if(uri.equals("")) {
			if(qName.equals("pkb")) {
				this.newFile.println("</pkb>");
			}
			else if(qName.equals("card")) {
				if(this.reviewedCardFlag == true) {
					this.newFile.println("		<review rdate=\"" + this.iso8601Formatter.format(new Date()) + "\" result=\"" + this.result + "\" />");
					this.reviewedCardFlag = false;
				}
				this.newFile.println("	</card>");
			}
			else if(qName.equals("question")) {
				this.newFile.println("]]></question>");
				this.questionFlag = false;
			}
			else if(qName.equals("answer")) {
				this.newFile.println("]]></answer>");
				this.answerFlag = false;
			}
			else if(qName.equals("tag")) {
				this.newFile.println("</tag>");
				this.tagFlag = false;
			}
		} else {
			if(name.equals("pkb")) {
				this.newFile.println("</pkb>");
			}
			else if(name.equals("card")) {
				if(this.reviewedCardFlag == true) {
					this.newFile.println("		<review rdate=\"" + this.iso8601Formatter.format(new Date()) + "\" result=\"" + this.result + "\" />");
					this.reviewedCardFlag = false;
				}
				this.newFile.println("	</card>");
			}
			else if(name.equals("question")) {
				this.newFile.println("]]></question>");
				this.questionFlag = false;
			}
			else if(name.equals("answer")) {
				this.newFile.println("]]></answer>");
				this.answerFlag = false;
			}
			else if(name.equals("tag")) {
				this.newFile.println("</tag>");
				this.tagFlag = false;
			}
		}
	}

	/**
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		if(this.questionFlag == true || this.answerFlag == true || this.tagFlag == true) {
			for (int i=start ; i<start+length ; i++) this.newFile.print(ch[i]);
		}
	}
	
	/**
	 * 
	 */
	public void endDocument() {
		this.newFile.close();
	}
}
