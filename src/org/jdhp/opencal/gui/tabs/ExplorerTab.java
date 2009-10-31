/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.gui.tabs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.gui.images.SharedImages;
import org.jdhp.opencal.gui.widgets.EditableBrowser;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ExplorerTab {

	final private String[] displayModes = {"All Cards", "Reviewed Cards", "New Cards", "Hidden Cards", "Cards By Tag"};
	
	final private static int DEFAULT_DISPLAY_MODE = 1;
	
	final private static int ALL_CARDS = 0;
	
	final private static int REVIEWED_CARDS = 1;
	
	final private static int NEW_CARDS = 2;
	
	final private static int HIDDEN_CARDS = 3;
	
	final private static int CARDS_BY_TAG = 4;
	
	final private Composite parentComposite;
	
	final private List cardsList;
	
	final private Combo tagSelectionCombo;

	final private Button showHiddenCardsCheckbox;
	
	final private Combo displayModeCombo;
	
	final private Button saveButton;
	
	final private Button cancelButton;
	
	final private EditableBrowser questionArea;
	
	final private EditableBrowser answerArea;
	
	final private EditableBrowser tagsArea;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ExplorerTab(Composite parentComposite) {

		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(1, false));
		
		///////////////////////////////////////////////////////////////////////
		// SasheForm //////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

        SashForm horizontalSashForm = new SashForm(this.parentComposite, SWT.HORIZONTAL);
		horizontalSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		///////////////////////////////////////////////////////////////////////
		// CardSelectionComposite /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite cardSelectionComposite = new Composite(horizontalSashForm, SWT.NONE);
		cardSelectionComposite.setLayout(new GridLayout(1, false));
		
		// displayModeCombo ///////////
		displayModeCombo = new Combo(cardSelectionComposite, SWT.BORDER | SWT.READ_ONLY);
		displayModeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		displayModeCombo.setItems(displayModes);
		displayModeCombo.select(ExplorerTab.DEFAULT_DISPLAY_MODE);
		
		// tagSelectionCombo //////////
		tagSelectionCombo = new Combo(cardSelectionComposite, SWT.BORDER | SWT.READ_ONLY);
		tagSelectionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		tagSelectionCombo.setItems(OpenCAL.cardByTagList.tagList());
		tagSelectionCombo.select(0); // TODO
        setTagSelectionComboVisible(false);
		
		// showHiddenCardsCheckbox ////
        showHiddenCardsCheckbox = new Button(cardSelectionComposite, SWT.CHECK);
        showHiddenCardsCheckbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        showHiddenCardsCheckbox.setText("Show hidden cards");
        setShowHiddenCardsCheckboxVisible(false);

		// cardsList //////////////////
		cardsList = new List(cardSelectionComposite, SWT.BORDER | SWT.V_SCROLL);
		cardsList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		cardsList.setItems(new String[0]);
		
		///////////////////////////////////////////////////////////////////////
		// EditionCardComposite ///////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite cardEditionComposite = new Composite(horizontalSashForm, SWT.NONE);
		cardEditionComposite.setLayout(new GridLayout(1, false));
		
		///////////////////////////
		// SasheForm //////////////
		///////////////////////////

        SashForm verticalSashForm = new SashForm(cardEditionComposite, SWT.VERTICAL);
		verticalSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// Question ///////////////
		questionArea = new EditableBrowser(verticalSashForm);
		questionArea.label.setText("Question");

		questionArea.editableText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);
			}
		});
		
		// Answer /////////////////
		answerArea = new EditableBrowser(verticalSashForm);
		answerArea.label.setText("Answer");
		
		answerArea.editableText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);
			}
		});
		
		// Tags ///////////////////
		tagsArea = new EditableBrowser(verticalSashForm);
		tagsArea.label.setText("Tags");
		
		tagsArea.editableText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);
			}
		});

		///////////////////////////
		// FileButtonComposite ////
		///////////////////////////
		
		Composite fileButtonComposite = new Composite(cardEditionComposite, SWT.NONE);
		fileButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fileButtonComposite.setLayout(new GridLayout(2, true));
		
		// SaveButton /////////////
		saveButton = new Button(fileButtonComposite, SWT.PUSH);
		saveButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true));
		saveButton.setEnabled(false);
		saveButton.setText("Save");
		saveButton.setImage(SharedImages.getImage(SharedImages.MEDIA_FLOPPY));
		saveButton.setToolTipText("Save modification for this card");
		
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getSelectedCard().setQuestion(questionArea.editableText.getText());
				getSelectedCard().setAnswer(answerArea.editableText.getText());
				getSelectedCard().setTags(tagsArea.editableText.getText().split("\n"));
				
				saveButton.setEnabled(false);
				cancelButton.setEnabled(false);
				
				updateTagCombo();
				updateCardList(false);
			}
		});
		
		// CancelButton ///////////
		cancelButton = new Button(fileButtonComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
		cancelButton.setEnabled(false);
		cancelButton.setText("Cancel");
		cancelButton.setImage(SharedImages.getImage(SharedImages.EDIT_CLEAR));
		cancelButton.setToolTipText("Cancel modification for this card");
		
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateTextArea();
			}
		});

        // Equalize buttons size (buttons size may change in others languages...) //
        Point cancelButtonPoint = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        Point saveButtonPoint = saveButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

        if(cancelButtonPoint.x > saveButtonPoint.x) ((GridData) saveButton.getLayoutData()).widthHint = cancelButtonPoint.x;
        else ((GridData) cancelButton.getLayoutData()).widthHint = saveButtonPoint.x;
		
		///////////////////////////////////////////////////////////////////////
		// CardSelectionListeners /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// displayModeComboListener ////////////
		displayModeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch(getCurrentMode()) {
					case ExplorerTab.ALL_CARDS :
                        setShowHiddenCardsCheckboxVisible(true);
                        setTagSelectionComboVisible(false);
						break;
					case ExplorerTab.CARDS_BY_TAG :
                        setShowHiddenCardsCheckboxVisible(true);
                        setTagSelectionComboVisible(true);
						break;
					default :
                        setShowHiddenCardsCheckboxVisible(false);
                        setTagSelectionComboVisible(false);
				}
				
				updateCardList(true);
			}
		});
		
		// cardsListListener ////////////
		cardsList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateTextArea();
			}
		});
		
		// tagSelectionComboListener ////////////
		tagSelectionCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				OpenCAL.cardByTagList.setCurrentTag(tagSelectionCombo.getText());
				
				updateCardList(true);
			}
		});
	}
	
	
	
	
	/**
	 * Filtre le texte des items de la liste
	 * 
	 * @param text
	 * @return
	 */
	final private String[] itemFilter(String[] text) {
		for(int i=0 ; i<text.length ; i++) {
			// Supprime les balises images
			String pattern = "<img file=\"[0-9abcdef]{32}.(png|jpg|jpeg)\" />";
			Pattern regPat = Pattern.compile(pattern);
			Matcher matcher = regPat.matcher(text[i]);
			text[i] = matcher.replaceAll("[img]");
			
			// Ne conserve que la première ligne
			text[i] = text[i].split("\n")[0];
		}
		
		return text;
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	final private int getCurrentMode() {
		return displayModeCombo.getSelectionIndex();
	}
	
	
	
	
	/**
	 * Retourne la carte actuellement sélectionné dans la liste de gauche
	 * 
	 * TODO : attention, cette methode est suceptible de renvoyer "null"
	 * 
	 * @return
	 */
	final private Card getSelectedCard() {
		Card selectedCard = null;
		
		int selectionIndex = cardsList.getSelectionIndex();
		
		try {
			switch (getCurrentMode()) {
				case ExplorerTab.ALL_CARDS :
					selectedCard = OpenCAL.allCardList.get(selectionIndex);
					break;
				case ExplorerTab.REVIEWED_CARDS :
					selectedCard = OpenCAL.reviewedCardList.get(selectionIndex);
					break;
				case ExplorerTab.NEW_CARDS :
					selectedCard = OpenCAL.newCardList.get(selectionIndex);
					break;
				case ExplorerTab.HIDDEN_CARDS :
					selectedCard = OpenCAL.hiddenCardList.get(selectionIndex);
					break;
				case ExplorerTab.CARDS_BY_TAG :
					selectedCard = OpenCAL.cardByTagList.get(selectionIndex);
					break;
			}
		} catch(ArrayIndexOutOfBoundsException ex) {
			selectedCard = null;
		}
		
		return selectedCard;
	}



	
	/**
     *
     */
    final private void setShowHiddenCardsCheckboxVisible(boolean visible) {
        if(visible) {
            showHiddenCardsCheckbox.setVisible(true);
            ((GridData) showHiddenCardsCheckbox.getLayoutData()).exclude = false;
            showHiddenCardsCheckbox.getParent().layout();
        } else {
            showHiddenCardsCheckbox.setVisible(false);
            ((GridData) showHiddenCardsCheckbox.getLayoutData()).exclude = true;
            showHiddenCardsCheckbox.getParent().layout();
        }
    }



	
	/**
     *
     */
    final private void setTagSelectionComboVisible(boolean visible) {
        if(visible) {
            tagSelectionCombo.setVisible(true);
            ((GridData) tagSelectionCombo.getLayoutData()).exclude = false;
            tagSelectionCombo.getParent().layout();
        } else {
            tagSelectionCombo.setVisible(false);
            ((GridData) tagSelectionCombo.getLayoutData()).exclude = true;
            tagSelectionCombo.getParent().layout();
        }
    }



	
	/**
	 * Met à jour la liste des items dans le combo "TagSelection"
	 * 
	 * TODO : Mieux gérer l'ajout et la supression de tags !
	 */
	final private void updateTagCombo() {
//		System.out.println("Call updateTagCombo");
		int previousIndex = tagSelectionCombo.getSelectionIndex();
		
		tagSelectionCombo.setItems(OpenCAL.cardByTagList.tagList());
		
		if(tagSelectionCombo.getItemCount() > previousIndex) tagSelectionCombo.select(previousIndex);
		else tagSelectionCombo.select(tagSelectionCombo.getItemCount() - 1);
	}
	
	
	
	
	/**
	 * Met à jour la liste des items
	 * 
	 * TODO : Mieux gérer l'ajout et la supression de d'items !
	 */
	final private void updateCardList(Boolean init) {
//		System.out.println("Call updateCardList");
		
		int previousIndex = 0;
			
		if(!init) previousIndex = cardsList.getSelectionIndex();
		
		switch(getCurrentMode()) {
			case ExplorerTab.ALL_CARDS :
				cardsList.setItems(itemFilter(MainWindow.getQuestionStrings(OpenCAL.allCardList, false)));
				break;
			case ExplorerTab.REVIEWED_CARDS : 
				cardsList.setItems(itemFilter(MainWindow.getQuestionStrings(OpenCAL.reviewedCardList, true)));
				break;
			case ExplorerTab.NEW_CARDS :
				cardsList.setItems(itemFilter(MainWindow.getQuestionStrings(OpenCAL.newCardList, false)));
				break;
			case ExplorerTab.HIDDEN_CARDS :
				cardsList.setItems(itemFilter(MainWindow.getQuestionStrings(OpenCAL.hiddenCardList, false)));
				break;
			case ExplorerTab.CARDS_BY_TAG :
				cardsList.setItems(itemFilter(MainWindow.getQuestionStrings(OpenCAL.cardByTagList, false)));
				break;
		}
		
		if(init) {
			if(cardsList.getItemCount() > 0) cardsList.select(0);
		} else {
			if(cardsList.getItemCount() > previousIndex) cardsList.select(previousIndex);
			else if(cardsList.getItemCount() > 0) cardsList.select(cardsList.getItemCount() - 1);
		}
		
		updateTextArea();
	}
	
	
	
	
	/**
	 * 
	 */
	final private void updateTextArea() {
//		System.out.println("Call updateTextArea");
		Card selectedCard = getSelectedCard();
		
		if(selectedCard != null) {
			questionArea.editableText.setText(selectedCard.getQuestion());
			answerArea.editableText.setText(selectedCard.getAnswer());
			tagsArea.editableText.setText(selectedCard.getTagsString());
		} else {
			questionArea.editableText.setText("");
			answerArea.editableText.setText("");
			tagsArea.editableText.setText("");
		}
		
		saveButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		updateTextAreaStatus();
	}
	
	
	
	
	/**
	 * Si la liste est vide, les textArea ne sont plus éditables
	 */
	final private void updateTextAreaStatus() {
//		System.out.println("Call updateTextAreaStatus");
		if(getSelectedCard() != null) {
//			System.out.println("Not null");
			questionArea.editableText.setEditable(true);
			answerArea.editableText.setEditable(true);
			tagsArea.editableText.setEditable(true);
		} else {
//			System.out.println("Null");
			questionArea.editableText.setEditable(false);
			answerArea.editableText.setEditable(false);
			tagsArea.editableText.setEditable(false);
		}
	}
	
	
	
	
	/**
	 * La mise à jours faite lors de la sélection du tab "ExplorerTab"
	 */
	final public void update() {
		updateTagCombo();
		updateCardList(false);
	}
}
