/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.util;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdhp.opencal.model.card.Card;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CardListToXML {

//  public static String cardListToXML(List<Card> cardList) {
//
//      // Créer un document
//      Document document = null;
//      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//      DocumentBuilder db;
//      try {
//          db = dbf.newDocumentBuilder();
//          document = db.newDocument();
//      } catch (ParserConfigurationException e) {
//          e.printStackTrace();
//      }
//      
//      // Ajouter un noeud root au document
//      Element rootElement = document.createElement("root");
//      document.appendChild(rootElement);
//      
//      // Ajouter les elements de chaques cartes dans le rootElement
//      Iterator<Card> it = cardList.iterator();
//      while(it.hasNext()) {
//          Card card = it.next();
//          rootElement.appendChild(document.importNode(card.getElement(), true));
//      }
//      
//      // Convert the document
//      StringWriter stringWriter = new StringWriter();
//      
//      try {
//          // Make DOM source
//          Source domSource = new DOMSource(document);
//      
//          // Make output file
//          Result streamResult = new StreamResult(stringWriter);
//      
//          // Setup transformer
//          TransformerFactory factory = TransformerFactory.newInstance();
//          Transformer transformer = factory.newTransformer();
//          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//          transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//      
//          // Transformation
//          transformer.transform(domSource, streamResult);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//      
//      return stringWriter.toString();
//  }

}
