package org.jdhp.opencal.controller.reviewer.inspector;

import java.util.ArrayList;
import java.util.Date;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorBrian implements Inspector {

	private final int[] revisionDates = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
	
	/**
	 * InspectorBrian don't care about lates revision...
	 * 
	 * @return
	 */
	public int valueCardPriority(ArrayList<ReviewItem> revisionList, Date cardCreationDate) {
		int grade = 0;
		Date previousRevisionDate = cardCreationDate;
		Date expectedRevisionDate = getExpectedRevisionDate(cardCreationDate, grade);
		while(revisionList.size() > 0) {
			int oldestRevisionIndex = this.getOldestRevision(revisionList);
			grade = this.checkRevisionAndUpdateGrade((ReviewItem) revisionList.get(oldestRevisionIndex), expectedRevisionDate, grade);
			previousRevisionDate = ((ReviewItem) revisionList.get(oldestRevisionIndex)).getReviewDate();
			expectedRevisionDate = getExpectedRevisionDate(previousRevisionDate, grade);
			revisionList.remove(oldestRevisionIndex);
		}
		if(isTooEarly(expectedRevisionDate, new Date())) {
			grade = -1;
		}
		return grade;
	}
	
	/**
	 * Get the oldest revision
	 * 
	 * @return
	 */
	private int getOldestRevision(ArrayList<ReviewItem> revisionList) {
		int oldestRevisionIndex = 0;
		for(int revisionIndex=0 ; revisionIndex<revisionList.size() ; revisionIndex++) {
			if(((ReviewItem) revisionList.get(revisionIndex)).getReviewDate().before(((ReviewItem) revisionList.get(oldestRevisionIndex)).getReviewDate())) {
				oldestRevisionIndex = revisionIndex;
			}
		}
		return oldestRevisionIndex;
	}
	
	/**
	 * Check the oldest revision and update the grade
	 * 
	 * @param oldestRevisionItem
	 */
	private int checkRevisionAndUpdateGrade(ReviewItem oldestRevisionItem, Date expectedRevisionDate, int grade) {
		if(oldestRevisionItem.getReviewResult().toUpperCase().equals("GOOD")) {
			if(!isTooEarly(expectedRevisionDate, oldestRevisionItem.getReviewDate())) grade++;
		} else {
			grade = 0;
		}
		return grade;
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
			expectedRevisionDate = new Date(previousRevisionDate.getTime() + (revisionDates[grade] * 24 * 60 * 60 * 1000));
		}
		return expectedRevisionDate;
	}

}
