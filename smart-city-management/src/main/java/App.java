import java.util.List;
import java.util.Optional;

import com.city.model.*;
import com.city.repository.*;
import com.city.service.*;

public class App {
	
	private static CitizenRepository citizenRepository;
	private static TrafficRepository trafficRepository;
	private static UtilityRepository utilityRepository;
	private static ComplaintRepository complaintRepository;
	private static EmergencyRepository emergencyRepository;

	private static TrafficManagementService trafficService;
	private static UtilityMonitoringService utilityService;
	private static ComplaintService complaintService;
	private static EmergencyService emergencyService;
	private static AnalyticsService analyticsService;

	public static void main(String[] args) {

		System.out.println("Starting Smart City Management System...\n");
		try {
			initializeServices();
			populateData();
			demonstrateAllServices();
		} catch (Exception e) {
			System.out.println("Error in application: " + e.getMessage());
		}
	}

	private static void initializeServices() {
		System.out.println("=== INITIALIZING SERVICES ===");

		citizenRepository = new CitizenRepository();
		trafficRepository = new TrafficRepository();
		utilityRepository = new UtilityRepository();
		complaintRepository = new ComplaintRepository();
		emergencyRepository = new EmergencyRepository();

		trafficService = new TrafficManagementService(trafficRepository);
		utilityService = new UtilityMonitoringService(utilityRepository);
		complaintService = new ComplaintService(complaintRepository, citizenRepository);
		emergencyService = new EmergencyService(emergencyRepository);
		analyticsService = new AnalyticsService(trafficRepository, utilityRepository, 
				complaintRepository, emergencyRepository);

		System.out.println("All services initialized successfully\n");
	}

	private static void populateData() {
		System.out.println("=== POPULATING DATA ===");
		addCitizens();
		addTrafficSensors();
		addUtilityMeters();
		addComplaints();
		addEmergencyAlerts();
		System.out.println("Data Populated Successfully\n");
	}

	private static void addCitizens() {
		Citizen[] citizens = {
				new Citizen("C001", "Alice Johnson", "123 Main Street", "555-0101", "alice.johnson@email.com"),
				new Citizen("C002", "Bob Smith", "456 Oak Avenue", "555-0102", "bob.smith@email.com"),
				new Citizen("C003", "Carol Davis", "789 Pine Road", "555-0103", "carol.davis@email.com"),
				new Citizen("C004", "David Wilson", "321 Elm Street", "555-0104", "david.wilson@email.com"),
				new Citizen("C005", "Eva Brown", "654 Maple Drive", "555-0105", "eva.brown@email.com")
		};

		for (Citizen citizen : citizens) {
			citizenRepository.addCitizen(citizen);
		}
		System.out.println("Added " + citizens.length + " Citizens");
	}

	private static void addTrafficSensors() {
		TrafficSensor[] sensors = {
				new TrafficSensor("TS001", "Downtown Main St", 65, TrafficSensor.Status.ACTIVE),
				new TrafficSensor("TS002", "City Center Circle", 80, TrafficSensor.Status.ACTIVE),
				new TrafficSensor("TS003", "North Highway", 25, TrafficSensor.Status.ACTIVE),
				new TrafficSensor("TS004", "South Bridge", 45, TrafficSensor.Status.ACTIVE),
				new TrafficSensor("TS005", "East Freeway", 90, TrafficSensor.Status.ACTIVE),
				new TrafficSensor("TS006", "West Expressway", 70, TrafficSensor.Status.INACTIVE)
		};

		for (TrafficSensor sensor : sensors) {
			trafficService.addSensor(sensor);
		}
		System.out.println("Added " + sensors.length + " Traffic Sensors");
	}

