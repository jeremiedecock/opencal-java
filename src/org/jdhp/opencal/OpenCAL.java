/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal;

import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.view.swt.SWTGUI;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class OpenCAL {

	public final static String programVersion = "3.0";
	
	public final static String programName = "OpenCAL";
	
	public final static String pkbFilePath = "/home/gremy/user_dev.pkb";
	
	public final static String tmpPkbFile = "/tmp/" + OpenCAL.programName + "_user.tmp.pkb";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Controller.init();
		
		// Controller.setUserInterface(new SwingGUI());
		Controller.setUserInterface(new SWTGUI());
		
		Controller.getUserInterface().run();
	}

}
