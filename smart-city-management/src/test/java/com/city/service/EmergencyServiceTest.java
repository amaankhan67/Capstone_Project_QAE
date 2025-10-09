package com.city.service;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Optional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.city.exception.EmergencyPriorityException;
import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.EmergencyAlert;
import com.city.repository.EmergencyRepository;

public class EmergencyServiceTest {
	
	private EmergencyService emergencyService;
    private EmergencyRepository emergencyRepository;

    @BeforeMethod
    public void setUp() {
        emergencyRepository = new EmergencyRepository();
        emergencyService = new EmergencyService(emergencyRepository);
       
        populateTestData();
    }
    
    public void populateTestData() {
    	Object[][] emergencyData = {
    			{EmergencyAlert.Type.FIRE, "123 Industrial Zone", EmergencyAlert.Severity.HIGH},
    			{EmergencyAlert.Type.MEDICAL, "456 Residential Area", EmergencyAlert.Severity.MEDIUM},
    			{EmergencyAlert.Type.SECURITY, "789 City Center", EmergencyAlert.Severity.LOW},
    			{EmergencyAlert.Type.FIRE, "321 Downtown Mall", EmergencyAlert.Severity.HIGH},
    			{EmergencyAlert.Type.MEDICAL, "654 Hospital Area", EmergencyAlert.Severity.MEDIUM},
    			{EmergencyAlert.Type.SECURITY, "987 Bank Street", EmergencyAlert.Severity.LOW},
    			{EmergencyAlert.Type.FIRE, "147 Factory District", EmergencyAlert.Severity.HIGH},
    			{EmergencyAlert.Type.MEDICAL, "258 Apartment Complex", EmergencyAlert.Severity.MEDIUM},
    			{EmergencyAlert.Type.SECURITY, "369 Government Building", EmergencyAlert.Severity.LOW},
    			{EmergencyAlert.Type.FIRE, "741 Shopping Center", EmergencyAlert.Severity.HIGH},
    			{EmergencyAlert.Type.MEDICAL, "852 School Zone", EmergencyAlert.Severity.MEDIUM},
    			{EmergencyAlert.Type.SECURITY, "963 Park Area", EmergencyAlert.Severity.LOW},
    			{EmergencyAlert.Type.FIRE, "159 Industrial Park", EmergencyAlert.Severity.MEDIUM},
    			{EmergencyAlert.Type.MEDICAL, "753 Medical Center", EmergencyAlert.Severity.MEDIUM},
    			{EmergencyAlert.Type.SECURITY, "246 Police Station", EmergencyAlert.Severity.LOW}
    	};

    	for (Object[] data : emergencyData) {
    		EmergencyAlert.Type type = (EmergencyAlert.Type) data[0];
    		String location = (String) data[1];
    		EmergencyAlert.Severity severity = (EmergencyAlert.Severity) data[2];
    		emergencyService.raiseEmergencyAlert(type, location, severity);
    	}
    }
    
    @Test
    public void testRaiseEmergencyAlert() {
        EmergencyAlert.Type type = EmergencyAlert.Type.FIRE;
        String location = "New Emergency Location";
        EmergencyAlert.Severity severity = EmergencyAlert.Severity.MEDIUM;
        
        EmergencyAlert alert = emergencyService.raiseEmergencyAlert(type, location, severity);
        
        assertNotNull(alert);
        assertNotNull(alert.getAlertId());
        assertEquals(alert.getType(), type);
        assertEquals(alert.getLocation(), location);
        assertEquals(alert.getSeverity(), severity);
        assertEquals(alert.getStatus(), EmergencyAlert.Status.PENDING);
        assertNotNull(alert.getTimestamp());
        
        Optional<EmergencyAlert> foundAlert = emergencyService.findAlertById(alert.getAlertId());
        assertTrue(foundAlert.isPresent());
    }
    
    @Test(dataProvider = "invalidEmergencyData", expectedExceptions = InvalidInputException.class)
    public void testRaiseEmergencyAlert_InvalidData_ThrowsException(EmergencyAlert.Type type, String location, 
    		EmergencyAlert.Severity severity) {
        emergencyService.raiseEmergencyAlert(type, location, severity);
    }
    
