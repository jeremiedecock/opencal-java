/*
 * OpenCAL version 3.0
 * Copyright (c) 2009,2011 Jérémie Decock
 */

package org.jdhp.opencal.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.jdhp.opencal.OpenCAL;

/**
 *
 * @author Jérémie Decock
 */
public class ApplicationProperties {

	private static Properties applicationProperties;
	
	/**
	 * Set user properties object.
	 * 
	 * @return
	 */
	public static void loadApplicationProperties() {
		// Create and load default properties ///////////////////////
		Properties defaultProperties = new Properties();
		try {
			InputStream inStream = OpenCAL.class.getResourceAsStream("default.properties");
			InputStreamReader reader = new InputStreamReader(inStream);
			defaultProperties.load(reader);
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Create application properties with default ///////////////
		ApplicationProperties.applicationProperties = new Properties(defaultProperties);
		
		// Load user properties from last invocation ////////////////
		String userHome = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator");
		String userPropertiesDefaultLocation = userHome + fileSeparator + ".opencal.properties";
		
		/* 
		 * Utiliser l'option -D de la JVM pour utiliser un fichier "userproperties" alternatif
		 * (utile en phase de développement).
		 * Exemple : java -Dopencal.userproperties.location=/home/gremy/.opencal_dev.properties
		 */
		String userPropertiesLocation = System.getProperty("opencal.userproperties.location", userPropertiesDefaultLocation);
		
		try {
			FileInputStream inStream = new FileInputStream(userPropertiesLocation);
			ApplicationProperties.applicationProperties.load(inStream);
			inStream.close();
		} catch(FileNotFoundException e) {
			// TODO : create userProperties file from defaultProperties
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPkbPath() {
		return ApplicationProperties.applicationProperties.getProperty("pkb.path");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getProfessorName() {
		return ApplicationProperties.applicationProperties.getProperty("professor.name");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getImgPath() {
		return ApplicationProperties.applicationProperties.getProperty("img.path");
	}
}
