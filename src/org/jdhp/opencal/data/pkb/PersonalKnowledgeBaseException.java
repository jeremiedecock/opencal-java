/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.data.pkb;

public class PersonalKnowledgeBaseException extends Exception {
    
    private Exception originalException;

    public PersonalKnowledgeBaseException(String msg) {
        this(msg, null);
    }
    
    public PersonalKnowledgeBaseException(String msg, Exception orig) {
        super(msg);
        this.originalException = orig;
    }

    public Exception getOriginalException() {
        return originalException;
    }
    
}
