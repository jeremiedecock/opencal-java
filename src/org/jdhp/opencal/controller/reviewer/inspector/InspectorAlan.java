package org.jdhp.opencal.controller.reviewer.inspector;

import java.util.ArrayList;
import java.util.Date;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorAlan implements Inspector {

	/**
	 * InspectorAlan don't care about time...
	 * 
	 * @return
	 */
	public int valueCardPriority(ArrayList<ReviewItem> revisionList, Date cardCreationDate) {
		int grade = 0;
		while(revisionList.size() > 0) {
			int oldestRevisionIndex = this.getOldestRevision(revisionList);
			grade = this.checkRevisionAndUpdateGrade((ReviewItem) revisionList.get(oldestRevisionIndex), grade);
			revisionList.remove(oldestRevisionIndex);
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
	private int checkRevisionAndUpdateGrade(ReviewItem oldestRevisionItem, int grade) {
		if(oldestRevisionItem.getReviewResult().toLowerCase().equals("good")) {
			grade++;
		} else {
			grade = 0;
		}
		return grade;
	}
	
}
