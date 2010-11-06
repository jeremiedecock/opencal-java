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

	public static final String SOURCE_HEADER = "set terminal png\n";
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
		
		this.codeTemplate = "set zeroaxis\n\n"
                          + "f(x) = \n\n"
                          + "plot f(x)\n";

		this.defaultCursorPosition = 21;
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
	        BufferedReader is = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	        StringBuffer sb = new StringBuffer();
			String line;
			while((line = is.readLine()) != null) {
				sb.append(line);
			}
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
