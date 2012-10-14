package org.jdhp.opencal.ui.swt.dialogs;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.ui.swt.images.SharedImages;
import org.jdhp.opencal.util.DataToolKit;

public abstract class InsertFileDialog extends InsertDialog {
	
	public static final String PREVIEW_DEFAULT_MESSAGE = "No preview available.";
	
	protected String filepath;

	public InsertFileDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	protected abstract String buildTag(String path);
	
	protected abstract boolean isValidExtension(String extension);
	
	protected abstract String htmlPreview();

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell the dialog window
	 */
	protected void createContents(final Shell shell) {
		
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
		
		final Browser browser = new Browser(shell, SWT.BORDER);
		browser.setText(PREVIEW_DEFAULT_MESSAGE);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		///////////////////////////
		// PropertiesComposite ////
		///////////////////////////
		
		// TODO: factorize PropertiesComposite (defined once for all Dialogs)
		
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
		okButton.setEnabled(false);
		okButton.setText("Ok");
		okButton.setToolTipText("Insert this object");
		
		// CancelButton ///////////
		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
		cancelButton.setEnabled(true);
		cancelButton.setText("Cancel");
		cancelButton.setImage(SharedImages.getImage(SharedImages.WINDOW_CLOSE_22));
		cancelButton.setToolTipText("Cancel object insertion");

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
				
				String local_file_path = null;
				if(isValidURL(address)) {
					local_file_path = downloadFile(address);
				} else {
					local_file_path = address;  // TODO: null ?
				}
				
				if(isValidFile(local_file_path)) {
					filepath = local_file_path;
					okButton.setEnabled(true);
				} else {
					filepath = null;
					okButton.setEnabled(false);
				}
				
				browser.setText(htmlPreview());
			}
		});
		
		// File selection button //
		fileDialogButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// Make a file dialog
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
				
				// Configure the dialog
				String dir = ApplicationProperties.getLastInsertFilePath();
				if(!new File(dir).exists()) {
					dir = System.getProperty("user.home");
				}
				fileDialog.setFilterPath(dir);
				
				// Open the dialog
				String path = fileDialog.open();
				if(path != null) {
					text.setText(path);
					ApplicationProperties.setLastInsertFilePath(new File(path).getParent()); // save dirname
				}
			}
		});
		
		// OkButton ///////////////
		okButton.addSelectionListener(new SelectionAdapter() {   // TODO
			public void widgetSelected(SelectionEvent event) {
				if(filepath != null) {
					tag = buildTag(filepath);
				} else {
					tag = null;
				}
				shell.close();
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
	 * 
	 * @param path
	 * @return
	 */
	protected final String insertFileInPKB(String path) {	// prend src, licence, ... en argument
		String hexDigest = null;
		String extension = extractExtension(path);	// TODO
		
		if(isValidExtension(extension)) {				// TODO
			try {
				// Compute MD5SUM ///////
				MessageDigest md5  = MessageDigest.getInstance("MD5");
				
				FileInputStream     fis = new FileInputStream(path);
		        BufferedInputStream bis = new BufferedInputStream(fis);
		        DigestInputStream   dis = new DigestInputStream(bis, md5);
		        
		        while (dis.read() != -1);			// Reads the file, and updates the message digest
		        byte[] digest    = md5.digest();	// Completes the digest computation
		        hexDigest = DataToolKit.byteArray2Hex(digest);
		        
		        dis.close();						// Add fis.close() and bis.close() ? No, "dis.close()" is enough to close the stream (checked with "lsof" Unix command).

		        // Make destination directory if it don't exist
		        // TODO
		        File dstDir = new File(ApplicationProperties.getImgPath());
		        if(!dstDir.exists()) {
		        	dstDir.mkdirs();
		        }

				// Copy file ////////////
		        // TODO : vérifier l'emprunte MD5 du fichier, vérif que le fichier est bien fermé avec "lsof", ne pas copier le fichier si dest existe déjà, ...
		        File src = new File(path);
		        File dst = new File(ApplicationProperties.getImgPath() + hexDigest + "." + extension); // TODO
		        
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
		        
			} catch(NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return hexDigest;
	}
	
	protected boolean isValidFile(String path) {
		// Check if the file exists
		return (new File(path)).exists();
	}
	
	// TOOLS //////////////////////////////////////////////////////////////////
	
	/**
	 * Download a remote file and copy it into the temp directory.
	 * 
	 * TODO: "protected final" or "public static" ?
	 * 
	 * @param fileAddress the remote file url
	 * @return string the local file address
	 */
	protected final String downloadFile(String fileAddress) {
		
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
	 * TODO: "protected final" or "public static" ?
	 * 
	 * @param filename
	 * @return
	 */
	protected final String extractExtension(String filename) {
		String extension = filename.toLowerCase().substring(filename.lastIndexOf('.') + 1);
		return extension;
	}
	
	/**
	 * 
	 * TODO: "protected final" or "public static" ?
	 * 
	 * @param url
	 * @return
	 */
	protected final boolean isValidURL(String address) {
		boolean valid = true;
		
		try {
			URL url = new URL(address);
		} catch (MalformedURLException e) {
			valid = false;
		}
		
		return address != null && valid;
	}
}
