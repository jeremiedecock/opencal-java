/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.usecase.explore.MadeCardsController;
import org.jdhp.opencal.usecase.explore.ReviewedCardsController;
import org.jdhp.opencal.usecase.getstats.StatsController;
import org.jdhp.opencal.usecase.make.MakeController;
import org.jdhp.opencal.usecase.review.ReviewController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class OpenCAL {

	public final static String programVersion = "3.0";
	
	public final static String programName = "OpenCAL";
	
	public final static String pkbFilePath = "/home/gremy/user_dev.pkb";
	
	public final static String tmpPkbFile = "/tmp/" + OpenCAL.programName + "_user.tmp.pkb";
	
	public static MainWindow MainWindow;
	
	public static Document domDocument;
	
	public static SimpleDateFormat iso8601Formatter;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OpenCAL.iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		// Build the XML DOM tree
		try {
			File pkbFile = new File(OpenCAL.pkbFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			OpenCAL.domDocument = db.parse(pkbFile);
		} catch(SAXException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.MainWindow.print(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.MainWindow.printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			OpenCAL.exit(2);
		} catch(ParserConfigurationException e) {
			OpenCAL.MainWindow.printError("The XML parser was not configured (ParserConfigurationException)");
			OpenCAL.exit(2);
		}
		
		// Misc init
		OpenCAL.init();
		OpenCAL.MainWindow = new MainWindow();
		OpenCAL.MainWindow.run();
	}

	/**
	 * Initialize all controllers
	 */
	public static void init() {
		MakeController.init();
		ReviewController.init();
		MadeCardsController.init();
		ReviewedCardsController.init();
	}
		
	/**
	 * Quit the program
	 * 
	 * @param status
	 */
	public static void exit(int status) {
		System.exit(status);
	}

}
