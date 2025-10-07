package com.city.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.city.exception.DuplicateEntityException;
import com.city.exception.InvalidInputException;
import com.city.model.Citizen;

public class CitizenRepository {
	
	private Map<String, Citizen> citizens;
	
	public CitizenRepository() {
        this.citizens = new HashMap<>();
    }
	
	public void addCitizen(Citizen citizen) {
		if(citizen == null) {
			throw new InvalidInputException("Citizen cannot be null");
		}

		String citizenId = citizen.getCitizenId();
		if (citizens.containsKey(citizenId)) {
			throw new DuplicateEntityException("Citizen with ID " + citizenId + " already exists");
		}

		citizens.put(citizenId, citizen);
	}
	
	public Optional<Citizen> findCitizenById(String citizenId) {
        if (citizenId == null || citizenId.trim().isEmpty()) {
            throw new InvalidInputException("Citizen ID cannot be null or empty");
        }
        
        return Optional.ofNullable(citizens.get(citizenId));
    }
	
	public void updateCitizen(String citizenId, Citizen updatedCitizen) {
        if (citizenId == null || citizenId.trim().isEmpty()) {
            throw new InvalidInputException("Citizen ID cannot be null or empty");
        }
        
        if (updatedCitizen == null) {
            throw new InvalidInputException("Updated citizen cannot be null");
        }
        
        if (!citizens.containsKey(citizenId)) {
            throw new InvalidInputException("Citizen with ID " + citizenId + " not found");
        }
        
        citizens.put(citizenId, updatedCitizen);
    }
	
	public boolean removeCitizen(String citizenId) {
        if (citizenId == null || citizenId.trim().isEmpty()) {
            throw new InvalidInputException("Citizen ID cannot be null or empty");
        }
        
        return citizens.remove(citizenId) != null;
    }
	
	public Map<String, Citizen> getAllCitizens() {
        return new HashMap<>(citizens);
    }
	
	public boolean citizenExists(String citizenId) {
        return citizens.containsKey(citizenId);
    }
	
	public int getCitizenCount() {
        return citizens.size();
    }
}
