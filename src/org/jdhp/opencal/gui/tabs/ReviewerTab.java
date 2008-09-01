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
import org.jdhp.opencal.gui.images.SharedImages;
import org.jdhp.opencal.usecase.Card;
import org.jdhp.opencal.usecase.CardManipulator;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewerTab {

	final private Composite parentComposite;
	
	final private StyledText reviewerText;
	
	final private CardManipulator manipulator = OpenCAL.plannedCardList.manipulator();
	
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
		
		Card card = manipulator.pop();
		if(card != null) {
			this.reviewerText.setText("QUESTION\n\n" + card.getQuestion());
			this.reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
		} else {
			this.reviewerText.setText("Review done");
			this.reviewerText.setStyleRange(new StyleRange(0, 11, null, null, SWT.BOLD));
		}
		
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
		navigationButtonComposite.setLayout(new GridLayout(3, false));
		
		GridData navigationButtonCompositeGridData = new GridData(GridData.FILL_HORIZONTAL);
		navigationButtonCompositeGridData.horizontalSpan = 2;
		navigationButtonComposite.setLayoutData(navigationButtonCompositeGridData);
		
		final Button firstButton = new Button(navigationButtonComposite, SWT.PUSH);
		Composite centralNavigationButtonComposite = new Composite(navigationButtonComposite, SWT.NONE);
		final Button lastButton = new Button(navigationButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// centralNavigationButtonComposite ///////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		GridLayout centralNavigationButtonCompositeGridLayout = new GridLayout(3, true);
		centralNavigationButtonCompositeGridLayout.marginWidth = 0;
		centralNavigationButtonComposite.setLayout(centralNavigationButtonCompositeGridLayout);
		
		centralNavigationButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Button previousButton = new Button(centralNavigationButtonComposite, SWT.PUSH);
		final Button answerButton = new Button(centralNavigationButtonComposite, SWT.PUSH);
		final Button nextButton = new Button(centralNavigationButtonComposite, SWT.PUSH);
		
		///////////////////////////////////////////////////////////////////////
		// resultButtons //////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// rightAnswerButton /////////////
		goodButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		goodButton.setEnabled(false);
		goodButton.setText("Correct");
		goodButton.setImage(SharedImages.getImage(SharedImages.FACE_SMILE));
		goodButton.setToolTipText("Right answer");
		
		goodButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manipulator.pop().putReview(OpenCAL.RIGHT_ANSWER_STRING);
				manipulator.remove();
				
				if(manipulator.hasPreview()) {
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
				answerButton.setEnabled(true);
				goodButton.setEnabled(false);
				badButton.setEnabled(false);
				
				Card card = manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
			}
		});
		
		// wrongAnswerButton /////////////
		badButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		badButton.setEnabled(false);
		badButton.setText("Wrong");
		badButton.setImage(SharedImages.getImage(SharedImages.FACE_SAD));
		badButton.setToolTipText("Wrong answer");
		
		badButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manipulator.pop().putReview(OpenCAL.WRONG_ANSWER_STRING);
				manipulator.remove();
				
				if(manipulator.hasPreview()) {
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
				answerButton.setEnabled(true);
				goodButton.setEnabled(false);
				badButton.setEnabled(false);

				Card card = manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
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
				manipulator.first();
				Card card = manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
				
				if(manipulator.hasPreview()) {
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
		});
		
		// PreviousButton /////////
		previousButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		previousButton.setEnabled(false);
		previousButton.setText("Previous");
		previousButton.setImage(SharedImages.getImage(SharedImages.GO_PREVIOUS));
		previousButton.setToolTipText("Goto the previous card");
		
		previousButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manipulator.preview();
				Card card = manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
				
				if(manipulator.hasPreview()) {
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
		});
		
		// AnswerButton //////////
		answerButton.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerButton.setText("Answer");
		answerButton.setImage(SharedImages.getImage(SharedImages.EDIT_FIND));
		answerButton.setToolTipText("Show the answer for this card (review this card)");
		
		answerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				nextButton.setEnabled(false);
				answerButton.setEnabled(false);
				previousButton.setEnabled(false);
				goodButton.setEnabled(true);
				badButton.setEnabled(true);
				
//				reviewerText.setText("QUESTION\n\n" + OpenCAL.card.getQuestion() + "\n\nANSWER\n\n" + OpenCAL.card.getAnswer());
				int textLength = reviewerText.getCharCount();
				
				Card card = manipulator.pop();
				if(card != null) reviewerText.append("\n\nANSWER\n\n" + card.getAnswer());
				reviewerText.setStyleRange(new StyleRange(textLength + 2, 6, null, null, SWT.BOLD));
				if(card != null) OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
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
				manipulator.next();
				Card card = manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
				
				if(manipulator.hasPreview()) {
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
		});
		
		// LastButton /////////
		lastButton.setEnabled(true);
		lastButton.setImage(SharedImages.getImage(SharedImages.GO_LAST));
		lastButton.setToolTipText("Goto the last card");
		
		lastButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				manipulator.last();
				Card card = manipulator.pop();
				reviewerText.setText("QUESTION\n\n" + card.getQuestion());
				reviewerText.setStyleRange(new StyleRange(0, 8, null, null, SWT.BOLD));
				OpenCAL.mainWindow.setStatusLabel2("G : " + card.getGrade(), "Card grade " + card.getGrade());
				OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
				OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
				
				if(manipulator.hasPreview()) {
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
		});
	}
	
	/**
	 * 
	 */
	public void update() {
		OpenCAL.mainWindow.setStatusLabel1("", "");
		Card card = manipulator.pop();
		if(card != null) OpenCAL.mainWindow.setStatusLabel2("L : " + card.getGrade(), "Card level " + card.getGrade());
		else OpenCAL.mainWindow.setStatusLabel2("", "");
		OpenCAL.mainWindow.setStatusLabel3("D : " + OpenCAL.reviewedCardList.size(), OpenCAL.reviewedCardList.size() + " review done today");
		OpenCAL.mainWindow.setStatusLabel4("R : " + OpenCAL.plannedCardList.size(), OpenCAL.plannedCardList.size() + " cards left for today");
		
//		if(manipulator.hasPreview()) {
//			previousButton.setEnabled(true);
//			firstButton.setEnabled(true);
//		} else {
//			previousButton.setEnabled(false);
//			firstButton.setEnabled(false);
//		}
//		if(manipulator.hasNext()) {
//			nextButton.setEnabled(true);
//			lastButton.setEnabled(true);
//		} else {
//			nextButton.setEnabled(false);
//			lastButton.setEnabled(false);
//		}
	}
	
}
