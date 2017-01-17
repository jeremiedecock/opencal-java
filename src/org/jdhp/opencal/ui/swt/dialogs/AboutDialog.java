/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.dialogs;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.ui.swt.images.SharedImages;

public class AboutDialog extends Dialog {
	
	final Font titleFont = new Font(Display.getCurrent(), "Arial", 12, SWT.BOLD);
	
	final Font smallFont = new Font(Display.getCurrent(), "Arial", 8, SWT.NORMAL);
	
	/**
	 * AboutDialog constructor
	 * 
	 * @param parent the parent
	 */
	public AboutDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	/**
	 * AboutDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public AboutDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		this.setText("About " + OpenCAL.PROGRAM_NAME + "...");
	}

	/**
	 * Opens the dialog and returns nothing
	 * 
	 * @return null null
	 */
	public String open() {
		// Create the dialog window
		Shell shell = new Shell(this.getParent(), this.getStyle());
		shell.setText(this.getText());
		shell.setMinimumSize(400, 480);
		this.createContents(shell);
		shell.pack();
		shell.open();
		
		Display display = this.getParent().getDisplay();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		titleFont.dispose();
		smallFont.dispose();
		
		return null;
	}
	
	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell the dialog window
	 */
	private void createContents(final Shell shell) {
		
		///////////////////////////
		// Shell //////////////////
		///////////////////////////
		
		shell.setLayout(new GridLayout(1, true));

		///////////////////////////
		// Title //////////////////
		///////////////////////////
		
		// TODO : remplacer par le logo (avec le nom) (cf. about dialog de firefox)
		Label programNameLabel = new Label(shell, SWT.CENTER);
		programNameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		programNameLabel.setText(OpenCAL.PROGRAM_NAME + " " + OpenCAL.PROGRAM_VERSION);
		programNameLabel.setFont(titleFont);
		
		///////////////////////////
		// General informations ///
		///////////////////////////
		
		Composite generalInfoComposite = new Composite(shell, SWT.NONE);
		GridData generalInfoGridData = new GridData(GridData.FILL_HORIZONTAL);
		generalInfoGridData.verticalSpan = 11;
		generalInfoComposite.setLayoutData(generalInfoGridData);
		
		generalInfoComposite.setLayout(new GridLayout(1, true));
		
		// Copyright
		Label copyrightLabel = new Label(generalInfoComposite, SWT.CENTER);
		copyrightLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		copyrightLabel.setText(OpenCAL.COPYRIGHT);
		copyrightLabel.setFont(smallFont);
		
		// Web site
		Button webSiteButton = new Button(generalInfoComposite, SWT.FLAT);
		webSiteButton.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		webSiteButton.setText("http://www.jdhp.org");
		webSiteButton.setFont(smallFont);
		
		// Create a horizontal separator
		Label separator1 = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		///////////////////////////
		// License and authors ////
		///////////////////////////
		
		final Composite stackComposite = new Composite(shell, SWT.NONE);
		stackComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final StackLayout stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);
		
		// License //////////
		String license = "";
		try {
			InputStream inStream = OpenCAL.class.getResourceAsStream("COPYING");
			if(inStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
				String line = null;
				String ls = System.getProperty("line.separator");
				StringBuilder builder = new StringBuilder();
				while((line = reader.readLine()) != null) {
					builder.append(line);
					builder.append(ls);
				}
				license = builder.toString();
				inStream.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		final Text licenseText = new Text(stackComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
		licenseText.setLayoutData(new GridData(GridData.FILL_BOTH));
		licenseText.setText("LICENSE\n\n" + license);
		
		// Authors //////////
		String authors = "";
		try {
			InputStream inStream = OpenCAL.class.getResourceAsStream("AUTHORS");
			if(inStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
				String line = null;
				String ls = System.getProperty("line.separator");
				StringBuilder builder = new StringBuilder();
				while((line = reader.readLine()) != null) {
					builder.append(line);
					builder.append(ls);
				}
				authors = builder.toString();
				inStream.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		final Text authorsText = new Text(stackComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
		authorsText.setLayoutData(new GridData(GridData.FILL_BOTH));
		authorsText.setText("AUTHORS\n\n" + authors);

		stackLayout.topControl = licenseText;
		
		// Create a horizontal separator
		Label separator2 = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		///////////////////////////
		// ButtonComposite ////////
		///////////////////////////
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		buttonComposite.setLayout(new GridLayout(2, true));
		
		// SwitchButton ////////////
		final Button switchButton = new Button(buttonComposite, SWT.PUSH);
		switchButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
		switchButton.setEnabled(true);
		
		if(stackLayout.topControl == licenseText) {
			switchButton.setText("Authors");
			switchButton.setToolTipText("Show authors names");
		} else {
			switchButton.setText("License");
			switchButton.setToolTipText("Show license");
		}
		
		// CloseButton /////////////
		final Button closeButton = new Button(buttonComposite, SWT.PUSH);
		closeButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true));
		closeButton.setEnabled(true);
		closeButton.setText("Close");
		closeButton.setImage(SharedImages.getImage(SharedImages.WINDOW_CLOSE_22));
		closeButton.setToolTipText("Close this dialog");

        // Equalize buttons size (buttons size may change in others languages...) //
        Point switchButtonPoint = switchButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        Point closeButtonPoint = closeButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

        if(switchButtonPoint.x > closeButtonPoint.x) ((GridData) closeButton.getLayoutData()).widthHint = switchButtonPoint.x;
        else ((GridData) switchButton.getLayoutData()).widthHint = closeButtonPoint.x;
        
        if(switchButtonPoint.y > closeButtonPoint.y) ((GridData) closeButton.getLayoutData()).heightHint = switchButtonPoint.y;
        else ((GridData) switchButton.getLayoutData()).heightHint = closeButtonPoint.y;
		
		// Set the Close button as the default
        closeButton.setFocus();
		
		///////////////////////////
		// Listeners //////////////
		///////////////////////////

        // WebSiteButton /////////
        webSiteButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// Open default navigator (cf. http://java.developpez.com/faq/java/?page=systeme#ouvrirPageWeb)
				if(Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					if(desktop.isSupported(Desktop.Action.BROWSE)) {
						try {
							URI uri = new URI(OpenCAL.WEB_SITE);
							desktop.browse(uri);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
        
        // SwitchButton //////////
        switchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(stackLayout.topControl == licenseText) {
					stackLayout.topControl = authorsText;
					switchButton.setText("License");
					switchButton.setToolTipText("Show license");
				} else {
					stackLayout.topControl = licenseText;
					switchButton.setText("Authors");
					switchButton.setToolTipText("Show authors names");
				}
				stackComposite.layout();
			}
		});
        
		// CloseButton ///////////
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});
		
	}

}
