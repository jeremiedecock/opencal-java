/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.util;

import java.util.Formatter;

public class DataToolKit {
	
	public static String byteArray2Hex(byte[] byteArray) {
        Formatter formatter = new Formatter();
        
        for(byte b : byteArray) {
            formatter.format("%02x", b);
        }
        
        return formatter.toString();
    }

}
