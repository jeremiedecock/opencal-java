/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.jdhp.opencal.gui.images.SharedImages;
import org.jdhp.opencal.gui.widgets.EditableBrowser;

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
		questionArea.label.setText("Question");
		
		// Answer //////////
		final EditableBrowser answerArea = new EditableBrowser(verticalSashForm);
		answerArea.label.setText("Answer");

		// Tags ////////////
		final EditableBrowser tagsArea = new EditableBrowser(verticalSashForm);
		tagsArea.label.setText("Tags");
		
		///////////////////////////
		// Add Button /////////////
		///////////////////////////

		Button addButton = new Button(this.parentComposite, SWT.PUSH);
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		addButton.setText("Add this card");
		addButton.setImage(SharedImages.getImage(SharedImages.LIST_ADD));
		addButton.setToolTipText("Add this card to the knowledge base");
		
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(questionArea.editableText.getText().equals("")) {
					OpenCAL.mainWindow.printAlert("La question ne doit pas être vide !");
				} else {
					Card newCard = new Card(questionArea.editableText.getText(), answerArea.editableText.getText(), tagsArea.editableText.getText().split("\n"));
                    CardList.mainCardList.add(newCard);
					//OpenCAL.newCardList.add(newCard);
					//OpenCAL.allCardList.add(newCard);
					questionArea.editableText.setText("");
					answerArea.editableText.setText("");
					tagsArea.editableText.setText("");
					
					OpenCAL.mainWindow.updateStatus();
					//OpenCAL.mainWindow.setStatusLabel2("A : " + OpenCAL.newCardList.size(), OpenCAL.newCardList.size() + " cards added today");
				}
			
				// Set focus to the question field
				questionArea.editableText.setFocus();
			}
		});
	}
	
	/**
	 * 
	 */
	public void update() { }
	
}
