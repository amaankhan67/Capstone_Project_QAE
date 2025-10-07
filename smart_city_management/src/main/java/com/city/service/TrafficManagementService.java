package com.city.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.TrafficSensor;
import com.city.repository.TrafficRepository;

public class TrafficManagementService {
	
	private TrafficRepository trafficRepository;
	
	public TrafficManagementService(TrafficRepository trafficRepository) {
		if(trafficRepository == null) {
			throw new InvalidInputException("Traffic Repository cannot be null");
		}
		this.trafficRepository = trafficRepository;
	}
	
	public void updateTrafficDensity(String sensorId, int density) {
		if(sensorId == null || sensorId.trim().isEmpty()) {
			throw new InvalidInputException("Sensor ID cannot be null or empty");
		}
		if(density < 0) {
			throw new InvalidInputException("Traffic Density cannot be negative");
		}
		
		Optional<TrafficSensor> sensorOpt = trafficRepository.findSensorById(sensorId);
		if(!sensorOpt.isPresent()) {
			throw new ServiceUnavailableException("Sensor with ID " + sensorId + " not found");
		}
		
		TrafficSensor sensor = sensorOpt.get();
		if(!sensor.isActive()) {
			throw new ServiceUnavailableException("Sensor with ID " + sensorId + " is not active");
		}
		
		sensor.setCurrentTrafficDensity(density);
		trafficRepository.updateSensor(sensorId, sensor);
	}
	
	public Optional<TrafficSensor> findSensorById(String sensorId) {
        return trafficRepository.findSensorById(sensorId);
    }
	
	public List<String> getHighTrafficAreas(double threshold){
		if(threshold<0) {
			throw new InvalidInputException("Threshold cannot be negative");
		}
		
		List<String> highTrafficAreas = new ArrayList<>();
		List<TrafficSensor> allActiveSensors = trafficRepository.getActiveSensors();
		for(TrafficSensor sensor : allActiveSensors) {
			if(sensor.getCurrentTrafficDensity()>=threshold) {
				highTrafficAreas.add(sensor.getLocation());
			}
		}
		
		return highTrafficAreas;
	}
	
	public String optimizeTrafficSignals(String area) {
		if(area == null || area.trim().isEmpty()) {
			throw new InvalidInputException("Area cannot be null or empty");
		}
		
		List<TrafficSensor> areaSensors = trafficRepository.getSensorsByLocation(area);
		if(areaSensors.isEmpty()){
			throw new ServiceUnavailableException("No Sensor found in area: " + area);
		}
		
		int activeSensors = 0;
		int totalDensity = 0;
		
		for(TrafficSensor sensor : areaSensors) {
			if(sensor.isActive()) {
				activeSensors++;
				totalDensity+=sensor.getCurrentTrafficDensity();
			}
		}
		
		if(activeSensors == 0) {
			throw new ServiceUnavailableException("No Active Sensors in area: " + area);
		}
		
		int averageDensity=totalDensity/activeSensors;
		
		String optimizationStrategy;
		if (averageDensity < 20) {
			optimizationStrategy = "NORMAL_FLOW";
		} else if (averageDensity < 60) {
			optimizationStrategy = "MODERATE_ADJUSTMENT";
		} else if (averageDensity < 90) {
			optimizationStrategy = "HEAVY_CONGESTION_PROTOCOL";
		} else {
			optimizationStrategy = "EMERGENCY_TRAFFIC_MANAGEMENT";
		}
		
		return optimizationStrategy;
	}
	
	public void addSensor(TrafficSensor sensor) {
		trafficRepository.addSensor(sensor);
	}

}
