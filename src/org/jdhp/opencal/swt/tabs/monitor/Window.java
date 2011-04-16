/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.swt.tabs.monitor;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Window {

	public static final int DAY = Calendar.DAY_OF_MONTH;
	
	public static final int WEEK = Calendar.WEEK_OF_MONTH;
	
	public static final int MONTH = Calendar.MONTH;
	
	public static final int YEAR = Calendar.YEAR;
	
	/**
	 * 
	 */
	private int resolution;
	
	/**
	 * width, range or scope ?
	 */
	private int width;
	
	/**
	 * 
	 * @param resolution Can be DAY, WEEK or MONTH
	 * @param width Can be WEEK, MONTH or YEAR
	 */
	public Window(int resolution, int width) {
		if(resolution == Window.DAY && width == Window.WEEK) {
			this.resolution = resolution;
			this.width = width;
		} else if(resolution == Window.DAY && width == Window.MONTH) {
			this.resolution = resolution;
			this.width = width;
		} else if(resolution == Window.DAY && width == Window.YEAR) {
			this.resolution = resolution;
			this.width = width;
		} else if(resolution == Window.WEEK && width == Window.MONTH) {
			this.resolution = resolution;
			this.width = width;
		} else if(resolution == Window.WEEK && width == Window.YEAR) {
			this.resolution = resolution;
			this.width = width;
		} else if(resolution == Window.MONTH && width == Window.YEAR) {
			this.resolution = resolution;
			this.width = width;
		} else {	// TODO : faire échouer la création de la vue...
			this.resolution = Window.DAY;
			this.width = Window.WEEK;
		}
	}
	
	/**
	 * 
	 * @param refDate
	 * @param src
	 * @return
	 */
	public Date firstDate(Date refDate) {
		Calendar referenceCalendar = Calendar.getInstance();
		referenceCalendar.setTime(refDate);
		
		Calendar firstDateCalendar = (Calendar) referenceCalendar.clone();
		
		if(this.resolution == Window.DAY && this.width == Window.WEEK) {
			int dayOfWeek = referenceCalendar.get(Calendar.DAY_OF_WEEK);
//			int firstDayOfWeek = referenceCalendar.getActualMinimum(Calendar.DAY_OF_WEEK);
			int firstDayOfWeek = referenceCalendar.getFirstDayOfWeek();
			firstDateCalendar.add(Calendar.DAY_OF_WEEK, - (((7 + dayOfWeek - (firstDayOfWeek - 1)) % 7) - 1)); // TODO : A TESTER !!!!! (avec différentes valeurs pour setFirstDateOfWeek() )
		} else if(this.resolution == Window.DAY && this.width == Window.MONTH) {
			int firstDayOfMonth = referenceCalendar.getActualMinimum(Calendar.DAY_OF_MONTH); // 01/MONTH/YEAR
			firstDateCalendar.set(Calendar.DAY_OF_MONTH, firstDayOfMonth);
		} else if(this.resolution == Window.DAY && this.width == Window.YEAR) {
			int firstDayOfYear = referenceCalendar.getActualMinimum(Calendar.DAY_OF_YEAR);   // 01/01/YEAR
			firstDateCalendar.set(Calendar.DAY_OF_YEAR, firstDayOfYear);
		} else if(this.resolution == Window.WEEK && this.width == Window.MONTH) {
			int firstWeekOfMonth = referenceCalendar.getActualMinimum(Calendar.WEEK_OF_MONTH);
			firstDateCalendar.set(Calendar.WEEK_OF_MONTH, firstWeekOfMonth);
		} else if(this.resolution == Window.WEEK && this.width == Window.YEAR) {
			int firstWeekOfYear = referenceCalendar.getActualMinimum(Calendar.WEEK_OF_YEAR);
			firstDateCalendar.set(Calendar.WEEK_OF_YEAR, firstWeekOfYear);
		} else if(this.resolution == Window.MONTH && this.width == Window.YEAR) {            // 01/01/YEAR
			int firstMonthOfYear = referenceCalendar.getActualMinimum(Calendar.MONTH);
			firstDateCalendar.set(Calendar.MONTH, firstMonthOfYear);
		}
		
		return firstDateCalendar.getTime();
	}
	
	/**
	 * 
	 * @param refDate
	 * @param src
	 * @return
	 */
	public Date lastDate(Date refDate, Date[] src) {
		// CE SERAIT MIEUX DE RETOURNER DES COUPLES "DATE/VALEUR" PLUTÔT QUE SIMPLEMENT "VALEUR"...
		
		// Crée le tableau
//		int[] stats = null;
		Calendar reference = Calendar.getInstance();
		Calendar lastDateCalendar = (Calendar) reference.clone();
//		reference.setTime(refDate);
//		int minField = 0;
//		int maxField = 0;
//		
//		if(this.resolution == Window.DAY && this.width == Window.WEEK) {
//			minField = reference.getActualMinimum(Calendar.DAY_OF_WEEK); // 1
//			maxField = reference.getActualMaximum(Calendar.DAY_OF_WEEK);
//		} else if(this.resolution == Window.DAY && this.width == Window.MONTH) {
//			minField = reference.getActualMinimum(Calendar.DAY_OF_MONTH); // 1
//			maxField = reference.getActualMaximum(Calendar.DAY_OF_MONTH);
//		} else if(this.resolution == Window.DAY && this.width == Window.YEAR) {
//			minField = reference.getActualMinimum(Calendar.DAY_OF_YEAR); // 1
//			maxField = reference.getActualMaximum(Calendar.DAY_OF_YEAR);
//		} else if(this.resolution == Window.WEEK && this.width == Window.MONTH) {
//			minField = reference.getActualMinimum(Calendar.WEEK_OF_MONTH); // 1
//			maxField = reference.getActualMaximum(Calendar.WEEK_OF_MONTH);
//		} else if(this.resolution == Window.WEEK && this.width == Window.YEAR) {
//			minField = reference.getActualMinimum(Calendar.WEEK_OF_YEAR); // 1
//			maxField = reference.getActualMaximum(Calendar.WEEK_OF_YEAR);
//		} else if(this.resolution == Window.MONTH && this.width == Window.YEAR) {
//			minField = reference.getActualMinimum(Calendar.MONTH); // 0
//			maxField = reference.getActualMaximum(Calendar.MONTH);
//		}
//		
//		minimum.set(this.resolution, minField);
//		maximum.set(this.resolution, maxField);
//		stats = new int[maxField-minField+1];
//		
//		// Distribu les sources
//		for(int i=0 ; i<src.length ; i++) {
////			if(src[i] >= minimum) {
////				j = ...
////				while(src[i]>j && j<stats.length) {
////					j = ...
////				}
////			}
//		}
		
		return lastDateCalendar.getTime();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getResolution() {
		return this.resolution;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}
}
