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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.gui.images.SharedImages;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ExplorerTab {

	final private static int ALL_CARDS = 0;
	
	final private static int REVIEWED_CARDS = 1;
	
	final private static int MADE_CARDS = 2;
	
	final private static int DISABLED_CARDS = 3;
	
	private static int currentDisplayMode; // pas très propre de mettre ça en static ?
	
	final private Composite parentComposite;
	
	final private String[] displayModes = {"All Cards", "Reviewed Cards", "New Cards", "Suspended Cards"};
	
	final List cardsList;
	
	final Combo displayModeCombo;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ExplorerTab(Composite parentComposite) {
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(2, false));
		
		final Text questionText;
		final Text answerText;
		final Text tagsText;
		
		ExplorerTab.currentDisplayMode = ExplorerTab.ALL_CARDS;

		///////////////////////////////////////////////////////////////////////
		// CardSelectionComposite /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite cardSelectionComposite = new Composite(this.parentComposite, SWT.NONE);
		cardSelectionComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		cardSelectionComposite.setLayout(new GridLayout(1, false));
		
		// displayModeCombo ////////////
		displayModeCombo = new Combo(cardSelectionComposite, SWT.BORDER | SWT.READ_ONLY);
		displayModeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		displayModeCombo.setItems(displayModes);
		displayModeCombo.select(ExplorerTab.currentDisplayMode);
		
		// cardsList ////////////
//		final List cardsList = new List(cardSelectionComposite, SWT.BORDER | SWT.V_SCROLL);
		cardsList = new List(cardSelectionComposite, SWT.BORDER | SWT.V_SCROLL);
		cardsList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		cardsList.setItems(new String[0]);
		
		///////////////////////////////////////////////////////////////////////
		// EditionCardComposite ///////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite editionCardComposite = new Composite(this.parentComposite, SWT.NONE);
		editionCardComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		editionCardComposite.setLayout(new GridLayout(1, false));
		
		Font monoFont = new Font(this.parentComposite.getDisplay(), "mono", 10, SWT.NORMAL);
		
		// Question ////////
		Group questionGroup = new Group(editionCardComposite, SWT.NONE);
		questionGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		questionGroup.setLayout(new GridLayout(1, false));
		questionGroup.setText("Question");
		
		questionText = new Text(questionGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		questionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		questionText.setFont(monoFont);
		questionText.setTabs(3);
		
		// Answer //////////
		Group answerGroup = new Group(editionCardComposite, SWT.NONE);
		answerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerGroup.setLayout(new GridLayout(1, false));
		answerGroup.setText("Answer");
		
		answerText = new Text(answerGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		answerText.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerText.setFont(monoFont);
		answerText.setTabs(3);
		
		// Tags ////////////
		Group tagGroup = new Group(editionCardComposite, SWT.NONE);
		tagGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		tagGroup.setLayout(new GridLayout(1, false));
		tagGroup.setText("Tags");
		
		tagsText = new Text(tagGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		tagsText.setLayoutData(new GridData(GridData.FILL_BOTH));
		tagsText.setFont(monoFont);
		tagsText.setTabs(3);

		///////////////////////////////////////////////////////////////////////
		// FileButtonComposite ////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite fileButtonComposite = new Composite(editionCardComposite, SWT.NONE);
		fileButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fileButtonComposite.setLayout(new GridLayout(2, true));
		
		// SaveButton /////////
		Button saveButton = new Button(fileButtonComposite, SWT.PUSH);
		saveButton.setEnabled(false);
		saveButton.setText("Save");
		saveButton.setImage(SharedImages.getImage(SharedImages.MEDIA_FLOPPY));
		saveButton.setToolTipText("Save modification for this card");
		
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		
		// CancelButton /////////
		Button cancelButton = new Button(fileButtonComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		cancelButton.setEnabled(false);
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText("Cancel modification for this card");
		
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		
		///////////////////////////////////////////////////////////////////////
		// CardSelectionListeners /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// displayModeComboListener ////////////
		displayModeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch(displayModeCombo.getSelectionIndex()) {
					case 0 :
						cardsList.setItems(OpenCAL.allCardList.getQuestionStrings());
						break;
					case 1 : 
						cardsList.setItems(OpenCAL.reviewedCardList.getQuestionStrings());
						break;
					case 2 :
						cardsList.setItems(OpenCAL.newCardList.getQuestionStrings());
						break;
					case 3 :
						cardsList.setItems(OpenCAL.suspendedCardList.getQuestionStrings());
						break;
				}
			}
		});
		
		// cardsListListener ////////////
		cardsList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch (displayModeCombo.getSelectionIndex()) {
					case 0:
						questionText.setText(OpenCAL.allCardList.get(cardsList.getSelectionIndex()).getQuestion());
						answerText.setText(OpenCAL.allCardList.get(cardsList.getSelectionIndex()).getAnswer());
						tagsText.setText(OpenCAL.allCardList.get(cardsList.getSelectionIndex()).getTagsString());
						break;
					case 1:
						questionText.setText(OpenCAL.reviewedCardList.get(cardsList.getSelectionIndex()).getQuestion());
						answerText.setText(OpenCAL.reviewedCardList.get(cardsList.getSelectionIndex()).getAnswer());
						tagsText.setText(OpenCAL.reviewedCardList.get(cardsList.getSelectionIndex()).getTagsString());
						break;
					case 2:
						questionText.setText(OpenCAL.newCardList.get(cardsList.getSelectionIndex()).getQuestion());
						answerText.setText(OpenCAL.newCardList.get(cardsList.getSelectionIndex()).getAnswer());
						tagsText.setText(OpenCAL.newCardList.get(cardsList.getSelectionIndex()).getTagsString());
						break;
					case 3:
						questionText.setText(OpenCAL.suspendedCardList.get(cardsList.getSelectionIndex()).getQuestion());
						answerText.setText(OpenCAL.suspendedCardList.get(cardsList.getSelectionIndex()).getAnswer());
						tagsText.setText(OpenCAL.suspendedCardList.get(cardsList.getSelectionIndex()).getTagsString());
						break;
				}
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
		
		switch(displayModeCombo.getSelectionIndex()) {
			case 0 :
				cardsList.setItems(OpenCAL.allCardList.getQuestionStrings());
				break;
			case 1 : 
				cardsList.setItems(OpenCAL.reviewedCardList.getQuestionStrings());
				break;
			case 2 :
				cardsList.setItems(OpenCAL.newCardList.getQuestionStrings());
				break;
			case 3 :
				cardsList.setItems(OpenCAL.suspendedCardList.getQuestionStrings());
				break;
		}
	}
	
}
