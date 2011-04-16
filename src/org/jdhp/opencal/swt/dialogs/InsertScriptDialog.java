/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.dialogs;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.swt.MainWindow;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.util.DataToolKit;

public abstract class InsertScriptDialog extends Dialog {
	
	public static final int EDITOR = 1;
	public static final int BROWSER = 2;
	
	public static final String PREVIEW_DEFAULT_MESSAGE = "No preview available.";
	
	public static final String DEFAULT_BUILD_ERR_MSG = "Unknown error";  // TODO
	
	public static final String[] IMAGE_EXTENSION_LIST = {"png", "jpg", "jpeg"}; // les extensions doivent être en minuscule
	
	public static final String SCRIPT_SUFFIX = ".src";
	
	protected String codeTemplate = "";

	protected int defaultCursorPosition = 0;
	
	private String imageTag;
	
	private String filepath;
	
	protected String log;
	
	///////////////////////////////////
	
	/**
	 * Interprète le script et retourne l'adresse de l'image générée.
	 * 
	 * @param srcPath
	 * @return
	 */
	public abstract String runScript(String srcPath);
	
	/**
	 * Préprocesseur :
	 * construit le code source complet du script (ajout éventuel d'entête, de pied de page, filtrage, ...)
	 * à partir de la chaîne content.
	 * 
	 * @param content
	 * @return 
	 */
	public abstract String scriptPreprocessor(String content);
	
	/**
	 * Display help content about the script language.
	 */
	public abstract void help();

