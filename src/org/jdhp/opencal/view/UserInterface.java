/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view;

/**
 * 
 * @author Jérémie Decock
 *
 */
public interface UserInterface {
	
	public abstract void setStatusLabel1(String text, String toolTipText);
	
	public abstract void setStatusLabel2(String text, String toolTipText);
	
	public abstract void setStatusLabel3(String text, String toolTipText);
	
	public abstract void setStatusLabel4(String text, String toolTipText);
	
	public abstract void print(String text);
	
	public abstract void printAlert(String text);
	
	public abstract void printError(String text);
	
	public abstract void update();
	
	public abstract void run();

}
