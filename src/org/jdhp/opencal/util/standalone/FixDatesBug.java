/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.util.standalone;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Properties;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class FixDatesBug {

	public final static String RIGHT_ANSWER_STRING = "good";

	public static File pkbFile;

	public static Document domDocument;

	public static void main(String[] args) {
		// Get properties
		Properties userProperties = new Properties();
		try {
			FileInputStream userPropertiesFile = new FileInputStream("/home/gremy/.opencal/opencal.properties");
			userProperties.load(userPropertiesFile);
			userPropertiesFile.close();
		} catch(Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		// Get pkb file path
		String pkbFilePath = userProperties.getProperty("pkb.path");
		pkbFile = new File(pkbFilePath);

		// Build the XML DOM tree
		domDocument = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			domDocument = db.parse(pkbFile);
		} catch(Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		// Make lists
		NodeList nodeCards = domDocument.getElementsByTagName("card");

		// Check each cards
		int brokenElementCpt = 0;
		int unsortedCardsCpt = 0;
		for(int c=0 ; c<nodeCards.getLength() ; c++) {
			Element card = (Element) nodeCards.item(c);

			int grade = 0;
			boolean isBroken = false;
			
			GregorianCalendar cdate = FixDatesBug.iso8601ToCalendar(card.getAttribute("cdate"));
			GregorianCalendar expectedRevisionDate = getExpectedRevisionDate(cdate, grade);
			
			NodeList reviewList = card.getElementsByTagName("review");
			if(!isSorted(reviewList)) unsortedCardsCpt++;
			
			// Check each reviews for the current card
			for(int r=0 ; r < reviewList.getLength() ; r++) {
				GregorianCalendar rdate = FixDatesBug.iso8601ToCalendar(((Element) reviewList.item(r)).getAttribute("rdate"));
				String result = ((Element) reviewList.item(r)).getAttribute("result"); 
				
				// If the element is broken
				if(rdate.before(expectedRevisionDate)) {
					brokenElementCpt++;

					if(!isBroken) {
						isBroken = true;
						System.out.println("***********************************************************************\n");

						String question = "";
						NodeList nodeQuestion = card.getElementsByTagName("question");
						if(nodeQuestion.getLength() != 0) {
							Element questionElement = (Element) nodeQuestion.item(0);
							question = questionElement.getTextContent();
						}
						System.out.println("Broken card (" + brokenElementCpt + ") :\n\n" + question + "\n");
					}

					for(int rp=0 ; rp < r ; rp++) {
						GregorianCalendar rpdate = FixDatesBug.iso8601ToCalendar(((Element) reviewList.item(rp)).getAttribute("rdate"));
						String rpresult = ((Element) reviewList.item(rp)).getAttribute("result"); 
						System.out.print("  " + rp + ". " + FixDatesBug.calendarToIso8601(rpdate));
						if(rpresult.equals(RIGHT_ANSWER_STRING)) {
							System.out.println(" +");
						} else {
							System.out.println(" -");
						}
					}
					System.out.print("     " + FixDatesBug.calendarToIso8601(rdate));
					if(result.equals(RIGHT_ANSWER_STRING)) {
						System.out.print(" +");
					} else {
						System.out.print(" -");
					}
					System.out.println("  <  " + FixDatesBug.calendarToIso8601(expectedRevisionDate));

					System.out.print("Fix this element ? (y/n/d/q) : ");
					String ans = readString();

					if(ans.equals("y")) {
						((Element) reviewList.item(r)).setAttribute("rdate", FixDatesBug.calendarToIso8601(expectedRevisionDate));
						System.out.println("fixed\n");

						rdate = (GregorianCalendar) expectedRevisionDate.clone();

						if(result.equals(RIGHT_ANSWER_STRING)) {
							grade++;
							expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
						} else {
							grade = 0;
							expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
						}
					} else if(ans.equals("n")) {
						System.out.println("left\n");
						if(!result.equals(RIGHT_ANSWER_STRING)) {
							grade = 0;
							expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
						}
					} else if(ans.equals("d")) {
						// TODO
						card.removeChild(reviewList.item(r));
						reviewList = card.getElementsByTagName("review");

						System.out.println("deleted\n");
					} else if(ans.equals("q")) {
						FixDatesBug.quit();
					}
				} else {
					if(result.equals(RIGHT_ANSWER_STRING)) {
						grade++;
						expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
					} else {
						grade = 0;
						expectedRevisionDate = getExpectedRevisionDate(rdate, grade);
					}
				}
			}
			
			if(isBroken) {
				for(int r=0 ; r < reviewList.getLength() ; r++) {
					GregorianCalendar rdate = FixDatesBug.iso8601ToCalendar(((Element) reviewList.item(r)).getAttribute("rdate"));
					String result = ((Element) reviewList.item(r)).getAttribute("result"); 

					System.out.print(" > " + r + ". " + FixDatesBug.calendarToIso8601(rdate));
					if(result.equals(RIGHT_ANSWER_STRING)) {
						System.out.println(" +");
					} else {
						System.out.println(" -");
					}
				}
				System.out.println("");
			}
		}

		System.out.println(nodeCards.getLength() + " cards registred");
		System.out.println(unsortedCardsCpt + " unsorted cards");
		System.out.println(brokenElementCpt + " boken elements detected");

		FixDatesBug.quit();
	}
	
	/**
	 * 
	 * @param reviewList
	 * @return
	 */
	public static boolean isSorted(NodeList reviewList) {
		boolean isSorted = true;
		
		if(reviewList.getLength()>1) {
			GregorianCalendar lastDate = FixDatesBug.iso8601ToCalendar(((Element) reviewList.item(0)).getAttribute("rdate"));
			
			int i = 1;
			while(i < reviewList.getLength() && isSorted) {
				GregorianCalendar rdate = FixDatesBug.iso8601ToCalendar(((Element) reviewList.item(i)).getAttribute("rdate"));
				
				if(rdate.before(lastDate)) isSorted = false;
				
				lastDate = rdate;
				i++;
			}
		}
		
		return isSorted;
	}
	
	/**
	 * Get the expected (next) revision date knowing the last revision date and the grade.
	 * 
	 * @param lastRevisionDate
	 * @param grade
	 * @return
	 */
	public static GregorianCalendar getExpectedRevisionDate(GregorianCalendar lastRevisionDate, int grade) {
		GregorianCalendar expectedRevisionDate = (GregorianCalendar) lastRevisionDate.clone();
		expectedRevisionDate.add(Calendar.DAY_OF_MONTH, deltaDays(grade));
		
		return expectedRevisionDate;
	}
	
	/**
	 * Return the delta day (time between expectedRevisionDate and rdate)
	 * knowing the grade : delta = 2^grade.
	 * 
	 * @param grade
	 * @return
	 */
	public static int deltaDays(int grade) {
		return (new Double(Math.pow(2, grade))).intValue();
	}

	public static GregorianCalendar iso8601ToCalendar(String iso8601) {
		String[] calendarFields = iso8601.split("-", 3);
		return new GregorianCalendar(Integer.parseInt(calendarFields[0]), Integer.parseInt(calendarFields[1]) - 1, Integer.parseInt(calendarFields[2]));
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static String calendarToIso8601(GregorianCalendar calendar) {
		String yearField = String.valueOf(calendar.get(Calendar.YEAR));
		
		String monthField = String.valueOf(calendar.get(Calendar.MONTH)+1);
		if(calendar.get(Calendar.MONTH)+1 < 10) monthField = "0" + monthField;
		
		String dayField = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10) dayField = "0" + dayField;
		
		return yearField + "-" + monthField + "-" + dayField;
	}

	/**
	 * Lit une chaîne de caractère au clavier
	 */
	public static String readString() {
		String str = null;
		
		try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			str = br.readLine();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		return str;
	}

	public static void quit() {
		System.out.print("Save ? (y/n) : ");
		String save = readString();
		if(save.equals("y")) {
			// TODO
			try {
				// Make DOM source
				Source domSource = new DOMSource(domDocument);

				// Make output file
				Result streamResult = new StreamResult(pkbFile);

				// Setup transformer
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

				// Transformation
				transformer.transform(domSource, streamResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Quit");
		System.exit(0);
	}
}

