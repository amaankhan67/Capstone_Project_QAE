package com.city.exception;

@SuppressWarnings("serial")
public class EmergencyPriorityException extends RuntimeException {
	
	public EmergencyPriorityException() {
        super();
    }
    
    public EmergencyPriorityException(String message) {
        super(message);
    }
    
    public EmergencyPriorityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EmergencyPriorityException(Throwable cause) {
        super(cause);
    }
}
