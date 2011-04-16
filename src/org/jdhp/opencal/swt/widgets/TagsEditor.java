/*
 * OpenCAL version 3.0
 * Copyright (c) 2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.swt.images.SharedImages;

public class TagsEditor {

	private CLabel titleLabel;
	
	final SashForm parent;
	final ViewForm viewform;
	
	final Text editableText;
	
	final ToolItem maximizeItem;
	
	final Vector<ModifyListener> modifyListeners;
	
	public TagsEditor(SashForm sashform) {
		parent = sashform;
		modifyListeners = new Vector<ModifyListener>();
		
		viewform = new ViewForm(parent, SWT.BORDER | SWT.FLAT);
		
		// Create the CLabel for the top left /////////////////////////////////
		this.titleLabel = new CLabel(viewform, SWT.NONE);
		
		titleLabel.setText("");
		titleLabel.setAlignment(SWT.LEFT);
		
		viewform.setTopLeft(titleLabel);
		
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
		Font monoFont = new Font(Display.getCurrent(), "mono", 10, SWT.NORMAL);
		
		editableText = new Text(viewform, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		editableText.setFont(monoFont);
		editableText.setTabs(3);
		
		viewform.setContent(editableText);
		
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
		
		editableText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent event) { }

			public void keyReleased(KeyEvent event) {
				// Display content assist information when user press botrh the CTRL and SPACE keys.
				if(((event.stateMask & SWT.CTRL) != 0) && (event.character == ' ')) {
					// Get tag list
					String[] fullTagList = OpenCAL.cardCollection.getTags(true);
					String basename = editableText.getText().split("\n", -1)[editableText.getCaretLineNumber()];
					basename = basename.toLowerCase().trim();
					ArrayList<String> tagList = new ArrayList<String>();
					for(int i=0 ; i<fullTagList.length ; i++) {
						if(fullTagList[i].startsWith(basename)) {
							tagList.add(fullTagList[i]);
						}
					}
					
					if(tagList.size() > 0) {
						// Display popup
						Shell shell = editableText.getShell();
						PopupList popup = new PopupList(shell);
						popup.setItems(tagList.toArray(new String[tagList.size()]));
						
						// Snippets from : http://www.ibm.com/developerworks/opensource/library/os-jface3/index.html
						Point p = editableText.getCaretLocation();
						p = shell.getDisplay().map(editableText, null, p.x, p.y);
						String choice = popup.open(new Rectangle(p.x, 
								                                 p.y + editableText.getLineHeight(),
								                                 400,
								                                 200));
						
						// Update text
						if(choice != null) {
							editableText.insert(choice.replaceFirst(basename, ""));
						}
					}
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
}
