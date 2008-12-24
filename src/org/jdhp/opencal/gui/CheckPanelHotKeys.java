package org.jdhp.opencal.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;

public class CheckPanelHotKeys implements KeyListener {
	
	private boolean shiftFlag;
	
	private Browser browser;
	
	private Button firstButton;
	
	private Button previousButton;
	
	private Button answerButton;
	
	private Button nextButton;
	
	private Button lastButton;
	
	private Button rightAnswerButton;
	
	private Button wrongAnswerButton;
	
	private final static int PREVIOUS_KEY = SWT.ARROW_LEFT;
	
	private final static int NEXT_KEY = SWT.ARROW_RIGHT;
	
	private final static char ANSWER_KEY = 'a';
	
	private final static char RIGHT_KEY = 'r';
	
	private final static char WRONG_KEY = 'w';
	
	/**
	 * 
	 * @param firstButton
	 * @param previousButton
	 * @param answerButton
	 * @param nextButton
	 * @param lastButton
	 * @param rightAnswerButton
	 * @param wrongAnswerButton
	 */
	public CheckPanelHotKeys(Browser browser, Button firstButton, Button previousButton, Button answerButton, Button nextButton, Button lastButton, Button rightAnswerButton, Button wrongAnswerButton) {
		this.browser = browser;
		this.firstButton = firstButton;
		this.previousButton = previousButton;
		this.answerButton = answerButton;
		this.nextButton = nextButton;
		this.lastButton = lastButton;
		this.rightAnswerButton = rightAnswerButton;
		this.wrongAnswerButton = wrongAnswerButton;
		
		this.shiftFlag = false;
	}
	
	/**
	 * 
	 */
	public void keyPressed(KeyEvent kev) {
		if(kev.keyCode == SWT.SHIFT) this.shiftFlag = true;
	}

	/**
	 * 
	 */
	public void keyReleased(KeyEvent kev) {
		if(kev.keyCode == CheckPanelHotKeys.PREVIOUS_KEY && this.shiftFlag) {
			this.firstButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.keyCode == CheckPanelHotKeys.PREVIOUS_KEY && !this.shiftFlag) {
			this.previousButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.keyCode == CheckPanelHotKeys.NEXT_KEY && this.shiftFlag) {
			this.lastButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.keyCode == CheckPanelHotKeys.NEXT_KEY && !this.shiftFlag) {
			this.nextButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.character == CheckPanelHotKeys.ANSWER_KEY) {
			this.answerButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.character == CheckPanelHotKeys.RIGHT_KEY) {
			this.rightAnswerButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.character == CheckPanelHotKeys.WRONG_KEY) {
			this.wrongAnswerButton.notifyListeners(SWT.Selection, new Event());
			this.browser.setFocus();
		} else if(kev.keyCode == SWT.SHIFT) {
			this.shiftFlag = false;
		}
	}

}
