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
		Group questionGroup = new Group(this.parentComposite, SWT.NONE);
		questionGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		questionGroup.setLayout(new GridLayout(1, false));
		questionGroup.setText("Question");
		
		final Text questionText = new Text(questionGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		questionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		questionText.setFont(monoFont);
		questionText.setTabs(3);
		
		// Answer //////////
		Group answerGroup = new Group(this.parentComposite, SWT.NONE);
		answerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerGroup.setLayout(new GridLayout(1, false));
		answerGroup.setText("Answer");
		
		final Text answerText = new Text(answerGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		answerText.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerText.setFont(monoFont);
		answerText.setTabs(3);

		// Tags ////////////
		Group tagGroup = new Group(this.parentComposite, SWT.NONE);
		tagGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		tagGroup.setLayout(new GridLayout(1, false));
		tagGroup.setText("Tags");
		
		final Text tagsText = new Text(tagGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		tagsText.setLayoutData(new GridData(GridData.FILL_BOTH));
		tagsText.setFont(monoFont);
		tagsText.setTabs(3);
		
		// Button /////////
		Button addButton = new Button(this.parentComposite, SWT.PUSH);
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		addButton.setText("Add this card");
		addButton.setImage(SharedImages.getImage(SharedImages.LIST_ADD));
		addButton.setToolTipText("Add this card to the knowledge base");
		
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(questionText.getText().equals("")) {
					OpenCAL.mainWindow.printAlert("La question ne doit pas être vide !");
				} else {
					Card newCard = new Card(questionText.getText(), answerText.getText(), tagsText.getText().split("\n"));
					OpenCAL.newCardList.add(newCard);
					OpenCAL.allCardList.add(newCard);
					questionText.setText("");
					answerText.setText("");
					tagsText.setText("");
					OpenCAL.mainWindow.setStatusLabel1("Card #" + newCard.getId() + " recorded", "Card #" + newCard.getId() + " recorded");
				}
			
				// Set focus to the question field
				questionText.setFocus();
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
