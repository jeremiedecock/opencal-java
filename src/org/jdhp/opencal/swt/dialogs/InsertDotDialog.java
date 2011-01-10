/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */
package org.jdhp.opencal.swt.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class InsertDotDialog extends InsertScriptDialog {
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 */
	public InsertDotDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
	}
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public InsertDotDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		
		this.setText("Insert a Dot graph");
		
		this.codeTemplate = "digraph G {\n"
                          + "\n"
			              + "size = \"3.7,2.8\", fontsize=10, ranksep=0.4;\n"
			              + "node[fontsize=10, height=0.3, width=0.3];\n"
			              + "edge[fontsize=7, arrowsize=0.5, labeldistance=1.1];\n"
			              + "\n"
			              + "\n"
			              + "\n"
			              + "}";

		
		
		this.defaultCursorPosition = 152;
	}

	
	
	/**
	 * Préprocesseur :
	 * construit le code source complet du script (ajout éventuel d'entête, de pied de page, filtrage, ...)
	 * à partir de la chaîne content.
	 * 
	 * @param content
	 * @return 
	 */
	public String scriptPreprocessor(String content) {
		return content;
	}
	
	/**
	 * Interprète le script et retourne l'adresse de l'image générée.
	 * 
	 * @param srcPath
	 * @return
	 */
	public String runScript(String srcPath) {
		String picturePath = null;
		
		try {
			File dotFile = new File(srcPath);
			
			File workingDirectory = new File(dotFile.getParent());
			
			File pngFile = File.createTempFile("opencal_", ".png");
			pngFile.deleteOnExit();
			
			// Run script /////////////
			String cmd = "dot -Tpng -o " + pngFile.getAbsolutePath() + " " + dotFile.getAbsolutePath(); // TODO : neato, twopi, ...
			Process process = Runtime.getRuntime().exec(cmd, null, workingDirectory);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int exitValue = process.exitValue();
			
			picturePath = pngFile.getAbsolutePath();

			// GET LOGS ///////////////
			String line;
	        StringBuffer sb = new StringBuffer();
			
			BufferedReader is = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line = is.readLine()) != null) {
				sb.append(line);
			}
			
			is = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while((line = is.readLine()) != null) {
				sb.append(line);
			}
			
			sb.append("\n\nExit value : " + exitValue);
			log = sb.toString();
			
			// TODO : destroy process ?
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return picturePath;
	}
	
	/**
	 * Display help content about the script language.
	 */
	public void help() {
		// TODO : display help dialog
	}
	
}
