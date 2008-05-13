/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.explorer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.controller.explorer.MadeCardsController;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class ExplorerView {

	final private static int ALL_CARDS = 0;
	
	final private static int REVIEWED_CARDS = 1;
	
	final private static int MADE_CARDS = 2;
	
	final private static int DISABLED_CARDS = 3;
	
	private static int currentDisplayMode; // pas très propre de mettre ça en static ?
	
	final private Composite parentComposite;
	
	final private String[] displayModes = {"All Cards", "Reviewed Cards", "Made Cards", "Disabled Cards"};
	
	/**
	 * 
	 * @param parentComposite
	 */
	public ExplorerView(Composite parentComposite) {
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(2, false));
		
		final Text questionText;
		final Text answerText;
		final Text tagsText;
		
		ExplorerView.currentDisplayMode = ExplorerView.ALL_CARDS;

		///////////////////////////////////////////////////////////////////////
		// CardSelectionComposite /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite cardSelectionComposite = new Composite(this.parentComposite, SWT.NONE);
		cardSelectionComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		cardSelectionComposite.setLayout(new GridLayout(1, false));
		
		// displayModeCombo ////////////
		final Combo displayModeCombo = new Combo(cardSelectionComposite, SWT.BORDER | SWT.READ_ONLY);
		displayModeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		displayModeCombo.setItems(displayModes);
		displayModeCombo.select(ExplorerView.currentDisplayMode);
		
		// cardsList ////////////
		final List cardsList = new List(cardSelectionComposite, SWT.BORDER | SWT.V_SCROLL);
		cardsList.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		cardsList.setItems(new String[0]);
		
		///////////////////////////////////////////////////////////////////////
		// EditionCardComposite ///////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite editionCardComposite = new Composite(this.parentComposite, SWT.NONE);
		editionCardComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		editionCardComposite.setLayout(new GridLayout(1, false));
		
		Font monoFont = new Font(this.parentComposite.getDisplay(), "mono", 10, SWT.NORMAL);
		
		// Question ////////
		Group questionGroup = new Group(editionCardComposite, SWT.NONE);
		questionGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		questionGroup.setLayout(new GridLayout(1, false));
		questionGroup.setText("Question");
		
		questionText = new Text(questionGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		questionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		questionText.setFont(monoFont);
		questionText.setTabs(3);
		
		// Answer //////////
		Group answerGroup = new Group(editionCardComposite, SWT.NONE);
		answerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerGroup.setLayout(new GridLayout(1, false));
		answerGroup.setText("Answer");
		
		answerText = new Text(answerGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		answerText.setLayoutData(new GridData(GridData.FILL_BOTH));
		answerText.setFont(monoFont);
		answerText.setTabs(3);
		
		// Tags ////////////
		Group tagGroup = new Group(editionCardComposite, SWT.NONE);
		tagGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		tagGroup.setLayout(new GridLayout(1, false));
		tagGroup.setText("Tags");
		
		tagsText = new Text(tagGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		tagsText.setLayoutData(new GridData(GridData.FILL_BOTH));
		tagsText.setFont(monoFont);
		tagsText.setTabs(3);

		///////////////////////////////////////////////////////////////////////
		// FileButtonComposite ////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		Composite fileButtonComposite = new Composite(editionCardComposite, SWT.NONE);
		fileButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		RowLayout fileButtonCompositeLayout = new RowLayout(SWT.HORIZONTAL);
		fileButtonCompositeLayout.justify = true;
		fileButtonComposite.setLayout(fileButtonCompositeLayout);
		
		// SaveButton /////////
		Button saveButton = new Button(fileButtonComposite, SWT.PUSH);
		saveButton.setEnabled(false);
		saveButton.setText("Save");
		saveButton.setToolTipText("Save modification for this card");
		
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		
		// CancelButton /////////
		Button cancelButton = new Button(fileButtonComposite, SWT.PUSH);
		cancelButton.setEnabled(false);
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText("Cancel modification for this card");
		
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		
		///////////////////////////////////////////////////////////////////////
		// CardSelectionListeners /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		// displayModeComboListener ////////////
		displayModeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch(displayModeCombo.getSelectionIndex()) {
					case 0 :
						cardsList.setItems(new String[0]);
						break;
					case 1 : 
						cardsList.setItems(new String[0]);
						break;
					case 2 :
						cardsList.setItems(MadeCardsController.getStrings());
						break;
					case 3 :
						cardsList.setItems(new String[0]);
						break;
				}
			}
		});
		
		// cardsListListener ////////////
		cardsList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch (displayModeCombo.getSelectionIndex()) {
					case 0:
						
						break;
					case 1:
						
						break;
					case 2:
						questionText.setText(MadeCardsController.getQuestion(cardsList.getSelectionIndex()));
						answerText.setText(MadeCardsController.getAnswer(cardsList.getSelectionIndex()));
						tagsText.setText(MadeCardsController.getTags(cardsList.getSelectionIndex()));
						break;
					case 3:
						
						break;
				}
			}
		});
	}
	
}
