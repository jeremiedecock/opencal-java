/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.stats;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.controller.Controller;
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
public class RevisionStatsHandler extends DefaultHandler {

	private TreeMap<Date, Integer> revisionStats = null;
	
	/**
	 * 
	 */
	public static TreeMap<Date, Integer> getRevisionStats() {
		// Parse le document XML avec SAX et crée le tableau
		// TODO : en plus de vérifier si le doc xml est bien formé, vérifier si il est conforme à la DTD !
		
		RevisionStatsHandler handler = new RevisionStatsHandler();

		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.pkbFile);
			xr.parse(new InputSource(r));
			r.close();
		} catch(SAXException e) {
			Controller.getUserInterface().printError(OpenCAL.pkbFile + " n'est pas valide (SAXException)");
			Controller.exit(2);
		} catch(FileNotFoundException e) {
			Controller.getUserInterface().printError(OpenCAL.pkbFile + " est introuvable (FileNotFoundException)");
			Controller.exit(2);
		} catch(IOException e) {
			Controller.getUserInterface().printError(OpenCAL.pkbFile + " est illisible (IOException)");
			Controller.exit(2);
		}
		
		return handler.revisionStats;
	}
	
	/**
	 * 
	 * @param revisionPile
	 */
	public RevisionStatsHandler() {
		super();
		
		this.revisionStats = new TreeMap<Date, Integer>();
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
		// void
	}
	
	/**
	 * 
	 */
	public void startElement(String uri, String name, String qName, Attributes atts) {
		if(uri.equals("")) {
			if(qName.equals("review")) {
				// Les dates sont au format ISO 8601 (YYYY-MM-DD)
				String[] date = atts.getValue("rdate").split("-",3);
				// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
				Date cdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				
				if(this.revisionStats.containsKey(cdate)) {
					this.revisionStats.put(cdate, new Integer(this.revisionStats.get(cdate).intValue() + 1));
				} else {
					this.revisionStats.put(cdate, new Integer(1));
				}
			}
		} else {
			if(name.equals("review")) {
				// Les dates sont au format ISO 8601 (YYYY-MM-DD)
				String[] date = atts.getValue("rdate").split("-",3);
				// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
				Date cdate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
				
				if(this.revisionStats.containsKey(cdate)) {
					this.revisionStats.put(cdate, new Integer(this.revisionStats.get(cdate).intValue() + 1));
				} else {
					this.revisionStats.put(cdate, new Integer(1));
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
