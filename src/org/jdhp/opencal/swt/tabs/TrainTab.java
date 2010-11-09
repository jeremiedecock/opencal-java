/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009 Jérémie Decock
 */

package org.jdhp.opencal.swt.tabs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
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
	
	final public static int NO_ANSWER = 0;
	
	final public static int RIGHT_ANSWER = 1;
	
	final public static int WRONG_ANSWER = -1;
	

	final private Composite parentComposite;
	
	final private ArrayList<ModifyListListener> modifyListListeners;
	
	final private List<Card> cardList;
	
	final private Map<Card, Integer> cardMap;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public TrainTab(Composite parentComposite) {
		
		this.modifyListListeners = new ArrayList<ModifyListListener>();
		
		this.cardList = new ArrayList<Card>();
		
		this.cardMap = new HashMap<Card, Integer>();
		
		
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
	
	public void notifyModifyListListeners(Collection<Card> cardCollection) {
		Iterator<ModifyListListener> it = this.modifyListListeners.iterator();
		
		while(it.hasNext()) {
			ModifyListListener listener = it.next();
            listener.listModification(cardCollection);     // TODO : il serait plus prudent de donner une copie de cardList plutôt que sa référence...
        }
	}

	public void resultNotification(Card card, boolean result) {  // TODO : remplacer boolean result par int result
		// Update card's value in cardMap
		this.cardMap.put(card, result ? new Integer(RIGHT_ANSWER) : new Integer(WRONG_ANSWER));
		
		// Vérifi si toutes les cartes ont été révisées
		if(!this.cardMap.containsValue(new Integer(NO_ANSWER))) {
			
			List<Card> newCardList = new ArrayList<Card>();
			
			// Vérifi si il y au au moins une mauvaise réponse
			if(this.cardMap.containsValue(new Integer(WRONG_ANSWER))) {
				
				// Crée une liste à partir des cartes WRONG_ANSWER
				Set<Map.Entry<Card, Integer>> cardSet = this.cardMap.entrySet();
				Iterator<Map.Entry<Card, Integer>> it = cardSet.iterator();
				while(it.hasNext()) {
					Map.Entry<Card, Integer> entry = it.next();
					if(entry.getValue().equals(new Integer(WRONG_ANSWER))) 
						newCardList.add(entry.getKey());
		        }
				
			} else {
				
				// Recrée la liste complète ?
				MessageBox message = new MessageBox(this.parentComposite.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				message.setText("Repeat ?");
				message.setMessage("Restart the training with this cards list of cards ?");
				if(message.open() == SWT.YES) {
					newCardList.addAll(this.cardList);
				}
				
			}
			
			resetCardMap(newCardList);
			
			// Envoyer un évenement modifyList
			this.notifyModifyListListeners(newCardList);
		}
	}

	public void listModification(Collection<Card> cardCollection) {
		// Update cardlist
		this.cardList.clear();
		this.cardList.addAll(cardCollection);
		
		// Update cardMap
		this.resetCardMap(this.cardList);
	}
	
	private void resetCardMap(Collection<Card> cardCollection) {
		Iterator<Card> it = cardCollection.iterator();
		this.cardMap.clear();
		while(it.hasNext()) {
			Card card = it.next();
			this.cardMap.put(card, new Integer(NO_ANSWER));
        }
	}

}
