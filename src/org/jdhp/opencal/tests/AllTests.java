package org.jdhp.opencal.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jdhp.opencal.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(CalendarToolKitTest.class);
		suite.addTestSuite(EditableBrowserTest.class);
		suite.addTestSuite(ProfessorAlanTest.class);
		suite.addTestSuite(ProfessorBenTest.class);
		//$JUnit-END$
		return suite;
	}

}
