package com.city.service;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Optional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.UtilityMeter;
import com.city.repository.UtilityRepository;

public class UtilityMonitoringServiceTest {
	
	private UtilityMonitoringService utilityService;
	private UtilityRepository utilityRepository;
	
	@BeforeMethod
	public void setup() {
		utilityRepository = new UtilityRepository();
		utilityService = new UtilityMonitoringService(utilityRepository);
		
		populateTestData();
	}
	
	public void populateTestData() {
		UtilityMeter[] meters = {
				new UtilityMeter("UM001", "PROP001", UtilityMeter.UtilityType.ELECTRICITY, 1250.5),
				new UtilityMeter("UM002", "PROP001", UtilityMeter.UtilityType.WATER, 850.0),
				new UtilityMeter("UM003", "PROP002", UtilityMeter.UtilityType.ELECTRICITY, 980.75),
				new UtilityMeter("UM004", "PROP002", UtilityMeter.UtilityType.GAS, 320.25),
				new UtilityMeter("UM005", "PROP003", UtilityMeter.UtilityType.ELECTRICITY, 1560.0),
				new UtilityMeter("UM006", "PROP003", UtilityMeter.UtilityType.WATER, 920.5),
				new UtilityMeter("UM007", "PROP004", UtilityMeter.UtilityType.ELECTRICITY, 780.25),
				new UtilityMeter("UM008", "PROP004", UtilityMeter.UtilityType.GAS, 410.75),
				new UtilityMeter("UM009", "PROP005", UtilityMeter.UtilityType.ELECTRICITY, 1340.0),
				new UtilityMeter("UM010", "PROP005", UtilityMeter.UtilityType.WATER, 760.0),
				new UtilityMeter("UM011", "PROP006", UtilityMeter.UtilityType.GAS, 290.5),
				new UtilityMeter("UM012", "PROP006", UtilityMeter.UtilityType.ELECTRICITY, 1120.25)
		};

		for (UtilityMeter meter : meters) {
			utilityRepository.addMeter(meter);
		}
	}
	
	@Test(dataProvider = "utilityData")
	public void testRecordUtilityConsumption(String meterId, double reading) {
		utilityService.recordUtilityConsumption(meterId, reading);
		double currentMeterReading = utilityService.getMeterReading(meterId);
		
		assertEquals(currentMeterReading,reading);
	}
	
	@DataProvider(name = "utilityData")
	public Object[][] utilityData(){
		return new Object[][] {
			{"UM002" , 1000.25},
			{"UM004" , 407.55},
			{"UM007" , 821.08},
			{"UM011" , 381.17}
		};
	}
	
	@Test(description = "Testing for Edge Cases")
    public void testRecordUtilityConsumptionWithSameReading() {
        String meterId = "UM001";
        double sameReading = 1250.5;
        utilityService.recordUtilityConsumption(meterId, sameReading);

        Optional<UtilityMeter> meter = utilityService.findMeterById(meterId);
        assertTrue(meter.isPresent());
        assertEquals(meter.get().getCurrentReading(), sameReading, 0.001);
    }
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testRecordUtilityConsumptionWithNegativeReading() {
		utilityService.recordUtilityConsumption("UM002", -1);
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testRecordUtilityConsumptionWithEmptyString() {
		utilityService.recordUtilityConsumption("", 60);
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testRecordUtilityConsumptionWithNull() {
		utilityService.recordUtilityConsumption(null, 60);
	}
	
	@Test(expectedExceptions = ServiceUnavailableException.class)
	public void testRecordUtilityConsumptionWithWrongMeter() {
		utilityService.recordUtilityConsumption("WRONG_METER", 60);
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testRecordUtilityConsumptionWithLessReading() {
		utilityService.recordUtilityConsumption("UM002", 600);
	}
	
	@Test(dataProvider = "generateBillData")
	public void testGenerateBill(String propertyID, UtilityMeter.UtilityType type) {
		String bill = utilityService.generateBill(propertyID, type);
		
		assertNotNull(bill);
		assertTrue(bill.contains(propertyID));
		assertTrue(bill.contains(type.toString()));
	}
	
	@DataProvider(name = "generateBillData")
	public Object[][] billData(){
		return new Object[][] {
			{"PROP002", UtilityMeter.UtilityType.ELECTRICITY},
			{"PROP002", UtilityMeter.UtilityType.GAS},
			{"PROP005", UtilityMeter.UtilityType.ELECTRICITY},
			{"PROP005", UtilityMeter.UtilityType.WATER}
		};
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
    public void testGenerateBillWithNullPropertyId() {
        utilityService.generateBill(null, UtilityMeter.UtilityType.ELECTRICITY);
    }
	
	@Test(expectedExceptions = InvalidInputException.class)
    public void testGenerateBillWithEmptyPropertyId() {
        utilityService.generateBill("", UtilityMeter.UtilityType.ELECTRICITY);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testGenerateBillWithNullUtilityType() {
        utilityService.generateBill("PROP001", null);
    }

    @Test(expectedExceptions = ServiceUnavailableException.class)
    public void testGenerateBillWithNonExistentProperty() {
        utilityService.generateBill("NON_EXISTENT", UtilityMeter.UtilityType.ELECTRICITY);
    }

    @Test(expectedExceptions = ServiceUnavailableException.class)
    public void testGenerateBillNoMeterForUtilityType() {
        utilityService.generateBill("PROP001", UtilityMeter.UtilityType.GAS);
    }
    
    @Test
    public void testDetectAnomalies() {
        double threshold = 50.0;
        List<UtilityMeter> anomalies = utilityService.detectAnomalies(threshold);
        assertNotNull(anomalies);
    }
    
    @Test
    public void testDetectAnomaliesWhereNoAnomalies() {
        double highThreshold = 200.0;
        List<UtilityMeter> anomalies = utilityService.detectAnomalies(highThreshold);
        assertNotNull(anomalies);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testDetectAnomaliesWithNegativeThreshold() {
    	utilityService.detectAnomalies(-10.0);
    }
    
    @Test(dataProvider = "invalidConsumptionData", expectedExceptions = Exception.class)
    public void testRecordUtilityConsumptionForInvalidData(String meterId, double reading) {
        utilityService.recordUtilityConsumption(meterId, reading);
    }
    
    @DataProvider(name = "invalidConsumptionData")
    public Object[][] provideInvalidConsumptionData() {
        return new Object[][] {
            {null, 1000.0},           
            {"", 1000.0},             
            {"UM001", -100.0},        
            {"UM001", 1000.0},        
            {"INVALID", 1000.0} 
        };
    }
}

