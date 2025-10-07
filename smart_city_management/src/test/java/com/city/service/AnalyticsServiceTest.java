package com.city.service;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.city.model.*;
import com.city.repository.*;

public class AnalyticsServiceTest {

	private AnalyticsService analyticsService;
    private TrafficRepository trafficRepository;
    private UtilityRepository utilityRepository;
    private ComplaintRepository complaintRepository;
    private EmergencyRepository emergencyRepository;
    
    private TrafficManagementService trafficService;
    private UtilityMonitoringService utilityService;
    private ComplaintService complaintService;
    private EmergencyService emergencyService;

    @BeforeMethod
    public void setUp() {
        trafficRepository = new TrafficRepository();
        utilityRepository = new UtilityRepository();
        complaintRepository = new ComplaintRepository();
        emergencyRepository = new EmergencyRepository();
        
        trafficService = new TrafficManagementService(trafficRepository);
        utilityService = new UtilityMonitoringService(utilityRepository);
        
        CitizenRepository citizenRepository = new CitizenRepository();
        setupCitizens(citizenRepository);
        complaintService = new ComplaintService(complaintRepository, citizenRepository);
        
        emergencyService = new EmergencyService(emergencyRepository);
        analyticsService = new AnalyticsService(trafficRepository, utilityRepository, 
                                              complaintRepository, emergencyRepository);
        
        populateTestData();
    }
    
    private void setupCitizens(CitizenRepository citizenRepository) {
    	Citizen[] citizens = {
    			new Citizen("C001", "John Doe", "123 Main St", "555-0101", "john.doe@email.com"),
    			new Citizen("C002", "Jane Smith", "456 Oak Ave", "555-0102", "jane.smith@email.com"),
    			new Citizen("C003", "Bob Johnson", "789 Pine Rd", "555-0103", "bob.johnson@email.com"),
    			};

    	for (Citizen citizen : citizens) {
    		citizenRepository.addCitizen(citizen);
    	}
    }
    
