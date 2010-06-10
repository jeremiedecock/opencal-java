/*
 * OpenCAL version 3.0
 * Copyright (c) 2009 Jérémie Decock
 */

package org.jdhp.opencal.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Jérémie Decock
 */
public class UserProperties {

	private static Properties userProperties;
	
	/**
	 * Get the user properties path (for example : "/home/foo/.opencal/opencal.properties").
	 * 
	 * @return
	 */
	public static String getUserPropertiesPath() {
		String userPropertiesPath = null;
		
		String userHome = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator"); // TODO : ça ne devrait pas être déclaré ici...
		
		if(userHome != null && fileSeparator != null) {
			userPropertiesPath = userHome + fileSeparator + ".opencal" + fileSeparator + "opencal.properties";
		} else {
			System.out.println("Unexpected error : your system or your JVM can't run this program.");
			System.exit(1);
		}
		
		return userPropertiesPath;
	}
	
	/**
	 * Set user properties object.
	 * 
	 * @return
	 */
	public static void loadUserProperties() {
		UserProperties.userProperties = new Properties();
		
		try {
			FileInputStream userPropertiesFile = new FileInputStream(UserProperties.getUserPropertiesPath());
			UserProperties.userProperties.load(userPropertiesFile);
			userPropertiesFile.close();
		} catch(FileNotFoundException e) {
			UserProperties.createUserPropertiesFile();
		} catch(IOException e) {
			e.printStackTrace();
			// TODO : error...
		}
	}
	
	/**
	 * Create a user properties file when it doesn't exist.
	 */
	private static void createUserPropertiesFile() {
		System.out.println(UserProperties.getUserPropertiesPath() + " is unreachable.");
		// TODO : call wizard (gui)...
		System.exit(1);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultPkbFilePath() {
		return UserProperties.userProperties.getProperty("pkb.path");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getProfessorName() {
		return UserProperties.userProperties.getProperty("professor.name");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getImgPath() {
		return UserProperties.userProperties.getProperty("img.path");
	}
}
