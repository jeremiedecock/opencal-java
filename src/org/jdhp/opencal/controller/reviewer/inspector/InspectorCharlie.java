package org.jdhp.opencal.controller.reviewer.inspector;

import java.util.ArrayList;
import java.util.Date;

import org.jdhp.opencal.model.xml.reviewer.ReviewItem;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class InspectorCharlie implements Inspector {

	private final int[] revisionDates = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
	
	/**
	 * 
	 * @return
	 */
	public int valueCardPriority(ArrayList<ReviewItem> revisionList, Date cardCreationDate) {
		int grade = 0;
		
		while(revisionList.size() > 0) {
			int oldestRevisionIndex = 0;
			
			// get the oldest revision
			for(int revisionIndex=0 ; revisionIndex<revisionList.size() ; revisionIndex++) {
				if(((ReviewItem) revisionList.get(revisionIndex)).getReviewDate().before(((ReviewItem) revisionList.get(oldestRevisionIndex)).getReviewDate())) {
					oldestRevisionIndex = revisionIndex;
				}
			}
			
			// check the oldest revision and update the grade
			if(((ReviewItem) revisionList.get(oldestRevisionIndex)).getReviewResult().toLowerCase().equals("good")) {
//				Date expectedDate = getExpectedDate();
				Date today = new Date();
				
//				if(expectedDate.before(today)) { // In advance
					// ...
//				} else if(expectedDate.after(today)) { // In late
					// ...
//				} else { // ...
//					grade = gedIncrementedGrade(grade);
//				}
				
			} else {
				grade = 0;
			}
			
			// remove the oldest revision
			revisionList.remove(oldestRevisionIndex);
		}
		
		return grade;
	}
	
	/**
	 * TODO : Faire un test unitaire sur cette fonction seule.
	 * 
	 * @param grade
	 * @return
	 */
	private int gedIncrementedGrade(int grade) {
		int i = 0;
		int sum = 0;
		
		while(i < revisionDates.length) {
			if(grade >= sum && grade < sum+revisionDates[i]) grade += sum;
			sum += revisionDates[i];
			i++;
		}
		
		return grade;
	}
	
}

