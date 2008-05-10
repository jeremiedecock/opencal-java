/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.reviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.controller.reviewer.ReviewController;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewerView {

	final private Composite parentComposite;
	
	final private StyledText reviewerText;
	
	final private Button nextButton;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ReviewerView(Composite parentComposite) {
		this.parentComposite = parentComposite;

		///////////////////////////////////////////////////////////////////////
		// Make reviewerComposite /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		GridLayout reviewerCompositeGridLayout = new GridLayout(2, false);
		this.parentComposite.setLayout(reviewerCompositeGridLayout);

		Font monoFont = new Font(this.parentComposite.getDisplay(), "mono", 10, SWT.NORMAL);
		
		// Text //////////////////
		this.reviewerText = new StyledText(this.parentComposite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
		this.reviewerText.setFont(monoFont);
		this.reviewerText.setTabs(3);
		
		GridData textGridData = new GridData(GridData.FILL_BOTH);
		this.reviewerText.setLayoutData(textGridData);
		
		// ResultButtons /////////
		Composite resultButtonComposite = new Composite(this.parentComposite, SWT.NONE);
		GridLayout resultButtonCompositeGridLayout = new GridLayout(1, false);
		resultButtonCompositeGridLayout.verticalSpacing = 40;
		resultButtonComposite.setLayout(resultButtonCompositeGridLayout);
		
		final Button goodButton = new Button(resultButtonComposite, SWT.PUSH);
		final Button badButton = new Button(resultButtonComposite, SWT.PUSH);
		
		// NavigationButton //////
		Composite navigationButtonComposite = new Composite(this.parentComposite, SWT.NONE);
		navigationButtonComposite.setLayout(new GridLayout(3, true));
		
		GridData navigationButtonCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
		navigationButtonCompositeGridData.horizontalSpan = 2;
		navigationButtonComposite.setLayoutData(navigationButtonCompositeGridData);
		
		final Button previewButton = new Button(navigationButtonComposite, SWT.PUSH);
		final Button answerButton = new Button(navigationButtonComposite, SWT.PUSH);
		this.nextButton = new Button(navigationButtonComposite, SWT.PUSH);
		
		// GoodButton ////////////
		goodButton.setText("Good");
		goodButton.setToolTipText("Good answer");
		
		GridData goodButtonGridData = new GridData(GridData.FILL_HORIZONTAL);
		goodButton.setLayoutData(goodButtonGridData);
		goodButton.setEnabled(false);
		
		goodButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ReviewController.updateCard("good");
				ReviewController.card = ReviewController.revisionPile.getPointedCard();
				if(!ReviewController.revisionPile.pointerIsOnTheLastCard()) {
					nextButton.setEnabled(true);
				}
				answerButton.setEnabled(true);
				if(!ReviewController.revisionPile.pointerIsOnTheFirstCard()) {
					previewButton.setEnabled(true);
				}
				goodButton.setEnabled(false);
				badButton.setEnabled(false);
				reviewerText.setText("QUESTION\n\n" + ReviewController.card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				Controller.getUserInterface().setStatusLabel2("L : " + ReviewController.card.getPriorityRank(), "Card level " + ReviewController.card.getPriorityRank());
				Controller.getUserInterface().setStatusLabel3("D : " + ReviewController.revisionPile.getReviewedCards(), ReviewController.revisionPile.getReviewedCards() + " review done today");
				Controller.getUserInterface().setStatusLabel4("R : " + ReviewController.revisionPile.getRemainingCards(), ReviewController.revisionPile.getRemainingCards() + " cards left for today");
			}
		});
		
		// BadButton /////////////
		badButton.setText("Bad");
		badButton.setToolTipText("Bad answer");
		
		GridData badButtonGridData = new GridData(GridData.FILL_HORIZONTAL);
		badButton.setLayoutData(badButtonGridData);
		badButton.setEnabled(false);
		
		badButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ReviewController.updateCard("bad");
				ReviewController.card = ReviewController.revisionPile.getPointedCard();
				if(!ReviewController.revisionPile.pointerIsOnTheLastCard()) {
					nextButton.setEnabled(true);
				}
				answerButton.setEnabled(true);
				if(!ReviewController.revisionPile.pointerIsOnTheFirstCard()) {
					previewButton.setEnabled(true);
				}
				goodButton.setEnabled(false);
				badButton.setEnabled(false);
				reviewerText.setText("QUESTION\n\n" + ReviewController.card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				Controller.getUserInterface().setStatusLabel2("L : " + ReviewController.card.getPriorityRank(), "Card level " + ReviewController.card.getPriorityRank());
				Controller.getUserInterface().setStatusLabel3("D : " + ReviewController.revisionPile.getReviewedCards(), ReviewController.revisionPile.getReviewedCards() + " review done today");
				Controller.getUserInterface().setStatusLabel4("R : " + ReviewController.revisionPile.getRemainingCards(), ReviewController.revisionPile.getRemainingCards() + " cards left for today");
			}
		});
		
		// PreviewButton /////////
		previewButton.setText("Preview");
		previewButton.setToolTipText("Goto the preview card");
		previewButton.setEnabled(false);
		
		GridData previewButtonGridData = new GridData(GridData.FILL_HORIZONTAL);
		previewButton.setLayoutData(previewButtonGridData);
		
		previewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ReviewController.revisionPile.gotoPrevCard();
				ReviewController.card = ReviewController.revisionPile.getPointedCard();
				reviewerText.setText("QUESTION\n\n" + ReviewController.card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				Controller.getUserInterface().setStatusLabel2("L : " + ReviewController.card.getPriorityRank(), "Card level " + ReviewController.card.getPriorityRank());
				Controller.getUserInterface().setStatusLabel3("D : " + ReviewController.revisionPile.getReviewedCards(), ReviewController.revisionPile.getReviewedCards() + " review done today");
				Controller.getUserInterface().setStatusLabel4("R : " + ReviewController.revisionPile.getRemainingCards(), ReviewController.revisionPile.getRemainingCards() + " cards left for today");
				
				if(ReviewController.revisionPile.pointerIsOnTheFirstCard()) {
					previewButton.setEnabled(false);
				}
				nextButton.setEnabled(true);
			}
		});
		
		// AnswerButton //////////
		answerButton.setText("Answer");
		answerButton.setToolTipText("Show the answer for this card (review this card)");

		GridData answerButtonGridData = new GridData(GridData.FILL_HORIZONTAL);
		answerButton.setLayoutData(answerButtonGridData);
		
		answerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				nextButton.setEnabled(false);
				answerButton.setEnabled(false);
				previewButton.setEnabled(false);
				goodButton.setEnabled(true);
				badButton.setEnabled(true);
