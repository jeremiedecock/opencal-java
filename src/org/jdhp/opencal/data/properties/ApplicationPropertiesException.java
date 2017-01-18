/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.data.properties;

public class ApplicationPropertiesException extends Exception {

    private Exception originalException;

    public ApplicationPropertiesException(String msg) {
        this(msg, null);
    }
    
    public ApplicationPropertiesException(String msg, Exception orig) {
        super(msg);
        this.originalException = orig;
    }

    public Exception getOriginalException() {
        return originalException;
    }
    
}
