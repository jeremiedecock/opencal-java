/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.dialogs;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class InsertVideoDialog extends InsertFileDialog {
	
	public static final String[] VIDEO_EXTENSION_LIST = {"ogg", "vp8", "mpg"}; // les extensions doivent être en minuscule
	
	/**
	 * 
	 * @param parent the parent
	 */
	public InsertVideoDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	/**
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public InsertVideoDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		this.setText("Insert a video");
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
			tag = "<video file=\"" + hexDigest + "." + extension + "\" />";	// TODO : source, auteur, licence
		}
		
		return tag;
	}
	
	/**
	 * 
	 * @param extension
	 * @return
	 */
	protected final boolean isValidExtension(String extension) {
		Arrays.sort(VIDEO_EXTENSION_LIST); // ne pas supprimer, nécessaire pour "Arrays.binarySearch" (cf. /doc/openjdk-6-jre/api/java/util/Arrays.html
		return (Arrays.binarySearch(VIDEO_EXTENSION_LIST, extension.toLowerCase()) >= 0);
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	protected final boolean isValidFile(String path) {
		// TODO
		return true;
	}
}
