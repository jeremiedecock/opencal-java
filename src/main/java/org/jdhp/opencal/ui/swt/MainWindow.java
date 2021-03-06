/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.ui.swt;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseException;
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseFactory;
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseFactoryException;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.data.properties.ApplicationPropertiesException;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.professor.ProfessorFactory;
import org.jdhp.opencal.model.professor.ProfessorFactoryException;
import org.jdhp.opencal.ui.swt.dialogs.AboutDialog;
import org.jdhp.opencal.ui.swt.dialogs.PreferencesDialog;
import org.jdhp.opencal.ui.swt.dialogs.VirtualKeyboardDialog;
import org.jdhp.opencal.ui.swt.images.SharedImages;
import org.jdhp.opencal.ui.swt.tabs.AddTab;
import org.jdhp.opencal.ui.swt.tabs.ExploreTab;
import org.jdhp.opencal.ui.swt.tabs.TestTab;
import org.jdhp.opencal.ui.swt.tabs.TrainTab;
import org.jdhp.opencal.ui.swt.tabs.monitor.MonitorTab;
import org.jdhp.opencal.util.CalendarToolKit;

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
    
    
    final private TabItem tabItemAddCard;

    final private TabItem tabItemTrain;
    
    final private TabItem tabItemTest;
    
    final private TabItem tabItemExplore;
    
    final private TabItem tabItemMonitor;
    
    
    final private AddTab addCardTab;
    
    final private TrainTab trainTab;
    
    final private TestTab testTab;
    
    final private ExploreTab exploreTab;
    
    final private MonitorTab monitorTab;
    
    private URI pkbURI;
    
    
    // Singleton pattern
    private static MainWindow uniqueInstance = null;
    
    /**
     * Singleton pattern
     * TODO : add sync...
     * 
     * @return
     */
    public static MainWindow getInstance() {
        if(MainWindow.uniqueInstance == null) {
            MainWindow.uniqueInstance = new MainWindow();
        }
        return MainWindow.uniqueInstance; 
    }
    
    /**
     * 
     */
    private MainWindow() {
        
        try {
            this.pkbURI = new URI(ApplicationProperties.getPkbPath());
        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        this.shell = new Shell(MainWindow.DISPLAY);
        this.shell.setLayout(new GridLayout(1, false));
        
        this.shell.setText(OpenCAL.PROGRAM_NAME + " " + OpenCAL.PROGRAM_VERSION + " - " + this.pkbURI.getPath());
        this.shell.setMinimumSize(400, 350);
        this.shell.setSize(640, 480);
        
        /*
         * Depending where the icon is displayed, the platform chooses the icon
         * with the "best" attributes. It is expected that the array will
         * contain the same icon rendered at different sizes, with different
         * depth and transparency attributes.
         */
        Image[] icons = {
                SharedImages.getImage(SharedImages.EMBLEM_DOCUMENTS_16),
                SharedImages.getImage(SharedImages.EMBLEM_DOCUMENTS_22),
                SharedImages.getImage(SharedImages.EMBLEM_DOCUMENTS_24),
                SharedImages.getImage(SharedImages.EMBLEM_DOCUMENTS_32),
                SharedImages.getImage(SharedImages.EMBLEM_DOCUMENTS_48)
                };
        this.shell.setImages(icons);
        
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
        
        MenuItem windowItem = new MenuItem(menu, SWT.CASCADE);
        windowItem.setText("&Window");
        
        MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
        helpItem.setText("&Help");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileItem.setMenu(fileMenu);
        
        Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
        editItem.setMenu(editMenu);
        
        Menu windowMenu = new Menu(shell, SWT.DROP_DOWN);
        windowItem.setMenu(windowMenu);
        
        Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
        helpItem.setMenu(helpMenu);
        
        ///////////////////////////////
        
        // File items /////////////////
        
        MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setImage(SharedImages.getImage(SharedImages.DOCUMENT_NEW_16));
        newItem.setText("New...");

        MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
        openItem.setImage(SharedImages.getImage(SharedImages.DOCUMENT_OPEN_16));
        openItem.setText("Open...");

        MenuItem closeItem = new MenuItem(fileMenu, SWT.PUSH);
        closeItem.setText("Close");

        closeItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                //OpenCAL.cardCollection.clear();
                //pkbURI. = null;
                //shell.setText(OpenCAL.PROGRAM_NAME + " " + OpenCAL.PROGRAM_VERSION);
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
        pdfItem.setImage(SharedImages.getImage(SharedImages.TEXT_16));
        pdfItem.setText("Export Test To PDF...");
        pdfItem.setEnabled(false);
        
        MenuItem printItem = new MenuItem(fileMenu, SWT.PUSH);
        printItem.setImage(SharedImages.getImage(SharedImages.DOCUMENT_PRINT_16));
        printItem.setText("Print Test...");
        printItem.setEnabled(false);

        new MenuItem(fileMenu, SWT.SEPARATOR);

        MenuItem quitItem = new MenuItem(fileMenu, SWT.PUSH);
        quitItem.setImage(SharedImages.getImage(SharedImages.SYSTEM_LOG_OUT_16));
        quitItem.setText("Quit");

        quitItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                shell.close();
            }
        });

        // Edit items /////////////////
        
        MenuItem undoItem = new MenuItem(editMenu, SWT.PUSH);
        undoItem.setImage(SharedImages.getImage(SharedImages.EDIT_UNDO_16));
        undoItem.setText("Undo Typing");
        undoItem.setEnabled(false);

        MenuItem redoItem = new MenuItem(editMenu, SWT.PUSH);
        redoItem.setImage(SharedImages.getImage(SharedImages.EDIT_REDO_16));
        redoItem.setText("Redo");
        redoItem.setEnabled(false);

        new MenuItem(editMenu, SWT.SEPARATOR);

        MenuItem copyItem = new MenuItem(editMenu, SWT.PUSH);
        copyItem.setImage(SharedImages.getImage(SharedImages.EDIT_COPY_16));
        copyItem.setText("Copy");
        copyItem.setEnabled(false);

        MenuItem cutItem = new MenuItem(editMenu, SWT.PUSH);
        cutItem.setImage(SharedImages.getImage(SharedImages.EDIT_CUT_16));
        cutItem.setText("Cut");
        cutItem.setEnabled(false);

        MenuItem pastItem = new MenuItem(editMenu, SWT.PUSH);
        pastItem.setImage(SharedImages.getImage(SharedImages.EDIT_PASTE_16));
        pastItem.setText("Past");
        pastItem.setEnabled(false);

        // Window items ///////////////
        
        // Virtual keyboard
        MenuItem virtualKeyboardItem = new MenuItem(windowMenu, SWT.PUSH);
        virtualKeyboardItem.setImage(SharedImages.getImage(SharedImages.PREFERENCES_SYSTEM_16));
        virtualKeyboardItem.setText("Virtual Keyboard...");
        virtualKeyboardItem.setAccelerator(SWT.MOD1 + 'W');  // Define a shortcut to open this Dialog (ctrl + w)

        virtualKeyboardItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                VirtualKeyboardDialog dialog = VirtualKeyboardDialog.getInstance();
                dialog.open();
            }
        });
        
        // Preferences
        MenuItem preferencesItem = new MenuItem(windowMenu, SWT.PUSH);
        preferencesItem.setImage(SharedImages.getImage(SharedImages.PREFERENCES_SYSTEM_16));
        preferencesItem.setText("Preferences...");

        preferencesItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                PreferencesDialog dialog = new PreferencesDialog(shell);
                dialog.open();
            }
        });
        
        // Help items /////////////////
        MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
        aboutItem.setImage(SharedImages.getImage(SharedImages.HELP_BROWSER_16));
        aboutItem.setText("About OpenCAL...");

        aboutItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                AboutDialog dialog = new AboutDialog(shell);
                dialog.open();
            }
        });
        
        ///////////////////////////////
        
        // Create the tabfolder
        final TabFolder tabFolder = new TabFolder(this.shell, SWT.NONE);
        
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        this.tabItemAddCard = new TabItem(tabFolder, SWT.NONE);
        this.tabItemTrain = new TabItem(tabFolder, SWT.NONE);
        this.tabItemTest = new TabItem(tabFolder, SWT.NONE);
        this.tabItemExplore = new TabItem(tabFolder, SWT.NONE);
        this.tabItemMonitor = new TabItem(tabFolder, SWT.NONE);
        
        tabItemAddCard.setText("Add");
        tabItemTrain.setText("Review");
        tabItemTest.setText("Test");
        tabItemExplore.setText("Explore");
        tabItemMonitor.setText("Monitor");
        
        tabItemAddCard.setToolTipText("Add new cards");
        tabItemTrain.setToolTipText("Review some cards");
        tabItemTest.setToolTipText("Test your knowledges");
        tabItemExplore.setToolTipText("Explore your knowledge base");
        tabItemMonitor.setToolTipText("Your statistics");
        
        Composite addCardComposite = new Composite(tabFolder, SWT.NONE);
        Composite trainComposite = new Composite(tabFolder, SWT.NONE);
        Composite testComposite = new Composite(tabFolder, SWT.NONE);
        Composite exploreComposite = new Composite(tabFolder, SWT.NONE);
        Composite monitorComposite = new Composite(tabFolder, SWT.NONE);
        
        tabItemAddCard.setControl(addCardComposite);
        tabItemTrain.setControl(trainComposite);
        tabItemTest.setControl(testComposite);
        tabItemExplore.setControl(exploreComposite);
        tabItemMonitor.setControl(monitorComposite);
        
        addCardTab = new AddTab(addCardComposite);
        trainTab = new TrainTab(trainComposite);
        testTab = new TestTab(testComposite);
        exploreTab = new ExploreTab(exploreComposite);
        monitorTab = new MonitorTab(monitorComposite);
        
        // Add listeners on tabFolder (prevent when a tabItem is selected)
        tabFolder.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                switch (tabFolder.getSelectionIndex()) {
                    case 0 :
                        addCardTab.update();
                        break;
                    case 1 :
                        trainTab.update();
                        break;
                    case 2 :
                        testTab.update();
                        break;
                    case 3 :
                        exploreTab.update();
                        break;
                    case 4 :
                        monitorTab.update();
                        break;
                }
            }
        });
        
        // Create the Status Bar
        Composite statusBar = new Composite(this.shell, SWT.NONE);
        statusBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
        this.statusLabel2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        this.statusLabel3 = new Label(statusBar, SWT.CENTER);
        this.statusLabel3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        this.statusLabel4 = new Label(statusBar, SWT.CENTER);
        this.statusLabel4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
     * @return
     */
    public static String[] getQuestionStrings(List<Card> cardList, boolean displayErrors) {
        ArrayList<String> questionStrings = new ArrayList<String>();
        
        Iterator<Card> it = cardList.iterator();
        while(it.hasNext()) {
            Card card = it.next();
            
            String prefix = "";
            
            if(card.isHidden()) {
                prefix = "⬚ ";
            } else {
                if(displayErrors) {
                    Review[] reviews = card.getReviews().toArray(new Review[0]);
                    
                    for(int j=0 ; j<reviews.length ; j++) {
                        if(reviews[j].getReviewDate().equals(CalendarToolKit.calendarToIso8601(new GregorianCalendar())))
                            if(reviews[j].getResult().equals(Review.WRONG_ANSWER_STRING)) prefix = "✖ ";
                            else prefix = "✔ ";
                    }
                }
            }

            questionStrings.add(prefix + card.getQuestion());
        }
        
        return questionStrings.toArray(new String[0]);
    }
    
    /**
     * 
     */
    public void updateStatus() {
        this.statusLabel1.setText("");
        this.statusLabel1.setToolTipText("");
        
        GregorianCalendar gc = new GregorianCalendar();

        // Cards Added /////////////////
        int nbCardsAdded = 0;
        
        for(Card card : OpenCAL.cardCollection) {
            if(card.getCreationDate().equals(CalendarToolKit.calendarToIso8601(gc))) nbCardsAdded++;
        }
        
        this.statusLabel2.setText("A : " + nbCardsAdded);
        this.statusLabel2.setToolTipText(nbCardsAdded + " cards added today");

        // Cards Checked ///////////////
        int nbCardsChecked = 0;
        
        for(Card card : OpenCAL.cardCollection) {
            boolean hasBeenReviewed = false;
            Review[] reviews = card.getReviews().toArray(new Review[0]);
            for(Review review : reviews) {
                if(review.getReviewDate().equals(CalendarToolKit.calendarToIso8601(gc)))
                    hasBeenReviewed = true;
            }
            
            if(hasBeenReviewed) nbCardsChecked++;
        }
        
        this.statusLabel3.setText("C : " + nbCardsChecked);
        this.statusLabel3.setToolTipText(nbCardsChecked + " cards checked today");

        // Cards Left //////////////////
        int nbCardsLeft = 0;
        // TODO...
        this.statusLabel4.setText("L : " + nbCardsLeft);
        this.statusLabel4.setToolTipText(nbCardsLeft + " cards left for today");
    }
    
    /**
     * 
     */
    public void run() {
        // Init statubar
        this.updateStatus();
        
        // Main loop
        this.shell.open();
        
        while(!this.shell.isDisposed()) {
            if(!MainWindow.DISPLAY.readAndDispatch()) MainWindow.DISPLAY.sleep();
        }
        
        MainWindow.DISPLAY.dispose();
    }
    
    /**
     * 
     */
    public void close() {
        this.shell.close();
    }
    
    /**
     * 
     * @return
     */
    public Shell getShell() {
        return this.shell;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // Load UserProperties
        try {
            ApplicationProperties.loadApplicationProperties();
        } catch (ApplicationPropertiesException e) {
            MainWindow.getInstance().printError(e.getMessage());
            MainWindow.getInstance().close();
            System.exit(1);
        }
        
        // Create Professor
        try {
            OpenCAL.professor = ProfessorFactory.createProfessor(ApplicationProperties.getProfessorName());
        } catch(ProfessorFactoryException e) {
            MainWindow.getInstance().printError(e.getMessage());
            MainWindow.getInstance().close();
            System.exit(1);
        }
        
        // Open default PKB File and create card set
        URI uri = null;
        try {
            uri = new URI(ApplicationProperties.getPkbPath());
            OpenCAL.pkb = PersonalKnowledgeBaseFactory.createPersonalKnowledgeBase("DOM");
            OpenCAL.cardCollection = OpenCAL.pkb.load(uri);
            
            // Assess cards
            for(Card card : OpenCAL.cardCollection) {
                card.setGrade(OpenCAL.professor.assess(card));
            }
        } catch(URISyntaxException e) {
            MainWindow.getInstance().printError(e.getMessage());
            MainWindow.getInstance().close();
            System.exit(1);
        } catch(PersonalKnowledgeBaseFactoryException e) {
            MainWindow.getInstance().printError(e.getMessage());
            MainWindow.getInstance().close();
            System.exit(1);
        } catch(PersonalKnowledgeBaseException e) {
            MainWindow.getInstance().printError(e.getMessage());
            MainWindow.getInstance().close();
            System.exit(1);
        }
        
        // Make and run GUI
        MainWindow.getInstance().run();
        
        // Save PKB file
        try {
            OpenCAL.pkb.save(OpenCAL.cardCollection, uri);
        } catch(PersonalKnowledgeBaseException e) {
            System.err.println(e.getMessage());
            //MainWindow.getInstance().printError(e.getMessage());  // TODO: le shell est fermé => pas d'affichage graphique... mettre les err dans des dialog (shell)  part ?
        }
        
        // Save UserProperties
        try {
            ApplicationProperties.saveApplicationProperties();
        } catch (ApplicationPropertiesException e) {
            System.err.println(e.getMessage());
            //MainWindow.getInstance().printError(e.getMessage());  // TODO: le shell est fermé => pas d'affichage graphique... mettre les err dans des dialog (shell)  part ?
            System.exit(1);
        }
    }
}
