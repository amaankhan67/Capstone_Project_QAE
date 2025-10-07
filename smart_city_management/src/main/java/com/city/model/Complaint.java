package com.city.model;

import java.time.LocalDateTime;

public class Complaint {
	
	private String complaintId;
    private String citizenId;
    private Category category;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum Category {
        ROAD, WATER, ELECTRICITY
    }
    
    public enum Status {
        OPEN, IN_PROGRESS, RESOLVED
    }
    
    public enum Priority {
        LOW, MEDIUM, HIGH
    }
    
    public Complaint(String complaintId, String citizenId, Category category, 
    		String description, Priority priority) {
    	
    	if (complaintId == null || complaintId.trim().isEmpty()) {
    		throw new IllegalArgumentException("Complaint ID cannot be null or empty");
    	}
    	if (citizenId == null || citizenId.trim().isEmpty()) {
    		throw new IllegalArgumentException("Citizen ID cannot be null or empty");
    	}
    	if (category == null) {
    		throw new IllegalArgumentException("Category cannot be null");
    	}
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
    	
    	this.complaintId = complaintId;
    	this.citizenId = citizenId;
    	this.category = category;
    	this.description = description;
    	this.priority = priority;
    	this.status = Status.OPEN;
    	this.createdAt = LocalDateTime.now();
    	this.updatedAt = LocalDateTime.now();
    }
    
    //Getter Functions
    public String getComplaintId() { return complaintId; }
    public String getCitizenId() { return citizenId; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    //Setter Functions
    public void setDescription(String description) { 
        this.description = description; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setStatus(Status status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setPriority(Priority priority) { 
        this.priority = priority; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String toString() {
        return "Complaint Details:" +
                "\nComplaint ID: " + complaintId +
                "\nCitizen ID: " + citizenId +
                "\nCategory: " + category +
                "\nDescription: " + description +
                "\nStatus: " + status +
                "\nPriority: " + priority +
                "\nCreated: " + createdAt +
                "\nLast Updated: " + updatedAt;
    }
}
