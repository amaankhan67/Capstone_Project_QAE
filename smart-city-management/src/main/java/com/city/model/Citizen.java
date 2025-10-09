package com.city.model;

public class Citizen {
	
	private String citizenId;
    private String name;
    private String address;
    private String contactNumber;
    private String email;
    
    public Citizen(String citizenId, String name, String address, String contactNumber, String email) {
    	
        if (name==null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Citizen Name cannot be null or empty");
        }
        if (citizenId==null || citizenId.trim().isEmpty()) {
            throw new IllegalArgumentException("Citizen ID cannot null be empty");
        }
        
        this.name = name;
        this.citizenId = citizenId;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
    }
    
    //Getter Functions
    public String getCitizenId() { return citizenId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }
    
 	//Setter Functions
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmail(String email) { this.email = email; }
    
    public String toString() {
        return "Citizen Details:" +
                "  \nID       : " + citizenId +
                "  \nName     : " + name +
                "  \nAddress  : " + address +
                "  \nContact  : " + contactNumber +
                "  \nEmail    : " + email;
    }

}
