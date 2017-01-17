/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Sensor {
	final public static int NULL_METHOD = 0;
	final public static int PRINT_METHOD = 1;
	final public static int LOG_FILE_METHOD = 2;
	final public static int CSV_METHOD = 3;
	final public static int SQL_METHOD = 4;
	
	private static int method = Sensor.NULL_METHOD;
	
	private static MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
	private static GregorianCalendar gc = new GregorianCalendar();
	
	/**
	 * 
	 * @param method
	 */
	public static void setOutputMethod(int method) {
		Sensor.method = method;
	}
	
	/**
	 * 
	 * @param source
	 */
	public static void measure(String source) {
		Sensor.gc.setTime(new Date());
		Sensor.measureTime(source);
		Sensor.measureMemoryUsage(source);
	}
	
	/**
	 * 
	 * @param source
	 */
	public static void measureTime(String source) {
		switch(Sensor.method) {
			case Sensor.NULL_METHOD :
				break;
			case Sensor.PRINT_METHOD : 
				System.out.print(" * " + source + " * Time : " + Sensor.gc.getTime());
				break;
			case Sensor.LOG_FILE_METHOD : 
				try {
					PrintWriter file = new PrintWriter(new FileWriter(Sensor.gc.get(Calendar.YEAR) + "_" + (Sensor.gc.get(Calendar.MONTH) + 1) + "_" + Sensor.gc.get(Calendar.DAY_OF_MONTH) + ".log", true));
					file.print(" * " + source + " * Time : " + Sensor.gc.get(Calendar.HOUR_OF_DAY) + ":" + Sensor.gc.get(Calendar.MINUTE) + ":" + Sensor.gc.get(Calendar.SECOND));
					file.close();
				} catch(IOException e) {
					
				}
				break;
			case Sensor.CSV_METHOD :
				try {
					PrintWriter file = new PrintWriter(new FileWriter(Sensor.gc.get(Calendar.YEAR) + "_" + (Sensor.gc.get(Calendar.MONTH) + 1) + "_" + Sensor.gc.get(Calendar.DAY_OF_MONTH) + ".csv", true));
					file.print(source + "," + Sensor.gc.get(Calendar.HOUR_OF_DAY) + ":" + Sensor.gc.get(Calendar.MINUTE) + ":" + Sensor.gc.get(Calendar.SECOND));
					file.close();
				} catch(IOException e) {
					
				}
				break;
			case Sensor.SQL_METHOD : 
				break;
		}
	}
	
	/**
	 * 
	 * @param source
	 */
	public static void measureMemoryUsage(String source) {
		switch(Sensor.method) {
			case Sensor.NULL_METHOD :
				break;
			case Sensor.PRINT_METHOD : 
				System.out.println(" * Mem : " + Sensor.memoryBean.getHeapMemoryUsage());
				break;
			case Sensor.LOG_FILE_METHOD : 
				try {
					PrintWriter file = new PrintWriter(new FileWriter(Sensor.gc.get(Calendar.YEAR) + "_" + (Sensor.gc.get(Calendar.MONTH) + 1) + "_" + Sensor.gc.get(Calendar.DAY_OF_MONTH) + ".log", true));
					file.println(" * Mem : " + Sensor.memoryBean.getHeapMemoryUsage());
					file.close();
				} catch(IOException e) {
					
				}
				break;
			case Sensor.CSV_METHOD :
				try {
					PrintWriter file = new PrintWriter(new FileWriter(Sensor.gc.get(Calendar.YEAR) + "_" + (Sensor.gc.get(Calendar.MONTH) + 1) + "_" + Sensor.gc.get(Calendar.DAY_OF_MONTH) + ".csv", true));
					MemoryUsage mu = Sensor.memoryBean.getNonHeapMemoryUsage();
					file.print("," + mu.getInit() + "," + mu.getUsed() + "," + mu.getCommitted() + "," + mu.getMax());
					mu = Sensor.memoryBean.getHeapMemoryUsage();
					file.println("," + mu.getInit() + "," + mu.getUsed() + "," + mu.getCommitted() + "," + mu.getMax());
					file.close();
				} catch(IOException e) {
					
				}
				break;
			case Sensor.SQL_METHOD : 
				break;
		}
	}
}
