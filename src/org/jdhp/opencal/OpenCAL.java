/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011 Jérémie Decock
 */

package org.jdhp.opencal;

//import org.jdhp.opencal.card.Card;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jdhp.opencal.data.pkb.PersonalKnowledgeBase;
import org.jdhp.opencal.data.properties.ApplicationProperties;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.model.professor.ProfessorAlan;
import org.jdhp.opencal.model.professor.ProfessorBen;
import org.jdhp.opencal.model.professor.ProfessorCharlie;
import org.jdhp.opencal.swt.MainWindow;

/**
 * OpenCAL
 * 
 * @author Jérémie Decock
 * @version 3.0
 */
public class OpenCAL {

	public final static String PROGRAM_VERSION = "3.0.0";
	
	public final static String PROGRAM_NAME = "OpenCAL";
	
	public final static String COPYRIGHT = "Copyright © 2007,2008,2009,2010,2011 Jérémie DECOCK";
	
	public final static String WEB_SITE = "http://www.jdhp.org";
	
	
	final public static int NO_ANSWER = 0;
	
	final public static int RIGHT_ANSWER = 1;
	
	final public static int WRONG_ANSWER = -1;
	
	// TODO : faire une map (dico) pour relier les clés {RIGHT_ANSWER, WRONG_ANSWER} aux valeures {RIGHT_ANSWER_STRING, WRONG_ANSWER_STRING}
	public final static String RIGHT_ANSWER_STRING = "good";
	
	public final static String WRONG_ANSWER_STRING = "bad";
	
	public static MainWindow mainWindow;
	
	private static String professorName;
	
	public final static Map<String, Professor> PROFESSOR_MAP;
	static {
		Map<String, Professor> map = new HashMap<String, Professor>();
		map.put("Alan", new ProfessorAlan());
		map.put("Ben", new ProfessorBen());
		map.put("Charlie", new ProfessorCharlie());
		PROFESSOR_MAP = Collections.unmodifiableMap(map);
	}
	
	public final static String DEFAULT_PROFESSOR_NAME = "Ben";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Load UserProperties
		ApplicationProperties.loadApplicationProperties();
		
		// Create Professor
		OpenCAL.setProfessorName(ApplicationProperties.getProfessorName());
		
		// Open default PKB File and create card set
		try {
			URI uri = new URI(ApplicationProperties.getPkbPath());
			PersonalKnowledgeBase.load(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
		// Make and run GUI
		OpenCAL.mainWindow = new MainWindow();
		OpenCAL.mainWindow.run();
		
		// Save UserProperties
		ApplicationProperties.saveApplicationProperties();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getProfessorName() {
		return OpenCAL.professorName;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Professor getProfessor() {
		return PROFESSOR_MAP.get(OpenCAL.getProfessorName());
	}
	
	/**
	 * 
	 * @return
	 */
	public static void setProfessorName(String professorName) {
		if(!PROFESSOR_MAP.containsKey(professorName)) {
			professorName = OpenCAL.DEFAULT_PROFESSOR_NAME;
		}
		
		OpenCAL.professorName = professorName;
		
		ApplicationProperties.setProfessorName(professorName);
	}

}
