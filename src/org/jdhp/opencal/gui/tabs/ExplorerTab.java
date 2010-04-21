/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.gui.tabs;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeSet;

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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.jdhp.opencal.card.Review;
import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.gui.images.SharedImages;
import org.jdhp.opencal.gui.widgets.EditableBrowser;
import org.jdhp.opencal.PersonalKnowledgeBase;
import org.jdhp.opencal.toolkit.CalendarToolKit;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ExplorerTab {

	final private static String[] LIST_LABELS = {"All Cards", "Reviewed Cards", "New Cards", "Hidden Cards", "Cards By Tag"};
	
	final private static int DEFAULT_DISPLAY_MODE = 1;

	final private static String ALL_TAGS_LABEL = "*";
	
	final private static int ALL_CARDS = 0;
	
	final private static int REVIEWED_CARDS = 1;
	
	final private static int NEW_CARDS = 2;
	
	final private static int HIDDEN_CARDS = 3;
	
	final private static int CARDS_BY_TAG = 4;
	
	final private CardList cardList;

	final private Composite parentComposite;
	
	final private List cardListWidget;
	
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

		///////////////////////////////////////////////////////////////////////
		// Card List View /////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

        cardList = new CardList();
        cardList.addAll(CardList.mainCardList);

		///////////////////////////////////////////////////////////////////////
		// GUI ////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

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
		
		displayModeCombo.setItems(ExplorerTab.LIST_LABELS);
		displayModeCombo.select(ExplorerTab.DEFAULT_DISPLAY_MODE);
		
		// tagSelectionCombo //////////
		tagSelectionCombo = new Combo(cardSelectionComposite, SWT.BORDER | SWT.READ_ONLY);
		tagSelectionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        //updateTagCombo();
        setTagSelectionComboVisible(false);
		
		// showHiddenCardsCheckbox ////
        showHiddenCardsCheckbox = new Button(cardSelectionComposite, SWT.CHECK);
        showHiddenCardsCheckbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        showHiddenCardsCheckbox.setText("Show hidden cards");
        setShowHiddenCardsCheckboxVisible(false);

		// cardListWidget /////////////
		cardListWidget = new List(cardSelectionComposite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		cardListWidget.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Menu menu = new Menu(cardListWidget);
		MenuItem hideItem = new MenuItem(menu, SWT.NONE);
		hideItem.setText("Hide");
		MenuItem unhideItem = new MenuItem(menu, SWT.NONE);
		unhideItem.setText("Unhide");
		
		cardListWidget.setMenu(menu);
		
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
		
		// showHiddenCardsCheckboxListener ////////////
		showHiddenCardsCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//System.out.println(showHiddenCardsCheckbox.getSelection());
				updateCardList(false);
			}
		});
		
		// cardListWidgetListener ////////////
		cardListWidget.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateTextArea();
			}
		});
		
		hideItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndices[] = cardListWidget.getSelectionIndices();

				for(int i=0 ; i<selectionIndices.length ; i++) {
		            cardList.get(selectionIndices[i]).setHidden(true);
				}
				
				updateCardList(false);
			}
		});
		
		unhideItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndices[] = cardListWidget.getSelectionIndices();

				for(int i=0 ; i<selectionIndices.length ; i++) {
		            cardList.get(selectionIndices[i]).setHidden(false);
				}
				
				updateCardList(false);
			}
		});
		
		// tagSelectionComboListener ////////////
		tagSelectionCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateCardList(true);
			}
		});

		///////////////////////////
		// Init ///////////////////
		///////////////////////////
        
        update();
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
		
		int selectionIndex = cardListWidget.getSelectionIndex();

		try {
            selectedCard = cardList.get(selectionIndex);
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
		int previousIndex = tagSelectionCombo.getSelectionIndex();

		TreeSet<String> tagSet = new TreeSet<String>();
		
		NodeList nodeTags = PersonalKnowledgeBase.getDomDocument().getElementsByTagName("tag");
		for(int i=0 ; i<nodeTags.getLength() ; i++) {
			Element tagElement = (Element) nodeTags.item(i);
			String tagText = ((Text) tagElement.getFirstChild()).getData();
			tagSet.add(tagText);
		}
		
		String[] tagsArray = new String[tagSet.size()];
		tagSelectionCombo.setItems(tagSet.toArray(tagsArray));
		tagSelectionCombo.add(ExplorerTab.ALL_TAGS_LABEL, 0);
		
        if(previousIndex < 0) tagSelectionCombo.select(0);
        else if(previousIndex > tagSelectionCombo.getItemCount() - 1) tagSelectionCombo.select(tagSelectionCombo.getItemCount() - 1);
        else tagSelectionCombo.select(previousIndex);
	}
	
	
	
	
	/**
	 * Met à jour la liste des items
	 * 
	 * TODO : Mieux gérer l'ajout et la supression de d'items !
	 */
	final private void updateCardList(Boolean init) {
		int previousIndex = 0;
		
		if(!init) previousIndex = cardListWidget.getSelectionIndex();
		
		switch(getCurrentMode()) {
			case ExplorerTab.ALL_CARDS :
                cardList.clear();
                
                for(int i=0 ; i<CardList.mainCardList.size() ; i++) {
                    Card card = CardList.mainCardList.get(i);
                    
                    if(!card.isHidden() || showHiddenCardsCheckbox.getSelection()) {
	                    cardList.add(card);
                    }
                }
                
                cardListWidget.setItems(itemFilter(MainWindow.getQuestionStrings(cardList, false)));
				break;
			case ExplorerTab.REVIEWED_CARDS : 
                cardList.clear();
                
                for(int i=0 ; i<CardList.mainCardList.size() ; i++) {
                    Card card = CardList.mainCardList.get(i);
                    
                    boolean hasBeenReviewed = false;
                    Review[] reviews = card.getReviews();
                    for(int j=0 ; j < reviews.length ; j++) {
                        if(reviews[j].getReviewDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar()))) hasBeenReviewed = true;
                    }
                    
                    if(hasBeenReviewed) cardList.add(card);
                }
                
                cardListWidget.setItems(itemFilter(MainWindow.getQuestionStrings(cardList, false)));
				break;
			case ExplorerTab.NEW_CARDS :
                cardList.clear();
                
                for(int i=0 ; i<CardList.mainCardList.size() ; i++) {
                    Card card = CardList.mainCardList.get(i);
                    if(card.getCreationDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar()))) cardList.add(card);
                }
                
                cardListWidget.setItems(itemFilter(MainWindow.getQuestionStrings(cardList, false)));
				break;
			case ExplorerTab.HIDDEN_CARDS :
                cardList.clear();
                
                for(int i=0 ; i<CardList.mainCardList.size() ; i++) {
                    Card card = CardList.mainCardList.get(i);
                    if(card.isHidden()) cardList.add(card);
                }
                
                cardListWidget.setItems(itemFilter(MainWindow.getQuestionStrings(cardList, false)));
				break;
			case ExplorerTab.CARDS_BY_TAG :
                cardList.clear();
                
                for(int i=0 ; i<CardList.mainCardList.size() ; i++) {
                    Card card = CardList.mainCardList.get(i);
                    
                    if(!card.isHidden() || showHiddenCardsCheckbox.getSelection()) {
	                    if(tagSelectionCombo.getItem(tagSelectionCombo.getSelectionIndex()).equals(ExplorerTab.ALL_TAGS_LABEL)) {
	                    	cardList.add(card);
	                    } else {
	                        String[] tags = card.getTags();
	                        
	                        boolean addCard = false;
	                        for(int j=0 ; j < tags.length ; j++) {
	                            if(tags[j].equals(tagSelectionCombo.getItem(tagSelectionCombo.getSelectionIndex()))) addCard = true;
	                        }
	                        
	                        if(addCard) cardList.add(card);
	                    }
                    }
                }
                
                cardListWidget.setItems(itemFilter(MainWindow.getQuestionStrings(cardList, false)));
				break;
		}
		
		if(init) {
			if(cardListWidget.getItemCount() > 0) cardListWidget.select(0);
		} else {
			if(cardListWidget.getItemCount() > previousIndex) cardListWidget.select(previousIndex);
			else if(cardListWidget.getItemCount() > 0) cardListWidget.select(cardListWidget.getItemCount() - 1);
		}

		updateTextArea();
	}
	
	
	
	
	/**
	 * 
	 */
	final private void updateTextArea() {
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
		if(getSelectedCard() != null) {
			questionArea.editableText.setEditable(true);
			answerArea.editableText.setEditable(true);
			tagsArea.editableText.setEditable(true);
		} else {
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
