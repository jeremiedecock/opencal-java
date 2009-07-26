/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.gui.tabs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardManipulator;
import org.jdhp.opencal.card.Review;
import org.jdhp.opencal.gui.CheckPanelHotKeys;
import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.gui.images.SharedImages;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewerTab {

	final private Composite parentComposite;
	
	final private Composite controlComposite;

	final private Composite resultButtonComposite;

	final private Composite navigationButtonComposite;
	
	final private Browser browser;
	
	final private Button rightAnswerButton;
	
	final private Button wrongAnswerButton;
	
	final private Button firstButton;
	
	final private Button lastButton;
	
	final private Button previousButton;
	
	final private Button answerButton;
	
	final private Button nextButton;
	
	final private CardManipulator manipulator;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ReviewerTab(Composite parentComposite) {

		manipulator = OpenCAL.plannedCardList.manipulator();
		
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(1, false));
		
		///////////////////////////////////////////////////////////////////////
		// browser ////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
//		try {
		this.browser = new Browser(this.parentComposite, SWT.BORDER);
		this.browser.setLayoutData(new GridData(GridData.FILL_BOTH));
//		} catch (SWTError e) {
//			System.out.println("Could not instantiate Browser : " + e.getMessage());
//			OpenCAL.exit(1);
//		}
	
		Card card = manipulator.pop();
		this.browser.setText(htmlOut(card, false));
		
		///////////////////////////////////////////////////////////////////////
		// controlComposite ///////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		controlComposite = new Composite(this.parentComposite, SWT.NONE);
		controlComposite.setLayout(new StackLayout());
		controlComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		///////////////////////////////////////////////////////////////////////
		// resultButtonComposite //////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		resultButtonComposite = new Composite(controlComposite, SWT.NONE);
		resultButtonComposite.setLayout(new GridLayout(2, true));
		
		rightAnswerButton = new Button(resultButtonComposite, SWT.PUSH);
		wrongAnswerButton = new Button(resultButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// navigationButtonComposite //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		navigationButtonComposite = new Composite(controlComposite, SWT.NONE);
		navigationButtonComposite.setLayout(new GridLayout(3, false));
		
		firstButton = new Button(navigationButtonComposite, SWT.PUSH);
		Composite centralNavigationButtonComposite = new Composite(navigationButtonComposite, SWT.NONE);
		lastButton = new Button(navigationButtonComposite, SWT.PUSH);

        ((StackLayout) controlComposite.getLayout()).topControl = navigationButtonComposite;
		
		///////////////////////////////////////////////////////////////////////
		// centralNavigationButtonComposite ///////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		GridLayout centralNavigationButtonCompositeGridLayout = new GridLayout(3, true);
		centralNavigationButtonCompositeGridLayout.marginWidth = 0;
		centralNavigationButtonComposite.setLayout(centralNavigationButtonCompositeGridLayout);
		
		centralNavigationButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		previousButton = new Button(centralNavigationButtonComposite, SWT.PUSH);
		answerButton = new Button(centralNavigationButtonComposite, SWT.PUSH);
		nextButton = new Button(centralNavigationButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// resultButtons //////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// rightAnswerButton /////////////
		rightAnswerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		rightAnswerButton.setText("Right");
		rightAnswerButton.setImage(SharedImages.getImage(SharedImages.FACE_SMILE));
		rightAnswerButton.setToolTipText("Right answer");
		
		rightAnswerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(rightAnswerButton.getEnabled()) {
					manipulator.pop().putReview(OpenCAL.RIGHT_ANSWER_STRING);
					manipulator.remove();
					
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

                    ((StackLayout) controlComposite.getLayout()).topControl = navigationButtonComposite;
					controlComposite.layout();

					Card card = manipulator.pop();
					browser.setText(htmlOut(card, false));

					OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
					OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
				}
			}
		});
		
		// wrongAnswerButton /////////////
		wrongAnswerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wrongAnswerButton.setText("Wrong");
		wrongAnswerButton.setImage(SharedImages.getImage(SharedImages.FACE_SAD));
		wrongAnswerButton.setToolTipText("Wrong answer");
		
		wrongAnswerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(wrongAnswerButton.getEnabled()) {
					manipulator.pop().putReview(OpenCAL.WRONG_ANSWER_STRING);
					manipulator.remove();
					
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

                    ((StackLayout) controlComposite.getLayout()).topControl = navigationButtonComposite;
					controlComposite.layout();
	
					Card card = manipulator.pop();
					browser.setText(htmlOut(card, false));

					OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
					OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
				}
			}
		});
		
		///////////////////////////////////////////////////////////////////////
		// navigationButtonComposite //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// FirstButton /////////
		firstButton.setEnabled(false);
		firstButton.setImage(SharedImages.getImage(SharedImages.GO_FIRST));
		firstButton.setToolTipText("Goto the first card");
		
		firstButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(firstButton.getEnabled()) {
					manipulator.first();
					Card card = manipulator.pop();
					browser.setText(htmlOut(card, false));
					
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
				}
			}
		});
		
		// PreviousButton /////////
		previousButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		previousButton.setEnabled(false);
		previousButton.setText("Previous");
		previousButton.setImage(SharedImages.getImage(SharedImages.GO_PREVIOUS));
		previousButton.setToolTipText("Goto the previous card");
		
		previousButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(previousButton.getEnabled()) {
					manipulator.previous();
					Card card = manipulator.pop();
					browser.setText(htmlOut(card, false));
					
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
				}
			}
		});
		
		// AnswerButton //////////
		answerButton.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerButton.setText("Answer");
		answerButton.setImage(SharedImages.getImage(SharedImages.EDIT_FIND));
		answerButton.setToolTipText("Show the answer for this card (review this card)");
		
		answerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(answerButton.getEnabled()) {
                    ((StackLayout) controlComposite.getLayout()).topControl = resultButtonComposite;
					controlComposite.layout();
					
					Card card = manipulator.pop();
					browser.setText(htmlOut(card, true));
				}
			}
		});
		
		// NextButton ////////////
		nextButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nextButton.setText("Next");
		nextButton.setImage(SharedImages.getImage(SharedImages.GO_NEXT));
		nextButton.setToolTipText("Goto the next card");
		
		if(manipulator.hasNext()) {
			nextButton.setEnabled(true);
			lastButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
			lastButton.setEnabled(false);
		}
		
		nextButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(nextButton.getEnabled()) {
					manipulator.next();
					Card card = manipulator.pop();
					browser.setText(htmlOut(card, false));
					
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
				}
			}
		});
		
		// LastButton /////////
		lastButton.setEnabled(true);
		lastButton.setImage(SharedImages.getImage(SharedImages.GO_LAST));
		lastButton.setToolTipText("Goto the last card");
		
		lastButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(lastButton.getEnabled()) {
					manipulator.last();
					Card card = manipulator.pop();
					browser.setText(htmlOut(card, false));
					
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
				}
			}
		});
		
		// Add Hot Keys (TODO : clean that...)
		CheckPanelHotKeys keyboardListener = new CheckPanelHotKeys(browser, firstButton, previousButton, answerButton, nextButton, lastButton, rightAnswerButton, wrongAnswerButton);
		browser.addKeyListener(keyboardListener);
		firstButton.addKeyListener(keyboardListener);
		previousButton.addKeyListener(keyboardListener);
		answerButton.addKeyListener(keyboardListener);
		nextButton.addKeyListener(keyboardListener);
		lastButton.addKeyListener(keyboardListener);
		rightAnswerButton.addKeyListener(keyboardListener);
		wrongAnswerButton.addKeyListener(keyboardListener);
	}
	
	/**
	 * 
	 */
	public void update() {	}
	
	/**
	 * TODO : Utiliser un StringBuffer pour la variable "html".
	 * 
	 * @param card
	 * @param answer
	 * @return
	 */
	final private String htmlOut(Card card, boolean printAnswer) {
		StringBuffer html = new StringBuffer();
		
		html.append("<html><head><style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.REVIEW_CSS);
		html.append("</style><head><body>");
		
		if(card == null) {
			html.append("<center>Review done</center>");
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
			if(printAnswer) {
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
		String html = text.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		
		// Rétabli l'interprétation pour les balises images
		String pattern = "&lt;(img src=\"file:///home/gremy/Desktop/opencal_materials/[0-9abcdef]{32}.(png|jpg|jpeg)\" /)&gt;";
		Pattern regPat = Pattern.compile(pattern);
		Matcher matcher = regPat.matcher(html);
		html = matcher.replaceAll("<$1>");
		
		return html;
	}
	
}
