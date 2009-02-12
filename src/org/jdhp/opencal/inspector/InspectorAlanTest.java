package org.jdhp.opencal.inspector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.card.Card;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.TestCase;

public class InspectorAlanTest extends TestCase {

	private Document document;
	
	/**
	 * 
	 * @param name
	 */
	public InspectorAlanTest(String name) {
		super(name);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			this.document = db.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		OpenCAL.setInspector("Alan");
	}

	/**
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
//		OpenCAL.setInspector(OpenCAL.getInspectorName());
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
		
		for(int i=0 ; i<testArray.length ; i++) {
			Card card = this.createCard(testArray[i][0], testArray[i][1]);
			assertEquals(Float.parseFloat(testArray[i][2]), card.getGrade());
		}
	}
	
	/**
	 * 
	 * @param cdateString
	 * @param reviewString
	 * @return
	 */
	private Card createCard(String cdateString, String reviewString) {
		Element element = this.document.createElement("card");
		
		// cdate
		element.setAttribute("cdate", cdateString);
		
		// reviews
		String reviews[] = reviewString.split(",");
		for(int i=0 ; i<reviews.length ; i++) {
			Element reviewElement = this.document.createElement("review");
			String review[] = reviews[i].split("=");
			reviewElement.setAttribute("rdate", review[0]);
			reviewElement.setAttribute("result", review[1]);
			element.appendChild(reviewElement);
		}
		
		return new Card(element);
	}

}
