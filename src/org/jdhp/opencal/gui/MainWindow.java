/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.gui.images.SharedImages;
import org.jdhp.opencal.gui.tabs.ExplorerTab;
import org.jdhp.opencal.gui.tabs.MakerTab;
import org.jdhp.opencal.gui.tabs.ReviewerTab;
import org.jdhp.opencal.gui.tabs.StatsTab;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MainWindow {
	
	final public static Display DISPLAY = new Display();
	
	final public static String REVIEW_CSS = MainWindow.loadCSS("review.css");
	
	final public static String EDITABLE_BROWSER_CSS = MainWindow.loadCSS("editable_browser.css");
	
	final private Shell shell;
	
	final private Label statusLabel1;
	
	final private Label statusLabel2;
	
	final private Label statusLabel3;
	
	final private Label statusLabel4;
	
	final private TabItem tabItemMake;
	
	final private TabItem tabItemReview;
	
	final private TabItem tabItemExplore;
	
	final private TabItem tabItemStat;
	
	final private MakerTab makeTab;
	
	final private ReviewerTab reviewTab;
	
	final private ExplorerTab exploreTab;
	
	final private StatsTab statsTab;
	
	/**
	 * 
	 */
	public MainWindow() {
//		this.display = new Display();
		this.shell = new Shell(MainWindow.DISPLAY);
		this.shell.setLayout(new GridLayout(1, false));
		
		this.shell.setText(OpenCAL.PROGRAM_NAME + " " + OpenCAL.PROGRAM_VERSION + " - " + OpenCAL.getPkbFile().getAbsolutePath());
		this.shell.setMinimumSize(400, 350);
		this.shell.setSize(640, 480);
		
		// Center the main shell on the primary monitor
        Monitor primary = MainWindow.DISPLAY.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = this.shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        this.shell.setLocation(x, y);
		
        // Create the menubar
        Menu menu = new Menu(this.shell, SWT.BAR);
        this.shell.setMenuBar(menu);
        
        MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
        fileItem.setText("&File");
        
        MenuItem editItem = new MenuItem(menu, SWT.CASCADE);
        editItem.setText("&Edit");
        
        MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
        helpItem.setText("&Help");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileItem.setMenu(fileMenu);
        
        Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
        editItem.setMenu(editMenu);
        
        Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
        helpItem.setMenu(helpMenu);
        
        // File items //
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
		newItem.setImage(SharedImages.getImage(SharedImages.FILE_NEW));
		newItem.setText("New...");

		MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
		openItem.setImage(SharedImages.getImage(SharedImages.FILE_OPEN));
		openItem.setText("Open...");

		MenuItem closeItem = new MenuItem(fileMenu, SWT.PUSH);
		closeItem.setText("Close");

		closeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				OpenCAL.closePkbFile();
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem importItem = new MenuItem(fileMenu, SWT.PUSH);
		importItem.setText("Import Card Set...");
		importItem.setEnabled(false);

		MenuItem exportItem = new MenuItem(fileMenu, SWT.PUSH);
		exportItem.setText("Export Card Set...");
		exportItem.setEnabled(false);

		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem pdfItem = new MenuItem(fileMenu, SWT.PUSH);
		pdfItem.setImage(SharedImages.getImage(SharedImages.FILE_PDF));
		pdfItem.setText("Export Review File...");
		
		MenuItem printItem = new MenuItem(fileMenu, SWT.PUSH);
		printItem.setImage(SharedImages.getImage(SharedImages.FILE_PRINT));
		printItem.setText("Print Review File...");

		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem quitItem = new MenuItem(fileMenu, SWT.PUSH);
		quitItem.setImage(SharedImages.getImage(SharedImages.EXIT));
		quitItem.setText("Quit");

		quitItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				OpenCAL.exit(0);
			}
		});

		// Edit items //
		MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
		undoItem.setImage(SharedImages.getImage(SharedImages.EDIT_UNDO));
		undoItem.setText("Undo Typing");
		undoItem.setEnabled(false);

		MenuItem redoItem = new MenuItem(editMenu, SWT.PUSH);
		redoItem.setImage(SharedImages.getImage(SharedImages.EDIT_REDO));
		redoItem.setText("Redo");
		redoItem.setEnabled(false);

		new MenuItem(editMenu, SWT.SEPARATOR);

		MenuItem copyItem = new MenuItem(editMenu, SWT.PUSH);
		copyItem.setImage(SharedImages.getImage(SharedImages.EDIT_COPY));
		copyItem.setText("Copy");
		copyItem.setEnabled(false);

		MenuItem cutItem = new MenuItem(editMenu, SWT.PUSH);
		cutItem.setImage(SharedImages.getImage(SharedImages.EDIT_CUT));
		cutItem.setText("Cut");
		cutItem.setEnabled(false);

		MenuItem pastItem = new MenuItem(editMenu, SWT.PUSH);
		pastItem.setImage(SharedImages.getImage(SharedImages.EDIT_PASTE));
		pastItem.setText("Past");
		pastItem.setEnabled(false);

		new MenuItem(editMenu, SWT.SEPARATOR);

		MenuItem preferencesItem = new MenuItem(editMenu, SWT.PUSH);
		preferencesItem.setImage(SharedImages
				.getImage(SharedImages.PREFERENCES_SYSTEM));
		preferencesItem.setText("Preferences...");

		// Help items //
		MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutItem.setImage(SharedImages.getImage(SharedImages.HELP_BROWSER));
		aboutItem.setText("About OpenCAL...");

		aboutItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				MessageBox mb = new MessageBox(shell, SWT.APPLICATION_MODAL
						| SWT.ICON_INFORMATION | SWT.OK);
				mb.setText("About OpenCAL");
				mb.setMessage(OpenCAL.PROGRAM_NAME + " "
						+ OpenCAL.PROGRAM_VERSION
						+ "\nCopyright (c) 2007,2008 Jérémie DECOCK");
				mb.open();
			}
		});
        
        // Create the tabfolder
		final TabFolder tabFolder = new TabFolder(this.shell, SWT.NONE);
		
		GridData tabFolderGridData = new GridData(GridData.FILL_BOTH);
		tabFolder.setLayoutData(tabFolderGridData);
		
		this.tabItemMake = new TabItem(tabFolder, SWT.NONE);
		this.tabItemReview = new TabItem(tabFolder, SWT.NONE);
		this.tabItemExplore = new TabItem(tabFolder, SWT.NONE);
		this.tabItemStat = new TabItem(tabFolder, SWT.NONE);
		
		tabItemMake.setText("Add");
		tabItemReview.setText("Check");
		tabItemExplore.setText("Explore");
		tabItemStat.setText("Monitor");
		
		tabItemMake.setToolTipText("Add Knowledge");
		tabItemReview.setToolTipText("Check Knowledge");
		tabItemExplore.setToolTipText("Explore Knowledge Base");
		tabItemStat.setToolTipText("Statistics");
		
		Composite makeCardComposite = new Composite(tabFolder, SWT.NONE);
		Composite reviewerComposite = new Composite(tabFolder, SWT.NONE);
		Composite explorerComposite = new Composite(tabFolder, SWT.NONE);
		Composite statsComposite = new Composite(tabFolder, SWT.NONE);
		
		tabItemMake.setControl(makeCardComposite);
		tabItemReview.setControl(reviewerComposite);
		tabItemExplore.setControl(explorerComposite);
		tabItemStat.setControl(statsComposite);
		
		makeTab = new MakerTab(makeCardComposite);
		reviewTab = new ReviewerTab(reviewerComposite);
		exploreTab = new ExplorerTab(explorerComposite);
		statsTab = new StatsTab(statsComposite);
		
		// Add listeners on tabFolder (prevent when a tabItem is selected)
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				switch (tabFolder.getSelectionIndex()) {
					case 0 :
						makeTab.update();
						break;
					case 1 :
						reviewTab.update();
						break;
					case 2 :
						exploreTab.update();
						break;
					case 3 :
						statsTab.update();
						break;
				}
			}
		});
		
		// Create the Status Bar
		Composite statusBar = new Composite(this.shell, SWT.NONE);
		GridData statusBarGridData = new GridData(GridData.FILL_HORIZONTAL);
		statusBar.setLayoutData(statusBarGridData);
		GridLayout statusBarGridLayout = new GridLayout(9, true);
		statusBarGridLayout.marginWidth = 0;
		statusBarGridLayout.marginHeight = 0;
		statusBarGridLayout.horizontalSpacing = 5;
		statusBarGridLayout.verticalSpacing = 0;
		statusBarGridLayout.marginTop = 4;
		statusBarGridLayout.marginBottom = 0;
		statusBarGridLayout.marginLeft = 1;
		statusBarGridLayout.marginRight = 0;
		statusBar.setLayout(statusBarGridLayout);
		
		this.statusLabel1 = new Label(statusBar, SWT.LEFT);
		GridData statusLabel1GridData = new GridData(GridData.FILL_HORIZONTAL);
		statusLabel1GridData.horizontalSpan = 6;
		this.statusLabel1.setLayoutData(statusLabel1GridData);
		
		this.statusLabel2 = new Label(statusBar, SWT.CENTER);
		GridData statusLabel2GridData = new GridData(GridData.FILL_HORIZONTAL);
		this.statusLabel2.setLayoutData(statusLabel2GridData);
		
		this.statusLabel3 = new Label(statusBar, SWT.CENTER);
		GridData statusLabel3GridData = new GridData(GridData.FILL_HORIZONTAL);
		this.statusLabel3.setLayoutData(statusLabel3GridData);
		
		this.statusLabel4 = new Label(statusBar, SWT.CENTER);
		GridData statusLabel4GridData = new GridData(GridData.FILL_HORIZONTAL);
		this.statusLabel4.setLayoutData(statusLabel4GridData);
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel1(String text, String toolTipText) {
		this.statusLabel1.setText(text);
		this.statusLabel1.setToolTipText(toolTipText);
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel2(String text, String toolTipText) {
		this.statusLabel2.setText(text);
		this.statusLabel2.setToolTipText(toolTipText);
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel3(String text, String toolTipText) {
		this.statusLabel3.setText(text);
		this.statusLabel3.setToolTipText(toolTipText);
	}
	
	/**
	 * 
	 * @param text
	 * @param toolTipText
	 */
	public void setStatusLabel4(String text, String toolTipText) {
		this.statusLabel4.setText(text);
		this.statusLabel4.setToolTipText(toolTipText);
	}
	
	/**
	 * 
	 */
	public void print(String text) {
		MessageBox mb = new MessageBox(this.shell, SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION | SWT.OK);
		mb.setText("Info...");
		mb.setMessage(text);
		mb.open();
	}
	
	/**
	 * 
	 */
	public void printAlert(String text) {
		MessageBox mb = new MessageBox(this.shell, SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK);
		mb.setText("Alert...");
		mb.setMessage(text);
		mb.open();
	}
	
	/**
	 * 
	 */
	public void printError(String text) {
		MessageBox mb = new MessageBox(this.shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
		mb.setText("Error...");
		mb.setMessage(text);
		mb.open();
	}
	
	/**
	 * TODO : charger le fichier CSS depuis "css/source"
	 * 
	 * @param source
	 * @return
	 */
	private static String loadCSS(String source) {
		StringBuffer css = new StringBuffer();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(MainWindow.class.getResourceAsStream("css/" + source)));
			
			String line;
			do {
				line = reader.readLine();
				if(line != null) {
					css.append(line);
				}
			} while(line != null);
		} catch(FileNotFoundException e) {
			System.out.println(e);
		} catch(IOException e) {
			System.out.println(e);
		}
		
		return css.toString();
	}
	
	/**
	 * 
	 */
	public void run() {
		// init statubar
		makeTab.update();
		
		// Main loop
		this.shell.open();
//		this.shell.setMaximized(true);
		
		while(!this.shell.isDisposed()) {
			if(!MainWindow.DISPLAY.readAndDispatch()) MainWindow.DISPLAY.sleep();
		}
		
		MainWindow.DISPLAY.dispose();
	}
	
}
