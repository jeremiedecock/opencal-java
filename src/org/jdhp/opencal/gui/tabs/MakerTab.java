/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
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
		
		Font monoFont = new Font(this.parentComposite.getDisplay(), "mono", 10, SWT.NORMAL);
		
		// Question ////////
		final EditableBrowser questionArea = new EditableBrowser(this.parentComposite);
		questionArea.label.setText("Question");
		questionArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// Answer //////////
		final EditableBrowser answerArea = new EditableBrowser(this.parentComposite);
		answerArea.label.setText("Answer");
		answerArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Tags ////////////
		final EditableBrowser tagsArea = new EditableBrowser(this.parentComposite);
		tagsArea.label.setText("Tags");
		tagsArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// Button /////////
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
					OpenCAL.newCardList.add(newCard);
					OpenCAL.allCardList.add(newCard);
					questionArea.editableText.setText("");
					answerArea.editableText.setText("");
					tagsArea.editableText.setText("");
//					OpenCAL.mainWindow.setStatusLabel1("Card recorded", "Card recorded");
//					
//					// TODO : faire quelque chose de plus joli pour les 6 lignes suivantes (avec un thread dédié, ...)
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e2) {
//						
//					}
//					OpenCAL.mainWindow.setStatusLabel1("", "");
				}
			
				// Set focus to the question field
				questionArea.editableText.setFocus();
			}
		});
	}
	
	/**
	 * 
	 */
	public void update() {
		OpenCAL.mainWindow.setStatusLabel1("", "");
		OpenCAL.mainWindow.setStatusLabel2("", "");
		OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
		OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
	}
	
}
