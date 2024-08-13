package com.testing.foodmanagement;

import android.graphics.Bitmap;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Include password for complete representation, though it's not recommended to expose it
    private String phoneNo;
    private String address;
    private Bitmap profileImage;

    public User(String firstName, String lastName, String email,String phoneNo,String address,Bitmap profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.address = this.address;
        this.profileImage = profileImage;


    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}