    @DataProvider(name = "invalidEmergencyData")
    public Object[][] provideInvalidEmergencyData() {
        return new Object[][] {
            {null, "Location", EmergencyAlert.Severity.HIGH},           
            {EmergencyAlert.Type.FIRE, null, EmergencyAlert.Severity.HIGH}, 
            {EmergencyAlert.Type.FIRE, "", EmergencyAlert.Severity.HIGH},   
            {EmergencyAlert.Type.FIRE, "Location", null}                
        };
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testRaiseEmergencyAlertWithNullType() {
        emergencyService.raiseEmergencyAlert(null, "Location", EmergencyAlert.Severity.HIGH);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRaiseEmergencyAlertWithNullLocation() {
        emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.FIRE, null, EmergencyAlert.Severity.HIGH);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRaiseEmergencyAlertWithEmptyLocation() {
        emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.FIRE, "", EmergencyAlert.Severity.HIGH);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRaiseEmergencyAlertWithNullSeverity() {
        emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.FIRE, "Location", null);
    }

    @Test(expectedExceptions = EmergencyPriorityException.class)
    public void testRaiseEmergencyAlertForHighSeverityCapacityExceeded() {
    	//Already 4 HIGH Severity Data in Repository so we will test by adding 2 more
        emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.FIRE, "Location ", EmergencyAlert.Severity.HIGH);
        emergencyService.raiseEmergencyAlert(EmergencyAlert.Type.MEDICAL, "Over Capacity Location", EmergencyAlert.Severity.HIGH);

    }
    
    @Test
    public void testDispatchEmergencyService() {
        List<EmergencyAlert> pendingAlerts = emergencyService.getEmergencyByStatus(EmergencyAlert.Status.PENDING);
        assertFalse(pendingAlerts.isEmpty());
        String alertId = pendingAlerts.get(0).getAlertId();
        
        emergencyService.dispatchEmergencyService(alertId);
        
        Optional<EmergencyAlert> updatedAlert = emergencyService.findAlertById(alertId);
        assertTrue(updatedAlert.isPresent());
        assertEquals(updatedAlert.get().getStatus(), EmergencyAlert.Status.DISPATCHED);
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testDispatchEmergencyServiceWithNullAlertId() {
       emergencyService.dispatchEmergencyService(null);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testDispatchEmergencyServiceWithEmptyAlertId() {
        emergencyService.dispatchEmergencyService("");
    }

    @Test(expectedExceptions = ServiceUnavailableException.class)
    public void testDispatchEmergencyServiceForNonExistentAlert() {
        emergencyService.dispatchEmergencyService("NON_EXISTENT");
    }

    @Test(expectedExceptions = EmergencyPriorityException.class)
    public void testDispatchEmergencyServiceForAlreadyResolvedAlert() {
    	//Resolving First 3 Emergencies
    	emergencyService.resolveEmergency("FIR1");
    	emergencyService.resolveEmergency("MED2");
    	emergencyService.resolveEmergency("SEC2");
    	
        List<EmergencyAlert> resolvedAlerts = emergencyService.getEmergencyByStatus(EmergencyAlert.Status.RESOLVED);
        assertFalse(resolvedAlerts.isEmpty());
        String resolvedAlertId = resolvedAlerts.get(0).getAlertId();
        
        emergencyService.dispatchEmergencyService(resolvedAlertId);
    }
    
    @Test
    public void testGetActiveEmergencies() {
        List<EmergencyAlert> activeEmergencies = emergencyService.getActiveEmergencies();
        
        assertNotNull(activeEmergencies);
        for (EmergencyAlert alert : activeEmergencies) {
            assertTrue(alert.isActive());
        }
    }
    
    @Test
    public void testGetEmergenciesByStatus() {
        List<EmergencyAlert> fireEmergencies = emergencyService.getEmergencyByStatus(EmergencyAlert.Status.PENDING);
        
        assertNotNull(fireEmergencies);
        for (EmergencyAlert alert : fireEmergencies) {
            assertEquals(alert.getStatus(), EmergencyAlert.Status.PENDING);
        }
    }

}