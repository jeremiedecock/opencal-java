/*
 * OpenCAL version 3.0
 * Copyright (c) 2009,2011 Jérémie Decock
 */

package org.jdhp.opencal.data.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	
	public final static String DEFAULT_PROFESSOR_NAME = "Ben";
	
	private static String userPropertiesLocation() {
		String userHome = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator");
		String userPropertiesDefaultLocation = userHome + fileSeparator + ".opencal.properties";
		
		/* 
		 * Utiliser l'option -D de la JVM pour utiliser un fichier "userproperties" alternatif
		 * (utile en phase de développement).
		 * 
		 * Exemple : java -Dopencal.userproperties.location=/home/gremy/.opencal_dev.properties
		 * 
		 * Pour eclipse, l'option précédente (-D...=...) doit être ajoutée dans :
		 *    Run / Run Configurations... / Arguments / VM arguments...
		 *    Run / Debug Configurations... / Arguments / VM arguments...
		 */
		String userPropertiesLocation = System.getProperty("opencal.userproperties.location", userPropertiesDefaultLocation);
		
		return userPropertiesLocation;
	}
	
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
		} catch (NullPointerException e1) {
			System.err.println("Can't find \"default.properties\" file.");
			System.err.println("Try \"ant build\" to build OpenCal and copy default files in the class path.");
			throw(e1);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Create application properties with default ///////////////
		ApplicationProperties.applicationProperties = new Properties(defaultProperties);
		
		// Load user properties from last invocation ////////////////
		String userPropertiesLocation = userPropertiesLocation();

		try {
			FileInputStream inStream = new FileInputStream(userPropertiesLocation);
			ApplicationProperties.applicationProperties.load(inStream);
			inStream.close();
		} catch(FileNotFoundException e) {
			// Don't do anything : the file will be created by the next saveApplicationProperties() call
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 *
	 */
	public static void saveApplicationProperties() {
		String userPropertiesLocation = userPropertiesLocation();
		
		try {
			FileOutputStream outStream = new FileOutputStream(userPropertiesLocation);
			ApplicationProperties.applicationProperties.store(outStream, null);
			outStream.close();
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultAuthor() {
		String userName = System.getProperty("user.name");
		return ApplicationProperties.applicationProperties.getProperty("default.author", userName);
	}
	
	/**
	 * 
	 * @return
	 */
	public static void setDefaultAuthor(String value) {
		ApplicationProperties.applicationProperties.setProperty("default.author", value);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultLicense() {
		return ApplicationProperties.applicationProperties.getProperty("default.license", "");
	}
	
	/**
	 * 
	 * @return
	 */
	public static void setDefaultLicense(String value) {
		ApplicationProperties.applicationProperties.setProperty("default.license", value);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getPkbPath() {
		String userHome = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator");
		String userName = System.getProperty("user.name");
		//String pkbDefaultPath = userHome + fileSeparator + userName + ".pkb"; // TODO
		String pkbDefaultPath = "file://" + userHome + fileSeparator + userName + ".pkb"; // TODO
		
		return ApplicationProperties.applicationProperties.getProperty("pkb.path", pkbDefaultPath);
	}

	/**
	 * 
	 * @return
	 */
	public static void setPkbPath(String value) {
		ApplicationProperties.applicationProperties.setProperty("pkb.path", value);
	}
	
	/**
	 * ATTENTION : doit être appellé uniquement au démarrage du programme pour initialiser OpenCAL.professorName
	 * OpenCAL.getProfessorName() doit être utilisé dans le cas contraire pour éviter les incohérences. 
	 * 
	 * @return
	 */
	public static String getProfessorName() {
		String defaultProfessorName = ApplicationProperties.DEFAULT_PROFESSOR_NAME;
		return ApplicationProperties.applicationProperties.getProperty("professor.name", defaultProfessorName);
	}

	/**
	 * ATTENTION : doit être appellé uniquement par OpenCAL.setProfessorName() pour éviter les incohérences avec OpenCAL.professorName
	 * 
	 * @return
	 */
	public static void setProfessorName(String value) {
		ApplicationProperties.applicationProperties.setProperty("professor.name", value);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLastInsertPicturePath() {
		String userHome = System.getProperty("user.home");
		return ApplicationProperties.applicationProperties.getProperty("last.insert.picture.path", userHome);
	}

	/**
	 * 
	 * @return
	 */
	public static void setLastInsertPicturePath(String value) {
		ApplicationProperties.applicationProperties.setProperty("last.insert.picture.path", value);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getImgPath() {
		String userHome = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator");
		//String defaultImgPath = "file://" + userHome + fileSeparator + ".opencal" + fileSeparator + "materials" + fileSeparator; // TODO 
		String defaultImgPath = userHome + fileSeparator + ".opencal" + fileSeparator + "materials" + fileSeparator; // TODO
		
		return ApplicationProperties.applicationProperties.getProperty("img.path", defaultImgPath); // TODO
	}
}