//				reviewerText.setText("QUESTION\n\n" + Controller.card.getQuestion() + "\n\nANSWER\n\n" + Controller.card.getAnswer());
				int textLength = reviewerText.getCharCount();
				if(ReviewController.card != null) reviewerText.append("\n\nANSWER\n\n" + ReviewController.card.getAnswer());
				reviewerText.setStyleRange(new StyleRange(textLength + 2, 6, null, null, SWT.BOLD));
				if(ReviewController.card != null) Controller.getUserInterface().setStatusLabel2("L : " + ReviewController.card.getPriorityRank(), "Card level " + ReviewController.card.getPriorityRank());
				Controller.getUserInterface().setStatusLabel3("D : " + ReviewController.revisionPile.getReviewedCards(), ReviewController.revisionPile.getReviewedCards() + " review done today");
				Controller.getUserInterface().setStatusLabel4("R : " + ReviewController.revisionPile.getRemainingCards(), ReviewController.revisionPile.getRemainingCards() + " cards left for today");
			}
		});
		
		// NextButton ////////////
		this.nextButton.setText("Next");
		this.nextButton.setToolTipText("Goto the next card");
		
		GridData nextButtonGridData = new GridData(GridData.FILL_HORIZONTAL);
		this.nextButton.setLayoutData(nextButtonGridData);
		
		this.nextButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ReviewController.revisionPile.gotoNextCard();
				ReviewController.card = ReviewController.revisionPile.getPointedCard();
				reviewerText.setText("QUESTION\n\n" + ReviewController.card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				Controller.getUserInterface().setStatusLabel2("L : " + ReviewController.card.getPriorityRank(), "Card level " + ReviewController.card.getPriorityRank());
				Controller.getUserInterface().setStatusLabel3("D : " + ReviewController.revisionPile.getReviewedCards(), ReviewController.revisionPile.getReviewedCards() + " review done today");
				Controller.getUserInterface().setStatusLabel4("R : " + ReviewController.revisionPile.getRemainingCards(), ReviewController.revisionPile.getRemainingCards() + " cards left for today");
				
				if(ReviewController.revisionPile.pointerIsOnTheLastCard()) {
					nextButton.setEnabled(false);
				}
				previewButton.setEnabled(true);
			}
		});
		
	}
	
	/**
	 * 
	 */
	public void init() {
		if(ReviewController.card != null) {
			this.reviewerText.setText("QUESTION\n\n" + ReviewController.card.getQuestion());
			this.reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
			
//			previewButton.setEnabled(false);
			if(ReviewController.revisionPile.pointerIsOnTheLastCard()) {
				this.nextButton.setEnabled(false);
			}
		} else {
			this.reviewerText.setText("Review done");
			this.reviewerText.setStyleRange(new StyleRange(0, 11, null, null, SWT.BOLD));
			this.nextButton.setEnabled(false);
		}
	}
	
}
