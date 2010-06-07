package org.jdhp.opencal.tests;

import org.jdhp.opencal.swt.widgets.EditableBrowser;

import junit.framework.TestCase;

public class EditableBrowserTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExtractExtension() {
		
		String[][] testArray = {
				{"file.txt", "txt"},
				{"file.tar.gz", "gz"},
				{".file.txt", "txt"},
				{".file", ""},
				{"file", ""},
				{".", ""},
				{"", ""},
				{"file.", ""}
		};
		
		for(int i=0 ; i<testArray.length ; i++) {
			assertEquals(testArray[i][0], testArray[i][1], EditableBrowser.extractExtension(testArray[i][0]));
		}
		
	}

	public void testIsAValidExtension() {
		
		String[] falseTestArray = {
				"txt",
				"png.",
				".png",
				""
		};
		
		for(int i=0 ; i<falseTestArray.length ; i++) {
			assertFalse(falseTestArray[i] + " (expected false)", EditableBrowser.isAValidImageExtension(falseTestArray[i]));
		}
		
		String[] trueTestArray = {
				"png",
				"jpg",
				"jpeg",
				"JPG",
				"jpEG",
				"PnG"
		};
		
		for(int i=0 ; i<trueTestArray.length ; i++) {
			assertTrue(trueTestArray[i] + " (expected true)", EditableBrowser.isAValidImageExtension(trueTestArray[i]));
		}
		
	}

}
