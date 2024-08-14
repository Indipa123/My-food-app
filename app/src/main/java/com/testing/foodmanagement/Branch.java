package com.testing.foodmanagement;

public class Branch {
    private String branchName;
    private String phone;
    private String email;
    private String openHours;
    private String location; // This will now store the address

    // Updated constructor to include address
    public Branch(String branchName, String phone, String email, String openHours, String location) {
        this.branchName = branchName;
        this.phone = phone;
        this.email = email;
        this.openHours = openHours;
        this.location = location;
    }

    // Getters
    public String getBranchName() {
        return branchName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getOpenHours() {
        return openHours;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
