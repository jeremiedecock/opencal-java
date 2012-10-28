package org.jdhp.opencal.data.pkb;

public class PersonalKnowledgeBaseFactoryException extends Exception {
	
	private Exception originalException;

	public PersonalKnowledgeBaseFactoryException(String msg) {
		this(msg, null);
	}
	
	public PersonalKnowledgeBaseFactoryException(String msg, Exception orig) {
		super(msg);
		this.originalException = orig;
	}

	public Exception getOriginalException() {
		return originalException;
	}
	
}
