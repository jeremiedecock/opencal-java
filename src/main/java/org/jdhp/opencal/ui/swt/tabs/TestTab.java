/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.ui.swt.tabs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import java.security.MessageDigest;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.cardcollection.CardManipulator;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.ui.css.CSS;
import org.jdhp.opencal.ui.html.HtmlWrapper;
import org.jdhp.opencal.ui.html.QuestionAnswerToHtml;
import org.jdhp.opencal.ui.html.QuestionAnswerToHtmlImpl;
import org.jdhp.opencal.ui.swt.MainWindow;
import org.jdhp.opencal.ui.swt.images.SharedImages;
import org.jdhp.opencal.util.CalendarToolKit;
import org.jdhp.opencal.util.DataToolKit;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class TestTab {

    final public static int RESULT_STATE = 1;

    final public static int NAVIGATION_STATE = 2;
    
    
    final private List<Card> cardList;

    final private Composite parentComposite;
    
    final private Composite controlComposite;

    final private Composite resultButtonComposite;

    final private Composite navigationButtonComposite;
    
    final private Browser browser;
    
    final private Button wrongAnswerButton;
    
    final private Button rightAnswerButton;
    
    final private Button firstButton;
    
    final private Button lastButton;
    
    final private Button previousButton;
    
    final private Button answerButton;
    
    final private Button nextButton;

    final private Scale scale;
    
    final private CardManipulator manipulator;
    
    final private QuestionAnswerToHtml filter;

    /**
     * 
     * @param parentComposite
     */
    public TestTab(Composite parentComposite) {
        
        cardList = new ArrayList<Card>();

        manipulator = new CardManipulator(cardList);
        
        this.parentComposite = parentComposite;
        this.parentComposite.setLayout(new GridLayout(1, false));
        
        this.filter = new QuestionAnswerToHtmlImpl();
        
        ///////////////////////////////////////////////////////////////////////
        // browser ////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
//      try {
        this.browser = new Browser(this.parentComposite, SWT.BORDER);
        this.browser.setLayoutData(new GridData(GridData.FILL_BOTH));
//      } catch (SWTError e) {
//          System.out.println("Could not instantiate Browser : " + e.getMessage());
//          OpenCAL.exit(1);
//      }
        
        ///////////////////////////////////////////////////////////////////////
        // controlComposite ///////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        controlComposite = new Composite(this.parentComposite, SWT.NONE);
        controlComposite.setLayout(new StackLayout());
        controlComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        ///////////////////////////////////////////////////////////////////////
        // resultButtonComposite //////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        resultButtonComposite = new Composite(controlComposite, SWT.NONE);
        resultButtonComposite.setLayout(new GridLayout(2, true));
        
        wrongAnswerButton = new Button(resultButtonComposite, SWT.PUSH);
        rightAnswerButton = new Button(resultButtonComposite, SWT.PUSH);
        
        ///////////////////////////////////////////////////////////////////////
        // navigationButtonComposite //////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        navigationButtonComposite = new Composite(controlComposite, SWT.NONE);
        navigationButtonComposite.setLayout(new GridLayout(5, false));
        
        scale = new Scale(navigationButtonComposite, SWT.HORIZONTAL);
        firstButton = new Button(navigationButtonComposite, SWT.PUSH);
        previousButton = new Button(navigationButtonComposite, SWT.PUSH);
        answerButton = new Button(navigationButtonComposite, SWT.PUSH);
        nextButton = new Button(navigationButtonComposite, SWT.PUSH);
        lastButton = new Button(navigationButtonComposite, SWT.PUSH);
        
        ///////////////////////////////////////////////////////////////////////
        // resultButtons //////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        // wrongAnswerButton /////////////
        wrongAnswerButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
        wrongAnswerButton.setText("Wrong");
        wrongAnswerButton.setImage(SharedImages.getImage(SharedImages.FACE_SAD_24));
        wrongAnswerButton.setToolTipText("Wrong answer");
        
        wrongAnswerButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(wrongAnswerButton.getEnabled()) {
                    Review newReview = new Review(CalendarToolKit.calendarToIso8601(new GregorianCalendar()), Review.WRONG_ANSWER_STRING);
                    manipulator.pop().getReviews().add(newReview);
                    manipulator.pop().setGrade(Professor.DONT_REVIEW_THIS_TODAY);
                    manipulator.remove();

                    setState(TestTab.NAVIGATION_STATE);
    
                    update();

                    MainWindow.getInstance().updateStatus();
                }
            }
        });
        
        // rightAnswerButton /////////////
        rightAnswerButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
        rightAnswerButton.setText("Right");
        rightAnswerButton.setImage(SharedImages.getImage(SharedImages.FACE_SMILE_24));
        rightAnswerButton.setToolTipText("Right answer");
        
        rightAnswerButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(rightAnswerButton.getEnabled()) {
                    Review newReview = new Review(CalendarToolKit.calendarToIso8601(new GregorianCalendar()), Review.RIGHT_ANSWER_STRING);
                    manipulator.pop().getReviews().add(newReview);
                    manipulator.pop().setGrade(Professor.DONT_REVIEW_THIS_TODAY);
                    manipulator.remove();

                    setState(TestTab.NAVIGATION_STATE);
                    
                    update();

                    MainWindow.getInstance().updateStatus();
                }
            }
        });
        
        ///////////////////////////////////////////////////////////////////////
        // navigationButtons //////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        
        // Scale ///////////////
        GridData scaleGridData = new GridData(GridData.FILL_HORIZONTAL);
        scaleGridData.horizontalSpan = 5;
        scale.setLayoutData(scaleGridData);
        
        scale.setMinimum(0);
        scale.setIncrement(1);
        scale.setPageIncrement(1);
        
        scale.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                manipulator.setIndex(scale.getSelection());
                
                updateBrowser();
                updateButtons();
            }
        });

        // FirstButton /////////
        firstButton.setImage(SharedImages.getImage(SharedImages.GO_FIRST_24));
        firstButton.setToolTipText("Goto the first card");
        
        firstButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(firstButton.getEnabled()) {
                    manipulator.first();
                    
                    updateBrowser();
                    updateButtons();
                    updateScale();
                }
            }
        });
        
        // PreviousButton /////////
        previousButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        previousButton.setText("Previous");
        previousButton.setImage(SharedImages.getImage(SharedImages.GO_PREVIOUS_24));
        previousButton.setToolTipText("Goto the previous card");
        
        previousButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(previousButton.getEnabled()) {
                    manipulator.previous();
                    
                    updateBrowser();
                    updateButtons();
                    updateScale();
                }
            }
        });
        
        // AnswerButton //////////
        answerButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        answerButton.setText("Answer");
        answerButton.setImage(SharedImages.getImage(SharedImages.EDIT_FIND_24));
        answerButton.setToolTipText("Show the answer for this card (review this card)");
        
        answerButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(answerButton.getEnabled()) {
                    setState(TestTab.RESULT_STATE);
                    updateBrowser();
                    updateButtons();
                }
            }
        });
        
        // NextButton ////////////
        nextButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nextButton.setText("Next");
        nextButton.setImage(SharedImages.getImage(SharedImages.GO_NEXT_24));
        nextButton.setToolTipText("Goto the next card");
        
        nextButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(nextButton.getEnabled()) {
                    manipulator.next();
                    
                    updateBrowser();
                    updateButtons();
                    updateScale();
                }
            }
        });
        
        // LastButton /////////
        lastButton.setImage(SharedImages.getImage(SharedImages.GO_LAST_24));
        lastButton.setToolTipText("Goto the last card");
        
        lastButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(lastButton.getEnabled()) {
                    manipulator.last();

                    updateBrowser();
                    updateButtons();
                    updateScale();
                }
            }
        });
        
        // Init composites
        setState(TestTab.NAVIGATION_STATE);

        // Init controls
        update();
    }
    
    /**
     * 
     */
    final private int getState() {
        if(((StackLayout) controlComposite.getLayout()).topControl == resultButtonComposite) return TestTab.RESULT_STATE;
        else return TestTab.NAVIGATION_STATE;
    }
    
    /**
     * 
     */
    final private void setState(int mode) {
        if(mode == TestTab.RESULT_STATE) ((StackLayout) controlComposite.getLayout()).topControl = resultButtonComposite;
        else ((StackLayout) controlComposite.getLayout()).topControl = navigationButtonComposite;

        controlComposite.layout();
    }
    
    /**
     * Met à jour la liste des cartes
     * 
     * TODO : le manipulator n'est pas mis à jour...
     */
    final private void updateCardList() {
        cardList.clear();
        
        for(Card card : OpenCAL.cardCollection) {
            if(card.getGrade() != Professor.DONT_REVIEW_THIS_TODAY && !card.isHidden())
                cardList.add(card);
        }

        TestTab.sortCards(cardList);

        // DEBUG /////////////////////////////////////////////////
        // The following bloc is useful to compare different implementations of Ben (i.e. Java VS Python)
        /*
        for(Card card : cardList) {
            String question = card.getQuestion();
            String answer = card.getAnswer();

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");

                md.reset();
                byte[] questionBytes = question.getBytes("UTF-8");
                byte[] questionMD5Bytes = md.digest(questionBytes);
                String questionMD5Str = DataToolKit.byteArray2Hex(questionMD5Bytes);

                md.reset();
                byte[] answerBytes = answer.getBytes("UTF-8");
                byte[] answerMD5Bytes = md.digest(answerBytes);
                String answerMD5Str = DataToolKit.byteArray2Hex(answerMD5Bytes);

                System.out.println(questionMD5Str + " " + answerMD5Str);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        */
        // END DEBUG /////////////////////////////////////////////
        
        updateBrowser();
    }

    /**
     * 
     */
    final private void updateButtons() {
        if(getState() == TestTab.RESULT_STATE) {
            // Result buttons /////////
            wrongAnswerButton.setEnabled(true);
            rightAnswerButton.setEnabled(true);

            // Navigation buttons /////
            firstButton.setEnabled(false);
            previousButton.setEnabled(false);
            answerButton.setEnabled(false);
            nextButton.setEnabled(false);
            lastButton.setEnabled(false);
        } else {
            // Result buttons /////////
            wrongAnswerButton.setEnabled(false);
            rightAnswerButton.setEnabled(false);

            // Navigation buttons /////
            if(manipulator.hasPrevious()) {
                previousButton.setEnabled(true);
                firstButton.setEnabled(true);
            } else {
                previousButton.setEnabled(false);
                firstButton.setEnabled(false);
            }

            if(manipulator.hasNext()) {
                nextButton.setEnabled(true);
                lastButton.setEnabled(true);
            } else {
                nextButton.setEnabled(false);
                lastButton.setEnabled(false);
            }

            if(manipulator.pop() != null) {
                answerButton.setEnabled(true);
            } else {
                answerButton.setEnabled(false);
            }
        }
    }

    /**
     * 
     */
    final private void updateBrowser() {
        Card card = manipulator.pop();
        browser.setText(htmlOut(card));
    }
    
    /**
     * 
     */
    final private void updateScale() {
        if(manipulator.pop() != null) {
            scale.setEnabled(true);
            scale.setSelection(manipulator.getIndex());
            scale.setMaximum(cardList.size() - 1); // TODO : +0, +1 ou -1 ?
        } else {
            scale.setEnabled(false);
        }
    }

    /**
     * TODO : Utiliser un StringBuffer pour la variable "html".
     * 
     * @param card
     * @param answer
     * @return
     */
    final private String htmlOut(Card card) {
        StringBuffer html_body = new StringBuffer();
        
        if(card == null) {
            html_body.append("<div id=\"empty\">Test done</div>\n");
        } else {
            // Informations
            html_body.append("<div id=\"informations\">");
            
            html_body.append("<span class=\"information\">");
            html_body.append("Created on ");
            html_body.append("<span class=\"highlight\">");
            html_body.append(card.getCreationDate());
            html_body.append("</span>");
            html_body.append("</span> - ");
            
            html_body.append("<span class=\"information\" title=\"");
            Review reviews[] = card.getReviews().toArray(new Review[0]);
            for(int i=0 ; i<reviews.length ; i++) {
                html_body.append(reviews[i].getReviewDate());
                html_body.append(" : ");
                html_body.append(reviews[i].getResult());
                html_body.append("\n");
            }
            html_body.append("\">");
            
            html_body.append("Checked ");
            html_body.append("<span class=\"highlight\">");
            html_body.append(card.getReviews().size());
            // TODO : Late ... days
            html_body.append("</span>");
            html_body.append(" times");
            html_body.append("</span> - ");
            
            html_body.append("<span class=\"information\">");
            html_body.append("Level ");
            html_body.append("<span class=\"highlight\">");
            html_body.append(card.getGrade() == Professor.HAS_NEVER_BEEN_REVIEWED ? "-" : card.getGrade());
            html_body.append("</span>");
            html_body.append("</span>");

            /*
             * Affiche une image pour mettre en evidence les cartes pour
             * lesquelles on a donné une réponse érronée la veille (pratique
             * quand il y a beaucoup de carte de niveau 0 en attente et qu'on
             * veut donner la priorité aux cartes révisées la veille).
             */
            if(card.getGrade() == 0) {
                Review[] review_tab = card.getReviews().toArray(new Review[0]);
                
                GregorianCalendar yesterday = new GregorianCalendar();
                yesterday.add(Calendar.DAY_OF_MONTH, -1);
                
                for(Review review : review_tab) {
                    if(review.getReviewDate().equals(CalendarToolKit.calendarToIso8601(yesterday))) {
                        // TODO: set the width in CSS and remove the workaround "&nbsp;&nbsp;&nbsp;&nbsp;"
                        html_body.append(" <span class=\"star\">&nbsp;&nbsp;&nbsp;&nbsp;</span>");
                    }
                }
            }
            
            html_body.append("</div>");
            
            // Tags
            html_body.append("<div id=\"tags\">");
            String tags[] = card.getTags().toArray(new String[0]);
            for(int i=0 ; i<tags.length ; i++) {
                html_body.append("<span class=\"tag\">");
                html_body.append(tags[i]);
                html_body.append("</span>");
                html_body.append(" ");      // a space is needed between each span to "wrap" it
            }
            html_body.append("</div>");
            
            // Question
            html_body.append("<h1 class=\"question\">Question</h1>");
            html_body.append("<div class=\"question\">");
            html_body.append(filter.questionAnswerToHtml(card.getQuestion()));
            html_body.append("</div>");
            
            // Answer
            if(getState() == TestTab.RESULT_STATE) {
                html_body.append("<h1 class=\"answer\">Answer</h1>");
                html_body.append("<div class=\"answer\">");
                html_body.append(filter.questionAnswerToHtml(card.getAnswer()));
                html_body.append("</div>");
            }
        }
        
        String html = HtmlWrapper.wrapHtmlBody(html_body.toString(), CSS.REVIEW_CSS);
 
        return html;
    }
    
    /**
     * 
     */
    public void update() {
        updateCardList();
        updateButtons();
        updateScale();
    }
    
    /**
     * TODO : remplacer ce bricolage par quelque chose de plus serieux...
     *        java.util.ArrayList.toArray()
     *        java.util.Arrays.sort() + Interface Comparable -> tri par fusion
     *        java.util.Arrays.asList()
     * 
     * Tri le tableau par "grade" décroissant
     */
    public static void sortCards(List<Card> cardList) {
        // Tri bulle
        for(int i=cardList.size()-1 ; i>0 ; i--) {
            for(int j=0 ; j<i ; j++) {
                if((cardList.get(j+1)).getGrade() < (cardList.get(j)).getGrade()) {
                    Card tmp = cardList.get(j+1);
                    cardList.set(j+1, cardList.get(j));
                    cardList.set(j, tmp);
                }
            }
        }
    }

}
