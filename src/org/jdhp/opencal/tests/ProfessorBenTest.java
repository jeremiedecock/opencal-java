/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.tests;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.model.professor.ProfessorBen;
import org.jdhp.opencal.model.professor.ProfessorFactory;
import org.jdhp.opencal.util.CalendarToolKit;

import junit.framework.TestCase;

public class ProfessorBenTest extends TestCase {

    private Professor professor;
    
    /**
     * 
     * @param name
     */
    public ProfessorBenTest(String name) {
        super(name);
    }

    /**
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        this.professor = ProfessorFactory.createProfessor("Ben");
    }

    /**
     * 
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 
     */
    public void testAssess() {
        /*
         * testArray[][0] = cdate         (ex: "2008-01-01")
         * testArray[][1] = reviewDates   (ex: "2008-01-02=good,2008-01-04=good")
         * testArray[][2] = expectedGrade (ex: "2.0")
         */
        String testArray[][] = {
                {"2008-01-01", "2008-01-02=good,2008-01-04=good", "2.0"},
                {"2008-01-01", "2008-01-02=good,2008-01-04=bad,2008-01-05=good", "1.0"},
                {"2008-01-01", "2008-01-02=good,2008-01-04=bad", "0.0"}};
        
        for(String[] test : testArray) {
            Card card = this.createCard(test[0], test[1]);
            assertEquals(Float.parseFloat(test[2]), card.getGrade());
        }
    }

    /**
     * 
     */
    public void testGetExpectedRevisionDate() {
        /*
         * testArray[][0] = lastRevisionDate             (ex: "2008-01-01")
         * testArray[][1] = grade                        (ex: "2")
         * testArray[][2] = expectedExpectedRevisionDate (ex: "2008-01-05")
         */
        String testArray[][] = {
                {"2008-01-01", "2", "2008-01-05"},
                {"2008-01-01", "3", "2008-01-09"},
                {"2008-01-01", "4", "2008-01-17"}};
        
        for(String[] test : testArray) {
            GregorianCalendar lastRevisionDate = CalendarToolKit.iso8601ToCalendar(test[0]);
            int grade = Integer.parseInt(test[1]);
            
            String expectedRevisionDate = CalendarToolKit.calendarToIso8601(ProfessorBen.getExpectedRevisionDate(lastRevisionDate, grade));
            String expectedExpectedRevisionDate = test[2];
            
            assertEquals(expectedExpectedRevisionDate, expectedRevisionDate);
        }
    }

    /**
     * 
     */
    public void testDeltaDays() {
        /*
         * testArray[][0] = grade         (ex: "1")
         * testArray[][1] = expectedDelta (ex: "2")
         */
        int testArray[][] = {
                {0, 1},
                {1, 2},
                {2, 4}};
        
        for(int test[] : testArray) {
            assertEquals(test[1], ProfessorBen.deltaDays(test[0]));
        }
    }
    
    /**
     * 
     * @param creationDate
     * @param reviewsString
     * @return
     */
    private Card createCard(String creationDate, String reviewsString) {
        
        // reviews
        String reviewArray[] = reviewsString.split(",");
        List<Review> reviewList = new ArrayList<Review>();
        
        for(String reviewString : reviewArray) {
            String reviewAttributes[] = reviewString.split("=");
            reviewList.add(new Review(reviewAttributes[0], reviewAttributes[1]));
        }
        
        Card card = new Card("test", "test", new ArrayList<String>(), reviewList, creationDate, false);
        card.setGrade(this.professor.assess(card));
        
        return card;
    }

}
