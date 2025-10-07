package com.city.service;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Optional;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.TrafficSensor;
import com.city.repository.TrafficRepository;

public class TrafficManagementServiceTest {
	
	private TrafficRepository trafficRepository;
	private TrafficManagementService trafficService;
	
	@BeforeMethod
	public void setUp() {
		trafficRepository = new TrafficRepository();
		trafficService = new TrafficManagementService(trafficRepository);
		
		populateTestData();
	}
	
	public void populateTestData() {
		
		TrafficSensor[] sensors = {
        		new TrafficSensor("TS001", "Downtown Main St", 45, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS002", "City Center Circle", 75, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS003", "North Highway", 25, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS004", "South Bridge", 15, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS005", "East Freeway", 85, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS006", "West Expressway", 65, TrafficSensor.Status.INACTIVE),
        		new TrafficSensor("TS007", "Central Square", 35, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS008", "University Ave", 55, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS009", "Industrial Zone", 95, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS010", "Residential Area", 10, TrafficSensor.Status.ACTIVE),
        		new TrafficSensor("TS011", "Shopping District", 70, TrafficSensor.Status.INACTIVE),
        		new TrafficSensor("TS012", "Park Entrance", 5, TrafficSensor.Status.ACTIVE)
        };

        for (TrafficSensor sensor : sensors) {
        	trafficRepository.addSensor(sensor);
        }
	}
	
	@Test
	public void testUpdateTrafficDensity() {
		String sensorId = "TS005";
		int newDensity = 60;
		
		trafficService.updateTrafficDensity(sensorId, newDensity);
		Optional<TrafficSensor> updatedSensor = trafficService.findSensorById(sensorId);
		assertTrue(updatedSensor.isPresent());
		assertEquals(updatedSensor.get().getCurrentTrafficDensity(),newDensity);
	}
	
	@Test(dataProvider = "trafficDensityData")
	public void testUpdateTrafficDensityWithDataProvider(String sensorId, int density) {
		trafficService.updateTrafficDensity(sensorId, density);
		Optional<TrafficSensor> updatedSensor = trafficService.findSensorById(sensorId);
		assertTrue(updatedSensor.isPresent());
		assertEquals(updatedSensor.get().getCurrentTrafficDensity(),density);
	}
	
	@DataProvider(name = "trafficDensityData")
	public Object[][] dataSet(){
		return new Object[][]
		{{"TS003" , 40},
		{"TS008" , 80},
		{"TS010" , 20}};
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testUpdateTrafficDensityWithNull() {
		trafficService.updateTrafficDensity(null, 60);
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testUpdateTrafficDensityWithEmptyString() {
		trafficService.updateTrafficDensity("", 60);
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testUpdateTrafficDensityWithNegativeDensity() {
		trafficService.updateTrafficDensity("TS001", -1);
	}
	
	@Test(expectedExceptions = ServiceUnavailableException.class)
	public void testUpdateTrafficDensityWithNonExistingSensor() {
		trafficService.updateTrafficDensity("WRONG_SENSOR", 60);
	}
	
	@Test(expectedExceptions = ServiceUnavailableException.class)
	public void testUpdateTrafficDensityForInactiveSensor() {
		trafficService.updateTrafficDensity("TS006", 60);
	}
	
	@Test
	public void testHighTrafficAreas() {
		int threshold = 60;
		List<String> highTrafficAreas = trafficService.getHighTrafficAreas(threshold);
		
		assertNotNull(highTrafficAreas);
		assertTrue(highTrafficAreas.contains("East Freeway"));
		assertTrue(highTrafficAreas.contains("Industrial Zone"));
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testHighTrafficAreasWithNegativeThreshold() {
		trafficService.getHighTrafficAreas(-1);
	}
	
	@Test
	public void testHighTrafficAreasForInactiveSensor() {
		int threshold = 60;
		List<String> highTrafficAreas = trafficService.getHighTrafficAreas(threshold);
		
		assertFalse(highTrafficAreas.contains("West Expressway"));
	}
	
	@Test
	public void testOptimizeTrafficSignal() {
		String area = "City Center Circle";
		String optimizationStrategy = trafficService.optimizeTrafficSignals(area);
		
		assertNotNull(optimizationStrategy);
		assertEquals(optimizationStrategy,"HEAVY_CONGESTION_PROTOCOL");
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testOptimizeTrafficSignalWithNull() {
		trafficService.optimizeTrafficSignals(null);
	}
	
	@Test(expectedExceptions = InvalidInputException.class)
	public void testOptimizeTrafficSignalWithEmptyString() {
		trafficService.optimizeTrafficSignals("");
	}
	
	@Test(expectedExceptions = ServiceUnavailableException.class)
	public void testOptimizeTrafficSignalWithWrongArea() {
		trafficService.optimizeTrafficSignals("WRONG_AREA");
	}
	
	@Test(expectedExceptions = ServiceUnavailableException.class)
	public void testOptimizeTrafficSignalForInactiveArea() {
		trafficService.optimizeTrafficSignals("West Expressway");
	}
	
	
	@AfterMethod
	public void cleanUp() {
		trafficRepository = null;
		trafficService = null;
	}
}
