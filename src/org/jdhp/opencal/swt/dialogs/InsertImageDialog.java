/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.dialogs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.util.DataToolKit;

public class InsertImageDialog extends Dialog {
	
	public static final String[] IMAGE_EXTENSION_LIST = {"png", "jpg", "jpeg"}; // les extensions doivent être en minuscule
	
	private String imageTag;
	
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
	 * Get the image tag
	 * 
	 * @return tag the image tag
	 */
	public String getImageTag() {
		return imageTag;
	}

	/**
	 * Set the image tag
	 * 
	 * @param imageTag the new tag
	 */
	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
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
		
		shell.setMinimumSize(480, 0);
		shell.setLayout(new GridLayout(1, true));
		
		///////////////////////////
		// FileAddressComposite ///
		///////////////////////////
		
		Composite fileAddressComposite = new Composite(shell, SWT.NONE);
		fileAddressComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fileAddressComposite.setLayout(new GridLayout(2, false));
		
		// Text control ///////////
		final Text text = new Text(fileAddressComposite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// File selection button //
		Button fileDialogButton = new Button(fileAddressComposite, SWT.PUSH);
		fileDialogButton.setText("Open...");
		fileDialogButton.setImage(SharedImages.getImage(SharedImages.DOCUMENT_OPEN_16));
		
		///////////////////////////
		// PreviewComposite ///////
		///////////////////////////
		
		
		///////////////////////////
		// PropertiesComposite ////
		///////////////////////////
		
		
		///////////////////////////
		// ButtonComposite ////////
		///////////////////////////
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		buttonComposite.setLayout(new GridLayout(2, true));
		
		// SaveButton /////////////
		final Button okButton = new Button(buttonComposite, SWT.PUSH);
		okButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true));
		okButton.setEnabled(false);
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
		
		// Text control ///////////
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String uri = text.getText();
				if(isValidPictureFile(uri)) {
					okButton.setEnabled(true);
				} else {
					okButton.setEnabled(false);
				}
			}
		});
		
		// File selection button //
		fileDialogButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// Make a file dialog
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
				
				// Configure the dialog
				String userHome = System.getProperty("user.home");
				fileDialog.setFilterPath(userHome);
//				if(!new File(openPictureFileDialog.getFilterPath()).exists()) {
//				// openPictureFileDialog.getFilterPath() don't exist"
//				//System.out.println(openPictureFileDialog.getFilterPath() + " don't exist");
//				String userHome = System.getProperty("user.home");	// TODO : et si le repertoire user.home n'existe pas non plus ? (est-ce que ça peut arriver ?)
//				openPictureFileDialog.setFilterPath(userHome);
//			}
		//
//			String filePath = openPictureFileDialog.open();
				
				// Open the dialog
				String uri = fileDialog.open();
				if(uri != null) {
					text.setText(uri);
				}
			}
		});
		
		// OkButton ///////////////
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String uri = text.getText();
				if(isValidPictureFile(uri)) {
					imageTag = buildImageTag(uri);
				} else {
					imageTag = null;
				}
				shell.close();
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
	 * 
	 * @param uri
	 * @return
	 */
	private String buildImageTag(String uri) {
		String tag = null;
		String extension = extractExtension(uri);	// TODO
		
		if(isValidPictureExtension(extension)) {				// TODO
			try {
				// Compute MD5SUM ///////
				MessageDigest md5  = MessageDigest.getInstance("MD5");
				
				FileInputStream     fis = new FileInputStream(uri);
		        BufferedInputStream bis = new BufferedInputStream(fis);
		        DigestInputStream   dis = new DigestInputStream(bis, md5);
		        
		        while (dis.read() != -1);			// Reads the file, and updates the message digest
		        byte[] digest    = md5.digest();	// Completes the digest computation
		        String hexDigest = DataToolKit.byteArray2Hex(digest);
		        
		        dis.close();						// Add fis.close() and bis.close() ? No, "dis.close()" is enough to close the stream (checked with "lsof" Unix command).
				
				// Copy file ////////////
		        // TODO : vérifier l'emprunte MD5 du fichier, vérif que le fichier est bien fermé avec "lsof", ne pas copier le fichier si dest existe déjà, ...
		        File src = new File(uri);
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
	 * 
	 * @param extension
	 * @return
	 */
	public static boolean isValidPictureExtension(String extension) {
		Arrays.sort(IMAGE_EXTENSION_LIST); // ne pas supprimer, nécessaire pour "Arrays.binarySearch" (cf. /doc/openjdk-6-jre/api/java/util/Arrays.html
		return (Arrays.binarySearch(IMAGE_EXTENSION_LIST, extension.toLowerCase()) >= 0);
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isValidPictureFile(String uri) {
		return uri != null && (new File(uri)).exists() && isValidPictureExtension(extractExtension(uri));
	}
}
