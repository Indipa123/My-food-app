package com.testing.foodmanagement;

public class Branch {
    private String branchName;
    private String phone;
    private String email;
    private String openHours;
    private String location;

    public Branch(String branchName, String phone, String email, String openHours, String location) {
        this.branchName = branchName;
        this.phone = phone;
        this.email = email;
        this.openHours = openHours;
        this.location = location;
    }

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
}
