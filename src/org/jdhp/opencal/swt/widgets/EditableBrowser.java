/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */

package org.jdhp.opencal.swt.widgets;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdhp.opencal.data.ApplicationProperties;
import org.jdhp.opencal.swt.MainWindow;
import org.jdhp.opencal.swt.dialogs.InsertDotDialog;
import org.jdhp.opencal.swt.dialogs.InsertImageDialog;
import org.jdhp.opencal.swt.dialogs.InsertLatexDialog;
import org.jdhp.opencal.swt.dialogs.InsertPlotDialog;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.util.HTML;

public class EditableBrowser {

	public static final int EDITOR = 1;
	public static final int BROWSER = 2;
	
	private CLabel titleLabel;
	
	private final SashForm parent;
	private final ViewForm viewform;
	
	private final StackLayout stackLayout;
	
	private final Text editableText;
	private final Browser browser;
	
	private final ToolItem maximizeItem;
	private final ToolItem switchDisplayItem;
	private final ToolItem insertPictureItem;
	private final ToolItem insertLatexItem;
	private final ToolItem insertGnuplotItem;
	private final ToolItem insertDotItem;
	
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

		switchDisplayItem = new ToolItem(tbCenter, SWT.PUSH);
		switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW_16));
		switchDisplayItem.setToolTipText("Switch display mode");
		
		insertPictureItem = new ToolItem(tbCenter, SWT.PUSH);
		insertPictureItem.setImage(SharedImages.getImage(SharedImages.INSERT_IMAGE_16));
		insertPictureItem.setToolTipText("Insert picture");
		
		insertLatexItem = new ToolItem(tbCenter, SWT.PUSH);
		insertLatexItem.setImage(SharedImages.getImage(SharedImages.LATEX_16));
		insertLatexItem.setToolTipText("Insert LaTeX formula");
		
		insertGnuplotItem = new ToolItem(tbCenter, SWT.PUSH);
		insertGnuplotItem.setImage(SharedImages.getImage(SharedImages.PLOT_16));
		insertGnuplotItem.setToolTipText("Insert Gnuplot graphics");
		
		insertDotItem = new ToolItem(tbCenter, SWT.PUSH);
		insertDotItem.setImage(SharedImages.getImage(SharedImages.DOT_16));
		insertDotItem.setToolTipText("Insert Dot graph");
		
		viewform.setTopCenter(tbCenter);
		
		// Create the top right buttons (size) /////////////////////////////////
		ToolBar tbRight = new ToolBar(viewform, SWT.FLAT);
		
//		final ToolItem minimizeItem = new ToolItem(tbRight, SWT.PUSH);
//		minimizeItem.setImage(SharedImages.getImage(SharedImages.MINIMALIZE));
//		minimizeItem.setToolTipText("Minimize");
		
		maximizeItem = new ToolItem(tbRight, SWT.PUSH);
		maximizeItem.setImage(SharedImages.getImage(SharedImages.MAXIMIZE_16));
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
		
        ///////////////////////////
		// Listeners //////////////
		///////////////////////////

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
					maximizeItem.setImage(SharedImages.getImage(SharedImages.MAXIMIZE_16));
					maximizeItem.setToolTipText("Maximize");
				} else {
					parent.setMaximizedControl(viewform);
					maximizeItem.setImage(SharedImages.getImage(SharedImages.RESTORE_16));
					maximizeItem.setToolTipText("Restore");
				}
			}
		});
		
		switchDisplayItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(getMode() == EDITOR) {
					setMode(BROWSER);
				} else {
					setMode(EDITOR);
				}
			}
		});
		
		insertPictureItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InsertImageDialog dialog = new InsertImageDialog(parent.getShell());
				String imageTag = dialog.open();
				if(imageTag != null) {
					editableText.insert(imageTag);
				}
			}
		});
		
		insertLatexItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InsertLatexDialog dialog = new InsertLatexDialog(parent.getShell());
				String imageTag = dialog.open();
				if(imageTag != null) {
					editableText.insert(imageTag);
				}
			}
		});
		
		insertGnuplotItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InsertPlotDialog dialog = new InsertPlotDialog(parent.getShell());
				String imageTag = dialog.open();
				if(imageTag != null) {
					editableText.insert(imageTag);
				}
			}
		});
		
		insertDotItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InsertDotDialog dialog = new InsertDotDialog(parent.getShell());
				String imageTag = dialog.open();
				if(imageTag != null) {
					editableText.insert(imageTag);
				}
			}
		});
		

		if(stackLayout.topControl == editableText) {
			insertPictureItem.setEnabled(true);
			insertLatexItem.setEnabled(true);
			insertGnuplotItem.setEnabled(true);
			insertDotItem.setEnabled(true);
		} else {
			insertPictureItem.setEnabled(false);
			insertLatexItem.setEnabled(false);
			insertGnuplotItem.setEnabled(false);
			insertDotItem.setEnabled(false);
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
	 * @return
	 */
	public int getMode() {
		return stackLayout.topControl == editableText ? EDITOR : BROWSER;
	}

	/**
	 * 
	 * @param title
	 */
	public void setMode(int mode) {
		if(mode == EDITOR) {
			stackLayout.topControl = editableText;
			switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW_16));
			switchDisplayItem.setToolTipText("Switch to browser view");
			insertPictureItem.setEnabled(true);
			insertLatexItem.setEnabled(true);
			insertGnuplotItem.setEnabled(true);
			insertDotItem.setEnabled(true);
		} else {
			stackLayout.topControl = browser;
			browser.setText(toHtml(editableText.getText()));
			switchDisplayItem.setImage(SharedImages.getImage(SharedImages.EDIT_VIEW_16));
			switchDisplayItem.setToolTipText("Switch to edit view");
			insertPictureItem.setEnabled(false);
			insertLatexItem.setEnabled(false);
			insertGnuplotItem.setEnabled(false);
			insertDotItem.setEnabled(false);
		}
		editableText.getParent().layout();
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
		String html = HTML.replaceSpecialChars(text);

//		// Espace (doit être traité comme n'importe quel autre caractère pour conserver plusieurs espaces successifs, l'indentation, etc.
//		html = html.replaceAll("\t", "    ");
//		html = html.replaceAll(" ", "&nbsp;");
//		
//		// Retour à la ligne
//		html = html.replaceAll("\n", "<br />");
		
		// Rétabli l'interprétation pour les balises images
		String pattern = "&lt;img file=&quot;([0-9abcdef]{32}.(png|jpg|jpeg))&quot; /&gt;";
		Pattern regPat = Pattern.compile(pattern);
		Matcher matcher = regPat.matcher(html);
		html = matcher.replaceAll("<img src=\"" + ApplicationProperties.getImgPath() + "$1\" />");
		
		return html;
	}
	
}