	///////////////////////////////////
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 */
	protected InsertScriptDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE);
	}
	
	/**
	 * InsertImageDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	protected InsertScriptDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		this.setText("Insert a picture");
	}

	/**
	 * Opens the dialog and returns the image tag
	 * 
	 * @return tag the image tag
	 */
	public String open() {
		// Create the dialog window
		Shell shell = new Shell(this.getParent(), this.getStyle());
		shell.setText(this.getText());
		shell.setMinimumSize(400, 520);
		this.createContents(shell);
		shell.pack();
		shell.open();
		
		Display display = this.getParent().getDisplay();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		// Return the image tag, or null
		return this.imageTag;
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
		// EditorComposite ////////
		///////////////////////////
		
		final ViewForm viewform = new ViewForm(shell, SWT.BORDER | SWT.FLAT);
		viewform.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// Create the CLabel for the top left /////////////////////////////////
		CLabel titleLabel = new CLabel(viewform, SWT.NONE);
		titleLabel.setText("Source code");                              // TODO
		titleLabel.setAlignment(SWT.LEFT);
		viewform.setTopLeft(titleLabel);
		
		// Create the top right buttons ///////////////////////////////////////
		ToolBar tbRight = new ToolBar(viewform, SWT.FLAT);
		
		final ToolItem switchDisplayItem = new ToolItem(tbRight, SWT.PUSH);
		switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW_16));
		switchDisplayItem.setToolTipText("Switch display mode");
		
		// TODO : Custom items -> insert templates, document properties, ...
		
		final ToolItem logviewerItem = new ToolItem(tbRight, SWT.PUSH);
		logviewerItem.setImage(SharedImages.getImage(SharedImages.UTILITIES_TERMINAL_16));
		logviewerItem.setToolTipText("Logviewer");

		final ToolItem helpItem = new ToolItem(tbRight, SWT.PUSH);
		helpItem.setImage(SharedImages.getImage(SharedImages.HELP_BROWSER_16));
		helpItem.setToolTipText("Help");
		
		viewform.setTopRight(tbRight);
		
		// Create the content : an editable browser /////////////////////////// TODO
		final Composite displayArea = new Composite(viewform, SWT.NONE);
		final StackLayout stackLayout = new StackLayout();
		displayArea.setLayout(stackLayout);
		viewform.setContent(displayArea);

		Font monoFont = new Font(Display.getCurrent(), "mono", 10, SWT.NORMAL);
		final Text editableText = new Text(displayArea, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		editableText.setFont(monoFont);
		editableText.setTabs(3);
		
		final Browser browser = new Browser(displayArea, SWT.NONE);
		
		stackLayout.topControl = editableText;
		
		editableText.setText(this.codeTemplate);
		editableText.setSelection(this.defaultCursorPosition);
		editableText.setFocus();
		
		///////////////////////////
		// PropertiesComposite ////
		///////////////////////////
		
		Composite propertiesComposite = new Composite(shell, SWT.NONE);
		propertiesComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		propertiesComposite.setLayout(new GridLayout(2, false));

		// Source /////////////////
		Label sourceLabel = new Label(propertiesComposite, SWT.NONE);
		sourceLabel.setText("Source");
		
		final Text sourceText = new Text(propertiesComposite, SWT.BORDER);
		sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Author /////////////////
		Label authorLabel = new Label(propertiesComposite, SWT.NONE);
		authorLabel.setText("Author");
		
		final Text authorText = new Text(propertiesComposite, SWT.BORDER);
		authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		authorText.setText(ApplicationProperties.getDefaultAuthor());
		
		// Licence ////////////////
		Label licenceLabel = new Label(propertiesComposite, SWT.NONE);
		licenceLabel.setText("Licence");
		
		final Text licenceText = new Text(propertiesComposite, SWT.BORDER);
		licenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		licenceText.setText(ApplicationProperties.getDefaultLicense());
		
		// Create a horizontal separator
		Label separator = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		///////////////////////////
		// ButtonComposite ////////
		///////////////////////////
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		buttonComposite.setLayout(new GridLayout(2, true));
		
		// SaveButton /////////////
		final Button okButton = new Button(buttonComposite, SWT.PUSH);
		okButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true));
		okButton.setEnabled(true);
		okButton.setText("Ok");
		okButton.setToolTipText("Insert this picture");
		
		// CancelButton ///////////
		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
		cancelButton.setEnabled(true);
		cancelButton.setText("Cancel");
		cancelButton.setImage(SharedImages.getImage(SharedImages.WINDOW_CLOSE_22));
		cancelButton.setToolTipText("Cancel picture insertion");

        // Equalize buttons size (buttons size may change in others languages...) //
        Point cancelButtonPoint = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        Point saveButtonPoint = okButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

        if(cancelButtonPoint.x > saveButtonPoint.x) ((GridData) okButton.getLayoutData()).widthHint = cancelButtonPoint.x;
        else ((GridData) cancelButton.getLayoutData()).widthHint = saveButtonPoint.x;
        
        if(cancelButtonPoint.y > saveButtonPoint.y) ((GridData) okButton.getLayoutData()).heightHint = cancelButtonPoint.y;
        else ((GridData) cancelButton.getLayoutData()).heightHint = saveButtonPoint.y;
		
		// Set the Ok button as the default
		shell.setDefaultButton(okButton);
		
		///////////////////////////
		// Listeners //////////////
		///////////////////////////
		
		// switch button //////////
		switchDisplayItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(stackLayout.topControl == editableText) {
					stackLayout.topControl = browser;
					
					String content = editableText.getText();
					String script = scriptPreprocessor(content);
					filepath = buildPictureFile(script);
					if(isValidPictureFile(filepath)) {
						browser.setText(toHtml("<img src=\"" + filepath + "\" />"));   // TODO
					} else {
						// Error...
						String log = getLog();
						if(log != null && !log.equals("")) {
							browser.setText(toHtml(log));  // TODO
						} else {
							browser.setText(toHtml(DEFAULT_BUILD_ERR_MSG));  // TODO
						}
					}
					
					switchDisplayItem.setImage(SharedImages.getImage(SharedImages.EDIT_VIEW_16));
					switchDisplayItem.setToolTipText("Switch to edit view");
				} else {
					stackLayout.topControl = editableText;
					switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW_16));
					switchDisplayItem.setToolTipText("Switch to browser view");
				}
				displayArea.layout();
			}
		});
		
		// logviewer button ///////
		logviewerItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO
			}
		});
		
		// help button ////////////
		helpItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				help();
			}
		});
		
		// OkButton ///////////////
		okButton.addSelectionListener(new SelectionAdapter() {   // TODO
			public void widgetSelected(SelectionEvent event) {
				String content = editableText.getText();
				String script = scriptPreprocessor(content);
				filepath = buildPictureFile(script);
				
				boolean close = true;
				if(isValidPictureFile(filepath)) {
					imageTag = buildImageTag(filepath);          // TODO : redondance avec InsertImageDialog -> factoriser ! (dans package data)
				} else {
					imageTag = null;
					
					MessageBox message = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES | SWT.NO);
					message.setText("Error...");
					message.setMessage("An error occurred during processing.\n\nThe script may be wrong, would you fix it ?");
					int reply = message.open();
					
					close = (reply == SWT.YES ? false : true);
				}
				
				if(close) shell.close();
			}
		});
		
		// CancelButton ///////////
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				imageTag = null;
				shell.close();
			}
		});

	}


	/**
	 * Crée l'image correspondant au script courant.
	 * Retourne l'adresse du fichier image créé
	 * ou null en cas d'échec.
	 * 
	 * @param path
	 * @return
	 */
	private String buildPictureFile(String script) {
		
		String picturePath = null;
		
		if(script != null) {
			try {
				File scriptFile = File.createTempFile("opencal_" , SCRIPT_SUFFIX);
				scriptFile.deleteOnExit();
				
				// Écrire le script dans le fichier scriptFile
				BufferedWriter out = new BufferedWriter(new FileWriter(scriptFile));
				out.write(script);
				out.close();
		
				picturePath = runScript(scriptFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return picturePath;
	}
	
	
	/**
	 * Retourne le dernier log généré par runScript()
	 * @return
	 */
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	
	// TODO : À DÉMÉNAGER !!! /////////////////////////////////////////////////

	/**
	 * 
	 * @param path
	 * @return
	 */
	private String buildImageTag(String path) {	// prend src, licence, ... en argument
		String tag = null;
		String extension = extractExtension(path);	// TODO
		
		if(isValidPictureExtension(extension)) {				// TODO
			try {
				// Compute MD5SUM ///////
				MessageDigest md5  = MessageDigest.getInstance("MD5");
				
				FileInputStream     fis = new FileInputStream(path);
		        BufferedInputStream bis = new BufferedInputStream(fis);
		        DigestInputStream   dis = new DigestInputStream(bis, md5);
		        
		        while (dis.read() != -1);			// Reads the file, and updates the message digest
		        byte[] digest    = md5.digest();	// Completes the digest computation
		        String hexDigest = DataToolKit.byteArray2Hex(digest);
		        
		        dis.close();						// Add fis.close() and bis.close() ? No, "dis.close()" is enough to close the stream (checked with "lsof" Unix command).
				
				// Copy file ////////////
		        // TODO : vérifier l'emprunte MD5 du fichier, vérif que le fichier est bien fermé avec "lsof", ne pas copier le fichier si dest existe déjà, ...
		        File src = new File(path);
		        File dst = new File("/home/gremy/.opencal/materials/" + hexDigest + "." + extension); // TODO
		        
		        FileInputStream  srcStream = new FileInputStream(src);
		        FileOutputStream dstStream = new FileOutputStream(dst);
		        try {
		            byte[] buf = new byte[1024];
		            int i = 0;
		            while ((i = srcStream.read(buf)) != -1) {
		            	dstStream.write(buf, 0, i);
		            }
		        } finally {
		            if (srcStream != null) srcStream.close();
		            if (dstStream != null) dstStream.close();
		        }

		        tag = "<img file=\"" + hexDigest + "." + extension + "\" />";	// TODO : source, auteur, licence
				
			} catch(NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return tag;
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String extractExtension(String filename) {
		String extension = filename.toLowerCase().substring(filename.lastIndexOf('.') + 1);
		return extension;
	}
	
	/**
	 * TODO
	 * 
	 * @param extension
	 * @return
	 */
	public static boolean isValidPictureExtension(String extension) {
		Arrays.sort(IMAGE_EXTENSION_LIST); // ne pas supprimer, nécessaire pour "Arrays.binarySearch" (cf. /doc/openjdk-6-jre/api/java/util/Arrays.html
		return (Arrays.binarySearch(IMAGE_EXTENSION_LIST, extension.toLowerCase()) >= 0);
	}
	
	/**
	 * TODO
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isValidPictureFile(String path) {
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
	

	// TODO : À SUPPRIMER ? //////////////////////////////////////////////////
	
	
	/**
	 * TODO (useless here ?)
	 * 
	 * @param src
	 * @return
	 */
	final private String toHtml(String src) {
		StringBuffer html = new StringBuffer();
		
		html.append("<html><head><style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.EDITABLE_BROWSER_CSS);
		html.append("</style><head><body>");
		
		html.append(src);
		
		html.append("</body></html>");
		
		return html.toString();
	}
}
