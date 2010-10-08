/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2010 Jérémie Decock
 */

package org.jdhp.opencal.swt.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.swt.widgets.EditableBrowser;
import org.jdhp.opencal.swt.widgets.TagsEditor;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MakerTab {

	final private Composite parentComposite;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public MakerTab(Composite parentComposite) {
		
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(1, false));

		///////////////////////////
		// SasheForm //////////////
		///////////////////////////

        SashForm verticalSashForm = new SashForm(this.parentComposite, SWT.VERTICAL);
		verticalSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// Question ////////
		final EditableBrowser questionArea = new EditableBrowser(verticalSashForm);
		questionArea.setTitle("Question");
		
		// Answer //////////
		final EditableBrowser answerArea = new EditableBrowser(verticalSashForm);
		answerArea.setTitle("Answer");

		// Tags ////////////
		final TagsEditor tagsArea = new TagsEditor(verticalSashForm);
		tagsArea.setTitle("Tags");
		
		verticalSashForm.setWeights(new int[] {40, 40, 20});
		
		///////////////////////////
		// Add Button /////////////
		///////////////////////////

		final Button addButton = new Button(this.parentComposite, SWT.PUSH);
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		addButton.setText("Add this card");
		addButton.setImage(SharedImages.getImage(SharedImages.LIST_ADD_24));
		addButton.setToolTipText("Add this card to the knowledge base");
		addButton.setEnabled(false);

		///////////////////////////
		// Listeners //////////////
		///////////////////////////

		questionArea.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// Question can't be empty
				if(questionArea.getText().trim().equals("")) {
					addButton.setEnabled(false);
				} else {
					addButton.setEnabled(true);
				}
			}
		});
		
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Card newCard = new Card(questionArea.getText(), answerArea.getText(), tagsArea.getText().split("\n"));
				OpenCAL.cardCollection.add(newCard);
				
				questionArea.setText("");
				answerArea.setText("");
				tagsArea.setText("");
				
				OpenCAL.mainWindow.updateStatus();
			
				// Set focus to the question field
				questionArea.setFocus();
			}
		});
	}
	
	/**
	 * 
	 */
	public void update() { }
	
}