	private static void addUtilityMeters() {
		UtilityMeter[] meters = {
				new UtilityMeter("UM001", "PROP001", UtilityMeter.UtilityType.ELECTRICITY, 1250.50),
				new UtilityMeter("UM002", "PROP001", UtilityMeter.UtilityType.WATER, 850.25),
				new UtilityMeter("UM003", "PROP002", UtilityMeter.UtilityType.ELECTRICITY, 980.75),
				new UtilityMeter("UM004", "PROP002", UtilityMeter.UtilityType.GAS, 320.40),
				new UtilityMeter("UM005", "PROP003", UtilityMeter.UtilityType.ELECTRICITY, 1560.00),
				new UtilityMeter("UM006", "PROP003", UtilityMeter.UtilityType.WATER, 920.60)
		};

		for (UtilityMeter meter : meters) {
			utilityService.addMeter(meter);
		}
		System.out.println("Added " + meters.length + " Utility Meters");
	}

	private static void addComplaints() {
		Complaint complaint1 = complaintService.registerComplaint(
				"C001", Complaint.Category.ROAD, "Large pothole on Main Street causing traffic", Complaint.Priority.HIGH);

		Complaint complaint2 = complaintService.registerComplaint(
				"C002", Complaint.Category.WATER, "Low water pressure in the morning", Complaint.Priority.MEDIUM);

		complaintService.registerComplaint(
				"C003", Complaint.Category.ELECTRICITY, "Street light not working for 2 days", Complaint.Priority.HIGH);
		complaintService.updateComplaintStatus(complaint1.getComplaintId(), Complaint.Status.IN_PROGRESS);
		complaintService.resolveComplaint(complaint2.getComplaintId());

		System.out.println("Added 3 Complaints with different Status");

	}

	private static void addEmergencyAlerts() {
		EmergencyAlert alert1 = emergencyService.raiseEmergencyAlert(
				EmergencyAlert.Type.FIRE, "123 Industrial Zone", EmergencyAlert.Severity.HIGH);

		emergencyService.raiseEmergencyAlert(
				EmergencyAlert.Type.MEDICAL, "456 Residential Area", EmergencyAlert.Severity.MEDIUM);

		EmergencyAlert alert3 = emergencyService.raiseEmergencyAlert(
				EmergencyAlert.Type.SECURITY, "789 City Center", EmergencyAlert.Severity.LOW);

		emergencyService.dispatchEmergencyService(alert1.getAlertId());
		emergencyService.resolveEmergency(alert3.getAlertId());

		System.out.println("Added 3 Emergency Alerts with different Status");

	} 
	
	private static void demonstrateAllServices() {
        System.out.println("\n=== DEMONSTRATING ALL SERVICES ===");
        
        demonstrateTrafficServices();
        demonstrateUtilityServices();
        demonstrateComplaintServices();
        demonstrateEmergencyServices();
    }
	
	private static void demonstrateTrafficServices() {
        System.out.println("\nTRAFFIC MANAGEMENT DEMONSTRATION");
        System.out.println("====================================");
        
        try {
            //Update Traffic Density
            System.out.println("1. Update Traffic Density");
            trafficService.updateTrafficDensity("TS001", 85);
            System.out.println("Updated TS001 density to 85");
            
            //Get High Traffic Areas
            System.out.println("2. Analyzing High Traffic Areas");
            List<String> highTrafficAreas = trafficService.getHighTrafficAreas(70);
            System.out.println("High traffic areas (>70): " + highTrafficAreas.size() + " locations");
            System.out.println("Locations: " + highTrafficAreas.toString());
            
            //Optimize Traffic Signals
            System.out.println("3. Optimize Traffic Signals");
            String optimization = trafficService.optimizeTrafficSignals("Downtown Main St");
            System.out.println(optimization);
            
            //Get Traffic Analysis Report
            String summary = analyticsService.generateTrafficReport();
            System.out.println(summary);
            
        } catch (Exception e) {
            System.err.println("Traffic service error: " + e.getMessage());
        }
    }

