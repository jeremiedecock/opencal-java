/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.stats;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatsView {

	final private Composite parentComposite;
	
	public StatsView(Composite parentComposite) {
		this.parentComposite = parentComposite;

		///////////////////////////////////////////////////////////////////////
		// Make statComposite /////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		this.parentComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label statsCompositeEmptyLabel = new Label(this.parentComposite, SWT.CENTER);
		statsCompositeEmptyLabel.setText("");
		
		Label statsCompositeLabel = new Label(this.parentComposite, SWT.CENTER);
		statsCompositeLabel.setText("Not yet available...");
	}
	
}
