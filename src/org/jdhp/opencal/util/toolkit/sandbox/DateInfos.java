/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.util.toolkit.sandbox;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateInfos {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        
        System.out.println("");
        
        Date date = null;
        
        try {
            date = df.parse("11/11/09");
        } catch(ParseException ex) {
            
        }
        
        getInfos(date);
    }
    
    public static void getInfos(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        
        GregorianCalendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
        calendar.setTime(date);
        
        int fdw = calendar.getFirstDayOfWeek();
        switch(fdw) {
            case GregorianCalendar.MONDAY : System.out.println("Monday : " + fdw); break;
            case GregorianCalendar.TUESDAY : System.out.println("Tuesday : " + fdw); break;
            case GregorianCalendar.WEDNESDAY : System.out.println("Wednesday : " + fdw); break;
            case GregorianCalendar.THURSDAY : System.out.println("Thursday : " + fdw); break;
            case GregorianCalendar.FRIDAY : System.out.println("Friday : " + fdw); break;
            case GregorianCalendar.SATURDAY : System.out.println("Saturday : " + fdw); break;
            case GregorianCalendar.SUNDAY : System.out.println("Sunday : " + fdw); break;
        }
        
        System.out.println("\nDate testée : " + df.format(date) + "\n");
        System.out.println("\nDate testée : " + date + "\n");
        
        System.out.println("Mon test : " + ((7 + calendar.get(GregorianCalendar.DAY_OF_WEEK) - (calendar.getFirstDayOfWeek() - 1)) % 7));
        System.out.println("*** get(Calendar.DAY_OF_WEEK) : " + calendar.get(GregorianCalendar.DAY_OF_WEEK));
        System.out.println("get(Calendar.DAY_OF_MONTH) : " + calendar.get(GregorianCalendar.DAY_OF_MONTH));
        System.out.println("get(Calendar.DAY_OF_YEAR) : " + calendar.get(GregorianCalendar.DAY_OF_YEAR));
        System.out.println("get(Calendar.WEEK_OF_MONTH) : " + calendar.get(GregorianCalendar.WEEK_OF_MONTH));
        System.out.println("get(Calendar.WEEK_OF_YEAR) : " + calendar.get(GregorianCalendar.WEEK_OF_YEAR));
        System.out.println("get(Calendar.MONTH) : " + calendar.get(GregorianCalendar.MONTH));
        System.out.println("get(Calendar.DAY_OF_WEEK_IN_MONTH) : " + calendar.get(GregorianCalendar.DAY_OF_WEEK_IN_MONTH));
        
        System.out.println(" ************************ ");
        
        System.out.println("getActualMinimum(Calendar.DAY_OF_WEEK) : " + calendar.getActualMinimum(GregorianCalendar.DAY_OF_WEEK));
        System.out.println("getActualMinimum(Calendar.DAY_OF_MONTH) : " + calendar.getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
        System.out.println("getActualMinimum(Calendar.DAY_OF_YEAR) : " + calendar.getActualMinimum(GregorianCalendar.DAY_OF_YEAR));
        System.out.println("getActualMinimum(Calendar.WEEK_OF_MONTH) : " + calendar.getActualMinimum(GregorianCalendar.WEEK_OF_MONTH));
        System.out.println("getActualMinimum(Calendar.WEEK_OF_YEAR) : " + calendar.getActualMinimum(GregorianCalendar.WEEK_OF_YEAR));
        System.out.println("getActualMinimum(Calendar.MONTH) : " + calendar.getActualMinimum(GregorianCalendar.MONTH));
        System.out.println(" ************ ");
        System.out.println("getActualMaximum(Calendar.DAY_OF_WEEK) : " + calendar.getActualMaximum(GregorianCalendar.DAY_OF_WEEK));
        System.out.println("getActualMaximum(Calendar.DAY_OF_MONTH) : " + calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        System.out.println("getActualMaximum(Calendar.DAY_OF_YEAR) : " + calendar.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
        System.out.println("getActualMaximum(Calendar.WEEK_OF_MONTH) : " + calendar.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH));
        System.out.println("getActualMaximum(Calendar.WEEK_OF_YEAR) : " + calendar.getActualMaximum(GregorianCalendar.WEEK_OF_YEAR));
        System.out.println("getActualMaximum(Calendar.MONTH) : " + calendar.getActualMaximum(GregorianCalendar.MONTH));
    }

}
