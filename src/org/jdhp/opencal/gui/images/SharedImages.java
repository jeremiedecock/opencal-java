package org.jdhp.opencal.gui.images;

import org.eclipse.swt.graphics.Image;
import org.jdhp.opencal.gui.MainWindow;

public class SharedImages {

	public static final String EDIT_FIND = "edit-find.png";
	public static final String FACE_SAD = "face-sad.png";
	public static final String FACE_SMILE = "face-smile.png";
	public static final String GO_FIRST = "go-first.png";
	public static final String GO_LAST = "go-last.png";
	public static final String GO_NEXT = "go-next.png";
	public static final String GO_PREVIOUS = "go-previous.png";
	public static final String LIST_ADD = "list-add.png";
	public static final String LIST_REMOVE = "list-remove.png";
	public static final String MEDIA_FLOPPY = "media-floppy.png";
	
	public static Image getImage(String name) {
		return new Image(MainWindow.DISPLAY, SharedImages.class.getResourceAsStream(name));
	}
	
}
