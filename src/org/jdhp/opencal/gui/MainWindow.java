/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.gui;

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
		
		this.shell.setText(OpenCAL.PROGRAM_NAME + " " + OpenCAL.PROGRAM_VERSION);
		this.shell.setMinimumSize(400, 350);
		this.shell.setSize (640, 480);
		
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
        fileItem.setText("File");
        
        MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
        helpItem.setText("About");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileItem.setMenu(fileMenu);
        
        Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
        helpItem.setMenu(aboutMenu);
        
        MenuItem quitItem = new MenuItem(fileMenu, SWT.PUSH);
        quitItem.setImage(SharedImages.getImage(SharedImages.EXIT));
        quitItem.setText("Quit");
        
        quitItem.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		OpenCAL.exit(0);
        	}
        });
        
        MenuItem aboutItem = new MenuItem(aboutMenu, SWT.PUSH);
        aboutItem.setText("About OpenCAL");
        
        aboutItem.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		MessageBox mb = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION | SWT.OK);
        		mb.setText("About OpenCAL");
        		mb.setMessage(OpenCAL.PROGRAM_NAME + " " + OpenCAL.PROGRAM_VERSION + "\nCopyright (c) 2007,2008 Jérémie DECOCK");
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
		
		tabItemMake.setText("Make");
		tabItemReview.setText("Review");
		tabItemExplore.setText("Explore");
		tabItemStat.setText("Statistics");
		
		tabItemMake.setToolTipText("Make Cards");
		tabItemReview.setToolTipText("Review Cards");
		tabItemExplore.setToolTipText("Explore Knowledge Base");
		tabItemStat.setToolTipText("Look Use Statistics");
		
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
	 * 
	 */
	public void run() {
		// init statubar
		makeTab.update();
		
		// Main loop
		this.shell.open();
		
		while(!this.shell.isDisposed()) {
			if(!MainWindow.DISPLAY.readAndDispatch()) MainWindow.DISPLAY.sleep();
		}
		
		MainWindow.DISPLAY.dispose();
	}
	
}
