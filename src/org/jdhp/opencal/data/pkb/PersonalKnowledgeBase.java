/*
 * OpenCAL version 3.0
 * Copyright (c) 2009,2010,2011,2012 Jérémie Decock
 */

package org.jdhp.opencal.data.pkb;

import java.net.URI;

import org.jdhp.opencal.model.cardcollection.CardCollection;

/**
 *
 * @author Jérémie Decock
 */
public interface PersonalKnowledgeBase {

	/**
	 * 
	 * @param uri
	 * @return
	 * @throws PersonalKnowledgeBaseException
	 */
	public CardCollection load(URI uri) throws PersonalKnowledgeBaseException;
	
	/**
	 * 
	 * @param cardCollection
	 * @param uri
	 * @throws PersonalKnowledgeBaseException
	 */
	public void save(CardCollection cardCollection, URI uri) throws PersonalKnowledgeBaseException;
}
