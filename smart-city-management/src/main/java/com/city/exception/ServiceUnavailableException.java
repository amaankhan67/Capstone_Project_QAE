package com.city.exception;

@SuppressWarnings("serial")
public class ServiceUnavailableException extends RuntimeException {
	
	public ServiceUnavailableException() {
		super();
	}
	
	public ServiceUnavailableException(String message) {
		super(message);
	}
	
	public ServiceUnavailableException(String message, Throwable clause) {
		super(message,clause);
	}
	
	public ServiceUnavailableException(Throwable clause) {
		super(clause);
	}

}
