/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.dialogs;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdhp.opencal.ui.swt.MainWindow;

public class InsertImageDialog extends InsertFileDialog {
	
	public static final String[] IMAGE_EXTENSION_LIST = {"png", "jpg", "jpeg", "gif"}; // les extensions doivent être en minuscule
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 */
	public InsertImageDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public InsertImageDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		this.setText("Insert a picture");
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	protected String buildTag(String path) {	// prend src, licence, ... en argument
		String tag = null;
		String extension = extractExtension(path);	    // TODO
		
		if(isValidExtension(extension)) {				// TODO
			String hexDigest = insertFileInPKB(path);
			tag = "<img file=\"" + hexDigest + "." + extension + "\" />";	// TODO : source, auteur, licence
		}
		
		return tag;
	}
	
	/**
	 * 
	 * @param extension
	 * @return
	 */
	protected final boolean isValidExtension(String extension) {
		Arrays.sort(IMAGE_EXTENSION_LIST); // ne pas supprimer, nécessaire pour "Arrays.binarySearch" (cf. /doc/openjdk-6-jre/api/java/util/Arrays.html
		return (Arrays.binarySearch(IMAGE_EXTENSION_LIST, extension.toLowerCase()) >= 0);
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	protected final boolean isValidFile(String path) {
		// Check if the file exists
		boolean valid = super.isValidFile(path);
		
		// Check if the file is a valid image
		if(valid) {
			Image image = null;
			
			try {
				image = new Image(Display.getCurrent(), path);
			} catch(Exception e) {
				// do nothing
			}
			
			if(image == null) {
				valid = false;
			} else {
				valid = true;
				image.dispose();
			}
		}
		
		return valid;
	}
	
	/**
	 * Utilisé par les browsers (donnant un apperçu des fichiers insérés ou des scripts créés).
	 */
	protected String htmlPreview() {
		StringBuffer html = new StringBuffer();
		
		html.append("<html><head><style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.EDITABLE_BROWSER_CSS);
		html.append("</style></head><body>");
		
		if(this.filepath != null) {
			html.append("<img src=\"" + this.filepath + "\" />");
		} else {
			html.append("<p>" + PREVIEW_DEFAULT_MESSAGE + "</p>");
		}
		
		html.append("</body></html>");
		
		return html.toString();
	}
}
