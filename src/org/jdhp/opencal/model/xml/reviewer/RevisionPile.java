/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.model.xml.reviewer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.controller.Controller;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class RevisionPile {
	
	private ArrayList<Card> cardList;
	
	private int pointer;
	
	private int reviewedCards;
	
	/**
	 * 
	 * @param cardDb
	 */
	public RevisionPile() {
		this.reviewedCards = 0;
		this.pointer = 0;
		this.cardList = new ArrayList<Card>();
		
		// Parse le document XML avec SAX et crée le tableau
		// TODO : en plus de vérifier si le doc xml est bien formé, vérifier si il est conforme à la DTD !
		try {
			// Crée le Handler
			/* TODO : Comment ça se fait que la ligne suivante ne provoque pas d'erreur ?
			 *        On passe à CardHandler l'objet this, or comme on est dans le constructeur,
			 *        cette objet n'existe pas encore (il est en train d'être créé)... A méditer...
			 *        Ne serait-il pas mieux de créer une méthode "init()" dans la classe RevisionPile
			 *        et de créer le handler dans cette classe en l'appelant après la création
			 *        de l'objet revisionPile ?
			 */        
			XMLReader xr = XMLReaderFactory.createXMLReader();
			CardHandler handler = new CardHandler(this);
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			//xr.parse(new InputSource((InputStream) ClassLoader.getSystemResourceAsStream(Controller.cardDb))); // parse le fichier à la racine du .jar
			FileReader r = new FileReader(OpenCAL.cardDb);
			xr.parse(new InputSource(r));
			r.close();
		} catch(SAXException e) {
			Controller.getUserInterface().printError(OpenCAL.cardDb + " n'est pas valide (SAXException)");
			Controller.exit(2);
		} catch(FileNotFoundException e) {
			Controller.getUserInterface().printError(OpenCAL.cardDb + " est introuvable (FileNotFoundException)");
			Controller.exit(2);
		} catch(IOException e) {
			Controller.getUserInterface().printError(OpenCAL.cardDb + " est illisible (IOException)");
			Controller.exit(2);
		}
		
		this.sortCards();
	}
	
	/**
	 * Toutes les cartes dont le degré de priorité est négatif sont ignorés
	 * 
	 * @param newCard
	 */
	public void addCard(Card newCard) {
		if(newCard.getPriorityRank() >= 0) {
			this.cardList.add(newCard);
		}
	}

	/**
	 * Tri le tableau par priorité décroissante
	 */
	private void sortCards() {
		// Tri bulle
		for(int i=this.cardList.size()-1 ; i>0 ; i--) {
			for(int j=0 ; j<i ; j++) {
				if(((Card) this.cardList.get(j+1)).getPriorityRank() < ((Card) this.cardList.get(j)).getPriorityRank()) {
					Card tmp = this.cardList.get(j+1);
					this.cardList.set(j+1, this.cardList.get(j));
					this.cardList.set(j, tmp);
				}
			}
		}
	}

	/**
	 * tire la carte de plus haute priorité
	 * @return
	 */
	public Card getPointedCard() {
		if(!this.isEmpty()) {
			return (Card) this.cardList.get(this.pointer);
		} else {
			Controller.getUserInterface().printAlert("Review terminated");
			return null;
		}
	}

	/**
	 * 
	 */
	public void removePointedCard() {
		this.cardList.remove(this.pointer);
		if(this.pointer >= this.cardList.size() && !this.isEmpty()) {
			this.pointer = this.cardList.size() - 1;
		}
	}

	/**
	 * 
	 */
	public void gotoPrevCard() {
		if(!this.pointerIsOnTheFirstCard()) this.pointer--;
	}

	/**
	 * 
	 */
	public void gotoNextCard() {
		if(!this.pointerIsOnTheLastCard()) this.pointer++;
	}

	/**
	 * 
	 * @return
	 */
	public boolean pointerIsOnTheFirstCard() {
		if(this.pointer == 0) return true;
		else return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean pointerIsOnTheLastCard() {
		if(this.pointer >= this.cardList.size() - 1) return true;
		else return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if(this.cardList.size() == 0) return true;
		else return false;
	}

	/**
	 * 
	 * @return
	 */
	public int getReviewedCards() {
		return this.reviewedCards;
	}

	/**
	 * 
	 */
	public void incrementReviewedCards() {
		this.reviewedCards++;
	}

	/**
	 * 
	 * @return
	 */
	public int getRemainingCards() {
		return this.cardList.size();
	}
}
