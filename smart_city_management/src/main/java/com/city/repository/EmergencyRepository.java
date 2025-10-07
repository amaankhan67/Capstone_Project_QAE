package com.city.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

import com.city.exception.InvalidInputException;
import com.city.model.EmergencyAlert;

public class EmergencyRepository {
	
	private Queue<EmergencyAlert> emergencyQueue;

    public EmergencyRepository() {
        //Creating Priority Queue where HIGH Priority is stored at the top
        this.emergencyQueue = new PriorityQueue<>((alert1, alert2) -> {
            return alert2.getSeverity().compareTo(alert1.getSeverity());
        });
    }
    
    public void addAlert(EmergencyAlert alert) {
        if (alert == null) {
            throw new InvalidInputException("Emergency alert cannot be null");
        }
        
        emergencyQueue.offer(alert);
    }
    
    public Optional<EmergencyAlert> peekNextAlert() {
        return Optional.ofNullable(emergencyQueue.peek());
    }
    
    public Optional<EmergencyAlert> getNextAlert() {
        return Optional.ofNullable(emergencyQueue.poll());
    }
    
    public Optional<EmergencyAlert> findAlertById(String alertId) {
        if (alertId == null || alertId.trim().isEmpty()) {
            throw new InvalidInputException("Alert ID cannot be null or empty");
        }
        
        for (EmergencyAlert alert : emergencyQueue) {
            if (alertId.equals(alert.getAlertId())) {
                return Optional.of(alert);
            }
        }
        return Optional.empty();
    }
    
    public List<EmergencyAlert> getAllAlerts() {
        return new ArrayList<>(emergencyQueue);
    }
    
    public List<EmergencyAlert> getActiveAlerts() {
        List<EmergencyAlert> activeAlerts = new ArrayList<>();
        for (EmergencyAlert alert : emergencyQueue) {
            if (alert.isActive()) {
                activeAlerts.add(alert);
            }
        }
        return activeAlerts;
    }
    
    public List<EmergencyAlert> getAlertsBySeverity(EmergencyAlert.Severity severity) {
        if (severity == null) {
            throw new InvalidInputException("Severity cannot be null");
        }
        
        List<EmergencyAlert> severityAlerts = new ArrayList<>();
        for (EmergencyAlert alert : emergencyQueue) {
            if (severity == alert.getSeverity()) {
                severityAlerts.add(alert);
            }
        }
        return severityAlerts;
    }
    
    public List<EmergencyAlert> getEmergencyByStatus(EmergencyAlert.Status status) {
        if (status == null) {
            throw new InvalidInputException("Status cannot be null");
        }
        
        List<EmergencyAlert> alertsByStatus = new ArrayList<>();
        for (EmergencyAlert alert : emergencyQueue) {
            if (status == alert.getStatus()) {
            	alertsByStatus.add(alert);
            }
        }
        return alertsByStatus;
    }
    
    public boolean removeAlert(String alertId) {
        if (alertId == null || alertId.trim().isEmpty()) {
            throw new InvalidInputException("Alert ID cannot be null or empty");
        }
        
        Iterator<EmergencyAlert> iterator = emergencyQueue.iterator();
        while (iterator.hasNext()) {
            EmergencyAlert alert = iterator.next();
            if (alertId.equals(alert.getAlertId())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    public boolean updateAlertStatus(String alertId, EmergencyAlert.Status newStatus) {
        if (alertId == null || alertId.trim().isEmpty()) {
            throw new InvalidInputException("Alert ID cannot be null or empty");
        }
        if (newStatus == null) {
            throw new InvalidInputException("Status cannot be null");
        }
        
        for (EmergencyAlert alert : emergencyQueue) {
            if (alertId.equals(alert.getAlertId())) {
                alert.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }
    
    public int getCountBySeverity(EmergencyAlert.Severity severity) {
    	if(severity == null) {
    		throw new InvalidInputException("Emergency Severity cannot be null");
    	}
    	
    	return getAlertsBySeverity(severity).size();
    }
    
    public int getAlertCount() {
        return emergencyQueue.size();
    }
}
