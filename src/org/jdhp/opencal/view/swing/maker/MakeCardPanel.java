/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.view.swing.maker;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.controller.maker.MakeController;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MakeCardPanel extends JPanel implements ActionListener {
	
	final private JButton addButton;
	
	private JTextArea questionArea;
	
	private JTextArea answerArea;
	
	private JTextArea tagsArea;
	
	/**
	 * 
	 */
	public MakeCardPanel() {
		super();
		
		this.questionArea = new JTextArea(8, 83);
		this.questionArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.questionArea.setLineWrap(true);
		this.questionArea.setWrapStyleWord(true);
		this.answerArea = new JTextArea(8, 83);
		this.answerArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.answerArea.setLineWrap(true);
		this.answerArea.setWrapStyleWord(true);
		this.tagsArea = new JTextArea(4, 83);
		this.tagsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.tagsArea.setLineWrap(true);
		this.tagsArea.setWrapStyleWord(true);
		
		JScrollPane questionPane = new JScrollPane(this.questionArea);
		JScrollPane answerPane = new JScrollPane(this.answerArea);
		JScrollPane tagsPane = new JScrollPane(this.tagsArea);
		
		JLabel questionLabel = new JLabel("Question :");
		JLabel answerLabel = new JLabel("Answer :");
		JLabel tagsLabel = new JLabel("Tags :");

		this.addButton = new JButton("Save");
		this.addButton.setToolTipText("Add this card to the knowledge base.");
		this.addButton.addActionListener(this);
		
		this.setLayout(new FlowLayout());
		this.add(questionLabel);
		this.add(questionPane);
		this.add(answerLabel);
		this.add(answerPane);
		this.add(tagsLabel);
		this.add(tagsPane);
		this.add(this.addButton);
	}
	
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent ev) {
		if(this.questionArea.getText().equals("")) {
			Controller.getUserInterface().printAlert("La question ne doit pas être vide");
		} else {
			MakeController.addCard(this.questionArea.getText(), this.answerArea.getText(), this.tagsArea.getText());
			this.questionArea.setText("");
			this.answerArea.setText("");
			this.tagsArea.setText("");
		}
		
		// Donne le focus à la questionArea
		this.questionArea.requestFocus();
	}
}
