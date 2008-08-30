/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.usecase.review.inspector;

import java.util.Date;
import java.util.GregorianCalendar;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.usecase.Card;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorBrian implements Inspector {

	private final int[] revisionDates = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
	
	/**
	 * Brian is a little more professional than Alan.
	 * He don't validate reviews when it's too early...
	 * but he doesn't care about lates.
	 * 
	 * @return
	 */
	public int assess(Card card) {
		int grade = 0;
		
		String[] date = card.getElement().getAttribute("cdate").split("-",3);
		// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
		Date lastRevisionDate = (new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))).getTime();
		
		Date expectedRevisionDate = getExpectedRevisionDate(lastRevisionDate, grade);
		
		// TODO : vérifier que les noeuds "review" sont bien classés par date croissante
		NodeList reviewList = card.getElement().getElementsByTagName("review");
		for(int i=0 ; i < reviewList.getLength() ; i++) {
			String[] rdateString = ((Element) reviewList.item(i)).getAttribute("rdate").split("-",3);
			// TODO : s'assurer que le tableau date a bien 3 entrées (pour pas planter le programme en modifiant manuellement le fichier XML)
			Date rdate = (new GregorianCalendar(Integer.parseInt(rdateString[0]), Integer.parseInt(rdateString[1]) - 1, Integer.parseInt(rdateString[2]))).getTime();
			
			if(((Element) reviewList.item(i)).getAttribute("result").equals(OpenCAL.RIGHT_ANSWER_STRING)) {
				grade++;
				lastRevisionDate = rdate;
				expectedRevisionDate = getExpectedRevisionDate(lastRevisionDate, grade);
			} else {
				grade = 0;
				lastRevisionDate = rdate;
				expectedRevisionDate = getExpectedRevisionDate(lastRevisionDate, grade);
			}
		}
		
		if(isTooEarly(expectedRevisionDate, new Date())) {
			// It's too early to review this card. The card will be hide.
			grade = -1;
		}
		
		return grade;
		
/*
	lastRevisionDate = date(*strptime(cardNode.getAttribute('cdate') ,"%Y-%m-%d")[0:3])
	expectedRevisionDate = lastRevisionDate + timedelta(days=REVISION_DATES[grade])

	for reviewNode in cardNode.getElementsByTagName("review"):
		rdate = date(*strptime(reviewNode.getAttribute('rdate') ,"%Y-%m-%d")[0:3])
		if reviewNode.getAttribute('result') == 'good':
			if rdate >= expectedRevisionDate:
				# That's ok, we're not in advance.
				grade += 1
				lastRevisionDate = rdate
				expectedRevisionDate = lastRevisionDate + timedelta(days=REVISION_DATES[grade]) 
		else:
			grade = 0
			lastRevisionDate = rdate
			expectedRevisionDate = lastRevisionDate + timedelta(days=REVISION_DATES[grade]) 

	if date.today() >= expectedRevisionDate:
		# That's ok, we're not in advance.
		return grade
	else:
		# It's too early to review this card. The card will be hide.
		return -1
*/
	}
	
	/**
	 * 
	 * @param oldestRevisionItem
	 * @return
	 */
	private boolean isTooEarly(Date expectedRevisionDate, Date revisionDate) {
		boolean isTooEarly;
		if(expectedRevisionDate == null) {
			isTooEarly = false;
		} else {
			if(revisionDate.before(expectedRevisionDate)) {
				isTooEarly = true;
			} else {
				isTooEarly = false;
			}
		}
		return isTooEarly;
	}
	
	/**
	 * 
	 * @param previousRevisionDate
	 * @param grade
	 * @return
	 */
	private Date getExpectedRevisionDate(Date previousRevisionDate, int grade) {
		Date expectedRevisionDate;
		if(previousRevisionDate == null) {
			expectedRevisionDate = null;
		} else {
			expectedRevisionDate = new Date(previousRevisionDate.getTime() + (((long) this.revisionDates[grade]) * 24 * 60 * 60 * 1000));
		}
		return expectedRevisionDate;
	}

}
