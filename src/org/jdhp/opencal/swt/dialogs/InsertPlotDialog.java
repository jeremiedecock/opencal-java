package org.jdhp.opencal.swt.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class InsertPlotDialog extends InsertScriptDialog {

	public static final String TERMINAL = "png"; // TODO : pngcairo (gnuplot >= 4.4)
	public static final String SOURCE_HEADER = "set terminal " + TERMINAL + " size 350, 250\n";
	public static final String SOURCE_FOOTER = "";
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 */
	public InsertPlotDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
	}
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public InsertPlotDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		
		this.setText("Insert a Gnuplot graphics");
		
		this.codeTemplate = "set zeroaxis ls -1\n"
			              + "set grid\n"
			              + "\n"
                          + "f(x) = \n\n"
                          + "plot [-10:10] f(x) with lines\n";

		this.defaultCursorPosition = 36;
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
		return SOURCE_HEADER + content + SOURCE_FOOTER;
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
			File gpFile = new File(srcPath);
			
			File workingDirectory = new File(gpFile.getParent());
			
			File pngFile = File.createTempFile("opencal_", ".png");
			pngFile.deleteOnExit();
			
			// RUN SCRIPT /////////////
			String cmd = "gnuplot " + gpFile.getAbsolutePath();
			Process process = Runtime.getRuntime().exec(cmd, null, workingDirectory);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int exitValue = process.exitValue();
			
			// WRITE PNG FILE /////////
			InputStream in = process.getInputStream();
			FileOutputStream dstStream = new FileOutputStream(pngFile);
	        try {
	            byte[] buf = new byte[1024];
	            int i = 0;
	            while ((i = in.read(buf)) != -1) {
	            	dstStream.write(buf, 0, i);
	            }
	        } finally {
	            if (in != null) in.close();
	            if (dstStream != null) dstStream.close();
	        }
	        
	        picturePath = pngFile.getAbsolutePath();
	        
			// GET LOGS ///////////////
	        String line;
	        StringBuffer sb = new StringBuffer();
			
			BufferedReader is = new BufferedReader(new InputStreamReader(process.getErrorStream()));
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
