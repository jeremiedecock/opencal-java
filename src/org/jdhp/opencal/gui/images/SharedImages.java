package org.jdhp.opencal.gui.images;

import org.eclipse.swt.graphics.Image;
import org.jdhp.opencal.gui.MainWindow;

public class SharedImages {

	// 12x12 images
//	public static final String MINIMALIZE = "x12/minimalize.png";
//	public static final String BROWSER_VIEW = "x12/switch.png";
	
	// 16x16 images
	public static final String FILE_NEW = "x16/document-new.png";
	public static final String FILE_OPEN = "x16/document-open.png";
	public static final String FILE_PDF = "x16/text.png";
	public static final String FILE_PRINT = "x16/document-print.png";
	public static final String EDIT_COPY = "x16/edit-copy.png";
	public static final String EDIT_CUT = "x16/edit-cut.png";
	public static final String EDIT_PASTE = "x16/edit-paste.png";
	public static final String EDIT_REDO = "x16/edit-redo.png";
	public static final String EDIT_SELECT_ALL = "x16/edit-select-all.png";
	public static final String EDIT_UNDO = "x16/edit-undo.png";
	public static final String HELP_BROWSER = "x16/help-browser.png";
	public static final String INSERT_IMAGE = "x16/insert-image.png";
	public static final String INTERNET_WEB_BROWSER = "x16/internet-web-browser.png";
	public static final String PREFERENCES_DESKTOP = "x16/preferences-desktop.png";
	public static final String PREFERENCES_SYSTEM = "x16/preferences-system.png";
	public static final String EXIT = "x16/system-log-out.png";
	public static final String MINIMALIZE = "x16/minimalize.png";
	public static final String MAXIMIZE = "x16/maximize.png";
	public static final String RESTORE = "x16/restore.png";
	public static final String BROWSER_VIEW = "x16/browser_view.png";
	public static final String EDIT_VIEW = "x16/edit_view.png";
	
	
	// 24x24 images
	public static final String EDIT_CLEAR = "x24/edit-clear.png";
	public static final String EDIT_FIND = "x24/edit-find.png";
	public static final String FACE_SAD = "x24/face-sad.png";
	public static final String FACE_SMILE = "x24/face-smile.png";
	public static final String GO_FIRST = "x24/go-first.png";
	public static final String GO_LAST = "x24/go-last.png";
	public static final String GO_NEXT = "x24/go-next.png";
	public static final String GO_PREVIOUS = "x24/go-previous.png";
	public static final String LIST_ADD = "x24/list-add.png";
	public static final String LIST_REMOVE = "x24/list-remove.png";
	public static final String MEDIA_FLOPPY = "x24/media-floppy.png";

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Image getImage(String name) {
		return new Image(MainWindow.DISPLAY, SharedImages.class.getResourceAsStream(name));
	}
	
}
