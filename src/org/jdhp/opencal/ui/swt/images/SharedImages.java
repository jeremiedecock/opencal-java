/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.images;

import org.eclipse.swt.graphics.Image;
import org.jdhp.opencal.ui.swt.MainWindow;

public class SharedImages {

    // 12x12 images
//  public static final String MINIMALIZE_12 = "x12/minimalize.png";
//  public static final String SWITCH_12 = "x12/switch.png";
    
    // 16x16 images
    public static final String APPLICATIONS_OFFICE_16 = "x16/applications-office.png";
    public static final String AUDIO_X_GENERIC_16 = "x16/audio-x-generic.png";
    public static final String BROWSER_VIEW_16 = "x16/browser_view.png";
    public static final String DIALOG_INFORMATION_16 = "x16/dialog-information.png";
    public static final String DOCUMENT_NEW_16 = "x16/document-new.png";
    public static final String DOCUMENT_OPEN_16 = "x16/document-open.png";
    public static final String DOCUMENT_PAGE_SETUP_16 = "x16/document-page-setup.png";
    public static final String DOCUMENT_PRINT_16 = "x16/document-print.png";
    public static final String DOCUMENT_PROPERTIES_16 = "x16/document-properties.png";
    public static final String DOCUMENT_PROPERTIES_DEBIAN_16 = "x16/document-properties_deb.png";
    public static final String DOT_16 = "x16/dot.png";
    public static final String EDIT_COPY_16 = "x16/edit-copy.png";
    public static final String EDIT_CUT_16 = "x16/edit-cut.png";
    public static final String EDIT_FIND_16 = "x16/edit-find.png";
    public static final String EDIT_PASTE_16 = "x16/edit-paste.png";
    public static final String EDIT_REDO_16 = "x16/edit-redo.png";
    public static final String EDIT_SELECT_ALL_16 = "x16/edit-select-all.png";
    public static final String EDIT_UNDO_16 = "x16/edit-undo.png";
    public static final String EDIT_VIEW_16 = "x16/edit_view.png";
    public static final String EMBLEM_DOCUMENTS_16 = "x16/emblem-documents.png";
    public static final String EMBLEM_PACKAGE_16 = "x16/emblem-package.png";
    public static final String EMBLEM_PHOTOS_16 = "x16/emblem-photos.png";
    public static final String HELP_BROWSER_16 = "x16/help-browser.png";
    public static final String IMAGE_X_GENERIC_16 = "x16/image-x-generic.png";
    public static final String INSERT_IMAGE_16 = "x16/insert-image.png";
    public static final String INSERT_OBJECT_16 = "x16/insert-object.png";
    public static final String INSERT_TEXT_16 = "x16/insert-text.png";
    public static final String INTERNET_WEB_BROWSER_16 = "x16/internet-web-browser.png";
    public static final String LATEX_16 = "x16/latex.png";
    public static final String LOGVIEWER_16 = "x16/logviewer.png";
    public static final String MAXIMIZE_16 = "x16/maximize.png";
    public static final String MINIMALIZE_16 = "x16/minimalize.png";
    public static final String PLOT_16 = "x16/plot.png";
    public static final String PREFERENCES_DESKTOP_16 = "x16/preferences-desktop.png";
    public static final String PREFERENCES_DESKTOP_FONT_16 = "x16/preferences-desktop-font.png";
    public static final String PREFERENCES_SYSTEM_16 = "x16/preferences-system.png";
    public static final String RESTORE_16 = "x16/restore.png";
    public static final String SYSTEM_FILE_MANAGER_16 = "x16/system-file-manager.png";
    public static final String SYSTEM_LOG_OUT_16 = "x16/system-log-out.png";
    public static final String SYSTEM_SEARCH_16 = "x16/system-search.png";
    public static final String SYSTEM_SEARCH_DEBIAN_16 = "x16/system-search_deb.png";
    public static final String TEX_16 = "x16/tex.png";
    public static final String TEXT_16 = "x16/text.png";
    public static final String TEXT_HTML_16 = "x16/text-html.png";
    public static final String TOOLS_CHECK_SPELLING_16 = "x16/tools-check-spelling.png";
    public static final String UTILITIES_TERMINAL_16 = "x16/utilities-terminal.png";
    public static final String VIDEO_X_GENERIC_16 = "x16/video-x-generic.png";
    public static final String X_OFFICE_DOCUMENT_16 = "x16/x-office-document.png";
    
    // 22x22 images
    public static final String DOCUMENT_OPEN_22 = "x22/document-open.png";
    public static final String EMBLEM_DOCUMENTS_22 = "x22/emblem-documents.png";
    public static final String WINDOW_CLOSE_22 = "x22/window-close.png";
    
    // 24x24 images
    public static final String EDIT_CLEAR_24 = "x24/edit-clear.png";
    public static final String EDIT_FIND_24 = "x24/edit-find.png";
    public static final String EMBLEM_DOCUMENTS_24 = "x24/emblem-documents.png";
    public static final String EMBLEM_NEW_24 = "x24/emblem-new.png";
    public static final String FACE_SAD_24 = "x24/face-sad.png";
    public static final String FACE_SMILE_24 = "x24/face-smile.png";
    public static final String GO_FIRST_24 = "x24/go-first.png";
    public static final String GO_LAST_24 = "x24/go-last.png";
    public static final String GO_NEXT_24 = "x24/go-next.png";
    public static final String GO_PREVIOUS_24 = "x24/go-previous.png";
    public static final String LIST_ADD_24 = "x24/list-add.png";
    public static final String LIST_REMOVE_24 = "x24/list-remove.png";
    public static final String MEDIA_FLOPPY_24 = "x24/media-floppy.png";
    
    // 32x32 images
    public static final String EMBLEM_DOCUMENTS_32 = "x32/emblem-documents.png";
    
    // 48x48 images
    public static final String EMBLEM_DOCUMENTS_48 = "x48/emblem-documents.png";

    /**
     * 
     * @param name
     * @return
     */
    public static Image getImage(String name) {
        return new Image(MainWindow.DISPLAY, SharedImages.class.getResourceAsStream(name));
    }
    
}
