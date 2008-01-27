/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.view.UserInterface;
import org.jdhp.opencal.view.swing.explorer.ExplorerPanel;
import org.jdhp.opencal.view.swing.maker.MakeCardPanel;
import org.jdhp.opencal.view.swing.reviewer.ReviewPanel;
import org.jdhp.opencal.view.swing.stats.StatPanel;

public class SwingGUI extends UserInterface {
	
	final private JFrame window;
	
	final private MakeCardPanel makeCardPanel;
	final private ReviewPanel reviewerPanel;
	final private ExplorerPanel explorerPanel;
	final private StatPanel statsPanel;
	
	public SwingGUI() {
		this.window = new JFrame();
		
		this.window.setTitle(OpenCAL.programName + " " + OpenCAL.programVersion);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(false);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		// Add MakeCardPanel
		this.makeCardPanel = new MakeCardPanel();
		this.makeCardPanel.setPreferredSize(new Dimension(630, 436));
		tabbedPane.addTab("Make", null, this.makeCardPanel, "Make Cards");
		
		// Add ReviewCards
		this.reviewerPanel = new ReviewPanel();
		this.reviewerPanel.setPreferredSize(new Dimension(630, 436));
		tabbedPane.addTab("Review", null, this.reviewerPanel, "Review Cards");
		
		// Add ExplorerPanel
		this.explorerPanel = new ExplorerPanel();
		tabbedPane.addTab("Explorer", null, this.explorerPanel, "Explorer");
		
		// Add StatsPanel
		this.statsPanel = new StatPanel();
		tabbedPane.addTab("Stats", null, this.statsPanel, "Stats");
		
		// Window
		Container container = this.window.getContentPane();
//		container.setLayout(new FlowLayout());
		container.add(tabbedPane);
		
		this.window.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.window.setLocation(dim.width/2 - this.window.getWidth()/2, dim.height/2 - this.window.getHeight()/2);
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel1(String text, String toolTipText) {
		
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel2(String text, String toolTipText) {
		
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel3(String text, String toolTipText) {
		
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel4(String text, String toolTipText) {
		
	}
	
	public void print(String text) {
		JOptionPane.showMessageDialog(this.window, text, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void printAlert(String text) {
		JOptionPane.showMessageDialog(this.window, text, "Attention", JOptionPane.WARNING_MESSAGE);
	}
	
	public void printError(String text) {
		JOptionPane.showMessageDialog(this.window, text, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	public void update() {
		// appel repaint ...
	}
	
	public void run() {
		// Init pour reviewer
		this.reviewerPanel.textArea.setText("QUESTION " + (Controller.pile.getReviewedCards() + 1) + " (r:" + Controller.pile.getRemainingCards() + " - p:" + Controller.card.getPriority() + ") :\n" + Controller.card.getQuestion());
		
		this.reviewerPanel.prevButton.setEnabled(false);
		if(Controller.pile.pointerIsOnTheLastCard()) {
			this.reviewerPanel.nextButton.setEnabled(false);
		}
		
		// Init commun
		this.window.setVisible(true);
	}
	
}
