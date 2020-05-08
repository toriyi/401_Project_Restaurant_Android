package com.example.a401_project_restaurant;

import android.widget.SeekBar;

public class UserInfo {

    public String name;
    public String email;
    public String phoneno;
    public SeekBar distance;
    public boolean location;

    public UserInfo(){
    }

    public UserInfo(String email, Boolean location, SeekBar distance, String name, String phoneno){
        this.email = email;
        this.location = location;
        this.distance = distance;
        this.name = name;
        this.phoneno = phoneno;
    }
    public String getUserName() {
        return name;
    }
    public String getUserEmail() {
        return email;
    }
    public String getUserPhoneno() {
        return phoneno;
    }
    public boolean getLocation() {
        return location;
    }
    public SeekBar getDistance() {
        return distance;
    }
}