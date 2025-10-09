package com.city.model;

import java.time.LocalDateTime;

public class TrafficSensor {
	
	private String sensorId;
	private String location;
	private int currentTrafficDensity;
	private LocalDateTime timestamp;
	private Status status;
	
	public enum Status{
		ACTIVE, INACTIVE
	}
	
	public TrafficSensor(String sensorId, String location, int currentTrafficDensity, Status status) {
		
		if (sensorId == null || sensorId.trim().isEmpty()) {
			throw new IllegalArgumentException("Sensor ID cannot be null or empty");
		}
		if (currentTrafficDensity < 0) {
			throw new IllegalArgumentException("Traffic density cannot be negative");
		}
		if (location == null || location.trim().isEmpty()) {
	        throw new IllegalArgumentException("Location cannot be null or empty");
	    }
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be null");
		}
		
		this.sensorId = sensorId;
		this.currentTrafficDensity = currentTrafficDensity;
		this.location = location;
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}

	//Getter Functions
	public String getSensorId() { return sensorId; }
	public String getLocation() { return location; }
	public int getCurrentTrafficDensity() { return currentTrafficDensity; }
	public LocalDateTime getTimestamp() { return timestamp; }
	public Status getStatus() { return status; }

	// Setter Functions
	public void setLocation(String location) { this.location = location; }
	public void setCurrentTrafficDensity(int currentTrafficDensity) { 
		this.currentTrafficDensity = currentTrafficDensity; 
		this.timestamp = LocalDateTime.now();
	}
	public void setStatus(Status status) { this.status = status; }
	
	public boolean isActive() {
		return status==Status.ACTIVE;
	}
	
	public String toString() {
        return "TrafficSensor Details:" +
                "\nSensor ID: " + sensorId +
                "\nLocation: " + location +
                "\nTraffic Density: " + currentTrafficDensity +
                "\nStatus: " + status +
                "\nLast Updated: " + timestamp;
    }

}