    private static void demonstrateUtilityServices() {
        System.out.println("\nUTILITY MONITORING DEMONSTRATION");
        System.out.println("===================================");
        
        try {
            //Record Consumption
            System.out.println("1. Record Consumption");
            utilityService.recordUtilityConsumption("UM001", 1350.75);
            System.out.println("Updated UM001 reading to 1350.75");
            
            //Generate Bills
            System.out.println("2. Generate Bills");
            String electricityBill = utilityService.generateBill("PROP001", UtilityMeter.UtilityType.ELECTRICITY);
            String waterBill = utilityService.generateBill("PROP001", UtilityMeter.UtilityType.WATER);
            
            System.out.println(electricityBill );
            System.out.println(waterBill);
            
            //Detect Anomalies
            System.out.println("3. Detect Anomalies");
            List<UtilityMeter> anomalies = utilityService.detectAnomalies(50.0);
            System.out.println("Found " + anomalies.size() + " Consumption Anomalies");
            
            //Get Consumption Analysis Report
            String summary = analyticsService.generateUtilityConsumptionReport();
            System.out.println(summary);
            
        } catch (Exception e) {
            System.err.println("Utility service error: " + e.getMessage());
        }
    }

    private static void demonstrateComplaintServices() {
    	System.out.println("\nCOMPLAINT MANAGEMENT DEMONSTRATION");
    	System.out.println("=====================================");

    	try {
    		//Register New Complaint
    		System.out.println("1. Register New Complaint");
    		Complaint newComplaint = complaintService.registerComplaint(
    				"C004", Complaint.Category.ROAD, "New pothole appeared after rain", Complaint.Priority.HIGH);
    		System.out.println("Registered Complaint: " + newComplaint.getComplaintId());

    		//Get Complaints by Different Criteria
    		System.out.println("2. Get Complaints by Different Criteria");
    		List<Complaint> highPriority = complaintService.getComplaintsByPriority(Complaint.Priority.HIGH);
    		List<Complaint> openComplaints = complaintService.getComplaintByStatus(Complaint.Status.OPEN);
    		List<Complaint> roadComplaints = complaintService.getComplaintByCategory(Complaint.Category.ROAD);

    		System.out.println("High Priority Complaints: " + highPriority.size());
    		System.out.println("Open Complaints: " + openComplaints.size());
    		System.out.println("Road Complaints: " + roadComplaints.size());

    		//Update Complaint Status
    		System.out.println("3. Update Complaint Status");
    		if (!openComplaints.isEmpty()) {
    			Complaint complaintToUpdate = openComplaints.get(0);
    			complaintService.updateComplaintStatus(complaintToUpdate.getComplaintId(), Complaint.Status.IN_PROGRESS);
    			System.out.println("Marked " + complaintToUpdate.getComplaintId() + " as IN_PROGRESS");
    		}

    		//Get Complaint Analysis Report
    		String summary = analyticsService.getComplaintStatistics();
    		System.out.println(summary);

    	} catch (Exception e) {
    		System.err.println("Complaint service error: " + e.getMessage());
    	}
    }

    private static void demonstrateEmergencyServices() {
    	System.out.println("\n🚨 EMERGENCY SERVICES DEMONSTRATION");
    	System.out.println("===================================");

    	try {
    		//Raise New Emergency
    		System.out.println("1. Raise New Emergency");
    		EmergencyAlert newEmergency = emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.MEDICAL, 
    				"123 Emergency Lane", EmergencyAlert.Severity.HIGH);
    		System.out.println("Raised Emergency: " + newEmergency.getAlertId());

    		//Dispatch Emergency Services
    		System.out.println("2. Dispatch Emergency Services");
    		List<EmergencyAlert> activeEmergencies = emergencyService.getActiveEmergencies();
    		if (!activeEmergencies.isEmpty()) {
    			EmergencyAlert nextEmergency = activeEmergencies.get(0);
    			emergencyService.dispatchEmergencyService(nextEmergency.getAlertId());
    			System.out.println("Dispatched: " + nextEmergency.getAlertId());
    		}

    		//Get Next Priority Emergency
    		System.out.println("3. Get Next Priority Emergency");
    		Optional<EmergencyAlert> nextEmergency = emergencyService.getNextEmergency();
    		if (nextEmergency.isPresent()) {
    			System.out.println("Next Emergency: " + nextEmergency.get().getAlertId() + 
    					" (" + nextEmergency.get().getSeverity() + " Severity)");
    		}

    		//Emergency Analysis Report
    		String summary = analyticsService.generateEmergencyReport();
    		System.out.println(summary);

    	} catch (Exception e) {
    		System.err.println("Emergency service error: " + e.getMessage());
    	}
    }
}
