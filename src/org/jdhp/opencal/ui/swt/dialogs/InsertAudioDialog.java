/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.dialogs;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jdhp.opencal.ui.swt.MainWindow;

public class InsertAudioDialog extends InsertFileDialog {
		
	public static final String[] AUDIO_EXTENSION_LIST = {"ogg", "oga", "flac", "spx", "wav", "mp3"}; // les extensions doivent être en minuscule
	
	/**
	 * 
	 * @param parent the parent
	 */
	public InsertAudioDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	/**
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public InsertAudioDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		this.setText("Insert an audio file");
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
			tag = "<audio file=\"" + hexDigest + "." + extension + "\" />";	// TODO : source, auteur, licence
		}
		
		return tag;
	}
	
	/**
	 * 
	 * @param extension
	 * @return
	 */
	protected final boolean isValidExtension(String extension) {
		Arrays.sort(AUDIO_EXTENSION_LIST); // ne pas supprimer, nécessaire pour "Arrays.binarySearch" (cf. /doc/openjdk-6-jre/api/java/util/Arrays.html
		return (Arrays.binarySearch(AUDIO_EXTENSION_LIST, extension.toLowerCase()) >= 0);
	}
	
	/**
	 * Utilisé par les browsers (donnant un apperçu des fichiers insérés ou des scripts créés).
	 */
	protected String htmlPreview() {
		StringBuffer html = new StringBuffer();
		
		html.append("<!DOCTYPE html>\n<html>\n<head>\n<style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.EDITABLE_BROWSER_CSS);
		html.append("</style>\n</head>\n<body>\n");
		
		if(this.filepath != null) {
			html.append("<audio controls src=\"" + this.filepath + "\">Your browser does not support the audio tag.<audio/>");
		} else {
			html.append("<p>" + PREVIEW_DEFAULT_MESSAGE + "</p>");
		}
		
		html.append("\n</body>\n</html>");
		
		return html.toString();
	}
}
