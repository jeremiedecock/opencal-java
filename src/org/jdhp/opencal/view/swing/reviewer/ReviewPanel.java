/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swing.reviewer;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdhp.opencal.controller.reviewer.ReviewController;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ReviewPanel extends JPanel implements ActionListener {
	
	final public JButton prevButton;
	
	final public JButton nextButton;
	
	final public JButton answerButton;
	
	final public JButton goodButton;
	
	final public JButton badButton;
	
	final public JTextArea textArea;
	
	/**
	 * 
	 */
	public ReviewPanel() {
		super();
		
		this.textArea = new JTextArea(20, 83);
		this.textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.textArea.setLineWrap(true);
		this.textArea.setWrapStyleWord(true);
		this.textArea.setEditable(false);
		
		JScrollPane textPane = new JScrollPane(this.textArea);
		
		this.setLayout(new FlowLayout());
		this.add(textPane);
		
		this.prevButton = new JButton("Prev");
		this.prevButton.setToolTipText("Carte précédente");
		this.prevButton.addActionListener(this);
		this.add(this.prevButton);
		
		this.answerButton = new JButton("Answer");
		this.answerButton.setToolTipText("Réponse");
		this.answerButton.addActionListener(this);
		this.add(this.answerButton);
		
		this.nextButton = new JButton("Next");
		this.nextButton.setToolTipText("Carte suivante");
		this.nextButton.addActionListener(this);
		this.add(this.nextButton);
		
		this.goodButton = new JButton("+");
		this.goodButton.setToolTipText("Bonne réponse");
		this.goodButton.setEnabled(false);
		this.goodButton.addActionListener(this);
		this.add(this.goodButton);
		
		this.badButton = new JButton("-");
		this.badButton.setToolTipText("Mauvaise réponse");
		this.badButton.setEnabled(false);
		this.badButton.addActionListener(this);
		this.add(this.badButton);
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == this.prevButton) {
			ReviewController.revisionPile.gotoPrevCard();
			ReviewController.card = ReviewController.revisionPile.getPointedCard();
			this.textArea.setText("QUESTION " + (ReviewController.revisionPile.getReviewedCards() + 1) + " (r:" + ReviewController.revisionPile.getRemainingCards() + " - p:" + ReviewController.card.getPriorityRank() + ") :\n" + ReviewController.card.getQuestion());
			if(ReviewController.revisionPile.pointerIsOnTheFirstCard()) {
				this.prevButton.setEnabled(false);
			}
			this.nextButton.setEnabled(true);
		} else if(ev.getSource() == this.answerButton) {
			this.nextButton.setEnabled(false);
			this.answerButton.setEnabled(false);
			this.prevButton.setEnabled(false);
			this.goodButton.setEnabled(true);
			this.badButton.setEnabled(true);
			this.textArea.setText("QUESTION " + (ReviewController.revisionPile.getReviewedCards() + 1) + " (r:" + ReviewController.revisionPile.getRemainingCards() + " - p:" + ReviewController.card.getPriorityRank() + ") :\n" + ReviewController.card.getQuestion() + "\n\nANSWER :\n" + ReviewController.card.getAnswer());
		} else if(ev.getSource() == this.nextButton) {
			ReviewController.revisionPile.gotoNextCard();
			ReviewController.card = ReviewController.revisionPile.getPointedCard();
			this.textArea.setText("QUESTION " + (ReviewController.revisionPile.getReviewedCards() + 1) + " (r:" + ReviewController.revisionPile.getRemainingCards() + " - p:" + ReviewController.card.getPriorityRank() + ") :\n" + ReviewController.card.getQuestion());
			if(ReviewController.revisionPile.pointerIsOnTheLastCard()) {
				this.nextButton.setEnabled(false);
			}
			this.prevButton.setEnabled(true);
		} else if(ev.getSource() == this.goodButton) {
			ReviewController.updateCard("good");
			ReviewController.card = ReviewController.revisionPile.getPointedCard();
			if(!ReviewController.revisionPile.pointerIsOnTheLastCard()) {
				this.nextButton.setEnabled(true);
			}
			this.answerButton.setEnabled(true);
			if(!ReviewController.revisionPile.pointerIsOnTheFirstCard()) {
				this.prevButton.setEnabled(true);
			}
			this.goodButton.setEnabled(false);
			this.badButton.setEnabled(false);
			this.textArea.setText("QUESTION " + (ReviewController.revisionPile.getReviewedCards() + 1) + " (r:" + ReviewController.revisionPile.getRemainingCards() + " - p:" + ReviewController.card.getPriorityRank() + ") :\n" + ReviewController.card.getQuestion());
		} else if(ev.getSource() == this.badButton) {
			ReviewController.updateCard("bad");
			ReviewController.card = ReviewController.revisionPile.getPointedCard();
			if(!ReviewController.revisionPile.pointerIsOnTheLastCard()) {
				this.nextButton.setEnabled(true);
			}
			this.answerButton.setEnabled(true);
			if(!ReviewController.revisionPile.pointerIsOnTheFirstCard()) {
				this.prevButton.setEnabled(true);
			}
			this.goodButton.setEnabled(false);
			this.badButton.setEnabled(false);
			this.textArea.setText("QUESTION " + (ReviewController.revisionPile.getReviewedCards() + 1) + " (r:" + ReviewController.revisionPile.getRemainingCards() + " - p:" + ReviewController.card.getPriorityRank() + ") :\n" + ReviewController.card.getQuestion());
		}
	}
}
