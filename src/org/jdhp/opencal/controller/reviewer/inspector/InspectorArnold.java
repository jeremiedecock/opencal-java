package org.jdhp.opencal.controller.reviewer.inspector;

import java.util.ArrayList;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorArnold implements Inspector {

	/**
	 * InspectorArnold don't care about time...
	 * 
	 * @return
	 */
	public int valueCardPriority(ArrayList<ReviewItem> revisionList) {
		int grade = 0;
		while(revisionList.size() > 0) {
			int oldestRevisionIndex = 0;
			
			// get the oldest revision
			for(int revisionIndex=0 ; revisionIndex<revisionList.size() ; revisionIndex++) {
				if(((ReviewItem) revisionList.get(revisionIndex)).getReviewDate().before(((ReviewItem) revisionList.get(oldestRevisionIndex)).getReviewDate())) {
					oldestRevisionIndex = revisionIndex;
				}
			}
			
			if(((ReviewItem) revisionList.get(oldestRevisionIndex)).getReviewResult().toUpperCase().equals("GOOD")) {
				grade++;
			} else {
				grade = 0;
			}
			
			revisionList.remove(oldestRevisionIndex);
		}
		return grade;
	}
	
}
