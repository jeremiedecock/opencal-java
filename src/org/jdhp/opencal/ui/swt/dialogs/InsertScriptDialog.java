/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.dialogs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.ui.swt.images.SharedImages;

public abstract class InsertScriptDialog extends InsertImageDialog {
	
	public static final int EDITOR = 1;
	public static final int BROWSER = 2;
	
	public static final String DEFAULT_BUILD_ERR_MSG = "Unknown error";  // TODO
	
	public static final String SCRIPT_SUFFIX = ".src";
	
	protected String codeTemplate = "";

	protected int defaultCursorPosition = 0;
	
	protected String log;
	
	///////////////////////////////////
	
	/**
	 * Interprète le script et retourne l'adresse de l'image générée.
	 * 
	 * @param srcPath
	 * @return
	 */
	protected abstract String runScript(String srcPath);
	
	/**
	 * Préprocesseur :
	 * construit le code source complet du script (ajout éventuel d'entête, de pied de page, filtrage, ...)
	 * à partir de la chaîne content.
	 * 
	 * @param content
	 * @return 
	 */
	protected abstract String scriptPreprocessor(String content);
	
	/**
	 * Display help content about the script language.
	 */
	protected abstract void help();

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
	 * Creates the dialog's contents
	 * 
	 * @param shell the dialog window
	 */
	protected final void createContents(final Shell shell) {
		
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
					String built_picture_filepath = buildPictureFile(script);
					if(isValidFile(built_picture_filepath)) {
						filepath = built_picture_filepath;
						browser.setText(htmlPreview());
					} else {
						// Error...
						filepath = null;
						String log = InsertScriptDialog.this.log;
						if(log != null && !log.equals("")) {
							browser.setText(log);  // TODO
						} else {
							browser.setText(DEFAULT_BUILD_ERR_MSG);  // TODO
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
				String built_picture_filepath = buildPictureFile(script);
				
				boolean close = true;
				if(isValidFile(built_picture_filepath)) {
					filepath = built_picture_filepath;
					tag = buildTag(filepath);
				} else {
					filepath = null;
					tag = null;
					
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
				tag = null;
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
	private final String buildPictureFile(String script) {
		
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
}
