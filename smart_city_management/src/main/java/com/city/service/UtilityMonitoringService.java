package com.city.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.UtilityMeter;
import com.city.repository.UtilityRepository;

public class UtilityMonitoringService {
	
	private UtilityRepository utilityRepository;
	
	public UtilityMonitoringService(UtilityRepository utilityRepository) {
		if(utilityRepository == null) {
			throw new InvalidInputException("Utility Repository cannot be null");
		}
		
		this.utilityRepository = utilityRepository;
	}
	
	public void recordUtilityConsumption(String meterId, double reading) {
		if(meterId == null || meterId.trim().isEmpty()) {
			throw new InvalidInputException("Meter ID cannot be null or empty");
		}
		
		if(reading < 0) {
			throw new InvalidInputException("Meter Reading cannot be negative");
		}
		
		Optional<UtilityMeter> meterOpt = utilityRepository.findMeterById(meterId);
		if(!meterOpt.isPresent()) {
			throw new ServiceUnavailableException("Meter with ID " + meterId + " not found");
		}
		UtilityMeter meter = meterOpt.get();
		
		if(meter.getCurrentReading() > reading) {
			throw new InvalidInputException("New reading cannot be less than current reading");
		}
		
		meter.setCurrentReading(reading);
		utilityRepository.updateMeter(meterId, meter);
	}
	
	public String generateBill(String propertyId, UtilityMeter.UtilityType utilityType) {
		if(propertyId == null || propertyId.trim().isEmpty()) {
			throw new InvalidInputException("Property ID cannot be null or empty");
		}
		
		if(utilityType == null) {
			throw new InvalidInputException("Utility type cannot be null");
		}
		
		List<UtilityMeter> propertyMeters = utilityRepository.getMetersByProperty(propertyId);
		if (propertyMeters.isEmpty()) {
            throw new ServiceUnavailableException("No utility meters found for property: " + propertyId);
        }
		
		UtilityMeter targetMeter = null;
		for(UtilityMeter meter : propertyMeters) {
			if(utilityType == meter.getUtilityType()) {
				targetMeter = meter;
			}
		}
		
		if(targetMeter == null) {
			throw new ServiceUnavailableException("Utility meter for " + utilityType.toString() + 
					" not found for property " + propertyId);
		}
		
		double consumption = targetMeter.getCurrentReading();
		double rate = getUtilityRate(utilityType);
		
		String bill = "Utility Bill for Property " + propertyId + 
				"\nUtility Type: " + utilityType.toString() +
				"\nCurrent Reading: " + consumption +
				"\nRate: " + rate +
				"\nTotal Amount: " + rate*consumption;
		return bill;
	}
	
	public List<UtilityMeter> detectAnomalies(double threshold){
		if(threshold<0) {
			throw new InvalidInputException("Threshold value cannot be negative");
		}
		
		List<UtilityMeter> anomalousMeters= new ArrayList<>();
		Map<String,UtilityMeter> allMeters= utilityRepository.getAllMeters();
		Map<UtilityMeter.UtilityType,Double> averageConsumption = calculateAverageConsumption();
		
		for (UtilityMeter meter : allMeters.values()) {
            UtilityMeter.UtilityType type = meter.getUtilityType();
            double currentReading = meter.getCurrentReading();
            double average = averageConsumption.get(type);

            //Checking if consumption is more than average and threshold
            if (average > 0 && currentReading > average * (1 + threshold / 100)) {
                anomalousMeters.add(meter);
            }
        }
        return anomalousMeters;
	}
	
	public Map<UtilityMeter.UtilityType,Double> calculateAverageConsumption(){
		Map<UtilityMeter.UtilityType, Double> averages = new java.util.HashMap<>();
		Map<UtilityMeter.UtilityType, Double> totals = new java.util.HashMap<>();
		Map<UtilityMeter.UtilityType, Integer> counts = new java.util.HashMap<>();
		
		for (UtilityMeter.UtilityType type : UtilityMeter.UtilityType.values()) {
            totals.put(type, 0.0);
            counts.put(type, 0);
        }
		
		Map<String,UtilityMeter> allMeters= utilityRepository.getAllMeters();
		for(UtilityMeter meter : allMeters.values()) {
			UtilityMeter.UtilityType type = meter.getUtilityType();
			totals.put(type,totals.get(type) + meter.getCurrentReading());
			counts.put(type, counts.get(type)+1);
		}
		
		for (UtilityMeter.UtilityType type : UtilityMeter.UtilityType.values()) {
            int count = counts.get(type);
            averages.put(type, count > 0 ? totals.get(type) / count : 0.0);
        }

        return averages;
	}
	
	public Optional<UtilityMeter> findMeterById(String meterId){
		return utilityRepository.findMeterById(meterId);
	}
	
	public double getMeterReading(String meterId) {
		return utilityRepository.getMeterReading(meterId);
	}
	
	private double getUtilityRate(UtilityMeter.UtilityType utilityType) {
        switch (utilityType) {
            case ELECTRICITY: return 7; 	// Rs 7 per kWh
            case WATER: return 0.1;       	// Rs 0.1 per liter
            case GAS: return 90;         	// Rs 90 per kg
            default: return 0.0;
        }
    }
	
	public void addMeter(UtilityMeter meter) {
		utilityRepository.addMeter(meter);
	}

}
