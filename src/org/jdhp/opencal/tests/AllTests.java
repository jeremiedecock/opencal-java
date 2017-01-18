/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jdhp.opencal.tests");
        //$JUnit-BEGIN$
        suite.addTestSuite(CalendarToolKitTest.class);
        suite.addTestSuite(InsertImageDialogTest.class);
        suite.addTestSuite(ProfessorAlanTest.class);
        suite.addTestSuite(ProfessorBenTest.class);
        //$JUnit-END$
        return suite;
    }

}
