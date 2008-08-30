package org.jdhp.opencal.gui.images;

import org.eclipse.swt.graphics.Image;
import org.jdhp.opencal.gui.MainWindow;

public class SharedImages {

	// 22x22 images
	public static final String EXIT = "x22/exit.png";
	public static final String PRINT = "x22/print.png";
	
	// 24x24 images
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
