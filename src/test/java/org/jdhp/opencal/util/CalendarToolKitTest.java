package org.jdhp.opencal.util;

import java.util.GregorianCalendar;

import org.jdhp.opencal.util.CalendarToolKit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalendarToolKitTest {

    @Test
    public void testIso8601ToCalendar() {
        GregorianCalendar calendar;
        String testArray[] = {
                "2008-01-01",
                "2000-09-09",
                "2004-10-10"};
        
        for(int i=0 ; i<testArray.length ; i++) {
            calendar = CalendarToolKit.iso8601ToCalendar(testArray[i]); // TODO
            assertEquals(testArray[i], CalendarToolKit.calendarToIso8601(calendar));
        }
    }

    @Test
    public void testCalendarToIso8601() {
        GregorianCalendar calendar;
        String testArray[] = {
                "2008-01-01",
                "2000-09-09",
                "2004-10-10"};
        
        for(int i=0 ; i<testArray.length ; i++) {
            calendar = CalendarToolKit.iso8601ToCalendar(testArray[i]); // TODO
            assertEquals(testArray[i], CalendarToolKit.calendarToIso8601(calendar));
        }
    }

}
