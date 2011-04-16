package org.jdhp.opencal.model.cardcollection;

import java.util.ArrayList;
import java.util.TreeSet;

import org.jdhp.opencal.model.card.Card;

//TODO : use an other collection ? (formerly HashSet -> it don't preserve chronology)
//public final static Collection<Card> cardCollection = new HashSet<Card>(); // TODO : use TreeSet instead ? (sort)

public class CardCollection extends ArrayList<Card> {

	public CardCollection() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getTags(boolean ignoreHiddenCards) {
		TreeSet<String> tagSet = new TreeSet<String>();
		
        for(Card card : this) {
            if(!card.isHidden() || !ignoreHiddenCards) {
                String[] tags = card.getTags();
                
                for(String tag : tags) {
                	tagSet.add(tag);
                }
            }
        }
		
		return tagSet.toArray(new String[tagSet.size()]);
	}
	
}
