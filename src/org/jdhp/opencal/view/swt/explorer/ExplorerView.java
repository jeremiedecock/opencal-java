/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.explorer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ExplorerView {

	final private Composite parentComposite;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ExplorerView(Composite parentComposite) {
		this.parentComposite = parentComposite;
		
		///////////////////////////////////////////////////////////////////////
		// Make explorerComposite /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		this.parentComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label explorerCompositeEmptyLabel = new Label(this.parentComposite, SWT.CENTER);
		explorerCompositeEmptyLabel.setText("");
		
		Label explorerCompositeLabel = new Label(this.parentComposite, SWT.CENTER);
		explorerCompositeLabel.setText("Not yet available...");
	}
	
}
