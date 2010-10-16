/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.dialogs;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.util.DataToolKit;

public class InsertImageDialog extends Dialog {
	
	public static final String PREVIEW_DEFAULT_MESSAGE = "No preview available.";
	
	public static final String[] IMAGE_EXTENSION_LIST = {"png", "jpg", "jpeg"}; // les extensions doivent être en minuscule
	
	private String imageTag;
	
	private String filepath;
	
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
		
		final ScrolledComposite scrolledPreviewComposite = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);

		final Composite previewComposite = new Composite(scrolledPreviewComposite, SWT.NONE);
		previewComposite.setLayout(new GridLayout(1, false));
		
		final Label label = new Label(previewComposite, SWT.NONE);
		label.setText(PREVIEW_DEFAULT_MESSAGE);
		label.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
		scrolledPreviewComposite.setContent(previewComposite);
		scrolledPreviewComposite.setMinSize(480, 320);
		
		scrolledPreviewComposite.setExpandHorizontal(true);
		scrolledPreviewComposite.setExpandVertical(true);
		
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
		
		// Licence ////////////////
		Label licenceLabel = new Label(propertiesComposite, SWT.NONE);
		licenceLabel.setText("Licence");
		
		final Text licenceText = new Text(propertiesComposite, SWT.BORDER);
		licenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
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
		text.addModifyListener(new ModifyListener() {   // TODO
			public void modifyText(ModifyEvent arg0) {
				String address = text.getText();
				
				if(isValidURL(address)) {
					filepath = downloadFile(address);
				} else {
					filepath = address;
				}
				
				if(isValidPictureFile(filepath)) {
					okButton.setEnabled(true);
					label.setText("");
					label.setImage(new Image(shell.getDisplay(), filepath)); // TODO : Image.dispose()
					previewComposite.layout();
					scrolledPreviewComposite.setMinSize(previewComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
				} else {
					okButton.setEnabled(false);
					label.setText(PREVIEW_DEFAULT_MESSAGE);
					label.setImage(null);
					previewComposite.layout();
					scrolledPreviewComposite.setMinSize(0, 0);
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
				String path = fileDialog.open();
				if(path != null) {
					text.setText(path);
				}
			}
		});
		
		// OkButton ///////////////
		okButton.addSelectionListener(new SelectionAdapter() {   // TODO
			public void widgetSelected(SelectionEvent event) {
				if(isValidPictureFile(filepath)) {
					imageTag = buildImageTag(filepath);
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
	 * @param path
	 * @return
	 */
	private String buildImageTag(String path) {
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
	 * Download a remote file and copy it into the temp directory.
	 * 
	 * @param fileAddress the remote file url
	 * @return string the local file address
	 */
	public static String downloadFile(String fileAddress) {
		
		File fileOut =  null;

		DataInputStream inStream = null;
		FileOutputStream outStream = null;
		
		if(fileAddress != null) {
			try {
				URL url = new URL(fileAddress);
				URLConnection urlConnection = url.openConnection();
				
				// Output stream
				fileOut = File.createTempFile("opencal_", "." + extractExtension(fileAddress));
				fileOut.deleteOnExit();
				outStream = new FileOutputStream(fileOut);
				
				// Input stream
				inStream = new DataInputStream(urlConnection.getInputStream());
				
				// Copy the remote file
				int data;
				while((data = inStream.read()) != -1) { // TODO
					outStream.write(data); // TODO
				}
			} catch (MalformedURLException e) {
				//e.printStackTrace(); // TODO
			} catch (IOException e) {
				e.printStackTrace(); // TODO
			} finally {
				try {
					if(inStream != null) inStream.close();
					if(outStream != null) outStream.close();
				} catch (IOException e) {
					System.out.println("Error : can't close file.");
				}
			}
		}
		
		return (fileOut != null ? fileOut.getAbsolutePath() : null);
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
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isValidURL(String address) {
		boolean valid = true;
		
		try {
			URL url = new URL(address);
		} catch (MalformedURLException e) {
			valid = false;
		}
		
		return address != null && valid;
	}
}
