package com.city.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.city.exception.DuplicateEntityException;
import com.city.exception.InvalidInputException;
import com.city.model.TrafficSensor;

public class TrafficRepository {
	
	private Map<String, TrafficSensor> trafficSensors;

    public TrafficRepository() {
        this.trafficSensors = new HashMap<>();
    }
    
    public void addSensor(TrafficSensor sensor) {
        if (sensor == null) {
            throw new InvalidInputException("Traffic sensor cannot be null");
        }
        
        String sensorId = sensor.getSensorId();
        if (trafficSensors.containsKey(sensorId)) {
            throw new DuplicateEntityException("Traffic sensor with ID " + sensorId + " already exists");
        }
        
        trafficSensors.put(sensorId, sensor);
    }
    
    public Optional<TrafficSensor> findSensorById(String sensorId) {
        if (sensorId == null || sensorId.trim().isEmpty()) {
            throw new InvalidInputException("Sensor ID cannot be null or empty");
        }
        
        return Optional.ofNullable(trafficSensors.get(sensorId));
    }
    
    public void updateSensor(String sensorId, TrafficSensor updatedSensor) {
        if (sensorId == null || sensorId.trim().isEmpty()) {
            throw new InvalidInputException("Sensor ID cannot be null or empty");
        }
        
        if (updatedSensor == null) {
            throw new InvalidInputException("Updated sensor cannot be null");
        }
        
        if (!trafficSensors.containsKey(sensorId)) {
            throw new InvalidInputException("Sensor with ID " + sensorId + " not found");
        }
        
        trafficSensors.put(sensorId, updatedSensor);
    }
    
    public Map<String, TrafficSensor> getAllSensors() {
        return new HashMap<>(trafficSensors);
    }
    
    public List<TrafficSensor> getSensorsByLocation(String location){
    	if(location == null || location.trim().isEmpty()) {
    		throw new InvalidInputException("Location cannot be null or empty");
    	}
    	
    	List<TrafficSensor> sensorsByLocation = new ArrayList<>();
    	for(TrafficSensor sensor : trafficSensors.values()) {
    		if(sensor.getLocation().equals(location))
    			sensorsByLocation.add(sensor);
    	}
    	
    	return sensorsByLocation;
    }
    
    public List<TrafficSensor> getActiveSensors() {
        List<TrafficSensor> activeSensors = new ArrayList<>();
        for (TrafficSensor sensor : trafficSensors.values()) {
            if (sensor.isActive()) {
                activeSensors.add(sensor);
            }
        }
        return activeSensors;
    }
    
    public int getSensorCount() {
        return trafficSensors.size();
    }

}
