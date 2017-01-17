/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.tabs.monitor;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class WindowTest extends TestCase {

	/**
	 * 
	 * @param name
	 */
	public WindowTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testWindow() {
		Window window;
		
		// Tous les cas supposés valides...
		window = new Window(Window.DAY, Window.WEEK);
		assertSame("Resolution for DAY/WEEK", Window.DAY, window.getResolution());
		assertSame("Width for DAY/WEEK", Window.WEEK, window.getWidth());
		
		window = new Window(Window.DAY, Window.MONTH);
		assertSame("Resolution for DAY/MONTH", Window.DAY, window.getResolution());
		assertSame("Width for DAY/MONTH", Window.MONTH, window.getWidth());
		
		window = new Window(Window.DAY, Window.YEAR);
		assertSame("Resolution for DAY/YEAR", Window.DAY, window.getResolution());
		assertSame("Width for DAY/YEAR", Window.YEAR, window.getWidth());
		
		window = new Window(Window.WEEK, Window.MONTH);
		assertSame("Resolution for WEEK/MONTH", Window.WEEK, window.getResolution());
		assertSame("Width for WEEK/MONTH", Window.MONTH, window.getWidth());
		
		window = new Window(Window.WEEK, Window.YEAR);
		assertSame("Resolution for WEEK/YEAR", Window.WEEK, window.getResolution());
		assertSame("Width for WEEK/YEAR", Window.YEAR, window.getWidth());
		
		window = new Window(Window.MONTH, Window.YEAR);
		assertSame("Resolution for MONTH/YEAR", Window.MONTH, window.getResolution());
		assertSame("Width for MONTH/YEAR", Window.YEAR, window.getWidth());
		
		// Quelques cas supposés faux...
		window = new Window(Window.DAY, Window.DAY);
		assertSame("Resolution for DAY/DAY", Window.DAY, window.getResolution());
		assertSame("Width for DAY/DAY", Window.WEEK, window.getWidth());
		
		window = new Window(Window.MONTH, Window.WEEK);
		assertSame("Resolution for MONTH/WEEK", Window.DAY, window.getResolution());
		assertSame("Width for MONTH/WEEK", Window.WEEK, window.getWidth());
		
		window = new Window(Window.YEAR, Window.MONTH);
		assertSame("Resolution for YEAR/MONTH", Window.DAY, window.getResolution());
		assertSame("Width for YEAR/MONTH", Window.WEEK, window.getWidth());
	}

	/**
	 * 
	 */
	public void testFirstDate() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE); // TODO : spécifier les locales et mettre un commentaire au dessus pour expliquer le format short français, ou utiliser un format customisé : ISO-... EN FAIT NON, IL FAUT UTILISER LES LOCALES FRANCAISES CAR DE TOUTES FAÇON, TOUS LES TESTS SONT FAIT POUR LE CALENDRIER FRANCAIS... (IE LES EXPECTED DATES)
		
		Date referenceDate, expectedReturn; // TODO : le nom "expectedReturn" c'est de l'anglais correct ? (cf. nom des vars dans JUnit) 
		
		Window window = null;
		Window dayOfWeekWindow = new Window(Window.DAY, Window.WEEK);
		Window dayOfMonthWindow = new Window(Window.DAY, Window.MONTH);
		Window dayOfYearWindow = new Window(Window.DAY, Window.YEAR);
		Window weekOfMonthWindow = new Window(Window.WEEK, Window.MONTH);
		Window weekOfYearWindow = new Window(Window.WEEK, Window.YEAR);
		Window monthOfYearWindow = new Window(Window.MONTH, Window.YEAR);
		
		// window, referenceDate, expectedReturn
		String testArray[][] = {  // TODO : la déclaration d'un tableau à 2 dimension -> est-ce correct ? risque de débordement ? Ok, cf slide 97 du cours 2 de Parchemal
				{"DAY_OF_WEEK", "11/11/09", "09/11/09"},
				{"DAY_OF_WEEK", "21/11/09", "16/11/09"},
				{"DAY_OF_WEEK", "01/09/09", "31/08/09"}};
		
		for(int i=0 ; i<testArray.length ; i++) { // TODO : length mesure quelle dimension exactement ? Ok, cf slide 97 du cours 2 de Parchemal
			// set window
			if(testArray[i][0].equals("DAY_OF_WEEK")) window = dayOfWeekWindow;
			else if(testArray[i][0].equals("DAY_OF_MONTH")) window = dayOfMonthWindow;
			else if(testArray[i][0].equals("DAY_OF_YEAR")) window = dayOfYearWindow;
			else if(testArray[i][0].equals("WEEK_OF_MONTH")) window = weekOfMonthWindow;
			else if(testArray[i][0].equals("WEEK_OF_YEAR")) window = weekOfYearWindow;
			else if(testArray[i][0].equals("MONTH_OF_YEAR")) window = monthOfYearWindow;
			else fail("Wrong test array"); // TODO : mettre un message un peu plus explicite
			
			referenceDate = null;
			expectedReturn = null;
			
			try {
				referenceDate = df.parse(testArray[i][1]);
				expectedReturn = df.parse(testArray[i][2]);
			} catch(ParseException ex) {
				fail("Parsing error : " + ex); // TODO : mettre un message un peu plus explicite
			}
			
			assertEquals("First " + testArray[i][0] + " for " + testArray[i][1], expectedReturn, window.firstDate(referenceDate)); // TODO : assertSame ou assertEquals ? 
		}
	}
	
	/**
	 * 
	 */
	public void testGetResolution() {
		Window window;
		
		window = new Window(Window.DAY, Window.WEEK);
		assertSame("Resolution for DAY/WEEK", Window.DAY, window.getResolution());
		
		window = new Window(Window.WEEK, Window.MONTH);
		assertSame("Resolution for WEEK/MONTH", Window.WEEK, window.getResolution());
		
		window = new Window(Window.MONTH, Window.YEAR);
		assertSame("Resolution for MONTH/YEAR", Window.MONTH, window.getResolution());
	}
	
	/**
	 * 
	 */
	public void testGetWidth() {
		Window window;
		
		window = new Window(Window.DAY, Window.WEEK);
		assertSame("Width for DAY/WEEK", Window.WEEK, window.getWidth());
		
		window = new Window(Window.WEEK, Window.MONTH);
		assertSame("Width for WEEK/MONTH", Window.MONTH, window.getWidth());
		
		window = new Window(Window.MONTH, Window.YEAR);
		assertSame("Width for MONTH/YEAR", Window.YEAR, window.getWidth());
	}

}
