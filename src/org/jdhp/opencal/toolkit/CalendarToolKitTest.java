package org.jdhp.opencal.toolkit;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
//		GregorianCalendar calendar;
//		String testArray[][] = {
//				{"2008-01-01", "2008", "01", "01"},
//				{"2000-01-01", "2000", "01", "01"},
//				{"2004-01-01", "2004", "01", "01"}};
//		
//		for(int i=0 ; i<testArray.length ; i++) {
//			calendar = CalendarToolKit.iso8601ToCalendar(testArray[i][0]);
//			assertEquals("Test year field for " + testArray[i][0], testArray[i][1], "" + calendar.get(Calendar.YEAR));
//			assertEquals("Test month field for " + testArray[i][0], testArray[i][2], "" + calendar.get(Calendar.MONTH) + 1);
//			assertEquals("Test day field for " + testArray[i][0], testArray[i][3], "" + calendar.get(Calendar.DAY_OF_MONTH));
//		}
//		
		fail("Not yet implemented");
	}

	public void testCalendarToIso8601() {
		fail("Not yet implemented");
	}

}
