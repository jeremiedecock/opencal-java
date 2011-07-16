/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */

package org.jdhp.opencal.swt.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.cardcollection.CardManipulator;
import org.jdhp.opencal.swt.MainWindow;
import org.jdhp.opencal.swt.images.SharedImages;
import org.jdhp.opencal.swt.listeners.ModifyListListener;
import org.jdhp.opencal.swt.listeners.ResultListener;
import org.jdhp.opencal.swt.tabs.TestTab;
import org.jdhp.opencal.util.HTML;

public class CardSlider implements ModifyListListener {
	
	final public static int RESULT_STATE = 1;

    final public static int NAVIGATION_STATE = 2;
    

	final private ArrayList<ResultListener> resultListeners;
	
    
    final private List<Card> cardList;

	final private Composite controlComposite;

	final private Composite resultButtonComposite;

	final private Composite navigationButtonComposite;
	
	final private Browser browser;
	
	final private Button wrongAnswerButton;
	
	final private Button rightAnswerButton;
	
	final private Button firstButton;
	
	final private Button lastButton;
	
	final private Button previousButton;
	
	final private Button answerButton;
	
	final private Button nextButton;

    final private Scale scale;
	
	final private CardManipulator manipulator;
	
	private boolean sortCardList;

	
	public CardSlider(Composite parent) {
		
		resultListeners = new ArrayList<ResultListener>();
		
		
		cardList = new ArrayList<Card>();

		manipulator = new CardManipulator(cardList);
		
		setSortCardList(false);
		
		
		Composite sliderComposite = new Composite(parent, SWT.NONE);
		sliderComposite.setLayout(new GridLayout(1, false));
		
		///////////////////////////////////////////////////////////////////////
		// browser ////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
//		try {
		this.browser = new Browser(sliderComposite, SWT.BORDER);
		this.browser.setLayoutData(new GridData(GridData.FILL_BOTH));
//		} catch (SWTError e) {
//			System.out.println("Could not instantiate Browser : " + e.getMessage());
//			OpenCAL.exit(1);
//		}
		
		///////////////////////////////////////////////////////////////////////
		// controlComposite ///////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		controlComposite = new Composite(sliderComposite, SWT.NONE);
		controlComposite.setLayout(new StackLayout());
		controlComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		///////////////////////////////////////////////////////////////////////
		// resultButtonComposite //////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		resultButtonComposite = new Composite(controlComposite, SWT.NONE);
		resultButtonComposite.setLayout(new GridLayout(2, true));
		
		wrongAnswerButton = new Button(resultButtonComposite, SWT.PUSH);
		rightAnswerButton = new Button(resultButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// navigationButtonComposite //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		navigationButtonComposite = new Composite(controlComposite, SWT.NONE);
		navigationButtonComposite.setLayout(new GridLayout(5, false));
		
        scale = new Scale(navigationButtonComposite, SWT.HORIZONTAL);
		firstButton = new Button(navigationButtonComposite, SWT.PUSH);
		previousButton = new Button(navigationButtonComposite, SWT.PUSH);
		answerButton = new Button(navigationButtonComposite, SWT.PUSH);
		nextButton = new Button(navigationButtonComposite, SWT.PUSH);
		lastButton = new Button(navigationButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// resultButtons //////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// wrongAnswerButton /////////////
		wrongAnswerButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
		wrongAnswerButton.setText("Wrong");
		wrongAnswerButton.setImage(SharedImages.getImage(SharedImages.FACE_SAD_24));
		wrongAnswerButton.setToolTipText("Wrong answer");
		
		wrongAnswerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(wrongAnswerButton.getEnabled()) { // Attention, les ordres d'appel ne doivent pas être modifiés !
					Card card = manipulator.pop();
					
					manipulator.remove();
                    setState(TestTab.NAVIGATION_STATE);
                    updateButtons();
                    updateScale();
                    updateBrowser();
                    
                    notifyResultListeners(card, Review.WRONG_ANSWER);
				}
			}
		});
		
		// rightAnswerButton /////////////
		rightAnswerButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
		rightAnswerButton.setText("Right");
		rightAnswerButton.setImage(SharedImages.getImage(SharedImages.FACE_SMILE_24));
		rightAnswerButton.setToolTipText("Right answer");
		
		rightAnswerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(rightAnswerButton.getEnabled()) { // Attention, les ordres d'appel ne doivent pas être modifiés !
					Card card = manipulator.pop();
					
					manipulator.remove();
                    setState(TestTab.NAVIGATION_STATE);
                    updateButtons();
                    updateScale();
                    updateBrowser();
                    
                    notifyResultListeners(card, Review.RIGHT_ANSWER);
				}
			}
		});
		
		///////////////////////////////////////////////////////////////////////
		// navigationButtons //////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
        // Scale ///////////////
		GridData scaleGridData = new GridData(GridData.FILL_HORIZONTAL);
        scaleGridData.horizontalSpan = 5;
		scale.setLayoutData(scaleGridData);
        
        scale.setMinimum(0);
        scale.setIncrement(1);
        scale.setPageIncrement(1);
		
		scale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
                manipulator.setIndex(scale.getSelection());
				
                updateBrowser();
				updateButtons();
			}
		});

		// FirstButton /////////
		firstButton.setImage(SharedImages.getImage(SharedImages.GO_FIRST_24));
		firstButton.setToolTipText("Goto the first card");
		
		firstButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(firstButton.getEnabled()) {
					manipulator.first();
					
                    updateBrowser();
					updateButtons();
                    updateScale();
				}
			}
		});
		
		// PreviousButton /////////
		previousButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		previousButton.setText("Previous");
		previousButton.setImage(SharedImages.getImage(SharedImages.GO_PREVIOUS_24));
		previousButton.setToolTipText("Goto the previous card");
		
		previousButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(previousButton.getEnabled()) {
					manipulator.previous();
					
                    updateBrowser();
					updateButtons();
                    updateScale();
				}
			}
		});
		
		// AnswerButton //////////
		answerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		answerButton.setText("Answer");
		answerButton.setImage(SharedImages.getImage(SharedImages.EDIT_FIND_24));
		answerButton.setToolTipText("Show the answer for this card (review this card)");
		
		answerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(answerButton.getEnabled()) {
                    setState(TestTab.RESULT_STATE);
                    updateBrowser();
					updateButtons();
				}
			}
		});
		
		// NextButton ////////////
		nextButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nextButton.setText("Next");
		nextButton.setImage(SharedImages.getImage(SharedImages.GO_NEXT_24));
		nextButton.setToolTipText("Goto the next card");
		
		nextButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(nextButton.getEnabled()) {
					manipulator.next();
					
                    updateBrowser();
					updateButtons();
                    updateScale();
				}
			}
		});
		
		// LastButton /////////
		lastButton.setImage(SharedImages.getImage(SharedImages.GO_LAST_24));
		lastButton.setToolTipText("Goto the last card");
		
		lastButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(lastButton.getEnabled()) {
					manipulator.last();

                    updateBrowser();
					updateButtons();
                    updateScale();
				}
			}
		});
		
        // Init composites
        setState(TestTab.NAVIGATION_STATE);

        // Init controls
        updateButtons();
        updateScale();
        updateBrowser();
	}
	
	/**
	 * 
	 */
	final private int getState() {
        if(((StackLayout) controlComposite.getLayout()).topControl == resultButtonComposite) return TestTab.RESULT_STATE;
        else return TestTab.NAVIGATION_STATE;
    }
	
	/**
	 * 
	 */
	final private void setState(int mode) {
        if(mode == TestTab.RESULT_STATE) ((StackLayout) controlComposite.getLayout()).topControl = resultButtonComposite;
        else ((StackLayout) controlComposite.getLayout()).topControl = navigationButtonComposite;

        controlComposite.layout();
    }
	
	/**
	 * 
	 */
	final private void updateButtons() {
        if(getState() == TestTab.RESULT_STATE) {
            // Result buttons /////////
            wrongAnswerButton.setEnabled(true);
            rightAnswerButton.setEnabled(true);

            // Navigation buttons /////
            firstButton.setEnabled(false);
            previousButton.setEnabled(false);
            answerButton.setEnabled(false);
            nextButton.setEnabled(false);
            lastButton.setEnabled(false);
        } else {
            // Result buttons /////////
            wrongAnswerButton.setEnabled(false);
            rightAnswerButton.setEnabled(false);

            // Navigation buttons /////
            if(manipulator.hasPrevious()) {
                previousButton.setEnabled(true);
                firstButton.setEnabled(true);
            } else {
                previousButton.setEnabled(false);
                firstButton.setEnabled(false);
            }

            if(manipulator.hasNext()) {
                nextButton.setEnabled(true);
                lastButton.setEnabled(true);
            } else {
                nextButton.setEnabled(false);
                lastButton.setEnabled(false);
            }

            if(manipulator.pop() != null) {
                answerButton.setEnabled(true);
            } else {
                answerButton.setEnabled(false);
            }
        }
    }

	/**
	 * 
	 */
	final private void updateBrowser() {
        Card card = manipulator.pop();
        browser.setText(htmlOut(card));
    }
	
	/**
	 * 
	 */
	final private void updateScale() {
        if(manipulator.pop() != null) {
            scale.setEnabled(true);
            scale.setSelection(manipulator.getIndex());
            scale.setMaximum(cardList.size() - 1); // TODO : +0, +1 ou -1 ?
        } else {
            scale.setEnabled(false);
        }
    }

	/**
	 * TODO : Utiliser un StringBuffer pour la variable "html".
	 * 
	 * @param card
	 * @param answer
	 * @return
	 */
	final private String htmlOut(Card card) {
		StringBuffer html = new StringBuffer();
		
		html.append("<html><head><style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.REVIEW_CSS);
		html.append("</style><head><body>");
		
		if(card == null) {
			html.append("<center>Empty selection</center>");
		} else {
			// Informations
			html.append("<div id=\"informations\">Created on <span class=\"information\">");
			html.append(card.getCreationDate());
			html.append("</span> | <span title=\"");
			Review reviews[] = card.getReviews();
			for(int i=0 ; i<reviews.length ; i++) {
				html.append(reviews[i].getReviewDate());
				html.append(" : ");
				html.append(reviews[i].getResult());
				html.append("\n");
			}
			html.append("\">Checked <span class=\"information\">");
			html.append(card.getReviews().length);
			// TODO : Late ... days
			html.append("</span> times</span> | Level <span class=\"information\">");
			html.append(card.getGrade());
			html.append("</span></div>");
			
			// Tags
			html.append("<div id=\"tags\">");
			String tags[] = card.getTags();
			for(int i=0 ; i<tags.length ; i++) {
				html.append("<span class=\"tag\">");
				html.append(tags[i]);
				html.append("</span>");
			}
			html.append("</div>");
			
			// Question
			html.append("<div id=\"question\">");
			html.append("<h1>Question</h1>");
			html.append(filter(card.getQuestion()));
			html.append("</div>");
			
			// Answer
            if(getState() == TestTab.RESULT_STATE) {
				html.append("<hr />");
				html.append("<div id=\"answer\">");
				html.append("<h1>Answer</h1>");
				html.append(filter(card.getAnswer()));
				html.append("</div>");
			}
		}
		
		html.append("</body></html>");
		
		return html.toString();
	}
	
	/**
	 * This method is used to prepare questions and answers for HTML browser.
	 * This method is not very clean...
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
	
	/**
	 * TODO : remplacer ce bricolage par quelque chose de plus serieux...
     *        java.util.ArrayList.toArray()
     *        java.util.Arrays.sort() + Interface Comparable -> tri par fusion
     *        java.util.Arrays.asList()
	 * 
	 * Tri le tableau par "grade" décroissant
	 */
	public static void sortCards(List<Card> cardList) {
		// Tri bulle
		for(int i=cardList.size()-1 ; i>0 ; i--) {
			for(int j=0 ; j<i ; j++) {
				if((cardList.get(j+1)).getGrade() < (cardList.get(j)).getGrade()) {
					Card tmp = cardList.get(j+1);
					cardList.set(j+1, cardList.get(j));
					cardList.set(j, tmp);
				}
			}
		}
	}
	
	
	
	
	
	
	public boolean isSortCardList() {
		return sortCardList;
	}

	public void setSortCardList(boolean sortCardList) {
		this.sortCardList = sortCardList;
	}
	
	

	public void addResultListener(ResultListener listener) {
		this.resultListeners.add(listener);
	}
	
	public void removeResultListener(ResultListener listener) {
		this.resultListeners.remove(listener);
	}
	
	public void notifyResultListeners(Card card, int result) {
		Iterator<ResultListener> it = this.resultListeners.iterator();
		
		while(it.hasNext()) {
			ResultListener listener = it.next();
            listener.resultNotification(card, result);
        }
	}

	public void listModification(Collection<Card> cardCollection) {
		this.cardList.clear();
		this.cardList.addAll(cardCollection);

		if(this.isSortCardList())
			TestTab.sortCards(this.cardList);
		else
			Collections.shuffle(this.cardList);
		
		setState(TestTab.NAVIGATION_STATE);
		this.manipulator.first();             // this.index = 0;
		
		updateButtons();
		updateScale();
		updateBrowser();
	}

}