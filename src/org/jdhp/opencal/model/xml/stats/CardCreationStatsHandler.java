/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.stats;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.model.xml.reviewer.CardHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardCreationStatsHandler extends DefaultHandler {

	private HashMap<String, Integer> cardCreationStats;
	
	/**
	 * 
	 */
	public static void getCardCreationStats() {
		// Parse le document XML avec SAX et crée le tableau
		// TODO : en plus de vérifier si le doc xml est bien formé, vérifier si il est conforme à la DTD !
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			CardCreationStatsHandler handler = new CardCreationStatsHandler();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.cardDb);
			xr.parse(new InputSource(r));
			r.close();
		} catch(SAXException e) {
			Controller.getUserInterface().printError(OpenCAL.cardDb + " n'est pas valide (SAXException)");
			Controller.exit(2);
		} catch(FileNotFoundException e) {
			Controller.getUserInterface().printError(OpenCAL.cardDb + " est introuvable (FileNotFoundException)");
			Controller.exit(2);
		} catch(IOException e) {
			Controller.getUserInterface().printError(OpenCAL.cardDb + " est illisible (IOException)");
			Controller.exit(2);
		}
	}
	
	/**
	 * 
	 * @param revisionPile
	 */
	public CardCreationStatsHandler() {
		super();
		
		this.cardCreationStats = new HashMap<String, Integer>();
	}
	
	/**
	 * 
	 */
	public void startDocument() {
		// void
	}
	
	/**
	 * 
	 */
	public void endDocument() {
		System.out.println(this.cardCreationStats);
	}
	
	/**
	 * 
	 */
	public void startElement(String uri, String name, String qName, Attributes atts) {
		if(uri.equals("")) {
			if(qName.equals("card")) {
				String cdate = atts.getValue("cdate");
				if(this.cardCreationStats.containsKey(cdate)) {
					this.cardCreationStats.put(cdate, new Integer(this.cardCreationStats.get(cdate).intValue() + 1));
				} else {
					this.cardCreationStats.put(cdate, new Integer(1));
				}
			}
		} else {
			if(name.equals("card")) {
				String cdate = atts.getValue("cdate");
				if(this.cardCreationStats.containsKey(cdate)) {
					this.cardCreationStats.put(cdate, new Integer(this.cardCreationStats.get(cdate).intValue() + 1));
				} else {
					this.cardCreationStats.put(cdate, new Integer(1));
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		// void
	}
	
	/**
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		// void
	}

}
