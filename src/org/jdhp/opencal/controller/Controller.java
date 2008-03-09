/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.controller;

import org.jdhp.opencal.controller.maker.MakeController;
import org.jdhp.opencal.controller.reviewer.ReviewController;
import org.jdhp.opencal.view.UserInterface;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class Controller {
	
	private static UserInterface ui;
	
	/**
	 * Initialize all controllers
	 */
	public static void init() {
		MakeController.init();
		ReviewController.init();
	}
	
	/**
	 * Quit the program
	 * 
	 * @param status
	 */
	public static void exit(int status) {
		System.exit(status);
	}
	
	/**
	 * 
	 * @return
	 */
	public static UserInterface getUserInterface() {
		return Controller.ui;
	}

	/**
	 * 
	 * @param ui
	 */
	public static void setUserInterface(UserInterface ui) {
		Controller.ui = ui;
	}
}
