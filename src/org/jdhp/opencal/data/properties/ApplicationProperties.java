/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.data.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

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
     */
    public static void loadApplicationProperties() throws ApplicationPropertiesException {
        // Create and load default properties ///////////////////////
        Properties defaultProperties = new Properties();
        try {
            InputStream inStream = ApplicationProperties.class.getResourceAsStream("default.properties");
            InputStreamReader reader = new InputStreamReader(inStream);
            defaultProperties.load(reader);
            reader.close();
        } catch(NullPointerException e1) {
            throw new ApplicationPropertiesException("Can't find \"default.properties\" file.\nTry \"ant build\" to build OpenCal and copy default files in the class path.", e1);
        } catch(IOException e2) {
            throw new ApplicationPropertiesException("Can't load \"default.properties\" file.", e2);
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
        } catch(IOException e1) {
            throw new ApplicationPropertiesException("Can't load application properties.", e1);
        }
    }
    
    /**
     * 
     *
     */
    public static void saveApplicationProperties() throws ApplicationPropertiesException {
        String userPropertiesLocation = userPropertiesLocation();
        
        try {
            FileOutputStream outStream = new FileOutputStream(userPropertiesLocation);
            ApplicationProperties.applicationProperties.store(outStream, null);
            outStream.close();
        } catch(IOException e) {
            throw new ApplicationPropertiesException("Can't save application properties: can't write " + userPropertiesLocation + " file.", e);
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
     */
    public static void setDefaultLicense(String value) {
        ApplicationProperties.applicationProperties.setProperty("default.license", value);
    }
    
    /**
     * 
     * @return
     */
    public static String getPkbPath() {
        String pkbPath = "";
        
        /*
         *  Nécessaire car MainWindow utilise cette méthode pour définir sa barre de titre or
         *  MainWindow est suceptible d'être appelé pour afficher des messages d'erreur
         *  alors qu'applicationProperties n'est pas encore instancié.
         */
        if(ApplicationProperties.applicationProperties != null) {
            String userHome = System.getProperty("user.home");
            String fileSeparator = System.getProperty("file.separator");
            String userName = System.getProperty("user.name");
            //String pkbDefaultPath = userHome + fileSeparator + userName + ".pkb"; // TODO
            String pkbDefaultPath = "file://" + userHome + fileSeparator + userName + ".pkb"; // TODO
            
            pkbPath = ApplicationProperties.applicationProperties.getProperty("pkb.path", pkbDefaultPath);
        }
        
        return pkbPath;
    }

    /**
     * 
     */
    public static void setPkbPath(String value) {
        ApplicationProperties.applicationProperties.setProperty("pkb.path", value);
    }

    /**
     *
     * @return
     */
    public static String getMathjaxPath() {
        String mathjaxDefaultPath = "/usr/share/javascript/mathjax/mathjax.js?config=tex-ams_html-full";  // TODO: so far, this path is only valid for debian; include mathjax source code within the jar file or at least put it in a common directory...
        String mathjaxPath = ApplicationProperties.applicationProperties.getProperty("ext.mathjax.path", mathjaxDefaultPath);

        return mathjaxPath;
    }

    /**
     *
     */
    public static void setMathjaxPath(String value) {
        ApplicationProperties.applicationProperties.setProperty("ext.mathjax.path", value);
    }

    /**
     *
     * @return
     */
    public static String getUiPackage() {
        String uiDefaultPackage = "org.jdhp.opencal.ui.swt";
        String uiPackage = ApplicationProperties.applicationProperties.getProperty("package.ui", uiDefaultPackage);

        return uiPackage;
    }

    /**
     *
     */
    public static void setUiPackage(String value) {
        ApplicationProperties.applicationProperties.setProperty("package.ui", value);
    }

    /**
     * ATTENTION : doit être appellé uniquement au démarrage du programme pour initialiser OpenCAL.professorName
     * OpenCAL.getProfessorName() doit être utilisé dans le cas contraire pour éviter les incohérences. 
     * 
     * @return
     */
    public static String getProfessorName() {   // TODO: change to "getProfessorPackage"
        String defaultProfessorName = ApplicationProperties.DEFAULT_PROFESSOR_NAME;
        return ApplicationProperties.applicationProperties.getProperty("professor.name", defaultProfessorName);
    }

    /**
     * ATTENTION : doit être appellé uniquement par OpenCAL.setProfessorName() pour éviter les incohérences avec OpenCAL.professorName
     * 
     */
    public static void setProfessorName(String value) {   // TODO: change to "setProfessorPackage"
        ApplicationProperties.applicationProperties.setProperty("professor.name", value);
    }
    
    /**
     * 
     * @return
     */
    public static String getLastInsertFilePath() {
        String userHome = System.getProperty("user.home");
        return ApplicationProperties.applicationProperties.getProperty("last.insert.picture.path", userHome);
    }

    /**
     * 
     */
    public static void setLastInsertFilePath(String value) {
        ApplicationProperties.applicationProperties.setProperty("last.insert.picture.path", value);
    }
    
    /**
     * 
     * @return
     */
    public static String getImgPath() {
        String imgPath = "";
        
        /*
         *  Nécessaire car MainWindow utilise cette méthode or
         *  MainWindow est suceptible d'être appelé pour afficher des messages d'erreur
         *  alors qu'applicationProperties n'est pas encore instancié.
         */
        if(ApplicationProperties.applicationProperties != null) {
            String userHome = System.getProperty("user.home");
            String fileSeparator = System.getProperty("file.separator");
            //String defaultImgPath = "file://" + userHome + fileSeparator + ".opencal" + fileSeparator + "materials" + fileSeparator; // TODO 
            String defaultImgPath = userHome + fileSeparator + ".opencal" + fileSeparator + "materials" + fileSeparator; // TODO
            
            imgPath = ApplicationProperties.applicationProperties.getProperty("img.path", defaultImgPath);
        }
        
        return imgPath; // TODO
    }
}
