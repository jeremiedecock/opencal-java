package org.jdhp.opencal.gui.widgets;

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;

import org.jdhp.opencal.gui.MainWindow;
import org.jdhp.opencal.gui.images.SharedImages;
import org.jdhp.opencal.toolkit.DataToolKit;
import org.jdhp.opencal.UserProperties;

/**
 * Instances of this class implement a Composite that positions and sizes
 * children and allows programmatic control of layout and border parameters.
 * ViewForm is used in the workbench to lay out a view's label/menu/toolbar
 * local bar.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to set a layout on it.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, FLAT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(None)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */

public class EditableBrowser extends Composite {

	/**
	 * marginWidth specifies the number of pixels of horizontal margin that will
	 * be placed along the left and right edges of the form.
	 * 
	 * The default value is 0.
	 */
	public int marginWidth = 0;
	
	/**
	 * marginHeight specifies the number of pixels of vertical margin that will
	 * be placed along the top and bottom edges of the form.
	 * 
	 * The default value is 0.
	 */
	public int marginHeight = 0;
	
	/**
	 * horizontalSpacing specifies the number of pixels between the right edge
	 * of one cell and the left edge of its neighbouring cell to the right.
	 * 
	 * The default value is 1.
	 */
	public int horizontalSpacing = 1;
	
	/**
	 * verticalSpacing specifies the number of pixels between the bottom edge of
	 * one cell and the top edge of its neighbouring cell underneath.
	 * 
	 * The default value is 1.
	 */
	public int verticalSpacing = 1;

	// SWT widgets
	Control topLeft;
	Control topCenter;
	Control topRight;
	Control content;

	// Configuration and state info
	boolean separateTopCenter = false;
	boolean showBorder = false;

	int separator = -1;
	int borderTop = 0;
	int borderBottom = 0;
	int borderLeft = 0;
	int borderRight = 0;
	int highlight = 0;
	Point oldSize;

	Color selectionBackground;

	static final int OFFSCREEN = -200;
	static final int BORDER1_COLOR = SWT.COLOR_WIDGET_NORMAL_SHADOW;
	static final int SELECTION_BACKGROUND = SWT.COLOR_LIST_BACKGROUND;
	
