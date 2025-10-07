package com.city.service;

import java.util.List;
import java.util.Optional;

import com.city.exception.InvalidInputException;
import com.city.exception.ServiceUnavailableException;
import com.city.model.Citizen;
import com.city.model.Complaint;
import com.city.repository.CitizenRepository;
import com.city.repository.ComplaintRepository;

public class ComplaintService {
	
	private ComplaintRepository complaintRepository;
	private  CitizenRepository citizenRepository;
	
	public ComplaintService(ComplaintRepository complaintRepository, CitizenRepository citizenRepository) {
		if(complaintRepository == null) {
			throw new InvalidInputException("Complaint Repository cannot be null");
		}
		if(citizenRepository == null) {
			throw new InvalidInputException("Citizen Repository cannot be null");
		}
		
		this.complaintRepository = complaintRepository;
		this.citizenRepository = citizenRepository;
	}
	
	public Complaint registerComplaint(String citizenId, Complaint.Category category, 
			String description, Complaint.Priority priority) {
		
		if(citizenId == null || citizenId.trim().isEmpty()) {
			throw new InvalidInputException("Citizen ID cannot be null or empty");
		}
		if(category == null) {
			throw new InvalidInputException("Complaint Category cannot be null");
		}
		if(description == null || description.trim().isEmpty()) {
			throw new InvalidInputException("Complaint Description cannot be null or empty");
		}
		if(priority == null) {
			throw new InvalidInputException("Complaint Priority cannot be null");
		}
		
		Optional<Citizen> citizenOpt = citizenRepository.findCitizenById(citizenId);
		if (!citizenOpt.isPresent()) {
            throw new ServiceUnavailableException("Citizen with ID " + citizenId + " not found");
        }
		
		String complaintId = generateComplaintId(category);
		Complaint complaint = new Complaint(complaintId, citizenId, category, description, priority);
		complaintRepository.addComplaint(complaint);
		
		return complaint;
	}
	
	public void updateComplaintStatus(String complaintId, Complaint.Status status) {
		
		if(complaintId == null || complaintId.trim().isEmpty()) {
			throw new InvalidInputException("Complaint ID cannot be null or empty");
		}
		if(status == null) {
			throw new InvalidInputException("Status cannot be null");
		}
		
		Optional<Complaint> complaintOpt = complaintRepository.findComplaintById(complaintId);
		if(!complaintOpt.isPresent()) {
			 throw new ServiceUnavailableException("Complaint with ID " + complaintId + " not found");
		}
		
		Complaint complaint = complaintOpt.get();
		complaint.setStatus(status);
		complaintRepository.updateComplaint(complaintId, complaint);
	}
	
	private String generateComplaintId(Complaint.Category category) {
        String prefix;
        switch (category) {
            case ROAD: prefix = "RD"; break;
            case WATER: prefix = "WT"; break;
            case ELECTRICITY: prefix = "EL"; break;
            default: prefix = "CM";
        }
        
        int count = complaintRepository.getComplaintCount() + 1;
        return prefix + count;
    }
	
	public List<Complaint> getComplaintsByPriority(Complaint.Priority priority){
        return complaintRepository.getComplaintsByPriority(priority);
	}
	
	public List<Complaint> getComplaintByStatus(Complaint.Status status){
		return complaintRepository.getComplaintsByStatus(status);
	}
	
	public List<Complaint> getComplaintByCategory(Complaint.Category category){
		return complaintRepository.getComplaintsByCategory(category);
	}
	
	public Optional<Complaint> findComplaintById(String complaintId){
		return complaintRepository.findComplaintById(complaintId);
	}
	
	public void resolveComplaint(String complaintId) {
		updateComplaintStatus(complaintId, Complaint.Status.RESOLVED);
	}

}
