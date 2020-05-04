package com.example.a401_project_restaurant;

public class User {

    private String name;
    private String email;
    private int passwordLength;
    private String phoneNumber;
    private Boolean locationServicesEnabled;
    private String uid;

    public User(){
        this.name = "";
        this.email = "";
        this.passwordLength = 0;
        this.phoneNumber = "";
        this.locationServicesEnabled = false;
    }

    public User(String name, int passwordLength, String email, String phoneNumber, Boolean locationServicesEnabled){
        this.name = name;
        this.email = email;
        this.passwordLength = passwordLength;
        this.phoneNumber = phoneNumber;
        this.locationServicesEnabled = locationServicesEnabled;
    }

    public User(String email){
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Boolean getLocationServicesEnabled() {
        return this.locationServicesEnabled;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public String getUid() {
        return this.uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocationServicesEnabled(Boolean locationServicesEnabled) {
        this.locationServicesEnabled = locationServicesEnabled;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
