/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.swt.listeners;

import java.util.Collection;

import org.jdhp.opencal.model.card.Card;

public interface ModifyListListener {
	
	void listModification(Collection<Card> cardCollection);
	
}
