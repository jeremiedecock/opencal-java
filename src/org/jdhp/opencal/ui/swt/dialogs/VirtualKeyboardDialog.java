/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */
package org.jdhp.opencal.ui.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jdhp.opencal.ui.swt.images.SharedImages;

public class VirtualKeyboardDialog extends Dialog {
	
	/**
	 * The index of the item which have the focus in VirtualKeyboardDialog.charactersList.
	 * This attribute is static in order to keep it alive after closing/opening the dialog.
	 */
	private static int charactersListIndex = 0;
	
	final private static String[] CHARACTER_LISTS_NAME = {"greek", "maths", "pinyin"};
	
	final private static String[] CHARACTERS_LISTS = {
		"ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρςστυφχψω",
		"ℕℤⅅℚℝℂ℘ℬ∀∃∄∅∈∉∋∌∓√∛∜∝∞∂∇∆∧∨∩∪∼≃≄≈≉≠≡≢≤≥≪≫≮≯≰≱≺≻⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊕⋀⋁⋂⋃₀₁₂₃₄₅₆₇₈₉₊₋₌₍₎⁰¹²³⁴⁵⁶⁸⁹⁺⁻⁼⁽⁾∁½¼⅓⅔⅕⅖⅗⅘⅙⅚⅛⅜⅝⅞→↦⇎⇏⇒⇔ⅈⅉ",
		"āáăàēéĕèīíĭìōóŏòūúŭù"
		};
	
	/**
	 * PreferencesDialog constructor
	 * 
	 * @param parent the parent
	 */
	public VirtualKeyboardDialog(Shell parent) {
		// Pass the default styles here
		this(parent, SWT.SHELL_TRIM);
	}
	
	/**
	 * PreferencesDialog constructor
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public VirtualKeyboardDialog(Shell parent, int style) {
		// Let users override the default styles
		super(parent, style);
		this.setText("Virtual Keyboard");
	}

	/**
	 * Opens the dialog and returns nothing
	 * 
	 * @return null null
	 */
	public String open() {
		// Create the dialog window
		Shell shell = new Shell(this.getParent(), this.getStyle());
		shell.setText(this.getText());
		shell.setMinimumSize(640, 480);
		this.createContents(shell);
		shell.pack();
		shell.open();
		
		Display display = this.getParent().getDisplay();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return null;
	}
	
	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell the dialog window
	 */
	private void createContents(final Shell shell) {
		
		///////////////////////////
		// Shell //////////////////
		///////////////////////////
		
		shell.setLayout(new GridLayout(1, true));

		//////////////////////////
		// SasheForm /////////////
		//////////////////////////

        SashForm horizontalSashForm = new SashForm(shell, SWT.HORIZONTAL);
		horizontalSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		///////////////////////////
		// Character Set //////////
		///////////////////////////

		final List charactersList = new List(horizontalSashForm, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		charactersList.setItems(CHARACTER_LISTS_NAME);
		
		///////////////////////////
		// Keyboard ///////////////
		///////////////////////////
		
		final Composite keyboardComposite = new Composite(horizontalSashForm, SWT.NONE | SWT.V_SCROLL);
		
		final GridLayout keyboardLayout = new GridLayout(10, true);
		keyboardComposite.setLayout(keyboardLayout);
		
		// Fill the keyboard
//		charactersList.setSelection(charactersListIndex);
		charactersList.select(charactersListIndex);
		fillComposite(keyboardComposite);
		
		///////////////////////////
		///////////////////////////
		///////////////////////////
		
		// Create a horizontal separator
		Label separator = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		///////////////////////////
		// ButtonComposite ////////
		///////////////////////////
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		buttonComposite.setLayout(new GridLayout(1, true));
		
		// CloseButton ////////////////
		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		cancelButton.setEnabled(true);
		cancelButton.setText("Close");
		cancelButton.setToolTipText("Close the virtual keyboard");
		cancelButton.setImage(SharedImages.getImage(SharedImages.WINDOW_CLOSE_22));
        
        // Configure horizontalSasheForm
        horizontalSashForm.setWeights(new int[] {25, 75});
		
		///////////////////////////
		// Listeners //////////////
		///////////////////////////
        
        // TopicsList /////////////////
        charactersList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				charactersListIndex = charactersList.getSelectionIndex();
				
				// Fill the keyboard
				fillComposite(keyboardComposite);
				
				// Update the layout
				keyboardComposite.layout();
			}
		});
        
		// CloseButton ////////////////
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});
	}

	/**
	 * Fill keyboard the composite (create all buttons).
	 * 
	 * @param keyboardComposite
	 */
	private void fillComposite(Composite keyboardComposite) {
		
		// Remove all former controls
		for(Control control : keyboardComposite.getChildren()) {
			control.dispose();
		}

//		GridData buttonGridData = new GridData(GridData.FILL_BOTH);
		GridData buttonGridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		
		// TODO: test, try, exceptions, ...
		String characters = CHARACTERS_LISTS[charactersListIndex];
		
		for(int index=0 ; index < characters.length(); index++) {
			final Button btn = new Button(keyboardComposite, SWT.PUSH);
			btn.setText(Character.toString(characters.charAt(index)));
			btn.setLayoutData(buttonGridData);
			
			btn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					System.out.println(btn.getText());
	//				Control focusControl = this.getDisplay().getFocusControl();
	//				if(focusControl instanceof Text) {
	//					System.out.println("Yes!");
	//				} else {
	//					System.out.println("No!");
	//				}
				}
			});
		}
		
		// Equalize buttons size (buttons size may change in others languages...)
		int max_size = 0;
		
		for(Control control : keyboardComposite.getChildren()) {
			Point point = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
			max_size = Math.max(max_size, point.x);
			max_size = Math.max(max_size, point.y);
		}
		
		for(Control control : keyboardComposite.getChildren()) {
			((GridData) control.getLayoutData()).widthHint = max_size;
			((GridData) control.getLayoutData()).heightHint = max_size;
		}
	}
}
