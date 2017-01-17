/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Text;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.ui.swt.MainWindow;
import org.jdhp.opencal.ui.swt.images.SharedImages;
import org.jdhp.opencal.ui.swt.widgets.EditableBrowser;
import org.jdhp.opencal.ui.swt.widgets.TagsEditor;
import org.jdhp.opencal.util.CalendarToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ExploreTab {

	final private static String[] LIST_LABELS = {"All Cards", "Reviewed Cards", "New Cards", "Hidden Cards", "Cards By Tag"};
	
	final private static int DEFAULT_LIST = 1;

	final private static int ALL_CARDS_LIST = 0;
	
	final private static int REVIEWED_CARDS_LIST = 1;
	
	final private static int NEW_CARDS_LIST = 2;
	
	final private static int HIDDEN_CARDS_LIST = 3;
	
	final private static int CARDS_BY_TAG_LIST = 4;
	
	
	final private java.util.List<Card> cardList;

	final private Composite parentComposite;
	
	final private List cardListWidget;
	
	final private Composite tagSelectionComposite;
	
	final private Combo tagSelectionCombo;
	
	final private Button tagsWallButton;
	
	final private Text searchText;
	
	final private Button searchButton;
	
	final private Button caseSensitiveCheckbox;

	final private Button showHiddenCardsCheckbox;
	
	final private Combo displayModeCombo;
	
	final private Button saveButton;
	
	final private Button cancelButton;
	
	final private EditableBrowser questionArea;
	
	final private EditableBrowser answerArea;
	
	final private TagsEditor tagsArea;
	
	final private StringBuffer formerSelectedCardLabel;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ExploreTab(Composite parentComposite) {

		formerSelectedCardLabel = new StringBuffer();
		
		///////////////////////////////////////////////////////////////////////
		// Card List //////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

        cardList = new ArrayList<Card>();
        cardList.addAll(OpenCAL.cardCollection);

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
		
		displayModeCombo.setItems(ExploreTab.LIST_LABELS);
		displayModeCombo.select(ExploreTab.DEFAULT_LIST);
		
		// tagSelectionCombo //////////
		tagSelectionComposite = new Composite(cardSelectionComposite, SWT.NONE);
		tagSelectionComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        GridLayout tagSelectionCompositeLayout = new GridLayout(2, false);
        tagSelectionCompositeLayout.marginHeight = 0;
        tagSelectionCompositeLayout.marginWidth = 0;
        tagSelectionComposite.setLayout(tagSelectionCompositeLayout);
        
		tagSelectionCombo = new Combo(tagSelectionComposite, SWT.BORDER);
		tagSelectionCombo.setToolTipText("Tag to search");
		tagSelectionCombo.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		tagsWallButton = new Button(tagSelectionComposite, SWT.PUSH);
		tagsWallButton.setImage(SharedImages.getImage(SharedImages.SYSTEM_SEARCH_16));
		tagsWallButton.setToolTipText("Display the wall of tags");
		tagsWallButton.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		tagsWallButton.setEnabled(true);
        
        setTagSelectionCompositeVisible(false);
        
        // searchText /////////////////
        Composite searchTextComposite = new Composite(cardSelectionComposite, SWT.NONE);
        searchTextComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        GridLayout searchTextCompositeLayout = new GridLayout(2, false);
        searchTextCompositeLayout.marginHeight = 0;
        searchTextCompositeLayout.marginWidth = 0;
        searchTextComposite.setLayout(searchTextCompositeLayout);
        
        searchText = new Text(searchTextComposite, SWT.BORDER | SWT.SINGLE | SWT.SEARCH | SWT.CANCEL);
        searchText.setToolTipText("Text to search");
        searchText.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        searchButton = new Button(searchTextComposite, SWT.PUSH);
        searchButton.setImage(SharedImages.getImage(SharedImages.EDIT_FIND_16));
        searchButton.setToolTipText("Search");
        searchButton.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        
        caseSensitiveCheckbox = new Button(searchTextComposite, SWT.CHECK);
        GridData caseSensitiveGridData =  new GridData(GridData.FILL_BOTH);
        caseSensitiveGridData.horizontalSpan = 2;
        caseSensitiveCheckbox.setLayoutData(caseSensitiveGridData);
        caseSensitiveCheckbox.setText("Case Sensitive");
        
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
		questionArea.setTitle("Question");
		questionArea.setMode(EditableBrowser.BROWSER);

		questionArea.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);
			}
		});
		
		// Answer /////////////////
		answerArea = new EditableBrowser(verticalSashForm);
		answerArea.setTitle("Answer");
		answerArea.setMode(EditableBrowser.BROWSER);
		
		answerArea.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveButton.setEnabled(true);
				cancelButton.setEnabled(true);
			}
		});
		
		// Tags ///////////////////
		tagsArea = new TagsEditor(verticalSashForm);
		tagsArea.setTitle("Tags");
		
		tagsArea.addModifyListener(new ModifyListener() {
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
		saveButton.setImage(SharedImages.getImage(SharedImages.MEDIA_FLOPPY_24));
		saveButton.setToolTipText("Save modification for this card");
		
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getSelectedCard().setQuestion(questionArea.getText());
				getSelectedCard().setAnswer(answerArea.getText());
				
				String[] tagArray = tagsArea.getText().split("\n");
				getSelectedCard().setTags(new ArrayList<String>(Arrays.asList(tagArray)));
				
				saveButton.setEnabled(false);
				cancelButton.setEnabled(false);
				
				updateTagCombo();
				updateCardList();
			}
		});
		
		// CancelButton ///////////
		cancelButton = new Button(fileButtonComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
		cancelButton.setEnabled(false);
		cancelButton.setText("Cancel");
		cancelButton.setImage(SharedImages.getImage(SharedImages.EDIT_CLEAR_24));
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

		///////////////////////////
		// SasheForm //////////////
		///////////////////////////
        
        horizontalSashForm.setWeights(new int[] {25, 75});
		verticalSashForm.setWeights(new int[] {40, 40, 20});
		
		///////////////////////////////////////////////////////////////////////
		// CardSelectionListeners /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// displayModeComboListener ////////////
		displayModeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch(getCurrentMode()) {
					case ExploreTab.ALL_CARDS_LIST :
                        setShowHiddenCardsCheckboxVisible(true);
                        setTagSelectionCompositeVisible(false);
						break;
					case ExploreTab.CARDS_BY_TAG_LIST :
                        setShowHiddenCardsCheckboxVisible(true);
                        setTagSelectionCompositeVisible(true);
						break;
					default :
                        setShowHiddenCardsCheckboxVisible(false);
                        setTagSelectionCompositeVisible(false);
				}
				
				updateCardList();
			}
		});
		
		// tagSelectionComboListener ////////////
		tagSelectionCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateCardList();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// Appellé quand valide avec Entrer la zone de texte du combo 
				updateCardList();
			}
			
