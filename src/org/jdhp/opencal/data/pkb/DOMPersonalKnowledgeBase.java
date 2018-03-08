/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.data.pkb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.cardcollection.CardCollection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jérémie Decock
 */
public class DOMPersonalKnowledgeBase implements PersonalKnowledgeBase {

    public static final String NAME = "DOM";
    
    /**
     * 
     */
    public CardCollection load(URI uri) throws PersonalKnowledgeBaseException {
        CardCollection cardCollection = new CardCollection();
        
        File pkbFile = new File(uri);
        
        // Build the XML DOM tree
        try {
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document domDocument = null;
            
            if(pkbFile.exists()) {
                domDocument = db.parse(pkbFile);
            } else {
                domDocument = db.newDocument();
                Element root = domDocument.createElement("pkb");
                domDocument.appendChild(root);
            }

            // Build the CardCollection
            NodeList cardNodes = domDocument.getElementsByTagName("card");
            for(int i=0 ; i<cardNodes.getLength() ; i++) {
                Element cardNode = (Element) cardNodes.item(i);
                
                // Question
                String question = "";
                NodeList questionNodes = cardNode.getElementsByTagName("question");
                if(questionNodes.getLength() != 0) {
                    Element questionElement = (Element) questionNodes.item(0);
                    question = questionElement.getTextContent();
                } else {
                    // TODO: throw exception
                }
                
                // Answer
                String answer = "";
                NodeList answerNodes = cardNode.getElementsByTagName("answer");
                if(answerNodes.getLength() != 0) {
                    Element answerElement = (Element) answerNodes.item(0);
                    answer = answerElement.getTextContent();
                }
                
                // Tags
                List<String> tags = new ArrayList<String>();
                NodeList tagNodes = cardNode.getElementsByTagName("tag");
                for(int j=0 ; j < tagNodes.getLength() ; j++) {
                    Element tagElement = (Element) tagNodes.item(j);
                    tags.add(tagElement.getTextContent());
                }
                
                // Reviews
                List<Review> reviews = new ArrayList<Review>();
                NodeList reviewNodes = cardNode.getElementsByTagName("review");
                for(int j=0 ; j < reviewNodes.getLength() ; j++) {
                    Element reviewElement = (Element) reviewNodes.item(j);
                    reviews.add(new Review(reviewElement.getAttribute("rdate"), reviewElement.getAttribute("result")));
                }
                
                // Creation Date
                String creationDate = cardNode.getAttribute("cdate");
                
                // Is hidden
                String isHiddenString = cardNode.getAttribute("hidden");
                boolean isHidden = isHiddenString.equals("true") ? true : false;
                
                Card card = new Card(question, answer, tags, reviews, creationDate, isHidden);
                cardCollection.add(card);
            }
            
            // Check the CardCollection
            boolean isDateConsistent = cardCollection.isDateConsistent();
            if(!isDateConsistent) {
                throw new PersonalKnowledgeBaseException("The system date is not consistent with some dates in the knowledge base.\n\nPlease check your system date !");
            }
            
        } catch(SAXException e) {
            throw new PersonalKnowledgeBaseException(uri + " n'est pas valide (SAXException)", e);
        } catch(FileNotFoundException e) {
            throw new PersonalKnowledgeBaseException(uri + " est introuvable (FileNotFoundException)", e);
        } catch(IOException e) {
            throw new PersonalKnowledgeBaseException(uri + " est illisible (IOException)", e);
        } catch(ParserConfigurationException e) {
            throw new PersonalKnowledgeBaseException("The XML parser was not configured (ParserConfigurationException)", e);
        }
        
        return cardCollection;
    }
    

    /**
     * 
     */
    public void save(CardCollection cardCollection, URI uri) throws PersonalKnowledgeBaseException {
        File pkbFile = new File(uri);

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pkbFile), StandardCharsets.UTF_8));
            out.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
            out.append("<pkb>\n");
            for(Card card : cardCollection) {
                String is_hidden = card.isHidden() ? "true" : "false";
                out.append("<card cdate=\"" + card.getCreationDate() + "\" hidden=\"" + is_hidden + "\">\n");

                // Question
                out.append("<question><![CDATA[" + card.getQuestion() + "]]></question>\n");

                // Answer
                String answer = card.getAnswer();
                if(answer.equals("")) {
                    out.append("<answer/>\n");
                } else {
                    out.append("<answer><![CDATA[" + answer + "]]></answer>\n");
                }

                // Tags
                for(String tag : card.getTags()) {
                    if(!tag.equals("")) {
                        out.append("<tag>" + tag + "</tag>\n");
                    }
                }

                // Reviews
                for(Review review : card.getReviews()) {
                    out.append("<review rdate=\"" + review.getReviewDate() + "\" result=\"" + review.getResult() + "\"/>\n");
                }

                out.append("</card>\n");
            }
            out.append("</pkb>\n");
            out.close();
        } catch(FileNotFoundException e) {
            throw new PersonalKnowledgeBaseException(uri + " does not exist or is inaccessible (FileNotFoundException)", e);
        } catch(IOException e) {
            throw new PersonalKnowledgeBaseException(uri + " I/O error (IOException)", e);
        }
    }
    
}
