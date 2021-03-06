/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.ui.swt.listeners;

import org.jdhp.opencal.model.card.Card;

public interface ResultListener {
    
    void resultNotification(Card card, int result);
    
}