//			public void modifyText(ModifyEvent arg0) {
//			// Check if the combo text match with the begining of a tag
//			
//			
//			// Check if the combo text is equal to a tag
//		}
		});
		
		// tagsWallButtonListener ///////////////
		tagsWallButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO
			}
		});

		// searchTextListeners //////////////////
		searchText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				//updateCardList(); // recherche en temps réel -> un peu trop lent...
			}
		});
		
		searchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.character == SWT.CR || e.character == SWT.LF) {
					updateCardList();         // TODO : envoyer un SelectionEvent sur le bouton searchButton à la place (factoriser code)...
				}
			}
		});

		// searchButtonListener /////////////////
		searchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateCardList();
			}
		});
		
		// showHiddenCardsCheckboxListener ////////////
		showHiddenCardsCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateTagCombo();
				updateCardList();
			}
		});
		

		hideItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndices[] = cardListWidget.getSelectionIndices();

				for(int i=0 ; i<selectionIndices.length ; i++) {
		            cardList.get(selectionIndices[i]).setHidden(true);
				}
				
				updateTagCombo();
				updateCardList();
			}
		});
		
		unhideItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndices[] = cardListWidget.getSelectionIndices();

				for(int i=0 ; i<selectionIndices.length ; i++) {
		            cardList.get(selectionIndices[i]).setHidden(false);
				}
				
				updateTagCombo();
				updateCardList();
			}
		});
		
		// cardListWidgetListener ////////////
		cardListWidget.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateTextArea();
				
				// Mémorise le label
				int formerIndex = cardListWidget.getSelectionIndex(); // Vaut -1 si aucun item n'est sélectionné
				formerSelectedCardLabel.replace(
						0,
						formerSelectedCardLabel.length(),
						formerIndex >= 0 ? cardListWidget.getItem(formerIndex) : "");
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
	final private String[] formatItems(String[] text) {
		for(int i=0 ; i<text.length ; i++) {
			// Supprime les balises images
			String pattern = "<img file=\"[0-9abcdef]{32}.(png|jpg|jpeg|gif)\" />";
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
    final private void setTagSelectionCompositeVisible(boolean visible) {
        if(visible) {
        	tagSelectionComposite.setVisible(true);
            ((GridData) tagSelectionComposite.getLayoutData()).exclude = false;
            tagSelectionComposite.getParent().layout();
        } else {
        	tagSelectionComposite.setVisible(false);
            ((GridData) tagSelectionComposite.getLayoutData()).exclude = true;
            tagSelectionComposite.getParent().layout();
        }
    }


	
	/**
	 * Met à jour la liste des items dans le combo "TagSelection"
	 * 
	 * TODO : Mieux gérer l'ajout et la supression de tags !
	 */
	final private void updateTagCombo() {
		// Mémorise le label et la position courante
		String formerSelectedTagLabel = tagSelectionCombo.getText();

		// Update the list of existing tags
		String[] tags = OpenCAL.cardCollection.getTags(!showHiddenCardsCheckbox.getSelection());
		tagSelectionCombo.setItems(tags);

		// Re sélectionne l'ancien tag si c'est possible
		int index = tagSelectionCombo.indexOf(formerSelectedTagLabel);
		if(index >= 0) {
			tagSelectionCombo.select(index);
		}
	}
	
	
	
	
	/**
	 * Met à jour la liste des cartes
	 * 
	 * TODO : Mieux gérer l'ajout et la supression de d'items !
	 */
	final private void updateCardList() {
		cardList.clear();
		
		switch(getCurrentMode()) {
		
			case ExploreTab.ALL_CARDS_LIST :
                
                for(Card card : OpenCAL.cardCollection) {
                    if(!card.isHidden() || showHiddenCardsCheckbox.getSelection()) {
	                    cardList.add(card);
                    }
                }
				break;
				
			case ExploreTab.REVIEWED_CARDS_LIST : 
                
				for(Card card : OpenCAL.cardCollection) {
                    boolean hasBeenReviewed = false;
                    
                    Review[] reviews = card.getReviews().toArray(new Review[0]);
                    for(Review review : reviews) {
                        if(review.getReviewDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar())))
                        	hasBeenReviewed = true;
                    }
                    
                    if(hasBeenReviewed) cardList.add(card);
                }
				break;
				
			case ExploreTab.NEW_CARDS_LIST :
                
				for(Card card : OpenCAL.cardCollection) {
                    if(card.getCreationDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar())))
                    	cardList.add(card);
                }
				break;
				
			case ExploreTab.HIDDEN_CARDS_LIST :
                
				for(Card card : OpenCAL.cardCollection) {
                    if(card.isHidden())
                    	cardList.add(card);
                }
				break;
				
			case ExploreTab.CARDS_BY_TAG_LIST :
                
				// If a tag is selected add the tag's cards (else keep cardList empty)
				if(!tagSelectionCombo.getText().equals("")) {
					for(Card card : OpenCAL.cardCollection) {
	                    if(!card.isHidden() || showHiddenCardsCheckbox.getSelection()) {
	                        String[] tags = card.getTags().toArray(new String[0]);
	                        
	                        boolean addCard = false;
	                        for(String tag : tags) {
	                            if(tag.equals(tagSelectionCombo.getText()))
	                            	addCard = true;
	                        }
	                        
	                        if(addCard) cardList.add(card);
	                    }
	                }
				}
				break;
				
		}
		
		// Ne conserve que les cartes contenant le motif searchText (si celui ci n'est pas vide)
		String pattern = searchText.getText();
		if(pattern != null && !pattern.equals("")) {
			java.util.List<Card> filtredCardList = new ArrayList<Card>();
			
			boolean caseSensitive = caseSensitiveCheckbox.getSelection();
			
			for(Card card : cardList) {
                if(card.contains(pattern, caseSensitive)) {
                	filtredCardList.add(card);
                }
            }
			
			cardList.clear();
			cardList.addAll(filtredCardList);
		}
		
		// Remplit le widget cardlist
		if(getCurrentMode() == ExploreTab.REVIEWED_CARDS_LIST)
			cardListWidget.setItems(formatItems(MainWindow.getQuestionStrings(cardList, true))); // TODO
		else
			cardListWidget.setItems(formatItems(MainWindow.getQuestionStrings(cardList, false))); // TODO
		
		// Re sélectionne l'ancienne carte si c'est possible
		if(!formerSelectedCardLabel.toString().equals("")) {
			int formerIndex = cardListWidget.indexOf(formerSelectedCardLabel.toString());
			if(formerIndex >= 0) {
				cardListWidget.deselectAll();
				cardListWidget.select(formerIndex);
				cardListWidget.setTopIndex(formerIndex); 
			} else {
				formerSelectedCardLabel.replace(0, formerSelectedCardLabel.length(), "");
			}
		}
		
		updateTextArea();
	}
	
	
	
	
	/**
	 * 
	 */
	final private void updateTextArea() {
		Card selectedCard = getSelectedCard();
		
		if(selectedCard != null) {
			questionArea.setText(selectedCard.getQuestion());
			answerArea.setText(selectedCard.getAnswer());
			
			StringBuilder tagsString = new StringBuilder();
			for(String tag : selectedCard.getTags()) {
				tagsString.append(tag);
				tagsString.append("\n");
			}

			tagsArea.setText(tagsString.toString());
		} else {
			questionArea.setText("");
			answerArea.setText("");
			tagsArea.setText("");
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
			questionArea.setEditable(true);
			answerArea.setEditable(true);
			tagsArea.setEditable(true);
		} else {
			questionArea.setEditable(false);
			answerArea.setEditable(false);
			tagsArea.setEditable(false);
		}
	}


	
	/**
	 * La mise à jours faite lors de la sélection du tab "ExplorerTab"
	 */
	final public void update() {
		updateTagCombo();
		updateCardList();
	}
}
