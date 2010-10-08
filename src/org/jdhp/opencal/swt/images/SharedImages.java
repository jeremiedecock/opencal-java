package org.jdhp.opencal.swt.images;

import org.eclipse.swt.graphics.Image;
import org.jdhp.opencal.swt.MainWindow;

public class SharedImages {

	// 12x12 images
//	public static final String MINIMALIZE = "x12/minimalize.png";
//	public static final String BROWSER_VIEW = "x12/switch.png";
	
	// 16x16 images
	public static final String DOCUMENT_NEW_16 = "x16/document-new.png";
	public static final String DOCUMENT_OPEN_16 = "x16/document-open.png";
	public static final String FILE_PDF_16 = "x16/text.png";
	public static final String DOCUMENT_PRINT_16 = "x16/document-print.png";
	public static final String EDIT_COPY_16 = "x16/edit-copy.png";
	public static final String EDIT_CUT_16 = "x16/edit-cut.png";
	public static final String EDIT_PASTE_16 = "x16/edit-paste.png";
	public static final String EDIT_REDO_16 = "x16/edit-redo.png";
	public static final String EDIT_SELECT_ALL_16 = "x16/edit-select-all.png";
	public static final String EDIT_UNDO_16 = "x16/edit-undo.png";
	public static final String HELP_BROWSER_16 = "x16/help-browser.png";
	public static final String INSERT_IMAGE_16 = "x16/insert-image.png";
	public static final String INTERNET_WEB_BROWSER_16 = "x16/internet-web-browser.png";
	public static final String PREFERENCES_DESKTOP_16 = "x16/preferences-desktop.png";
	public static final String PREFERENCES_SYSTEM_16 = "x16/preferences-system.png";
	public static final String EXIT_16 = "x16/system-log-out.png";
	public static final String MINIMALIZE_16 = "x16/minimalize.png";
	public static final String MAXIMIZE_16 = "x16/maximize.png";
	public static final String RESTORE_16 = "x16/restore.png";
	public static final String BROWSER_VIEW_16 = "x16/browser_view.png";
	public static final String EDIT_VIEW_16 = "x16/edit_view.png";
	
	
	// 22x22 images
	public static final String WINDOW_CLOSE_22 = "x22/window-close.png";
	public static final String DOCUMENT_OPEN_22 = "x22/document-open.png";
	
	
	// 24x24 images
	public static final String EDIT_CLEAR_24 = "x24/edit-clear.png";
	public static final String EDIT_FIND_24 = "x24/edit-find.png";
	public static final String FACE_SAD_24 = "x24/face-sad.png";
	public static final String FACE_SMILE_24 = "x24/face-smile.png";
	public static final String GO_FIRST_24 = "x24/go-first.png";
	public static final String GO_LAST_24 = "x24/go-last.png";
	public static final String GO_NEXT_24 = "x24/go-next.png";
	public static final String GO_PREVIOUS_24 = "x24/go-previous.png";
	public static final String LIST_ADD_24 = "x24/list-add.png";
	public static final String LIST_REMOVE_24 = "x24/list-remove.png";
	public static final String MEDIA_FLOPPY_24 = "x24/media-floppy.png";

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Image getImage(String name) {
		return new Image(MainWindow.DISPLAY, SharedImages.class.getResourceAsStream(name));
	}
	
}
