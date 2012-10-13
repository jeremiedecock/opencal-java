/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.dialogs;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
		boolean valid = true;
		
		Image image = null;
		
		try {
			new Image(Display.getCurrent(), path);
		} catch(IllegalArgumentException e) {
			valid = false;
		} catch(SWTException e) {
			valid = false;
		} catch(SWTError e) {
			valid = false;
		}
		
		if(image != null) image.dispose();
		
		return valid;
	}
}
