/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.ui.swt.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class InsertLatexDialog extends InsertScriptDialog {
    
    public static final String SOURCE_HEADER = "";
    public static final String SOURCE_FOOTER = "";
    
    public static final double TEXPT_PER_IN = 72.27;
    public static final double TEXPT = 10.;
    
    public double font_size = 14.;  // default = 14
    
    /**
     * InsertImageDialog constructor
     * 
     * @param parent the parent
     */
    public InsertLatexDialog(Shell parent) {
        // Pass the default styles here
        this(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
    }
    
    /**
     * InsertImageDialog constructor
     * 
     * @param parent the parent
     * @param style the style
     */
    public InsertLatexDialog(Shell parent, int style) {
        // Let users override the default styles
        super(parent, style);
        
        this.setText("Insert a LaTeX formula");
        
        this.codeTemplate = "\\documentclass{minimal}\n"
                          + "\\begin{document}\n"
                          + "\n\n\n"
                          + "\\end{document}\n";

        this.defaultCursorPosition = 42;
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
            File texFile = new File(srcPath);
            
            String basename = texFile.getName().substring(0, texFile.getName().length() - SCRIPT_SUFFIX.length());
            
            File workingDirectory = new File(texFile.getParent());
            
            File dviFile = new File(workingDirectory.getAbsolutePath() + File.separator + basename + ".dvi");
            File logFile = new File(workingDirectory.getAbsolutePath() + File.separator  + basename + ".log");
            File auxFile = new File(workingDirectory.getAbsolutePath() + File.separator  + basename + ".aux");
            
            File pngFile = File.createTempFile("opencal_", ".png");
            pngFile.deleteOnExit();
            
            // Run scripts ////////////
            String latexCmd = "latex -interaction=nonstopmode " + srcPath;
            Process latexProcess = Runtime.getRuntime().exec(latexCmd, null, workingDirectory);
            try {
                latexProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // log
            int latexExitValue = latexProcess.exitValue();
            
            // if exitvalue == 0
            int dpi = (int) (font_size * TEXPT_PER_IN / TEXPT);
            String dvipngCmd = "dvipng -q -T tight -D " + dpi + " -o " + pngFile.getAbsolutePath() + " " + dviFile.getAbsolutePath();
            Process dvipngProcess = Runtime.getRuntime().exec(dvipngCmd, null, workingDirectory);
            try {
                dvipngProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int dvipngExitValue = dvipngProcess.exitValue();
            
            picturePath = pngFile.getAbsolutePath();
            
            // GET LOGS ///////////////
            String line;
            StringBuffer sb = new StringBuffer();
            
            BufferedReader is = new BufferedReader(new InputStreamReader(latexProcess.getInputStream()));
            while((line = is.readLine()) != null) {
                sb.append(line);
            }
            
            is = new BufferedReader(new InputStreamReader(latexProcess.getErrorStream()));
            while((line = is.readLine()) != null) {
                sb.append(line);
            }
            
            sb.append("\n\nExit value : " + latexExitValue);
            log = sb.toString();
            
            // REMOVE TMP FILES ///////
            dviFile.delete();
            logFile.delete();
            auxFile.delete();
            
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
