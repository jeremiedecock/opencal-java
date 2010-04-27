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