	/* *** BEGIN *** */
	public Label label;
	public final Text editableText;
	public final FileDialog openPictureFileDialog;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or
	 * must be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 * @see SWT#BORDER
	 * @see SWT#FLAT
	 * @see #getStyle()
	 */
	public EditableBrowser(Composite parent) {
		super(parent, SWT.BORDER);
		super.setLayout(new EditableBrowserLayout());

		// setBorderVisible()
		borderLeft = borderTop = borderRight = borderBottom = 1; // SWT.BORDER (sinon borderBottom = borderTop = borderLeft = borderRight = 0;)
		highlight = 2; // SWT.FLAT (sinon highlight = 0;)

		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Dispose:
					onDispose();
					break;
				case SWT.Paint:
					onPaint(e.gc);
					break;
				case SWT.Resize:
					onResize();
					break;
				}
			}
		};

		int[] events = new int[] { SWT.Dispose, SWT.Paint, SWT.Resize };

		for (int i = 0; i < events.length; i++) {
			addListener(events[i], listener);
		}
		
		/* *** BEGIN *** */
		Font monoFont = new Font(Display.getCurrent(), "mono", 10, SWT.NORMAL);
		
		this.label = new Label(this, SWT.NONE);
		this.setTopLeft(this.label);
		
		final Composite displayArea = new Composite(this, SWT.NONE);
		final StackLayout stackLayout = new StackLayout();
		displayArea.setLayout(stackLayout);
		this.setContent(displayArea);
		
		editableText = new Text(displayArea, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		editableText.setFont(monoFont);
		editableText.setTabs(3);
		
		final Browser browser = new Browser(displayArea, SWT.BORDER);
		browser.setText(htmlOut(editableText.getText()));
		
		stackLayout.topControl = editableText;
		
		// FileDialog
		openPictureFileDialog = new FileDialog(editableText.getShell(), SWT.OPEN);
		
		String userHome = System.getProperty("user.home");
		openPictureFileDialog.setFilterPath(userHome);
		
		/*
		// TODO : cf. dialog de Gimp pour des exemples
		fileDialog.setFilterNames(new String[] {
				"PNG Image (*.png)",
				"JPEG Image (*.jpg)",
				"JPEG Image (*.jpeg)",
				"All Files (*.*)"
		});
		fileDialog.setFilterNames(new String[] {
				"*.png", "*.jpg", "*.jpeg", "*.*"
		});
		*/
		
		/////////////////
		final ToolBar tbMenu = new ToolBar(this, SWT.FLAT);
		
		final ToolItem switchDisplayItem = new ToolItem(tbMenu, SWT.PUSH);
		final ToolItem insertPictureItem = new ToolItem(tbMenu, SWT.PUSH);
		
		switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW));
		switchDisplayItem.setToolTipText("Switch display mode");
		switchDisplayItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(stackLayout.topControl == editableText) {
					stackLayout.topControl = browser;
					browser.setText(htmlOut(editableText.getText()));
					switchDisplayItem.setImage(SharedImages.getImage(SharedImages.EDIT_VIEW));
					switchDisplayItem.setToolTipText("Switch to edit view");
					insertPictureItem.setEnabled(false);
				} else {
					stackLayout.topControl = editableText;
					switchDisplayItem.setImage(SharedImages.getImage(SharedImages.BROWSER_VIEW));
					switchDisplayItem.setToolTipText("Switch to browser view");
//					editableText.setFocus(); // TODO : Marche pas ???
					insertPictureItem.setEnabled(true);
				}
				editableText.getParent().layout();
			}
		});
		
		insertPictureItem.setImage(SharedImages.getImage(SharedImages.INSERT_IMAGE));
		insertPictureItem.setToolTipText("Insert picture");
		insertPictureItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(!new File(openPictureFileDialog.getFilterPath()).exists()) {
					// openPictureFileDialog.getFilterPath() don't exist"
					//System.out.println(openPictureFileDialog.getFilterPath() + " don't exist");
					String userHome = System.getProperty("user.home");	// TODO : et si le repertoire user.home n'existe pas non plus ? (est-ce que ça peut arriver ?)
					openPictureFileDialog.setFilterPath(userHome);
				}
				
				String filePath = openPictureFileDialog.open();
				
				if(filePath != null) {
					try {
						// Compute MD5SUM ///////
						MessageDigest md5  = MessageDigest.getInstance("MD5");
						
						FileInputStream     fis = new FileInputStream(filePath);
				        BufferedInputStream bis = new BufferedInputStream(fis);
				        DigestInputStream   dis = new DigestInputStream(bis, md5);
				        
				        while (dis.read() != -1);			// Reads the file, and updates the message digest
				        byte[] digest    = md5.digest();	// Completes the digest computation
				        String hexDigest = DataToolKit.byteArray2Hex(digest);
				        
				        dis.close();	// Add fis.close() and bis.close() ? No, "dis.close()" is enough to close the stream (checked with "lsof" Unix command).
						
						// Copy file ////////////
				        //File src = new File(filePath);
						
						// Insert element ///////
				        String tag = "<img file=\"" + hexDigest + "\" />";	// TODO : source, auteur, licence
						editableText.insert(tag);
						
					} catch(NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		if(stackLayout.topControl == editableText) {
			insertPictureItem.setEnabled(true);
		} else {
			insertPictureItem.setEnabled(false);
		}
		
		this.setTopCenter(tbMenu);
		
		/////////////
		final ToolBar tbRight = new ToolBar(this, SWT.FLAT);
		
		final ToolItem minimizeItem = new ToolItem(tbRight, SWT.PUSH);
		minimizeItem.setImage(SharedImages.getImage(SharedImages.MINIMALIZE));
		minimizeItem.setToolTipText("Minimize");
		
		final ToolItem maximizeItem = new ToolItem(tbRight, SWT.PUSH);
		maximizeItem.setImage(SharedImages.getImage(SharedImages.MAXIMIZE));
		maximizeItem.setToolTipText("Maximize");
		this.setTopRight(tbRight);
	}

	public Rectangle computeTrim(int x, int y, int width, int height) {
		checkWidget();
		int trimX = x - borderLeft - highlight;
		int trimY = y - borderTop - highlight;
		int trimWidth = width + borderLeft + borderRight + 2 * highlight;
		int trimHeight = height + borderTop + borderBottom + 2 * highlight;
		return new Rectangle(trimX, trimY, trimWidth, trimHeight);
	}

	public Rectangle getClientArea() {
		checkWidget();
		Rectangle clientArea = super.getClientArea();
		clientArea.x += borderLeft;
		clientArea.y += borderTop;
		clientArea.width -= borderLeft + borderRight;
		clientArea.height -= borderTop + borderBottom;
		return clientArea;
	}

	/**
	 * Returns the content area.
	 * 
	 * @return the control in the content area of the pane or null
	 */
	public Control getContent() {
		// checkWidget();
		return content;
	}

	/**
	 * Returns Control that appears in the top center of the pane. Typically
	 * this is a toolbar.
	 * 
	 * @return the control in the top center of the pane or null
	 */
	public Control getTopCenter() {
		// checkWidget();
		return topCenter;
	}

	/**
	 * Returns the Control that appears in the top left corner of the pane.
	 * Typically this is a label such as CLabel.
	 * 
	 * @return the control in the top left corner of the pane or null
	 */
	public Control getTopLeft() {
		// checkWidget();
		return topLeft;
	}

	/**
	 * Returns the control in the top right corner of the pane. Typically this
	 * is a Close button or a composite with a Menu and Close button.
	 * 
	 * @return the control in the top right corner of the pane or null
	 */
	public Control getTopRight() {
		// checkWidget();
		return topRight;
	}

	void onDispose() {
		topLeft = null;
		topCenter = null;
		topRight = null;
		content = null;
		oldSize = null;
		selectionBackground = null;
	}

	void onPaint(GC gc) {
//		Color gcForeground = gc.getForeground();
//		Point size = getSize();
//		Color border = getDisplay().getSystemColor(BORDER1_COLOR);
//		if (showBorder) {
//			gc.setForeground(border);
//			gc.drawRectangle(0, 0, size.x - 1, size.y - 1);
//			if (highlight > 0) {
//				int x1 = 1;
//				int y1 = 1;
//				int x2 = size.x - 1;
//				int y2 = size.y - 1;
//				int[] shape = new int[] { x1, y1, x2, y1, x2, y2, x1, y2, x1,
//						y1 + highlight, x1 + highlight, y1 + highlight,
//						x1 + highlight, y2 - highlight, x2 - highlight,
//						y2 - highlight, x2 - highlight, y1 + highlight, x1,
//						y1 + highlight };
//				Color highlightColor = getDisplay().getSystemColor(
//						SWT.COLOR_LIST_SELECTION);
//				gc.setBackground(highlightColor);
//				gc.fillPolygon(shape);
//			}
//		}
//		if (separator > -1) {
//			gc.setForeground(border);
//			gc.drawLine(borderLeft + highlight, separator, size.x - borderLeft
//					- borderRight - highlight, separator);
//		}
//		gc.setForeground(gcForeground);
	}

	void onResize() {
		Point size = getSize();
		if (oldSize == null || oldSize.x == 0 || oldSize.y == 0) {
			redraw();
		} else {
			int width = 0;
			if (oldSize.x < size.x) {
				width = size.x - oldSize.x + borderRight + highlight;
			} else if (oldSize.x > size.x) {
				width = borderRight + highlight;
			}
			redraw(size.x - width, 0, width, size.y, false);

			int height = 0;
			if (oldSize.y < size.y) {
				height = size.y - oldSize.y + borderBottom + highlight;
			}
			if (oldSize.y > size.y) {
				height = borderBottom + highlight;
			}
			redraw(0, size.y - height, size.x, height, false);
		}
		oldSize = size;
	}

	/**
	 * Sets the content. Setting the content to null will remove it from the
	 * pane - however, the creator of the content must dispose of the content.
	 * 
	 * @param content
	 *            the control to be displayed in the content area or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control is not a
	 *                child of this ViewForm</li>
	 *                </ul>
	 */
	public void setContent(Control content) {
		checkWidget();
		if (content != null && content.getParent() != this) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.content != null && !this.content.isDisposed()) {
			this.content.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
		}
		this.content = content;
		layout(false);
	}

	/**
	 * Sets the layout which is associated with the receiver to be the argument
	 * which may be null.
	 * <p>
	 * Note: No Layout can be set on this Control because it already manages the
	 * size and position of its children.
	 * </p>
	 * 
	 * @param layout
	 *            the receiver's new layout or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLayout(Layout layout) {
		checkWidget();
		return;
	}

	void setSelectionBackground(Color color) {
		checkWidget();
		if (selectionBackground == color)
			return;
		if (color == null)
			color = getDisplay().getSystemColor(SELECTION_BACKGROUND);
		selectionBackground = color;
		redraw();
	}

	/**
	 * Set the control that appears in the top center of the pane. Typically
	 * this is a toolbar. The topCenter is optional. Setting the topCenter to
	 * null will remove it from the pane - however, the creator of the topCenter
	 * must dispose of the topCenter.
	 * 
	 * @param topCenter
	 *            the control to be displayed in the top center or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control is not a
	 *                child of this ViewForm</li>
	 *                </ul>
	 */
	public void setTopCenter(Control topCenter) {
		checkWidget();
		if (topCenter != null && topCenter.getParent() != this) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.topCenter != null && !this.topCenter.isDisposed()) {
			Point size = this.topCenter.getSize();
			this.topCenter.setLocation(OFFSCREEN - size.x, OFFSCREEN - size.y);
		}
		this.topCenter = topCenter;
		layout(false);
	}

	/**
	 * Set the control that appears in the top left corner of the pane.
	 * Typically this is a label such as CLabel. The topLeft is optional.
	 * Setting the top left control to null will remove it from the pane -
	 * however, the creator of the control must dispose of the control.
	 * 
	 * @param c
	 *            the control to be displayed in the top left corner or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control is not a
	 *                child of this ViewForm</li>
	 *                </ul>
	 */
	public void setTopLeft(Control c) {
		checkWidget();
		if (c != null && c.getParent() != this) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.topLeft != null && !this.topLeft.isDisposed()) {
			Point size = this.topLeft.getSize();
			this.topLeft.setLocation(OFFSCREEN - size.x, OFFSCREEN - size.y);
		}
		this.topLeft = c;
		layout(false);
	}

	/**
	 * Set the control that appears in the top right corner of the pane.
	 * Typically this is a Close button or a composite with a Menu and Close
	 * button. The topRight is optional. Setting the top right control to null
	 * will remove it from the pane - however, the creator of the control must
	 * dispose of the control.
	 * 
	 * @param c
	 *            the control to be displayed in the top right corner or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control is not a
	 *                child of this ViewForm</li>
	 *                </ul>
	 */
	public void setTopRight(Control c) {
		checkWidget();
		if (c != null && c.getParent() != this) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.topRight != null && !this.topRight.isDisposed()) {
			Point size = this.topRight.getSize();
			this.topRight.setLocation(OFFSCREEN - size.x, OFFSCREEN - size.y);
		}
		this.topRight = c;
		layout(false);
	}

	/**
	 * If true, the topCenter will always appear on a separate line by itself,
	 * otherwise the topCenter will appear in the top row if there is room and
	 * will be moved to the second row if required.
	 * 
	 * @param show
	 *            true if the topCenter will always appear on a separate line by
	 *            itself
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTopCenterSeparate(boolean show) {
		checkWidget();
		separateTopCenter = show;
		layout(false);
	}
	
	final private String htmlOut(String src) {
		StringBuffer html = new StringBuffer();
		
		html.append("<html><head><style type=\"text/css\" media=\"all\">");
		html.append(MainWindow.EDITABLE_BROWSER_CSS);
		html.append("</style><head><body>");
		
		html.append(filter(src));
		
		html.append("</body></html>");
		
		return html.toString();
	}
	
	final private String filter(String text) {
		// Empèche l'interprétation d'eventuelles fausses balises comprises dans les cartes 
		String html = text.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		
		// Rétabli l'interprétation pour les balises images
		String pattern = "&lt;img file=\"([0-9abcdef]{32}.(png|jpg|jpeg))\" /&gt;";
		Pattern regPat = Pattern.compile(pattern);
		Matcher matcher = regPat.matcher(html);
		html = matcher.replaceAll("<img src=\"" + UserProperties.getImgPath() + "$1\" />");
		
		return html;
	}
}
