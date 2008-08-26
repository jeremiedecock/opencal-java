/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.model.xml.explorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class MadeCardsHandler extends DefaultHandler {

	private MadeCardsPile madeCardsPile;
	
	private String todayString;
	
	private String question;
	
	private String answer;
	
	private String tags;
	
	private boolean madeCardFlag;
	
	private boolean questionFlag;
	
	private boolean answerFlag;
	
	private boolean tagsFlag;
	
	private SimpleDateFormat iso8601Formatter;
	
	/**
	 * 
	 */
	public static void initMadeCardsPile(MadeCardsPile madeCardsPile) {
		// Parse le document XML avec SAX et crée le tableau
		// TODO : en plus de vérifier si le doc xml est bien formé, vérifier si il est conforme à la DTD !
		
		MadeCardsHandler handler = new MadeCardsHandler();
		
		handler.madeCardsPile = madeCardsPile;

		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.pkbFilePath);
			xr.parse(new InputSource(r));
			r.close();
		} catch(SAXException e) {
			OpenCAL.mainWindow.printError(OpenCAL.pkbFilePath + " n'est pas valide (SAXException)");
			OpenCAL.exit(2);
		} catch(FileNotFoundException e) {
			OpenCAL.mainWindow.printError(OpenCAL.pkbFilePath + " est introuvable (FileNotFoundException)");
			OpenCAL.exit(2);
		} catch(IOException e) {
			OpenCAL.mainWindow.printError(OpenCAL.pkbFilePath + " est illisible (IOException)");
			OpenCAL.exit(2);
		}
	}
	
	/**
	 * 
	 * @param cardManipulator
	 */
	public MadeCardsHandler() {
		super();
		
		this.iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd");
		this.todayString = this.iso8601Formatter.format(new Date());
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
			if(qName.equals("card")) {
				if(atts.getValue("cdate").equals(this.todayString)) this.madeCardFlag = true;
				this.question = "";
				this.answer = "";
				this.tags = "";
			}
			else if(qName.equals("question")) {
				this.questionFlag = true;
			}
			else if(qName.equals("answer")) {
				this.answerFlag = true;
			}
			else if(qName.equals("tag")) {
				this.tagsFlag = true;
				if(!this.tags.equals("")) this.tags += "\n";
			}
		} else {
			if(name.equals("card")) {
				if(atts.getValue("cdate").equals(this.todayString)) this.madeCardFlag = true;
				this.question = "";
				this.answer = "";
				this.tags = "";
			}
			else if(name.equals("question")) {
				this.questionFlag = true;
			}
			else if(name.equals("answer")) {
				this.answerFlag = true;
			}
			else if(name.equals("tag")) {
				this.tagsFlag = true;
				if(!this.tags.equals("")) this.tags += "\n";
			}
		}
	}
	
	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		if(uri.equals("")) {
			if(qName.equals("card") && this.madeCardFlag) {
				this.madeCardsPile.addCard(new Card(this.question, this.answer, this.tags, ""));
				this.madeCardFlag = false;
			}
			else if(qName.equals("question")) {
				this.questionFlag = false;
			}
			else if(qName.equals("answer")) {
				this.answerFlag = false;
			}
			else if(qName.equals("tag")) {
				this.tagsFlag = false;
			}
		} else {
			if(name.equals("card") && this.madeCardFlag) {
				this.madeCardsPile.addCard(new Card(this.question, this.answer, this.tags, ""));
				this.madeCardFlag = false;
			}
			else if(name.equals("question")) {
				this.questionFlag = false;
			}
			else if(name.equals("answer")) {
				this.answerFlag = false;
			}
			else if(name.equals("tag")) {
				this.tagsFlag = false;
			}
		}
	}
	
	/**
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		if(this.questionFlag == true) {
			for (int i=start ; i<start+length ; i++) this.question += ch[i];
		}
		else if(this.answerFlag == true) {
			for (int i=start ; i<start+length ; i++) this.answer += ch[i];
		}
		else if(this.tagsFlag == true) {
			for (int i=start ; i<start+length ; i++) this.tags += ch[i];
		}
	}

}
