/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.widgets;

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
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdhp.opencal.data.UserProperties;
import org.jdhp.opencal.swt.MainWindow;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.util.DataToolKit;

public class EditableBrowser {

	public static final String[] IMAGE_EXTENSION_LIST = {"png", "jpg", "jpeg"}; // les extensions doivent être en minuscule
	
	private CLabel titleLabel;
	
	private final SashForm parent;
	private final ViewForm viewform;
	
	private final StackLayout stackLayout;
	
	private final Text editableText;
	private final Browser browser;
	private final FileDialog openPictureFileDialog;
	
	private final Vector<ModifyListener> modifyListeners;
	
	public EditableBrowser(SashForm sashform) {
		parent = sashform;
		modifyListeners = new Vector<ModifyListener>();
		
		viewform = new ViewForm(parent, SWT.BORDER | SWT.FLAT);
		
		// Create the CLabel for the top left /////////////////////////////////
		this.titleLabel = new CLabel(viewform, SWT.NONE);
		titleLabel.setText("");
		titleLabel.setAlignment(SWT.LEFT);
		viewform.setTopLeft(titleLabel);
		
		// Create the top center toolbar //////////////////////////////////////
		ToolBar tbCenter = new ToolBar(viewform, SWT.FLAT);
		
		final ToolItem switchDisplayItem = new ToolItem(tbCenter, SWT.PUSH);
		switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW));
		switchDisplayItem.setToolTipText("Switch display mode");
		
		final ToolItem insertPictureItem = new ToolItem(tbCenter, SWT.PUSH);
		insertPictureItem.setImage(SharedImages.getImage(SharedImages.INSERT_IMAGE));
		insertPictureItem.setToolTipText("Insert picture");
		
		viewform.setTopCenter(tbCenter);
		
		// Create the top right buttons (size) /////////////////////////////////
		ToolBar tbRight = new ToolBar(viewform, SWT.FLAT);
		
