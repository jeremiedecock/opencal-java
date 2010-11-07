/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.swt.tabs;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class TrainTab {

	final private Composite parentComposite;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public TrainTab(Composite parentComposite) {
		
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(1, false));
		
	}
	
	/**
	 * La mise à jours faite lors de la sélection du tab "TrainTab"
	 */
	final public void update() {
		// TODO
	}

}
