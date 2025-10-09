package com.city.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.city.exception.DuplicateEntityException;
import com.city.exception.InvalidInputException;
import com.city.model.Complaint;

public class ComplaintRepository {
	
	private Map<String, Complaint> complaints;

    public ComplaintRepository() {
        this.complaints = new HashMap<>();
    }
    
    public void addComplaint(Complaint complaint) {
        if (complaint == null) {
            throw new InvalidInputException("Complaint cannot be null");
        }
        
        String complaintId = complaint.getComplaintId();
        if (complaints.containsKey(complaintId)) {
        	throw new DuplicateEntityException("Complaint with ID " + complaintId + " already exists");
        }
        
        complaints.put(complaintId, complaint);
    }
    
    public Optional<Complaint> findComplaintById(String complaintId) {
        if (complaintId == null || complaintId.trim().isEmpty()) {
            throw new InvalidInputException("Complaint ID cannot be null or empty");
        }
        
        return Optional.ofNullable(complaints.get(complaintId));
    }
    
    public void updateComplaint(String complaintId, Complaint updatedComplaint) {
        if (complaintId == null || complaintId.trim().isEmpty()) {
            throw new InvalidInputException("Complaint ID cannot be null or empty");
        }
        
        if (updatedComplaint == null) {
            throw new InvalidInputException("Updated complaint cannot be null");
        }
        
        if (!complaints.containsKey(complaintId)) {
            throw new InvalidInputException("Complaint with ID " + complaintId + " not found");
        }
        
        complaints.put(complaintId, updatedComplaint);
    }
    
    public boolean removeComplaint(String complaintId) {
        if (complaintId == null || complaintId.trim().isEmpty()) {
            throw new InvalidInputException("Complaint ID cannot be null or empty");
        }
        
        return complaints.remove(complaintId) != null;
    }
    
    public Map<String, Complaint> getAllComplaints() {
        return new HashMap<>(complaints);
    }
    
    public List<Complaint> getComplaintsByCitizen(String citizenId) {
        if (citizenId == null || citizenId.trim().isEmpty()) {
            throw new InvalidInputException("Citizen ID cannot be null or empty");
        }
        
        List<Complaint> citizenComplaints = new ArrayList<>();
        for (Complaint complaint : complaints.values()) {
            if (citizenId.equals(complaint.getCitizenId())) {
                citizenComplaints.add(complaint);
            }
        }
        return citizenComplaints;
    }
    
    public List<Complaint> getComplaintsByPriority(Complaint.Priority priority) {
        if (priority == null) {
            throw new InvalidInputException("Priority cannot be null");
        }
        
        List<Complaint> priorityComplaints = new ArrayList<>();
        for (Complaint complaint : complaints.values()) {
            if (priority == complaint.getPriority()) {
                priorityComplaints.add(complaint);
            }
        }
        return priorityComplaints;
    }
    
    public List<Complaint> getComplaintsByStatus(Complaint.Status status) {
        if (status == null) {
            throw new InvalidInputException("Status cannot be null");
        }
        
        List<Complaint> complaintByStatus = new ArrayList<>();
        for (Complaint complaint : complaints.values()) {
            if (status == complaint.getStatus()) {
            	complaintByStatus.add(complaint);
            }
        }
        return complaintByStatus;
    }
    
    public List<Complaint> getComplaintsByCategory(Complaint.Category category) {
        if (category == null) {
            throw new InvalidInputException("Category cannot be null");
        }
        
        List<Complaint> complaintByCategory = new ArrayList<>();
        for (Complaint complaint : complaints.values()) {
            if (category == complaint.getCategory()) {
            	complaintByCategory.add(complaint);
            }
        }
        return complaintByCategory;
    }
    
    public int getComplaintCount() {
        return complaints.size();
    }

}
