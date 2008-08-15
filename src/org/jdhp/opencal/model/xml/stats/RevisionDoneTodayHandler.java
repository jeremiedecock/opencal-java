/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.stats;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
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
public class RevisionDoneTodayHandler extends DefaultHandler {

	private int revisionDoneToday;
	
	private String todayString;
	
	/**
	 * 
	 */
	public static int getRevisionDoneToday() {
		// Parse le document XML avec SAX et crée le tableau
		// TODO : en plus de vérifier si le doc xml est bien formé, vérifier si il est conforme à la DTD !
		
		RevisionDoneTodayHandler handler = new RevisionDoneTodayHandler();

		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.pkbFilePath);
			xr.parse(new InputSource(r));
			r.close();
		} catch(SAXException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			OpenCAL.exit(2);
		}
		
		return handler.revisionDoneToday;
	}
	
	/**
	 * 
	 * @param revisionPile
	 */
	public RevisionDoneTodayHandler() {
		super();
		
		this.revisionDoneToday = 0;
		GregorianCalendar today = new GregorianCalendar();
		this.todayString = today.get(GregorianCalendar.YEAR) + "-" + (today.get(GregorianCalendar.MONTH) + 1) + "-" + today.get(GregorianCalendar.DAY_OF_MONTH);
//		SimpleDateFormat iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");  // ne marche pas car affiche "2008-05-10" au lieu de "2008-5-10"
//		this.todayString = iso8601Formatter.format(new Date());
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
				if(atts.getValue("rdate").equals(this.todayString)) {
					this.revisionDoneToday++;
				}
			}
		} else {
			if(name.equals("review")) {
				if(atts.getValue("rdate").equals(this.todayString)) {
					this.revisionDoneToday++;
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
