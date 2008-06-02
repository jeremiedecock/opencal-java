/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.controller.reviewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.model.xml.reviewer.Card;
import org.jdhp.opencal.model.xml.reviewer.RevisionPile;
import org.jdhp.opencal.model.xml.reviewer.ReviewHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewController {

	public static RevisionPile revisionPile;
	
	public static Card card;
	
	/**
	 * 
	 */
	public static void init() {
		ReviewController.revisionPile = new RevisionPile();
		ReviewController.card = ReviewController.revisionPile.getPointedCard();
	}
	
	/**
	 * 
	 * @param result
	 */
	public static void updateCard(String result) {
		// Write answer and date into xml file
		try {
			// Create the Handler
			XMLReader xr = XMLReaderFactory.createXMLReader();
			ReviewHandler handler = new ReviewHandler(OpenCAL.tmpPkbFile, ReviewController.card.getId(), result);
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
	
			// Parse the configuration file (config.xml) and create tmpDB
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.pkbFile);
			xr.parse(new InputSource(r));
			r.close();
			
			// Remplace pkbFile par tmpPkbFile
			File cardDbFile = new File(OpenCAL.pkbFile);
			File tmpDbFile = new File(OpenCAL.tmpPkbFile);
			boolean renameSuccess = tmpDbFile.renameTo(cardDbFile);
			
			if(!renameSuccess) {
				Controller.getUserInterface().printError("Impossible de renommer le fichier " + OpenCAL.tmpPkbFile);
				Controller.exit(12);
			}
		} catch(SAXException e) {
			Controller.getUserInterface().printError(OpenCAL.pkbFile + " n'est pas valide (SAXException)");
			Controller.exit(2);
		} catch(FileNotFoundException e) {
			Controller.getUserInterface().print(OpenCAL.pkbFile + " est introuvable (FileNotFoundException)");
			Controller.exit(2);
		} catch(IOException e) {
			Controller.getUserInterface().printError(OpenCAL.pkbFile + " est illisible (IOException)");
			Controller.exit(2);
		}

		ReviewController.revisionPile.incrementReviewedCards();
		ReviewController.revisionPile.removePointedCard();
	}
}
