/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.review;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jdhp.opencal.OpenCAL;
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
			FileReader r = new FileReader(OpenCAL.pkbFilePath);
			xr.parse(new InputSource(r));
			r.close();
			
			// Remplace pkbFilePath par tmpPkbFile
			File cardDbFile = new File(OpenCAL.pkbFilePath);
			File tmpDbFile = new File(OpenCAL.tmpPkbFile);
			boolean renameSuccess = tmpDbFile.renameTo(cardDbFile);
			
			if(!renameSuccess) {
				OpenCAL.MainWindow.printError("Impossible de renommer le fichier " + OpenCAL.tmpPkbFile);
				OpenCAL.exit(12);
			}
		} catch(SAXException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.MainWindow.print(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			OpenCAL.exit(2);
		}

		ReviewController.revisionPile.incrementReviewedCards();
		ReviewController.revisionPile.removePointedCard();
	}
}
