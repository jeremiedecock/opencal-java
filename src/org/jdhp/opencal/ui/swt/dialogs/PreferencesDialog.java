/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.model.professor.ProfessorFactory;
import org.jdhp.opencal.ui.swt.images.SharedImages;

public class PreferencesDialog extends Dialog {
    
    /**
     * PreferencesDialog constructor
     * 
     * @param parent the parent
     */
    public PreferencesDialog(Shell parent) {
        // Pass the default styles here
        this(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE);
    }
    
    /**
     * PreferencesDialog constructor
     * 
     * @param parent the parent
     * @param style the style
     */
    public PreferencesDialog(Shell parent, int style) {
        // Let users override the default styles
        super(parent, style);
        this.setText("Preferences...");
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
        // Topics List ////////////
        ///////////////////////////

        final List topicsList = new List(horizontalSashForm, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        
        ///////////////////////////
        // Stack //////////////////
        ///////////////////////////
        
        final Composite stackComposite = new Composite(horizontalSashForm, SWT.NONE);
        
        final StackLayout stackLayout = new StackLayout();
        stackComposite.setLayout(stackLayout);
        
        ///////////////////////////////
        // Topics and Composites //////
        ///////////////////////////////
        
        final Composite userPropertiesComposite = new Composite(stackComposite, SWT.NONE);
        final Composite cardContentPropertiesComposite = new Composite(stackComposite, SWT.NONE);
        final Composite professorPropertiesComposite = new Composite(stackComposite, SWT.NONE);
        final Composite externalToolsPropertiesComposite = new Composite(stackComposite, SWT.NONE);
        
        final String[] items = {"User",
                                "Card content",
                                "Professor",
                                "External Tools"};
        final Composite composites[] = {userPropertiesComposite,
                                        cardContentPropertiesComposite,
                                        professorPropertiesComposite,
                                        externalToolsPropertiesComposite};
        
        topicsList.setItems(items);
        
        /////////////////////////////////////////
        // UserPropertiesComposite //////////////
        /////////////////////////////////////////
        
        userPropertiesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        userPropertiesComposite.setLayout(new GridLayout(1, false));

        Group userInformationsGroup = new Group(userPropertiesComposite, SWT.NONE);
        userInformationsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        userInformationsGroup.setLayout(new GridLayout(2, false));
        userInformationsGroup.setText("User informations");
        
        // Name //////////////////
        Label userNameLabel = new Label(userInformationsGroup, SWT.NONE);
        userNameLabel.setText("User name");
        
        final Text userNameText = new Text(userInformationsGroup, SWT.BORDER);
        userNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        /////////////////////////////////////////
        // CardsPropertiesComposite /////////////
        /////////////////////////////////////////
        
        cardContentPropertiesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        cardContentPropertiesComposite.setLayout(new GridLayout(1, false));
        
        Group cardCreationGroup = new Group(cardContentPropertiesComposite, SWT.NONE);
        cardCreationGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        cardCreationGroup.setLayout(new GridLayout(2, false));
        cardCreationGroup.setText("Card content creation");
        
        // Author ////////////////
        Label authorLabel = new Label(cardCreationGroup, SWT.NONE);
        authorLabel.setText("Default author");
        
        final Text authorText = new Text(cardCreationGroup, SWT.BORDER);
        authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        authorText.setText(ApplicationProperties.getDefaultAuthor());
        
        // Licence ////////////////
        Label licenceLabel = new Label(cardCreationGroup, SWT.NONE);
        licenceLabel.setText("Default license");
        
        final Text licenceText = new Text(cardCreationGroup, SWT.BORDER);
        licenceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        licenceText.setText(ApplicationProperties.getDefaultLicense());

        /////////////////////////////////////////
        // UserPropertiesComposite //////////////
        /////////////////////////////////////////
        
        professorPropertiesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        professorPropertiesComposite.setLayout(new GridLayout(2, false));

        Label professorNameLabel = new Label(professorPropertiesComposite, SWT.NONE);
        professorNameLabel.setText("Professor");
        
        final Combo professorNameCombo = new Combo(professorPropertiesComposite, SWT.BORDER | SWT.READ_ONLY);
        professorNameCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        professorNameCombo.setItems(ProfessorFactory.AVAILABLE_PROFESSORS_NAME.toArray(new String[0]));
        professorNameCombo.setText(OpenCAL.professor.getName());
        
        /////////////////////////////////////////
        // ExternalToolsPropertiesComposite /////
        /////////////////////////////////////////
        
        externalToolsPropertiesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        externalToolsPropertiesComposite.setLayout(new GridLayout(1, false));

        Group pathsGroup = new Group(externalToolsPropertiesComposite, SWT.NONE);
        pathsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        pathsGroup.setLayout(new GridLayout(2, false));
        pathsGroup.setText("Paths");
        
        // LaTeX /////////////////
        Label latexPathLabel = new Label(pathsGroup, SWT.NONE);
        latexPathLabel.setText("LaTeX");
        
        final Text latexPathText = new Text(pathsGroup, SWT.BORDER);
        latexPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Gnuplot ///////////////
        Label gnuplotPathLabel = new Label(pathsGroup, SWT.NONE);
        gnuplotPathLabel.setText("Gnuplot");
        
        final Text gnuplotPathText = new Text(pathsGroup, SWT.BORDER);
        gnuplotPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Dot ///////////////////
        Label dotPathLabel = new Label(pathsGroup, SWT.NONE);
        dotPathLabel.setText("Dot");
        
        final Text dotPathText = new Text(pathsGroup, SWT.BORDER);
        dotPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ///////////////////////////
        ///////////////////////////
        ///////////////////////////
        
        // Create a horizontal separator
        Label separator2 = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
        separator2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        ///////////////////////////
        // ButtonComposite ////////
        ///////////////////////////
        
        Composite buttonComposite = new Composite(shell, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        buttonComposite.setLayout(new GridLayout(2, true));
        
        // OkButton ///////////////////
        final Button okButton = new Button(buttonComposite, SWT.PUSH);
        okButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true));
        okButton.setEnabled(true);
        okButton.setText("Ok");
        okButton.setToolTipText("Save changes and close");
        
        // CancelButton ///////////////
        final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
        cancelButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
        cancelButton.setEnabled(true);
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Close without saving changes");
        cancelButton.setImage(SharedImages.getImage(SharedImages.WINDOW_CLOSE_22));

        // Equalize buttons size (buttons size may change in others languages...) //
        Point okButtonPoint = okButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        Point cancelButtonPoint = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

        if(okButtonPoint.x > cancelButtonPoint.x) {
            ((GridData) cancelButton.getLayoutData()).widthHint = okButtonPoint.x;
        } else {
            ((GridData) okButton.getLayoutData()).widthHint = cancelButtonPoint.x;
        }
        
        if(okButtonPoint.y > cancelButtonPoint.y) {
            ((GridData) cancelButton.getLayoutData()).heightHint = okButtonPoint.y;
        } else {
            ((GridData) okButton.getLayoutData()).heightHint = cancelButtonPoint.y;
        }
        
        // Set the Close button as the default
        cancelButton.setFocus();
        
        // Configure horizontalSasheForm
        horizontalSashForm.setWeights(new int[] {25, 75});
        
        ///////////////////////////
        // Listeners //////////////
        ///////////////////////////
        
        // TopicsList /////////////////
        topicsList.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                int selectionIndex = topicsList.getSelectionIndex();
                stackLayout.topControl = composites[selectionIndex];
                stackComposite.layout();
            }
        });

        // OkButton ///////////////////
        okButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                ApplicationProperties.setDefaultAuthor(authorText.getText());
                ApplicationProperties.setDefaultLicense(licenceText.getText());
                ApplicationProperties.setProfessorName(professorNameCombo.getText());
                // TODO: change OpenCAL.professor
                // TODO: update cardList assess
                
                // Innutile, saveApplicationProperties() est appellé automatiquement à l'arret du programme (cf. classe OpenCAL)
                // ApplicationProperties.saveApplicationProperties();
                
                shell.close();
            }
        });
        
        // CancelButton ///////////////
        cancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                shell.close();
            }
        });
        
    }

}
