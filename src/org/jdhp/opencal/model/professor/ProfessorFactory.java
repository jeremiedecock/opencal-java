/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2017 Jérémie Decock
 */

package org.jdhp.opencal.model.professor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfessorFactory {
	
	public static Professor createProfessor(String professorName) throws ProfessorFactoryException {
		Professor professorInstance = null;
		
		if(professorName.equals(ProfessorAlan.NAME)) {
			professorInstance = new ProfessorAlan();
		} else if(professorName.equals(ProfessorBen.NAME)) {
			professorInstance =  new ProfessorBen();
		} else if(professorName.equals(ProfessorCharlie.NAME)) {
			professorInstance =  new ProfessorCharlie();
		} else {
			throw new ProfessorFactoryException("Unknown professor name: " + professorName);
		}
		
		return professorInstance;
	}

	public final static List<String> AVAILABLE_PROFESSORS_NAME;
	
	static {
		List<String> list = new ArrayList<String>();
		list.add(ProfessorAlan.NAME);
		list.add(ProfessorBen.NAME);
		list.add(ProfessorCharlie.NAME);
		AVAILABLE_PROFESSORS_NAME = Collections.unmodifiableList(list);
	}
}