    private void populateTestData()	{
    	TrafficSensor sensor1 = new TrafficSensor("TS001", "Downtown", 75, TrafficSensor.Status.ACTIVE);
    	TrafficSensor sensor2 = new TrafficSensor("TS002", "Uptown", 25, TrafficSensor.Status.ACTIVE);
    	TrafficSensor sensor3 = new TrafficSensor("TS003", "Suburbs", 45, TrafficSensor.Status.INACTIVE);
    	trafficService.addSensor(sensor1);
    	trafficService.addSensor(sensor2);
    	trafficService.addSensor(sensor3);

    	UtilityMeter meter1 = new UtilityMeter("UM001", "PROP001", UtilityMeter.UtilityType.ELECTRICITY, 1500.0);
    	UtilityMeter meter2 = new UtilityMeter("UM002", "PROP001", UtilityMeter.UtilityType.WATER, 800.0);
    	UtilityMeter meter3 = new UtilityMeter("UM003", "PROP002", UtilityMeter.UtilityType.GAS, 300.0);
    	utilityService.addMeter(meter1);
    	utilityService.addMeter(meter2);
    	utilityService.addMeter(meter3);
    	
    	Complaint complaint1 = complaintService.registerComplaint("C001", Complaint.Category.ROAD, "Pothole", Complaint.Priority.HIGH);
        Complaint complaint2 = complaintService.registerComplaint("C002", Complaint.Category.WATER, "Low pressure", Complaint.Priority.MEDIUM);
        Complaint complaint3 = complaintService.registerComplaint("C003", Complaint.Category.ELECTRICITY, "No Electricity", Complaint.Priority.HIGH);
        complaintService.updateComplaintStatus(complaint1.getComplaintId(), Complaint.Status.IN_PROGRESS);
        complaintService.updateComplaintStatus(complaint3.getComplaintId(), Complaint.Status.IN_PROGRESS);
        complaintService.resolveComplaint(complaint2.getComplaintId());
        
        EmergencyAlert alert1 = emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.FIRE, "Location1", EmergencyAlert.Severity.HIGH);
        EmergencyAlert alert2 = emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.MEDICAL, "Location2", EmergencyAlert.Severity.MEDIUM);
        emergencyService.dispatchEmergencyService(alert1.getAlertId());
        emergencyService.resolveEmergency(alert2.getAlertId());
    }
    
    @Test
    public void testGenerateTrafficReport() {
        String report = analyticsService.generateTrafficReport();
        
        assertNotNull(report);
        assertTrue(report.contains("TRAFFIC ANALYSIS REPORT"));
        assertTrue(report.contains("Total Sensors"));
        assertTrue(report.contains("Active Sensors"));
        assertTrue(report.contains("Average Traffic Density"));
        assertTrue(report.contains("Traffic Distribution"));
    }
    
    @Test
    public void testGenerateTrafficReportWithNoData() {
        TrafficRepository emptyRepository = new TrafficRepository();
        AnalyticsService emptyAnalytics = new AnalyticsService(emptyRepository, utilityRepository, complaintRepository, emergencyRepository);
        
        String report = emptyAnalytics.generateTrafficReport();
        
        assertNotNull(report);
        assertTrue(report.contains("No Traffic Sensors available to generate Report"));
    }
    
    @Test
    public void testGenerateUtilityConsumptionReport() {
        String report = analyticsService.generateUtilityConsumptionReport();
        
        assertNotNull(report);
        assertTrue(report.contains("UTILITY CONSUMPTION REPORT"));
        assertTrue(report.contains("Electricity Consumption"));
        assertTrue(report.contains("Water Consumption"));
        assertTrue(report.contains("Gas Consumption"));
        assertTrue(report.contains("Total"));
        assertTrue(report.contains("Average"));
        assertTrue(report.contains("Meters"));
    }
    
    @Test
    public void testGenerateUtilityConsumptionReportWithNoData() {
        UtilityRepository emptyRepository = new UtilityRepository();
        AnalyticsService emptyAnalytics = new AnalyticsService(trafficRepository, emptyRepository, complaintRepository, emergencyRepository);
        
        String report = emptyAnalytics.generateUtilityConsumptionReport();
        
        assertNotNull(report);
        assertTrue(report.contains("No Utility Meters available to generate Report"));
    }
    
    @Test
    public void testGetComplaintStatistics() {
        String statistics = analyticsService.getComplaintStatistics();
        
        assertNotNull(statistics);
        assertTrue(statistics.contains("COMPLAINT ANALYSIS REPORT"));
        assertTrue(statistics.contains("Total Complaints"));
        assertTrue(statistics.contains("Open"));
        assertTrue(statistics.contains("In Progress"));
        assertTrue(statistics.contains("Resolved"));
        assertTrue(statistics.contains("Priority Details"));
        assertTrue(statistics.contains("High"));
        assertTrue(statistics.contains("Road"));
        assertTrue(statistics.contains("Water"));
    }

    @Test
    public void testGetComplaintStatisticsWithNoData() {
        ComplaintRepository emptyRepository = new ComplaintRepository();
        AnalyticsService emptyAnalytics = new AnalyticsService(trafficRepository, utilityRepository, emptyRepository, emergencyRepository);
        
        String statistics = emptyAnalytics.getComplaintStatistics();
        
        assertNotNull(statistics);
        assertTrue(statistics.contains("No Complaints available to generate Report"));
    }
    
    @Test
    public void testGenerateEmergencyReport() {
        String report = analyticsService.generateEmergencyReport();
        
        assertNotNull(report);
        assertTrue(report.contains("EMERGENCY SERVICES ANALYSIS REPORT"));
        assertTrue(report.contains("Total Alerts"));
        assertTrue(report.contains("Alert Details"));
        assertTrue(report.contains("Severity Details"));
        assertTrue(report.contains("Fire"));
        assertTrue(report.contains("Medical"));
        assertTrue(report.contains("Security"));
    }

    @Test
    public void testGenerateEmergencyReport_NoData() {
        EmergencyRepository emptyRepository = new EmergencyRepository();
        AnalyticsService emptyAnalytics = new AnalyticsService(trafficRepository, utilityRepository, complaintRepository, emptyRepository);
        
        String report = emptyAnalytics.generateEmergencyReport();
        
        assertNotNull(report);
        assertTrue(report.contains("No Emergency Data available to generate Report"));
    }
}
