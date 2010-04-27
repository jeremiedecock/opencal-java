package org.jdhp.opencal.tests;

import java.util.GregorianCalendar;

import org.jdhp.opencal.util.CalendarToolKit;

import junit.framework.TestCase;

public class CalendarToolKitTest extends TestCase {

	public CalendarToolKitTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testIso8601ToCalendar() {
		GregorianCalendar calendar;
		String testArray[] = {
				"2008-01-01",
				"2000-09-09",
				"2004-10-10"};
		
		for(int i=0 ; i<testArray.length ; i++) {
			calendar = CalendarToolKit.iso8601ToCalendar(testArray[i]);	// TODO
			assertEquals(testArray[i], CalendarToolKit.calendarToIso8601(calendar));
		}
	}

	public void testCalendarToIso8601() {
		GregorianCalendar calendar;
		String testArray[] = {
				"2008-01-01",
				"2000-09-09",
				"2004-10-10"};
		
		for(int i=0 ; i<testArray.length ; i++) {
			calendar = CalendarToolKit.iso8601ToCalendar(testArray[i]);	// TODO
			assertEquals(testArray[i], CalendarToolKit.calendarToIso8601(calendar));
		}
	}

}
