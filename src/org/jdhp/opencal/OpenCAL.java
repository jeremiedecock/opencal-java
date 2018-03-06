/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal;

import org.jdhp.opencal.data.pkb.PersonalKnowledgeBase;
import org.jdhp.opencal.model.cardcollection.CardCollection;
import org.jdhp.opencal.model.professor.Professor;

/**
 * OpenCAL
 *
 * @author Jérémie Decock
 * @version 3.0
 */
public class OpenCAL {

    public final static String PROGRAM_VERSION = "3.0.0";

    public final static String PROGRAM_NAME = "OpenCAL";

    public final static String COPYRIGHT = "Copyright © 2007,2008,2009,2010,2011,2012,2016,2017,2018 Jérémie DECOCK";

    public final static String WEB_SITE = "http://www.jdhp.org";

    public static Professor professor = null;        // TODO
    public static PersonalKnowledgeBase pkb = null;  // TODO
    public static CardCollection cardCollection = null;

}
