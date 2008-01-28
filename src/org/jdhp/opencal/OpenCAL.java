/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal;

import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.model.xml.reviewer.Pile;
import org.jdhp.opencal.view.swing.SwingGUI;
import org.jdhp.opencal.view.swt.SWTGUI;

public class OpenCAL {

	// Common
	public final static String programVersion = "3.0.2";
	public final static String programName = "pdbm";
	
	public final static String cardDb = "/home/gremy/card_db.xml";
	public final static String tmpDb = "/tmp/" + OpenCAL.programName + "_card_db.tmp.xml";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Common
//		Controller.setUserInterface(new SwingGUI());
		Controller.setUserInterface(new SWTGUI());
		
		// Reviewer
		Controller.pile = new Pile(OpenCAL.cardDb);
		Controller.card = Controller.pile.getPointedCard();
		
		// Common
		Controller.getUserInterface().run();
	}

}