//		final ToolItem minimizeItem = new ToolItem(tbRight, SWT.PUSH);
//		minimizeItem.setImage(SharedImages.getImage(SharedImages.MINIMALIZE));
//		minimizeItem.setToolTipText("Minimize");
		
		final ToolItem maximizeItem = new ToolItem(tbRight, SWT.PUSH);
		maximizeItem.setImage(SharedImages.getImage(SharedImages.MAXIMIZE));
		maximizeItem.setToolTipText("Maximize");
		
		viewform.setTopRight(tbRight);
		
		// Create the content : an editable browser ///////////////////////////
		final Composite displayArea = new Composite(viewform, SWT.NONE);
		stackLayout = new StackLayout();
		displayArea.setLayout(stackLayout);
		viewform.setContent(displayArea);

		Font monoFont = new Font(Display.getCurrent(), "mono", 10, SWT.NORMAL);
		editableText = new Text(displayArea, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		editableText.setFont(monoFont);
		editableText.setTabs(3);
		
		browser = new Browser(displayArea, SWT.NONE);
		browser.setText("Test");
		
		stackLayout.topControl = editableText;
		
		///////////////////////////////////////////////////////////////////////
		
		// FileDialog
		openPictureFileDialog = new FileDialog(editableText.getShell(), SWT.OPEN);
		String userHome = System.getProperty("user.home");
		openPictureFileDialog.setFilterPath(userHome);
		
		///////////////////////////////////////////////////////////////////////

		editableText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				//ModifyEvent modifyEvent = new ModifyEvent(viewform); 
				Iterator<ModifyListener> it = modifyListeners.iterator();
				
				while(it.hasNext()) {
					it.next().modifyText(event); // TODO : ne faut-il pas construire un nouvel event, la source est erronée
				}
			}
		});
		
		maximizeItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Control maximizedControl = parent.getMaximizedControl();
				if(maximizedControl != null && maximizedControl.equals(viewform)) {
					parent.setMaximizedControl(null);
					maximizeItem.setImage(SharedImages.getImage(SharedImages.MAXIMIZE));
					maximizeItem.setToolTipText("Maximize");
				} else {
					parent.setMaximizedControl(viewform);
					maximizeItem.setImage(SharedImages.getImage(SharedImages.RESTORE));
					maximizeItem.setToolTipText("Restore");
				}
			}
		});
		
		switchDisplayItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(stackLayout.topControl == editableText) {
					stackLayout.topControl = browser;
					browser.setText(toHtml(editableText.getText()));
					switchDisplayItem.setImage(SharedImages.getImage(SharedImages.EDIT_VIEW));
					switchDisplayItem.setToolTipText("Switch to edit view");
					insertPictureItem.setEnabled(false);
				} else {
					stackLayout.topControl = editableText;
					switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW));
					switchDisplayItem.setToolTipText("Switch to browser view");
					insertPictureItem.setEnabled(true);
				}
				editableText.getParent().layout();
			}
		});
		
		insertPictureItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(!new File(openPictureFileDialog.getFilterPath()).exists()) {
					// openPictureFileDialog.getFilterPath() don't exist"
					//System.out.println(openPictureFileDialog.getFilterPath() + " don't exist");
					String userHome = System.getProperty("user.home");	// TODO : et si le repertoire user.home n'existe pas non plus ? (est-ce que ça peut arriver ?)
					openPictureFileDialog.setFilterPath(userHome);
				}
				
				String filePath = openPictureFileDialog.open();
				
				if(filePath != null) {
					String extension = EditableBrowser.extractExtension(filePath);	// TODO
					
					if(EditableBrowser.isAValidImageExtension(extension)) {				// TODO
						try {
							// Compute MD5SUM ///////
							MessageDigest md5  = MessageDigest.getInstance("MD5");
							
							FileInputStream     fis = new FileInputStream(filePath);
					        BufferedInputStream bis = new BufferedInputStream(fis);
					        DigestInputStream   dis = new DigestInputStream(bis, md5);
					        
					        while (dis.read() != -1);			// Reads the file, and updates the message digest
					        byte[] digest    = md5.digest();	// Completes the digest computation
					        String hexDigest = DataToolKit.byteArray2Hex(digest);
					        
					        dis.close();						// Add fis.close() and bis.close() ? No, "dis.close()" is enough to close the stream (checked with "lsof" Unix command).
							
							// Copy file ////////////
					        // TODO : vérifier l'emprunte MD5 du fichier, vérif que le fichier est bien fermé avec "lsof", ne pas copier le fichier si dest existe déjà, ...
					        File src = new File(filePath);
					        File dst = new File("/home/gremy/.opencal/materials/" + hexDigest + "." + extension); // TODO
					        
					        FileInputStream  srcStream = new FileInputStream(src);
					        FileOutputStream dstStream = new FileOutputStream(dst);
					        try {
					            byte[] buf = new byte[1024];
					            int i = 0;
					            while ((i = srcStream.read(buf)) != -1) {
					            	dstStream.write(buf, 0, i);
					            }
					        }
					        finally {
					            if (srcStream != null) srcStream.close();
					            if (dstStream != null) dstStream.close();
					        }

							
							// Insert element ///////
					        String tag = "<img file=\"" + hexDigest + "." + extension + "\" />";	// TODO : source, auteur, licence
							editableText.insert(tag);
							
						} catch(NoSuchAlgorithmException e) {
							e.printStackTrace();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Bad extension");
						// TODO
					}
				}
			}
		});
		

		if(stackLayout.topControl == editableText) {
			insertPictureItem.setEnabled(true);
		} else {
			insertPictureItem.setEnabled(false);
		}
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the ModifyListener interface.
	 * 
	 * @param listener the listener which should be notified
	 */
	public void addModifyListener(ModifyListener listener) {
		this.modifyListeners.addElement(listener);
	}
	
	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
	 * 
	 * @param listener the listener which should no longer be notified
	 */
	public void removeModifyListener(ModifyListener listener) {
		this.modifyListeners.removeElement(listener);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return this.titleLabel.getText();
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.titleLabel.setText(title);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getText() {
		return this.editableText.getText();
	}

	/**
	 * 
	 * @param title
	 */
	public void setText(String text) {
		this.editableText.setText(text);
		this.browser.setText(toHtml(text));
	}
	
	/**
	 * Returns the editable state.
	 * 
	 * @return
	 */
	public boolean getEditable() {
		return this.editableText.getEditable();
	}

	/**
	 * Sets the editable state.
	 * 
	 * @param title
	 */
	public void setEditable(boolean isEditable) {
		this.editableText.setEditable(isEditable);
	}
	
	/**
	 * Causes the editableText to have the keyboard focus, such that all keyboard events will be delivered to it.
	 * Focus reassignment will respect applicable platform constraints. 
	 *
	 * @return true if the control got focus, and false if it was unable to.
	 */
	public boolean setFocus() {
		return editableText.setFocus();
	}
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	final private String toHtml(String src) {
		StringBuffer html = new StringBuffer();
		
		html.append("<html><head><style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.EDITABLE_BROWSER_CSS);
		html.append("</style><head><body>");
		
		html.append(filter(src));
		
		html.append("</body></html>");
		
		return html.toString();
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	final private String filter(String text) {
		// Empèche l'interprétation d'eventuelles fausses balises comprises dans les cartes 
		String html = text.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		
		// Rétabli l'interprétation pour les balises images
		String pattern = "&lt;img file=\"([0-9abcdef]{32}.(png|jpg|jpeg))\" /&gt;";
		Pattern regPat = Pattern.compile(pattern);
		Matcher matcher = regPat.matcher(html);
		html = matcher.replaceAll("<img src=\"" + UserProperties.getImgPath() + "$1\" />");
		
		return html;
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
	public static boolean isAValidImageExtension(String extension) {
		Arrays.sort(IMAGE_EXTENSION_LIST); // ne pas supprimer, nécessaire pour "Arrays.binarySearch" (cf. /doc/openjdk-6-jre/api/java/util/Arrays.html
		return (Arrays.binarySearch(EditableBrowser.IMAGE_EXTENSION_LIST, extension.toLowerCase()) >= 0);
	}
}
