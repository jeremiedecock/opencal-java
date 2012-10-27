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
