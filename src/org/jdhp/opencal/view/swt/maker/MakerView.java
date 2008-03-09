/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.maker;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.controller.Controller;
import org.jdhp.opencal.controller.maker.MakeController;
import org.jdhp.opencal.model.xml.maker.CardMakerHandler;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MakerView {

	final private Composite parentComposite;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public MakerView(Composite parentComposite) {
		this.parentComposite = parentComposite;

		///////////////////////////////////////////////////////////////////////
		// Make makeCardComposite /////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		GridLayout makeCardCompositeGridLayout = new GridLayout(1, false);
		this.parentComposite.setLayout(makeCardCompositeGridLayout);
		
		Font monoFont = new Font(this.parentComposite.getDisplay(), "mono", 10, SWT.NORMAL);
		
		// Question ////////
		Group questionGroup = new Group(this.parentComposite, SWT.NONE);
		questionGroup.setLayout(new GridLayout(1, false));
		questionGroup.setText("Question");
		
		final Text questionText = new Text(questionGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		questionText.setFont(monoFont);
		questionText.setTabs(3);
		
		GridData questionGroupGridData = new GridData(GridData.FILL_BOTH);
		questionGroup.setLayoutData(questionGroupGridData);
		
		GridData questionTextGridData = new GridData(GridData.FILL_BOTH);
		questionText.setLayoutData(questionTextGridData);
		
		// Answer //////////
		Group answerGroup = new Group(this.parentComposite, SWT.NONE);
		answerGroup.setLayout(new GridLayout(1, false));
		answerGroup.setText("Answer");
		
		final Text answerText = new Text(answerGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		answerText.setFont(monoFont);
		answerText.setTabs(3);

		GridData answerGroupGridData = new GridData(GridData.FILL_BOTH);
		answerGroup.setLayoutData(answerGroupGridData);
		
		GridData answerTextGridData = new GridData(GridData.FILL_BOTH);
		answerText.setLayoutData(answerTextGridData);
		
		// Tags ////////////
		Group tagGroup = new Group(this.parentComposite, SWT.NONE);
		tagGroup.setLayout(new GridLayout(1, false));
		tagGroup.setText("Tags");
		
		final Text tagsText = new Text(tagGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		tagsText.setFont(monoFont);
		tagsText.setTabs(3);

		GridData tagsGroupGridData = new GridData(GridData.FILL_BOTH);
		tagGroup.setLayoutData(tagsGroupGridData);
		
		GridData tagsTextGridData = new GridData(GridData.FILL_BOTH);
		tagsText.setLayoutData(tagsTextGridData);
		
		// Button /////////
		Button addButton = new Button(this.parentComposite, SWT.PUSH);
		addButton.setText("Add this card");
		addButton.setToolTipText("Add this card to the database");
		
		GridData addButtonGridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		addButton.setLayoutData(addButtonGridData);
		
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(questionText.getText().equals("")) {
					Controller.getUserInterface().printAlert("La question ne doit pas être vide !");
				} else {
					MakeController.addCard(questionText.getText(), answerText.getText(), tagsText.getText());
					questionText.setText("");
					answerText.setText("");
					tagsText.setText("");
					Controller.getUserInterface().setStatusLabel1("Card #" + CardMakerHandler.getLastCardRecordedId() + " recorded", "Card #" + CardMakerHandler.getLastCardRecordedId() + " recorded");
				}
			
				// Donne le focus à la questionArea
				questionText.setFocus();
			}
		});
	}
	
}
