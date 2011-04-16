package org.jdhp.opencal.model.professor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jdhp.opencal.data.properties.ApplicationProperties;

public class Professors {

	public final static String DEFAULT_PROFESSOR_NAME = "Ben";

	public final static Map<String, Professor> PROFESSOR_MAP;
	static {
		Map<String, Professor> map = new HashMap<String, Professor>();
		map.put("Alan", new ProfessorAlan());
		map.put("Ben", new ProfessorBen());
		map.put("Charlie", new ProfessorCharlie());
		PROFESSOR_MAP = Collections.unmodifiableMap(map);
	}
	
	private static String professorName;
	
	/**
	 * TODO : move it
	 * 
	 * @return
	 */
	public static String getProfessorName() {
		return Professors.professorName;
	}
	
	/**
	 * TODO : move it
	 * 
	 * @return
	 */
	public static Professor getProfessor() {
		return PROFESSOR_MAP.get(Professors.getProfessorName());
	}
	
	/**
	 * TODO : move it
	 * 
	 * @return
	 */
	public static void setProfessorName(String professorName) {
		if(!PROFESSOR_MAP.containsKey(professorName)) {
			professorName = Professors.DEFAULT_PROFESSOR_NAME;
		}
		
		Professors.professorName = professorName;
		
		ApplicationProperties.setProfessorName(professorName);
	}
}
