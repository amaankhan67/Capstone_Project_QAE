package com.city.service;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Optional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.Citizen;
import com.city.model.Complaint;
import com.city.repository.CitizenRepository;
import com.city.repository.ComplaintRepository;

public class ComplaintServiceTest {
	
	private ComplaintService complaintService;
    private ComplaintRepository complaintRepository;
    private CitizenRepository citizenRepository;
    
    @BeforeMethod
    public void setUp() {
        citizenRepository = new CitizenRepository();
        complaintRepository = new ComplaintRepository();
        complaintService = new ComplaintService(complaintRepository, citizenRepository);
        
        populateTestData();
    }
    
    public void populateTestData() {
    	Citizen[] citizens = {
    			new Citizen("C001", "John Doe", "123 Main St", "555-0101", "john.doe@email.com"),
    			new Citizen("C002", "Jane Smith", "456 Oak Ave", "555-0102", "jane.smith@email.com"),
    			new Citizen("C003", "Bob Johnson", "789 Pine Rd", "555-0103", "bob.johnson@email.com"),
    			new Citizen("C004", "Alice Brown", "321 Elm St", "555-0104", "alice.brown@email.com"),
    			new Citizen("C005", "Charlie Wilson", "654 Maple Dr", "555-0105", "charlie.wilson@email.com"),
    			new Citizen("C006", "Diana Lee", "987 Cedar Ln", "555-0106", "diana.lee@email.com"),
    			new Citizen("C007", "Edward Davis", "147 Birch Ave", "555-0107", "edward.davis@email.com"),
    			new Citizen("C008", "Fiona Miller", "258 Walnut St", "555-0108", "fiona.miller@email.com"),
    			new Citizen("C009", "George Taylor", "369 Spruce Rd", "555-0109", "george.taylor@email.com"),
    			new Citizen("C010", "Helen Clark", "741 Oakwood Dr", "555-0110", "helen.clark@email.com"),
    			new Citizen("C011", "Ivan Martinez", "852 Riverside", "555-0111", "ivan.martinez@email.com"),
    			new Citizen("C012", "Julia Anderson", "963 Hilltop Rd", "555-0112", "julia.anderson@email.com")
    	};

    	for (Citizen citizen : citizens) {
    		citizenRepository.addCitizen(citizen);
    	}

    	Object[][] complaintData = {
    			{"C001", Complaint.Category.ROAD, "Large pothole on Main Street", Complaint.Priority.HIGH},
    			{"C002", Complaint.Category.WATER, "Low water pressure in morning", Complaint.Priority.MEDIUM},
    			{"C003", Complaint.Category.ELECTRICITY, "Street light not working for 3 days", Complaint.Priority.LOW},
    			{"C004", Complaint.Category.ROAD, "Broken traffic signal at intersection", Complaint.Priority.HIGH},
    			{"C005", Complaint.Category.WATER, "Water leakage from main pipe", Complaint.Priority.MEDIUM},
    			{"C006", Complaint.Category.ELECTRICITY, "Power outage in residential area", Complaint.Priority.LOW},
    			{"C007", Complaint.Category.ROAD, "Damaged road after construction", Complaint.Priority.HIGH},
    			{"C008", Complaint.Category.WATER, "Discolored water supply", Complaint.Priority.MEDIUM},
    			{"C009", Complaint.Category.ELECTRICITY, "Flickering lights in building", Complaint.Priority.LOW},
    			{"C010", Complaint.Category.ROAD, "Cracked pavement causing accidents", Complaint.Priority.HIGH},
    			{"C011", Complaint.Category.WATER, "No water supply since yesterday", Complaint.Priority.MEDIUM},
    			{"C012", Complaint.Category.ELECTRICITY, "Electrical wiring exposed on pole", Complaint.Priority.LOW}
    	};

    	for (Object[] data : complaintData) {
    		String citizenId = (String) data[0];
    		Complaint.Category category = (Complaint.Category) data[1];
    		String description = (String) data[2];
    		Complaint.Priority priority = (Complaint.Priority) data[3];
    		complaintService.registerComplaint(citizenId, category, description, priority);  
    	}
    }
    
    @Test(dataProvider = "registerComplaintData")
    public void testRegisterComplaint(String citizenId, Complaint.Category category, 
    		String description, Complaint.Priority priority) {

        Complaint complaint = complaintService.registerComplaint(citizenId, category, description, priority);

        assertNotNull(complaint);
        assertNotNull(complaint.getComplaintId());
        assertEquals(complaint.getCitizenId(), citizenId);
        assertEquals(complaint.getCategory(), category);
        assertEquals(complaint.getDescription(), description);
        assertEquals(complaint.getPriority(), priority);
        assertEquals(complaint.getStatus(), Complaint.Status.OPEN);
        assertNotNull(complaint.getCreatedAt());
        assertNotNull(complaint.getUpdatedAt());
    }
    
    @DataProvider(name = "registerComplaintData")
    public Object[][] complaintData(){
    	return new Object[][] {
    		{"C009", Complaint.Category.ELECTRICITY, "Flickering lights in building", Complaint.Priority.LOW},
			{"C010", Complaint.Category.ROAD, "Cracked pavement causing accidents", Complaint.Priority.HIGH},
			{"C011", Complaint.Category.WATER, "No water supply since yesterday", Complaint.Priority.MEDIUM},
			{"C012", Complaint.Category.ELECTRICITY, "Electrical wiring exposed on pole", Complaint.Priority.LOW}
    	};
    }
    
