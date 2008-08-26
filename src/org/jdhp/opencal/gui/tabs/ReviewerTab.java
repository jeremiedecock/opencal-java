/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.gui.tabs;

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
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.usecase.CardManipulator;
import org.jdhp.opencal.usecase.review.ReviewedCard;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewerTab {

	final private Composite parentComposite;
	
	final private StyledText reviewerText;
	
	final private Button nextButton;
	
	final private CardManipulator manipulator = OpenCAL.reviewedCardList.manipulator();
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ReviewerTab(Composite parentComposite) {
		
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(2, false));

		Font monoFont = new Font(this.parentComposite.getDisplay(), "mono", 10, SWT.NORMAL);
		
		///////////////////////////////////////////////////////////////////////
		// reviewerText ///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		this.reviewerText = new StyledText(this.parentComposite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
		this.reviewerText.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.reviewerText.setFont(monoFont);
		this.reviewerText.setTabs(3);
		
		///////////////////////////////////////////////////////////////////////
		// resultButtonComposite //////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite resultButtonComposite = new Composite(this.parentComposite, SWT.NONE);
		GridLayout resultButtonCompositeGridLayout = new GridLayout(1, false);
		resultButtonCompositeGridLayout.verticalSpacing = 40;
		resultButtonComposite.setLayout(resultButtonCompositeGridLayout);
		
		final Button goodButton = new Button(resultButtonComposite, SWT.PUSH);
		final Button badButton = new Button(resultButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// navigationButtonComposite //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite navigationButtonComposite = new Composite(this.parentComposite, SWT.NONE);
		navigationButtonComposite.setLayout(new GridLayout(3, true));
		
		GridData navigationButtonCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
		navigationButtonCompositeGridData.horizontalSpan = 2;
		navigationButtonComposite.setLayoutData(navigationButtonCompositeGridData);
		
		final Button previewButton = new Button(navigationButtonComposite, SWT.PUSH);
		final Button answerButton = new Button(navigationButtonComposite, SWT.PUSH);
		this.nextButton = new Button(navigationButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// resultButtons //////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// goodButton /////////////
		goodButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		goodButton.setEnabled(false);
		goodButton.setText("Good");
		goodButton.setToolTipText("Good answer");
		
		goodButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((ReviewedCard) manipulator.pop()).putReview("good");
				manipulator.remove();
				
				if(manipulator.hasPreview()) previewButton.setEnabled(true);
				else previewButton.setEnabled(false);
				answerButton.setEnabled(true);
				if(manipulator.hasNext()) nextButton.setEnabled(true);
				else nextButton.setEnabled(false);
				goodButton.setEnabled(false);
				badButton.setEnabled(false);
				
				ReviewedCard card = (ReviewedCard) manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.getReviewedCards(), OpenCAL.reviewedCardList.getReviewedCards() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.reviewedCardList.getRemainingCards(), OpenCAL.reviewedCardList.getRemainingCards() + " cards left for today");
			}
		});
		
		// badButton /////////////
		badButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		badButton.setEnabled(false);
		badButton.setText("Bad");
		badButton.setToolTipText("Bad answer");
		
		badButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((ReviewedCard) manipulator.pop()).putReview("bad");
				manipulator.remove();
				
				if(manipulator.hasPreview()) previewButton.setEnabled(true);
				else previewButton.setEnabled(false);
				answerButton.setEnabled(true);
				if(manipulator.hasNext()) nextButton.setEnabled(true);
				else nextButton.setEnabled(false);
				goodButton.setEnabled(false);
				badButton.setEnabled(false);

				ReviewedCard card = (ReviewedCard) manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.getReviewedCards(), OpenCAL.reviewedCardList.getReviewedCards() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.reviewedCardList.getRemainingCards(), OpenCAL.reviewedCardList.getRemainingCards() + " cards left for today");
			}
		});
		
		///////////////////////////////////////////////////////////////////////
		// navigationButtonComposite //////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// PreviewButton /////////
		previewButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		previewButton.setEnabled(false);
		previewButton.setText("Preview");
		previewButton.setToolTipText("Goto the preview card");
		
		previewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manipulator.preview();
				ReviewedCard card = (ReviewedCard) manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.getReviewedCards(), OpenCAL.reviewedCardList.getReviewedCards() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.reviewedCardList.getRemainingCards(), OpenCAL.reviewedCardList.getRemainingCards() + " cards left for today");
				
				if(manipulator.hasPreview()) previewButton.setEnabled(true);
				else previewButton.setEnabled(false);
				if(manipulator.hasNext()) nextButton.setEnabled(true);
				else nextButton.setEnabled(false);
			}
		});
		
		// AnswerButton //////////
		answerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		answerButton.setText("Answer");
		answerButton.setToolTipText("Show the answer for this card (review this card)");
		
		answerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				nextButton.setEnabled(false);
				answerButton.setEnabled(false);
				previewButton.setEnabled(false);
				goodButton.setEnabled(true);
				badButton.setEnabled(true);
				
//				reviewerText.setText("QUESTION\n\n" + OpenCAL.card.getQuestion() + "\n\nANSWER\n\n" + OpenCAL.card.getAnswer());
				int textLength = reviewerText.getCharCount();
				
				ReviewedCard card = (ReviewedCard) manipulator.pop();
				if(card != null) reviewerText.append("\n\nANSWER\n\n" + card.getAnswer());
				reviewerText.setStyleRange(new StyleRange(textLength + 2, 6, null, null, SWT.BOLD));
				if(card != null) OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.getReviewedCards(), OpenCAL.reviewedCardList.getReviewedCards() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.reviewedCardList.getRemainingCards(), OpenCAL.reviewedCardList.getRemainingCards() + " cards left for today");
			}
		});
		
		// NextButton ////////////
		this.nextButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.nextButton.setText("Next");
		this.nextButton.setToolTipText("Goto the next card");
		
		this.nextButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manipulator.next();
				ReviewedCard card = (ReviewedCard) manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.getReviewedCards(), OpenCAL.reviewedCardList.getReviewedCards() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.reviewedCardList.getRemainingCards(), OpenCAL.reviewedCardList.getRemainingCards() + " cards left for today");
				
				if(manipulator.hasPreview()) previewButton.setEnabled(true);
				else previewButton.setEnabled(false);
				if(manipulator.hasNext()) nextButton.setEnabled(true);
				else nextButton.setEnabled(false);
			}
		});
		
	}
	
	/**
	 * 
	 */
	public void init() {
		ReviewedCard card = (ReviewedCard) manipulator.pop();
		if(card != null) {
			this.reviewerText.setText("QUESTION\n\n" + card.getQuestion());
			this.reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
			
			if(manipulator.hasNext()) nextButton.setEnabled(true);
			else nextButton.setEnabled(false);
		} else {
			this.reviewerText.setText("Review done");
			this.reviewerText.setStyleRange(new StyleRange(0, 11, null, null, SWT.BOLD));
			this.nextButton.setEnabled(false);
		}
	}
	
}
