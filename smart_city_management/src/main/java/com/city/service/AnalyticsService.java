package com.city.service;

import java.util.List;
import java.util.Map;

import com.city.exception.InvalidInputException;
import com.city.model.Complaint;
import com.city.model.EmergencyAlert;
import com.city.model.TrafficSensor;
import com.city.model.UtilityMeter;
import com.city.repository.ComplaintRepository;
import com.city.repository.EmergencyRepository;
import com.city.repository.TrafficRepository;
import com.city.repository.UtilityRepository;

public class AnalyticsService {
	private TrafficRepository trafficRepository;
	private UtilityRepository utilityRepository;
	private ComplaintRepository complaintRepository;
	private EmergencyRepository emergencyRepository;

	public AnalyticsService(TrafficRepository trafficRepository, 
			UtilityRepository utilityRepository,
			ComplaintRepository complaintRepository,
			EmergencyRepository emergencyRepository) {
		if (trafficRepository == null) {
			throw new InvalidInputException("Traffic Repository cannot be null");
		}
		if (utilityRepository == null) {
			throw new InvalidInputException("Utility Repository cannot be null");
		}
		if (complaintRepository == null) {
			throw new InvalidInputException("Complaint Repository cannot be null");
		}
		if (emergencyRepository == null) {
			throw new InvalidInputException("Emergency Repository cannot be null");
		}

		this.trafficRepository = trafficRepository;
		this.utilityRepository = utilityRepository;
		this.complaintRepository = complaintRepository;
		this.emergencyRepository = emergencyRepository;
	}
	
	public String generateTrafficReport() {
		Map<String,TrafficSensor> allSensors = trafficRepository.getAllSensors();
		
		if(allSensors.isEmpty()) {
			return "No Traffic Sensors available to generate Report";
		}
		
		int totalSensors = allSensors.size();
        int activeSensors = 0;
        int inactiveSensors = 0;
        int highTrafficSensors = 0;
        int moderateTrafficSensors = 0;
        int lowTrafficSensors = 0;
        double totalDensity = 0.0;
        
        for (TrafficSensor sensor : allSensors.values()) {
        	if (sensor.isActive()) {
        		activeSensors++;
        		double density = sensor.getCurrentTrafficDensity();
        		totalDensity += density;

        		//Categorize Traffic Levels
        		if (density >= 60) {
        			highTrafficSensors++;
        		} 
        		else if (density >= 20) {
        			moderateTrafficSensors++;
        		} 
        		else {
        			lowTrafficSensors++;
        		}
        	} 
        	else {
        		inactiveSensors++;
        	}
        }

        double averageDensity = activeSensors > 0 ? totalDensity / activeSensors : 0;
        
        return "\n=== TRAFFIC ANALYSIS REPORT ===" +
        "\nTotal Sensors: " + totalSensors +
        "\nActive Sensors: " + activeSensors +
        "\nInactive Sensors: " + inactiveSensors +
        "\nAverage Traffic Density: " + averageDensity +
        "\n=Traffic Distribution=" + 
        "\n - High Traffic:" + highTrafficSensors +
        "\n - Moderate Traffic:" + moderateTrafficSensors +
        "\n - Low Traffic:" + lowTrafficSensors;

	}
	
	public String generateUtilityConsumptionReport() {
		Map<String, UtilityMeter> allMeters = utilityRepository.getAllMeters();

		if (allMeters.isEmpty()) {
			return "No Utility Meters available to generate Report";
		}
		
		double totalElectricity = 0;
        double totalWater = 0;
        double totalGas = 0;
        int electricityMeters = 0;
        int waterMeters = 0;
        int gasMeters = 0;
        
        for (UtilityMeter meter : allMeters.values()) {
            double consumption = meter.getCurrentReading();
            
            switch (meter.getUtilityType()) {
                case ELECTRICITY:
                    totalElectricity += consumption;
                    electricityMeters++;
                    break;
                case WATER:
                    totalWater += consumption;
                    waterMeters++;
                    break;
                case GAS:
                    totalGas += consumption;
                    gasMeters++;
                    break;
            }
        }
        
        double avgElectricity = electricityMeters > 0 ? totalElectricity / electricityMeters : 0;
        double avgWater = waterMeters > 0 ? totalWater / waterMeters : 0;
        double avgGas = gasMeters > 0 ? totalGas / gasMeters : 0;
        
        return "\n=== UTILITY CONSUMPTION REPORT ===" +
        "\nTotal Meters: " + allMeters.size() +
        "\nElectricity Consumption: " +
        "\n - Meters:" + electricityMeters +
        "\n - Total:" + totalElectricity + " kWh" +
        "\n - Average:" + avgElectricity + " kWh" +
        "\nWater Consumption: " +
        "\n - Meters:" + waterMeters +
        "\n - Total:" + totalWater + " l" +
        "\n - Average:" + avgWater + " l" +
        "\nGas Consumption: " +
        "\n - Meters:" + gasMeters +
        "\n - Total:" + totalGas + " Kg" +
        "\n - Average:" + avgGas + " Kg";
	}
	
