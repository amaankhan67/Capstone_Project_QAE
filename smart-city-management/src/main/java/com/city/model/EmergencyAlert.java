package com.city.model;

import java.time.LocalDateTime;

public class EmergencyAlert {
	
	private String alertId;
    private Type type;
    private String location;
    private Severity severity;
    private LocalDateTime timestamp;
    private Status status;
    
    public enum Type {
        FIRE, MEDICAL, SECURITY
    }
    
    public enum Severity {
        LOW, MEDIUM, HIGH
    }
    
    public enum Status {
        PENDING, DISPATCHED, RESOLVED
    }
    
    public EmergencyAlert(String alertId, Type type, String location, Severity severity) {
    	
    	if (alertId == null || alertId.trim().isEmpty()) {
            throw new IllegalArgumentException("Alert ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        if (severity == null) {
            throw new IllegalArgumentException("Severity cannot be null");
        }
    	
        this.alertId = alertId;
        this.type = type;
        this.location = location;
        this.severity = severity;
        this.timestamp = LocalDateTime.now();
        this.status = Status.PENDING; // Default when Alert is Created
    }
    
    //Getter Functions
    public String getAlertId() { return alertId; }
    public Type getType() { return type; }
    public String getLocation() { return location; }
    public Severity getSeverity() { return severity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Status getStatus() { return status; }
    
    //Setter Functions
    public void setLocation(String location) { this.location = location; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public void setStatus(Status status) { this.status = status; }
    
    public boolean isActive() {
    	return status != Status.RESOLVED;
    }
    
    public String toString() {
        return "EmergencyAlert Details:" +
                "\nAlert ID: " + alertId +
                "\nType: " + type +
                "\nLocation: " + location +
                "\nSeverity: " + severity +
                "\nTimestamp: " + timestamp +
                "\nStatus: " + status;
    }

}
