/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.swt.tabs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.swt.composites.CardSelector;
import org.jdhp.opencal.swt.composites.CardSlider;
import org.jdhp.opencal.swt.listeners.ModifyListListener;
import org.jdhp.opencal.swt.listeners.ResultListener;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class TrainTab implements ResultListener, ModifyListListener {

	final private Composite parentComposite;
	
	final private ArrayList<ModifyListListener> modifyListListeners;
	
	final private java.util.List<Card> cardList;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public TrainTab(Composite parentComposite) {
		
		this.modifyListListeners = new ArrayList<ModifyListListener>();
		
		this.cardList = new ArrayList<Card>();
		
		
		this.parentComposite = parentComposite;
		this.parentComposite.setLayout(new GridLayout(1, false));
		
		///////////////////////////////////////////////////////////////////////
		// SasheForm //////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

        SashForm horizontalSashForm = new SashForm(this.parentComposite, SWT.HORIZONTAL);
		horizontalSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		///////////////////////////////////////////////////////////////////////
		// CardSelector ///////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

		final CardSelector cardSelector = new CardSelector(horizontalSashForm);
		cardSelector.displayModeCombo.select(CardSelector.NEW_CARDS_LIST);
		cardSelector.updateTagCombo();
		cardSelector.updateCardList();
		
		///////////////////////////////////////////////////////////////////////
		// CardSlider /////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		final CardSlider cardSlider = new CardSlider(horizontalSashForm);
		
		///////////////////////////////////////////////////////////////////////
		// Events subscription (listeners) ////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		this.addModifyListListener(cardSlider);
		cardSlider.addResultListener(this);
		cardSelector.addModifyListListener(this);
		cardSelector.addModifyListListener(cardSlider);
		
		///////////////////////////
		// SasheForm //////////////
		///////////////////////////
        
        horizontalSashForm.setWeights(new int[] {25, 75});
		
	}
	
	/**
	 * La mise à jours faite lors de la sélection du tab "TrainTab"
	 */
	final public void update() {
		// TODO : cardSelector.update() ?????
	}
	
	
	
	
	
	public void addModifyListListener(ModifyListListener listener) {
		this.modifyListListeners.add(listener);
	}
	
	public void removeModifyListListener(ModifyListListener listener) {
		this.modifyListListeners.remove(listener);
	}
	
	public void notifyModifyListListeners(Card card, boolean result) {
		Iterator<ModifyListListener> it = this.modifyListListeners.iterator();
		
		while(it.hasNext()) {
			ModifyListListener listener = it.next();
            listener.listModification(cardList);     // TODO : il serait plus prudent de donner une copie de cardList plutôt que sa référence...
        }
	}

	public void resultNotification(Card card, boolean result) {
		// TODO Auto-generated method stub
		
	}

	public void listModification(Collection<Card> cardCollection) {
		// TODO Auto-generated method stub
		
	}


}