	public String getComplaintStatistics() {
		Map<String, Complaint> allComplaints = complaintRepository.getAllComplaints();

		if (allComplaints.isEmpty()) {
			return "No Complaints available to generate Report";
		}
		
		int totalComplaints = allComplaints.size();
        int openComplaints = 0;
        int inProgressComplaints = 0;
        int resolvedComplaints = 0;
        int highPriority = 0;
        int mediumPriority = 0;
        int lowPriority = 0;
        int roadComplaints = 0;
        int waterComplaints = 0;
        int electricityComplaints = 0;
        
        for (Complaint complaint : allComplaints.values()) {
          
            switch (complaint.getStatus()) {
                case OPEN: openComplaints++; break;
                case IN_PROGRESS: inProgressComplaints++; break;
                case RESOLVED: resolvedComplaints++; break;
            }

            switch (complaint.getPriority()) {
                case HIGH: highPriority++; break;
                case MEDIUM: mediumPriority++; break;
                case LOW: lowPriority++; break;
            }

            switch (complaint.getCategory()) {
                case ROAD: roadComplaints++; break;
                case WATER: waterComplaints++; break;
                case ELECTRICITY: electricityComplaints++; break;
            }
        }
        
        double resolutionRate = totalComplaints > 0 ? (double) resolvedComplaints / totalComplaints * 100 : 0;
        
        return "\n=== COMPLAINT ANALYSIS REPORT ===" +
        "\nTotal Complaints: " + totalComplaints +
        "\nStatus Details: " +
        "\n - Open:" + openComplaints +
        "\n - In Progress:" + inProgressComplaints +
        "\n - Resolved:" + resolvedComplaints +
        "\n - Resolution Rate:" + resolutionRate + "%" +
        "\nPriority Details: " +
        "\n - High:" + highPriority +
        "\n - Medium:" + mediumPriority +
        "\n - Low:" + lowPriority +
        "\nCategory Details: " +
        "\n - Road:" + roadComplaints +
        "\n - Water:" + waterComplaints +
        "\n - Electricity:" + electricityComplaints;
	}
	
	public String generateEmergencyReport() {
		List<EmergencyAlert> allAlerts = emergencyRepository.getAllAlerts();

		if (allAlerts.isEmpty()) {
			return "No Emergency Data available to generate Report";
		}

		int totalAlerts = allAlerts.size();
		int activeAlerts = 0;
		int dispatchedAlerts = 0;
		int resolvedAlerts = 0;
		int highSeverity = 0;
		int mediumSeverity = 0;
		int lowSeverity = 0;
		int fireAlerts = 0;
		int medicalAlerts = 0;
		int securityAlerts = 0;
		
		for (EmergencyAlert alert : allAlerts) {
         
            switch (alert.getStatus()) {
                case PENDING: activeAlerts++; break;
                case DISPATCHED: dispatchedAlerts++; break;
                case RESOLVED: resolvedAlerts++; break;
            }

            switch (alert.getSeverity()) {
                case HIGH: highSeverity++; break;
                case MEDIUM: mediumSeverity++; break;
                case LOW: lowSeverity++; break;
            }

            switch (alert.getType()) {
                case FIRE: fireAlerts++; break;
                case MEDICAL: medicalAlerts++; break;
                case SECURITY: securityAlerts++; break;
            }
        }
		
		return "\n=== EMERGENCY SERVICES ANALYSIS REPORT ===" +
        "\nTotal Alerts: " + totalAlerts +
        "\nAlert Details: " +
        "\n - Pending:" + activeAlerts +
        "\n - Dispatched:" + dispatchedAlerts +
        "\n - Resolved:" + resolvedAlerts +
        "\nSeverity Details: " +
        "\n - High:" + highSeverity +
        "\n - Medium:" + mediumSeverity +
        "\n - Low:" + lowSeverity +
        "\nType Details: " +
        "\n - Fire:" + fireAlerts +
        "\n - Medical:" + medicalAlerts +
        "\n - Security:" + securityAlerts;
	}

}
