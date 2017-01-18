/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * La classe de base de tous les dialog utilisés par EditableBrowser pour
 * insérer des objets dans les questions et les réponses des cartes.
 * 
 * @author jeremie
 */
public abstract class InsertDialog extends Dialog {

    /**
     * The tag to be inserted in card's question or answer
     */
    protected String tag;

    /**
     * Creates the dialog's contents
     * 
     * @param shell the dialog window
     */
    protected abstract void createContents(final Shell shell);

    public InsertDialog(Shell parent, int style) {
        super(parent, style);
    }

    /**
     * Opens the dialog and returns the image tag
     * 
     * @return tag the image tag
     */
    public final String open() {
        // Create the dialog window
        Shell shell = new Shell(this.getParent(), this.getStyle() | SWT.RESIZE);
        shell.setText(this.getText());
        shell.setMinimumSize(480, 520); // TODO
        //shell.setSize(480, 520);      // TODO
        this.createContents(shell);
        shell.pack();
        shell.open();

        Display display = this.getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // Return the image tag, or null
        return this.tag;
    }
}
