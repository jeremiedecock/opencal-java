/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017,2018 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.ui.javafx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import org.jdhp.opencal.data.pkb.PersonalKnowledgeBase;
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseException;
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseFactory;
import org.jdhp.opencal.data.pkb.PersonalKnowledgeBaseFactoryException;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.data.properties.ApplicationPropertiesException;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.cardcollection.CardCollection;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.model.professor.ProfessorFactory;
import org.jdhp.opencal.model.professor.ProfessorFactoryException;
import org.jdhp.opencal.util.CalendarToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MainWindow extends Application {

    private URI pkbURI;

    private Tab tabAddCard;
    private Tab tabReview;
    private Tab tabTest;
    private Tab tabExplore;
    private Tab tabMonitor;

    private Label paneAddCard;  // TODO
    private Label paneReview;  // TODO
    private Label paneTest;  // TODO
    private Label paneExplore;  // TODO
    private Label paneMonitor;  // TODO
    
    //// Singleton pattern
    //private static MainWindow uniqueInstance = null;
    //
    ///**
    // * Singleton pattern
    // * TODO : add sync...
    // * 
    // * @return
    // */
    //public static MainWindow getInstance() {
    //    if(MainWindow.uniqueInstance == null) {
    //        MainWindow.uniqueInstance = new MainWindow();
    //    }
    //    return MainWindow.uniqueInstance; 
    //}
    
    public void init() {
        System.out.println("Init...");

        // Load UserProperties
        try {
            ApplicationProperties.loadApplicationProperties();
        } catch (ApplicationPropertiesException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        }
        
        // Create Professor
        try {
            OpenCAL.professor = ProfessorFactory.createProfessor(ApplicationProperties.getProfessorName());
        } catch(ProfessorFactoryException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        }
        
        // Open default PKB File and create card set
        this.pkbURI = null;
        try {
            this.pkbURI = new URI(ApplicationProperties.getPkbPath());
            OpenCAL.pkb = PersonalKnowledgeBaseFactory.createPersonalKnowledgeBase("DOM");
            OpenCAL.cardCollection = OpenCAL.pkb.load(this.pkbURI);
            
            // Assess cards
            for(Card card : OpenCAL.cardCollection) {
                card.setGrade(OpenCAL.professor.assess(card));
            }
        } catch(URISyntaxException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        } catch(PersonalKnowledgeBaseFactoryException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        } catch(PersonalKnowledgeBaseException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        }

        System.out.println("End init...");
    }

    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // See http://stackoverflow.com/questions/31531059/how-to-remove-close-button-from-tabs-in-javafx
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Add Tab ///////////////////////////////

        this.tabAddCard = new Tab();
        this.tabAddCard.setText("Add");

        this.paneAddCard = new Label("Add");   // TODO
        this.tabAddCard.setContent(this.paneAddCard);

        tabPane.getTabs().add(this.tabAddCard);

        // Review Tab ////////////////////////////

        this.tabReview = new Tab();
        this.tabReview.setText("Review");

        this.paneReview = new Label("Review");
        this.tabReview.setContent(this.paneReview);

        tabPane.getTabs().add(this.tabReview);

        // Test Tab //////////////////////////////

        this.tabTest = new Tab();
        this.tabTest.setText("Test");

        this.paneTest = new Label("Test");
        this.tabTest.setContent(paneTest);

        tabPane.getTabs().add(this.tabTest);

        // Explore Tab ///////////////////////////

        this.tabExplore = new Tab();
        this.tabExplore.setText("Explore");

        this.paneExplore = new Label("Explore");
        this.tabExplore.setContent(paneExplore);

        tabPane.getTabs().add(this.tabExplore);

        // Monitor Tab ///////////////////////////

        this.tabMonitor = new Tab();
        this.tabMonitor.setText("Monitor");

        this.paneMonitor = new Label("Monitor");
        this.tabMonitor.setContent(this.paneMonitor);

        tabPane.getTabs().add(this.tabMonitor);

        /////////////////////////////////////////

        Scene scene = new Scene(tabPane, 800, 600);

        stage.setScene(scene);
        stage.show();
    }
    
    public void stop() {
        System.out.println("Stop...");

        // Save PKB file
        try {
            OpenCAL.pkb.save(OpenCAL.cardCollection, this.pkbURI);
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
            Platform.exit();
        }

        System.out.println("Stop...");
    }

    /**
     * TODO...
     */
    public void printError(String text) {
        System.err.println("Error: " + text);
        //MessageBox mb = new MessageBox(this.shell, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
        //mb.setText("Error...");
        //mb.setMessage(text);
        //mb.open();
    }
    
    ///**
    // * TODO...
    // */
    //public void run(String[] args) {
    //    System.out.println("Run...");
    //
    //    //// Init statubar
    //    //this.updateStatus();
    //    
    //    // Main loop
    //    this.launch(args);
    //    
    //    //while(!this.shell.isDisposed()) {
    //    //    if(!MainWindow.DISPLAY.readAndDispatch()) MainWindow.DISPLAY.sleep();
    //    //}
    //    //
    //    //MainWindow.DISPLAY.dispose();
    //    
    //    this.close();
    //}
    
    /**
     * 
     */
    public static void close() {
        Platform.exit();
    }
    
}
