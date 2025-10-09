package com.city.service;

import java.util.List;
import java.util.Optional;

import com.city.exception.EmergencyPriorityException;
import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.EmergencyAlert;
import com.city.repository.EmergencyRepository;

public class EmergencyService {
	
	private EmergencyRepository emergencyRepository;
	
	public EmergencyService(EmergencyRepository emergencyRepository) {
		if(emergencyRepository == null) {
			throw new InvalidInputException("Emergency Repository cannot be null");
		}
		
		this.emergencyRepository = emergencyRepository;
	}
	
	public EmergencyAlert raiseEmergencyAlert(EmergencyAlert.Type type, String location, EmergencyAlert.Severity severity) {
		if(type == null) {
			throw new InvalidInputException("Emergency Alert Type cannot be null");
		}
		if(location == null || location.trim().isEmpty()) {
			throw new InvalidInputException("Emergency Location cannot be null or empty");
		}
		if(severity == null) {
			throw new InvalidInputException("Emergency Severity cannot be null");
		}
		
		if(severity == EmergencyAlert.Severity.HIGH) {
			int highSeverityCount = emergencyRepository.getCountBySeverity(severity);
			if(highSeverityCount >= 5) {
				throw new EmergencyPriorityException("Cannot process more high severity Emergencies");
			}
		}
		
		String alertId = generateAlertId(type);
		EmergencyAlert alert = new EmergencyAlert(alertId, type, location, severity);
		emergencyRepository.addAlert(alert);
		
		return alert;
	}
	
	public void dispatchEmergencyService(String alertId) {
		if(alertId == null || alertId.trim().isEmpty()) {
			throw new InvalidInputException("Alert ID cannot be null or empty");
		}
		
		Optional<EmergencyAlert> alertOpt = emergencyRepository.findAlertById(alertId);
		if(!alertOpt.isPresent()) {
			throw new ServiceUnavailableException("Emergency alert with ID " + alertId + " not found");
		}
		
		EmergencyAlert alert = alertOpt.get();
		if(!alert.isActive()) {
			throw new EmergencyPriorityException("Emergency alert " + alertId + " is already resolved or closed");
		}
		
		emergencyRepository.updateAlertStatus(alertId, EmergencyAlert.Status.DISPATCHED);
	}
	
	public List<EmergencyAlert> getActiveEmergencies(){
		return emergencyRepository.getActiveAlerts();
	}
	
	public Optional<EmergencyAlert> findAlertById(String alertId){
		return emergencyRepository.findAlertById(alertId);
	}
	
	public List<EmergencyAlert> getEmergencyByStatus(EmergencyAlert.Status status){
		return emergencyRepository.getEmergencyByStatus(status);
	}
	
	public void resolveEmergency(String alertId) {
		emergencyRepository.updateAlertStatus(alertId,EmergencyAlert.Status.RESOLVED);
	}
	
	public Optional<EmergencyAlert> getNextEmergency(){
		return emergencyRepository.peekNextAlert();
	}
	
	private String generateAlertId(EmergencyAlert.Type type) {
        String prefix;
        switch (type) {
            case FIRE: prefix = "FIR"; break;
            case MEDICAL: prefix = "MED"; break;
            case SECURITY: prefix = "SEC"; break;
            default: prefix = "EMG";
        }
        
        int count = emergencyRepository.getAlertCount() + 1;
        return prefix + count;
    }

}
