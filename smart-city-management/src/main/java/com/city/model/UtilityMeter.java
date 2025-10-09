package com.city.model;

import java.time.LocalDateTime;

public class UtilityMeter {
	
	private String meterId;
    private String propertyId;
    private UtilityType utilityType;
    private double currentReading;
    private LocalDateTime lastUpdated;
    
    public enum UtilityType {
        WATER, ELECTRICITY, GAS
    }
    
    public UtilityMeter(String meterId, String propertyId, UtilityType utilityType, double currentReading) {
    	
    	if (meterId == null || meterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Meter ID cannot be null or empty");
        }
    	if (propertyId == null || propertyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Property ID cannot be null or empty");
        }
    	if (utilityType == null) {
            throw new IllegalArgumentException("Utility type cannot be null");
        }
        if (currentReading < 0) {
            throw new IllegalArgumentException("Current reading cannot be negative");
        }
        
        this.meterId = meterId;
        this.propertyId = propertyId;
        this.utilityType = utilityType;
        this.currentReading = currentReading;
        this.lastUpdated = LocalDateTime.now();
    }
    
    //Getter Functions
    public String getMeterId() { return meterId; }
    public String getPropertyId() { return propertyId; }
    public UtilityType getUtilityType() { return utilityType; }
    public double getCurrentReading() { return currentReading; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    
    //Setter Functions
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }
    public void setUtilityType(UtilityType utilityType) { this.utilityType = utilityType; }
    public void setCurrentReading(double currentReading) { 
    	this.currentReading = currentReading; 
    	this.lastUpdated = LocalDateTime.now();
    }
    
    public String toString() {
        return "UtilityMeter Details:" +
                "\nMeter ID: " + meterId +
                "\nProperty ID: " + propertyId +
                "\nUtility Type: " + utilityType +
                "\nCurrent Reading: " + currentReading +
                "\nLast Updated: " + lastUpdated;
    }

}
