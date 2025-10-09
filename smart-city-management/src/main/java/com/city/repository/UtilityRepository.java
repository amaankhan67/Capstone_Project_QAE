package com.city.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.city.exception.InvalidInputException;
import com.city.model.UtilityMeter;

public class UtilityRepository {
	
	private Map<String, UtilityMeter> utilityMeters;
	
	public UtilityRepository() {
		this.utilityMeters = new HashMap<>();
	}
	
	public void addMeter(UtilityMeter meter) {
		if(meter == null) {
			throw new InvalidInputException("Utility Meter cannot be null");
		}
		
		String meterId = meter.getMeterId();
		utilityMeters.put(meterId, meter);
	}
	
	public Optional<UtilityMeter> findMeterById(String meterId){
		if(meterId == null || meterId.trim().isEmpty()) {
			throw new InvalidInputException("Meter ID cannot be null or empty");
		}
		
		return Optional.ofNullable(utilityMeters.get(meterId));
	}
	
	public void updateMeter(String meterId, UtilityMeter updatedMeter) {
		if(meterId == null || meterId.trim().isEmpty()) {
			throw new InvalidInputException("Meter ID cannot be null or empty");
		}
		
		if(updatedMeter == null) {
			throw new InvalidInputException("Updated Meter cannot be null");
		}
		
		if(!utilityMeters.containsKey(meterId)) {
			throw new InvalidInputException("Meter with ID " + meterId + " not found");
		}
		
		utilityMeters.put(meterId, updatedMeter);
	}
	
	public List<UtilityMeter> getMetersByType(UtilityMeter.UtilityType utilityType) {
		if (utilityType == null) {
			throw new InvalidInputException("Utility type cannot be null");
		}
		List<UtilityMeter> typeMeters = new ArrayList<>();
		for (UtilityMeter meter : utilityMeters.values()) {
			if (utilityType == meter.getUtilityType()) {
				typeMeters.add(meter);
			}
		}
		return typeMeters;
	}
	
	public List<UtilityMeter> getMetersByProperty(String propertyId) {
		if (propertyId == null || propertyId.trim().isEmpty()) {
			throw new InvalidInputException("Property ID cannot be null or empty");
		}
		List<UtilityMeter> propertyMeters = new ArrayList<>();
		for (UtilityMeter meter : utilityMeters.values()) {
			if (propertyId.equals(meter.getPropertyId())) {
				propertyMeters.add(meter);
			}
		}
		return propertyMeters;
	}
	
	public double getMeterReading(String meterId) {
		if(meterId == null || meterId.trim().isEmpty()) {
			throw new InvalidInputException("Meter ID cannot be null or empty");
		}
		double currentReading = 0.0;
		for(UtilityMeter meter : utilityMeters.values()) {
			if(meter.getMeterId().equals(meterId)) {
				currentReading = meter.getCurrentReading();
			}
		}
		return currentReading;
	}
	
	public Map<String,UtilityMeter> getAllMeters(){
		return new HashMap<>(utilityMeters);
	}
	
	public int getMeterCount() {
        return utilityMeters.size();
    }

}
