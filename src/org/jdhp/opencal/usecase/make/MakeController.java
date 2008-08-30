/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.make;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.xml.maker.CardMakerHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MakeController {

	/**
	 * 
	 */
	public static void init() {
		// void
	}
	
	/**
	 * 
	 * @param question
	 * @param answer
	 * @param tag
	 */
	public static void addCard(String question, String answer, String tagString) {

		// Get tags
		String[] tagArray = tagString.split("\n");
		
		if(question.equals("")) {
			OpenCAL.mainWindow.printAlert("La question ne doit pas être vide");
		} else {
			try {
				// Crée le Handler
				XMLReader xr = XMLReaderFactory.createXMLReader();
				CardMakerHandler handler = new CardMakerHandler(OpenCAL.TMP_PKB_FILE, question, answer, tagArray);
				xr.setContentHandler(handler);
				xr.setErrorHandler(handler);
		
				// Parse the configuration file (config.xml) et crée tmpDB
				//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
				FileReader r = new FileReader(OpenCAL.PKB_FILE_PATH);
				xr.parse(new InputSource(r));
				r.close();
				
				// Remplace PKB_FILE_PATH par TMP_PKB_FILE
				File cardDbFile = new File(OpenCAL.PKB_FILE_PATH);
				File tmpDbFile = new File(OpenCAL.TMP_PKB_FILE);
				boolean result = tmpDbFile.renameTo(cardDbFile);
				
				if(!result) {
					OpenCAL.mainWindow.printError("Impossible de renommer le fichier " + OpenCAL.TMP_PKB_FILE);
					OpenCAL.exit(12);
				}
				
			} catch(SAXException e) {
				
				OpenCAL.mainWindow.printError(OpenCAL.PKB_FILE_PATH + " n'est pas valide (SAXException)");
				OpenCAL.exit(2);
				
			} catch(FileNotFoundException e) {
				
				// Le fichier PKB_FILE_PATH n'existe pas, on va le créer
				OpenCAL.mainWindow.print("Le fichier " + OpenCAL.PKB_FILE_PATH + " n'existe pas et va être créé.");
				try {
					PrintWriter file = new PrintWriter(OpenCAL.PKB_FILE_PATH);
					file.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					file.println("<!DOCTYPE pkb [");
					file.println("    <!--- Knowledge Base -->");
					file.println("    <!ELEMENT pkb (card)*>");
					file.println("");
					file.println("    <!--- Card -->");
					file.println("    <!--- cdate (Format ISO 8601 : YYYY-MM-DD) -->");
					file.println("    <!ELEMENT card (question,answer?,(review|tag)*)>");
					file.println("    <!ATTLIST card id ID #REQUIRED>");
					file.println("    <!ATTLIST card cdate CDATA #REQUIRED>");
					file.println("");
					file.println("    <!--- Question -->");
					file.println("    <!ELEMENT question (#PCDATA)>");
					file.println("");
					file.println("    <!--- Answer -->");
					file.println("    <!ELEMENT answer (#PCDATA)>");
					file.println("");
					file.println("    <!--- Review -->");
					file.println("    <!--- rdate (Format ISO 8601 : YYYY-MM-DD) -->");
					file.println("    <!ELEMENT review EMPTY>");
					file.println("    <!ATTLIST review rdate CDATA #REQUIRED>");
					file.println("    <!ATTLIST review result (good|bad) #REQUIRED>");
					file.println("");
					file.println("    <!--- Tag -->");
					file.println("    <!ELEMENT tag (#PCDATA)>");
					file.println("]>");
//					file.println("<?xml-stylesheet type=\"text/css\" href=\"pkb.css\" ?>");
					file.println("");
					file.println("<!--");
					file.println("    Personal Knowledge Base version 1.0");
					file.println("    Copyright (c) 2007,2008 Jérémie DECOCK");
//					file.println("    Generator  : " + OpenCAL.PROGRAM_NAME + " " + OpenCAL.version + " (Java - DTD v3)");
					file.println("-->");
					file.println("");
					
					// Ajout de la nouvelle carte
					GregorianCalendar gc = new GregorianCalendar();
					file.println("<pkb>");
					file.println("	<card id=\"c1\" cdate=\"" + gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH) + "\">");
					file.println("		<question><![CDATA[" + question + "]]></question>");
					if(!answer.equals("")) file.println("		<answer><![CDATA[" + answer + "]]></answer>");
					for(int i=0 ; i < tagArray.length ; i++) {
						file.println("		<tag>" + tagArray[i] + "</tag>");
					}
					file.println("	</card>");
					file.println("</pkb>");
					
					file.close();
				} catch(FileNotFoundException e2) {
					OpenCAL.mainWindow.printError(OpenCAL.PKB_FILE_PATH + " ne peut pas être créé (FileNotFoundException)");
					OpenCAL.exit(2);
				}
			} catch(IOException e) {
				OpenCAL.mainWindow.printError(OpenCAL.PKB_FILE_PATH + " est illisible (IOException)");
				OpenCAL.exit(2);
			}
		}
	}
}