    @Test(dataProvider = "invalidComplaintData", expectedExceptions = {InvalidInputException.class, ServiceUnavailableException.class})
    public void testRegisterComplaintWithInvalidData(String citizenId, Complaint.Category category, 
    		String description, Complaint.Priority priority) {
    	complaintService.registerComplaint(citizenId, category, description, priority);
    }
    
    @DataProvider(name = "invalidComplaintData")
    public Object[][] provideInvalidComplaintData() {
    	return new Object[][] {
    		{null, Complaint.Category.ROAD, "Description", Complaint.Priority.HIGH},      
    		{"", Complaint.Category.ROAD, "Description", Complaint.Priority.HIGH},        
    		{"C001", null, "Description", Complaint.Priority.HIGH},                       
    		{"C001", Complaint.Category.ROAD, null, Complaint.Priority.HIGH},           
    		{"C001", Complaint.Category.ROAD, "", Complaint.Priority.HIGH},             
    		{"C001", Complaint.Category.ROAD, "Description", null},
    		{"NON_EXISTENT", Complaint.Category.ROAD, "Description", Complaint.Priority.HIGH}
    	};
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRegisterComplaintWithNullCitizenId() {
        complaintService.registerComplaint(null, Complaint.Category.ROAD, "Description", Complaint.Priority.MEDIUM);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRegisterComplaintWithEmptyCitizenId() {
        complaintService.registerComplaint("", Complaint.Category.ROAD, "Description", Complaint.Priority.MEDIUM);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRegisterComplaintWithNullCategory() {
        complaintService.registerComplaint("C001", null, "Description", Complaint.Priority.MEDIUM);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRegisterComplaintWithNullDescription() {
        complaintService.registerComplaint("C001", Complaint.Category.ROAD, null, Complaint.Priority.MEDIUM);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRegisterComplaintWithEmptyDescription() {
        complaintService.registerComplaint("C001", Complaint.Category.ROAD, "", Complaint.Priority.MEDIUM);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testRegisterComplaintWithNullPriority() {
        complaintService.registerComplaint("C001", Complaint.Category.ROAD, "Description", null);
    }

    @Test(expectedExceptions = ServiceUnavailableException.class)
    public void testRegisterComplaintWithNonExistentCitizen() {
        complaintService.registerComplaint("NON_EXISTENT", Complaint.Category.ROAD, "Description", Complaint.Priority.MEDIUM);
    }
    
    @Test(dataProvider = "Iteration")
    public void testUpdateComplaintStatus(int i) {
    	List<Complaint> openComplaints = complaintService.getComplaintByStatus(Complaint.Status.OPEN);
    	String complaintId = openComplaints.get(i).getComplaintId();
    	complaintService.updateComplaintStatus(complaintId, Complaint.Status.IN_PROGRESS);

    	Optional<Complaint> complaintOpt = complaintService.findComplaintById(complaintId);
    	assertTrue(complaintOpt.isPresent());
    	assertEquals(complaintOpt.get().getStatus(), Complaint.Status.IN_PROGRESS);
    }
    
    @DataProvider(name = "Iteration")	//Data Provider used to run testUpdateComplaintStatus 10 times
    public Object[][] noOfIteration(){
    	Object[][] iteration= new Object[10][1];
    	for(int i = 0; i < 10; i++) {
    		iteration[i][0] = i;
    	}
    	return iteration;
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testUpdateComplaintStatus_NullComplaintId_ThrowsException() {
        // When & Then
        complaintService.updateComplaintStatus(null, Complaint.Status.IN_PROGRESS);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testUpdateComplaintStatusWithEmptyComplaintId() {
    	complaintService.updateComplaintStatus("", Complaint.Status.IN_PROGRESS);
    }

    @Test(expectedExceptions = InvalidInputException.class)
    public void testUpdateComplaintStatusWithNullStatus() {
        complaintService.updateComplaintStatus("COMP001", null);
    }

    @Test(expectedExceptions = ServiceUnavailableException.class)
    public void testUpdateComplaintStatusWithNonExistentComplaint() {
        complaintService.updateComplaintStatus("NON_EXISTENT", Complaint.Status.IN_PROGRESS);
    }
    
    @Test
    public void testGetComplaintsByPriority() {
        List<Complaint> highPriority = complaintService.getComplaintsByPriority(Complaint.Priority.HIGH);
        
        assertNotNull(highPriority);
        for (Complaint complaint : highPriority) {
            assertEquals(complaint.getPriority(), Complaint.Priority.HIGH);
        }
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testGetComplaintsByPriorityWithNullPriority() {
        complaintService.getComplaintsByPriority(null);
    }
    
    @Test
    public void testComplaintIdAutoGeneration() {
        Complaint complaint1 = complaintService.registerComplaint("C001", Complaint.Category.ROAD, "Test 1", Complaint.Priority.HIGH);
        Complaint complaint2 = complaintService.registerComplaint("C002", Complaint.Category.WATER, "Test 2", Complaint.Priority.MEDIUM);
        Complaint complaint3 = complaintService.registerComplaint("C003", Complaint.Category.ELECTRICITY, "Test 3", Complaint.Priority.LOW);
        
        assertNotNull(complaint1.getComplaintId());
        assertNotNull(complaint2.getComplaintId());
        assertNotNull(complaint3.getComplaintId());
        
        assertTrue(complaint1.getComplaintId().startsWith("RD"));
        assertTrue(complaint2.getComplaintId().startsWith("WT"));
        assertTrue(complaint3.getComplaintId().startsWith("EL"));
    }
}
