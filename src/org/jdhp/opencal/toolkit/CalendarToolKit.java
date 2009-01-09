/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.toolkit;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarToolKit {

	/**
	 * TODO : s'assurer que le tableau date a bien 3 entrées
	 * (pour pas planter le programme en modifiant manuellement le fichier XML)
	 * TODO : s'assurer que les 3 entrées sont bien des nombres
	 * TODO : gérer les exceptions (renvoyés par GregorianCalendar)
	 * 
	 * @param iso8601
	 * @return
	 */
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
		return "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
	}
	
}