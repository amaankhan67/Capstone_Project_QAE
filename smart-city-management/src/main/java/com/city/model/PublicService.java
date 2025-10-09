package com.city.model;

public class PublicService {
	
	private String serviceId;
    private ServiceType serviceType;
    private String location;
    private AvailabilityStatus availabilityStatus;
    
    public enum ServiceType {
        HOSPITAL, POLICE, FIRE_STATION
    }
    
    public enum AvailabilityStatus {
        AVAILABLE, BUSY, OFFLINE
    }
    
    public PublicService(String serviceId, ServiceType serviceType, String location, 
    		AvailabilityStatus availabilityStatus) {
    	
    	if (serviceId == null || serviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Service ID cannot be null or empty");
        }
        if (serviceType == null) {
            throw new IllegalArgumentException("Service type cannot be null");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        if (availabilityStatus == null) {
            throw new IllegalArgumentException("Availability status cannot be null");
        }
    	
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.location = location;
        this.availabilityStatus = availabilityStatus;
    }
    
    //Getter Functions
    public String getServiceId() { return serviceId; }
    public ServiceType getServiceType() { return serviceType; }
    public String getLocation() { return location; }
    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    
    //Setter Functions
    public void setLocation(String location) { this.location = location; }
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { 
    	this.availabilityStatus = availabilityStatus; 
    }
    
    public String toString() {
        return "PublicService Details:" +
                "\nService ID: " + serviceId +
                "\nService Type: " + serviceType +
                "\nLocation: " + location +
                "\nAvailability Status: " + availabilityStatus;
    }

}
