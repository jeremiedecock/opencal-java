/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008 Jérémie Decock
 */

package org.jdhp.opencal.card.lists;

import java.util.TreeSet;

import org.jdhp.opencal.card.Card;
import org.jdhp.opencal.card.CardList;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.PersonalKnowledgeBase;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class CardByTagList extends CardList {
	
	private final static String ALL_TAGS = "*";
	
	private String currentTag;

	/**
	 * 
	 */
	public CardByTagList() {
		super();
		
		this.currentTag = CardByTagList.ALL_TAGS;
		
		for(int i=0 ; i<OpenCAL.allCardList.size() ; i++) {
			this.add(OpenCAL.allCardList.get(i));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] tagList() {
		TreeSet<String> tagSet = new TreeSet<String>();
		
		NodeList nodeTags = PersonalKnowledgeBase.getDomDocument().getElementsByTagName("tag");
		for(int i=0 ; i<nodeTags.getLength() ; i++) {
			Element tagElement = (Element) nodeTags.item(i);
			String tagText = ((Text) tagElement.getFirstChild()).getData();
			tagSet.add(tagText);
		}
		
		String[] tagsArray = new String[tagSet.size()];
		return tagSet.toArray(tagsArray);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCurrentTag() {
		return this.currentTag;
	}
	
	/**
	 * 
	 * @param tagName
	 */
	public void setCurrentTag(String tagName) {
		this.currentTag = tagName;
		this.updateList();
	}
	
	/**
	 * 
	 */
	private void updateList() {
		this.clear();
		
		for(int i=0 ; i<OpenCAL.allCardList.size() ; i++) {
			Card card = OpenCAL.allCardList.get(i);
			
			if(this.currentTag.equals(CardByTagList.ALL_TAGS)) {
				this.add(card);
			} else {
				String[] tags = card.getTags();
				for(int j=0 ; j < tags.length ; j++) {
					if(tags[j].equals(this.currentTag)) this.add(card);
				}
			}
		}
	}
}
