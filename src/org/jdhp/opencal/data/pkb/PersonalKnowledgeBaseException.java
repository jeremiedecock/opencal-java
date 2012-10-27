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
