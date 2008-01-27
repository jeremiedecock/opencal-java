/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.xml.maker.CardMakerHandler;
import org.jdhp.opencal.model.xml.reviewer.Card;
import org.jdhp.opencal.model.xml.reviewer.Pile;
import org.jdhp.opencal.model.xml.reviewer.ReviewHandler;
import org.jdhp.opencal.view.UserInterface;

public class Controller {
	
	private static UserInterface ui;
	
	// Reviewer
	public static Pile pile;
	public static Card card;
	
	/**
	 * Methode utilisé par le module CardMaker
	 * @param question
	 * @param answer
	 * @param tag
	 */
	public static void addCard(String question, String answer, String tags) {
		if(question.equals("")) {
			Controller.getUserInterface().printAlert("La question ne doit pas être vide");
		} else {
			try {
				
				// Crée le Handler
				XMLReader xr = XMLReaderFactory.createXMLReader();
				CardMakerHandler handler = new CardMakerHandler(OpenCAL.tmpDb, question, answer, tags);
				xr.setContentHandler(handler);
				xr.setErrorHandler(handler);
		
				// Parse the configuration file (config.xml) et crée tmpDB
				//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
				FileReader r = new FileReader(OpenCAL.cardDb);
				xr.parse(new InputSource(r));
				r.close();
				
				// Remplace cardDb par tmpDb
				File cardDbFile = new File(OpenCAL.cardDb);
				File tmpDbFile = new File(OpenCAL.tmpDb);
				boolean result = tmpDbFile.renameTo(cardDbFile);
				
				if(!result) {
					Controller.ui.printError("Impossible de renommer le fichier " + OpenCAL.tmpDb);
					Controller.exit(12);
				}
				
			} catch(SAXException e) {
				
				Controller.ui.printError(OpenCAL.cardDb + " n'est pas valide (SAXException)");
				Controller.exit(2);
				
			} catch(FileNotFoundException e) {
				
				// Le fichier cardDb n'existe pas, on va le créer
				Controller.ui.print("Le fichier " + OpenCAL.cardDb + " n'existe pas et va être créé.");
				try {
					PrintWriter file = new PrintWriter(OpenCAL.cardDb);
					file.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//					file.println("<!DOCTYPE card_db [");
//					file.println("    <!--- Card Database -->");
//					file.println("    <!ELEMENT card_db (card)*>");
//					file.println("");
//					file.println("    <!--- Card -->");
//					file.println("    <!--- cdate (Format ISO 8601 : YYYY-MM-DD) -->");
//					file.println("    <!--- <!ELEMENT card (question,answer?,review*,tag*)> -->");
//					file.println("    <!ELEMENT card (question,answer?,(review|tag)*)>");
//					file.println("    <!ATTLIST card id ID #REQUIRED>");
//					file.println("    <!ATTLIST card cdate CDATA #REQUIRED>");
//					file.println("");
//					file.println("    <!--- Question -->");
//					file.println("    <!ELEMENT question (#PCDATA)>");
//					file.println("");
//					file.println("    <!--- Answer -->");
//					file.println("    <!ELEMENT answer (#PCDATA)>");
//					file.println("");
//					file.println("    <!--- Review -->");
//					file.println("    <!--- rdate (Format ISO 8601 : YYYY-MM-DD) -->");
//					file.println("    <!ELEMENT review EMPTY>");
//					file.println("    <!ATTLIST review rdate CDATA #REQUIRED>");
//					file.println("    <!ATTLIST review result (GOOD|BAD) #REQUIRED>");
//					file.println("");
//					file.println("    <!--- Tag -->");
//					file.println("    <!ELEMENT tag (#PCDATA)>");
//					file.println("]>");
//					file.println("<?xml-stylesheet type=\"text/css\" href=\"card_db.css\" ?>");
					file.println("");
					file.println("<!--");
					file.println("    Document   : card_db.xml");
					file.println("    Created on : " + (new Date()).toString());
					file.println("    Author     : Jérémie DECOCK");
//					file.println("    Generator  : " + Controller.programName + " " + Controller.version + " (Java - DTD v3)");
//					file.println("    Description: " + Controller.programName + "'s flashcards.");
					file.println("-->");
					file.println("");
					
					// Ajout de la nouvelle carte
					GregorianCalendar gc = new GregorianCalendar();
					file.println("<card_db>");
					file.println("	<card id=\"c1\" cdate=\"" + gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH) + "\">");
					file.println("		<question><![CDATA[" + question + "]]></question>");
					if(!answer.equals("")) file.println("		<answer><![CDATA[" + answer + "]]></answer>");
					if(!tags.equals("")) file.println("		<tag>" + tags + "</tag>");
					file.println("	</card>");
					file.println("</card_db>");
					
					file.close();
				} catch(FileNotFoundException e2) {
					Controller.ui.printError(OpenCAL.cardDb + " ne peut pas être créé (FileNotFoundException)");
					Controller.exit(2);
				}
			} catch(IOException e) {
				Controller.ui.printError(OpenCAL.cardDb + " est illisible (IOException)");
				Controller.exit(2);
			}
		}
	}
	
	public static void updateCard(String result) {
		// Write answer and date into xml file
		try {
			// Crée le Handler
			XMLReader xr = XMLReaderFactory.createXMLReader();
			ReviewHandler handler = new ReviewHandler(OpenCAL.tmpDb, Controller.card.getId(), result);
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
	
			// Parse the configuration file (config.xml) et crée tmpDB
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.cardDb);
			xr.parse(new InputSource(r));
			r.close();
			
			// Remplace cardDb par tmpDb
			File cardDbFile = new File(OpenCAL.cardDb);
			File tmpDbFile = new File(OpenCAL.tmpDb);
			boolean renameSuccess = tmpDbFile.renameTo(cardDbFile);
			
			if(!renameSuccess) {
				Controller.ui.printError("Impossible de renommer le fichier " + OpenCAL.tmpDb);
				Controller.exit(12);
			}
		} catch(SAXException e) {
			Controller.ui.printError(OpenCAL.cardDb + " n'est pas valide (SAXException)");
			Controller.exit(2);
		} catch(FileNotFoundException e) {
			Controller.ui.print(OpenCAL.cardDb + " est introuvable (FileNotFoundException)");
			Controller.exit(2);
		} catch(IOException e) {
			Controller.ui.printError(OpenCAL.cardDb + " est illisible (IOException)");
			Controller.exit(2);
		}

		Controller.pile.incrementReviewedCards();
		Controller.pile.removePointedCard();
	}
	
	/**
	 * 
	 * @param status
	 */
	public static void exit(int status) {
		System.exit(status);
	}
	
	/**
	 * 
	 * @return
	 */
	public static UserInterface getUserInterface() {
		return Controller.ui;
	}

	public static void setUserInterface(UserInterface ui) {
		Controller.ui = ui;
	}
}
