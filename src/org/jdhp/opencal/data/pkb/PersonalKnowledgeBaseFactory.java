/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2017 Jérémie Decock
 */

package org.jdhp.opencal.data.pkb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonalKnowledgeBaseFactory {

	public static PersonalKnowledgeBase createPersonalKnowledgeBase(String pkbName) throws PersonalKnowledgeBaseFactoryException {
		PersonalKnowledgeBase pkbInstance = null;
		
		if(pkbName.equals(DOMPersonalKnowledgeBase.NAME)) {
			pkbInstance = new DOMPersonalKnowledgeBase();
		} else {
			throw new PersonalKnowledgeBaseFactoryException("Unknown PKB type: " + pkbName);
		}
		
		return pkbInstance;
	}

	public final static List<String> AVAILABLE_PKB_NAME;
	
	static {
		List<String> list = new ArrayList<String>();
		list.add(DOMPersonalKnowledgeBase.NAME);
		AVAILABLE_PKB_NAME = Collections.unmodifiableList(list);
	}
	
}
