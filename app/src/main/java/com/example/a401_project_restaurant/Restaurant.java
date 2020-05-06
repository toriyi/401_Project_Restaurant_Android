package com.example.a401_project_restaurant;


import com.google.firebase.Timestamp;

public class Restaurant {

    private String description;
    private Boolean discountActive;
    private int discountAmount;
    private Timestamp timestamp;
    private String email;
    private Double latitude;
    private Double longitude;
    private String name;
    private Double rating;
    private int reservationSpots;
    private String uid;
    private String[] tags;

    public Restaurant(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountActive(Boolean discountActive) {
        this.discountActive = discountActive;
    }

    public Boolean getDiscountActive() {
        return discountActive;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int getReservationSpots() {
        return reservationSpots;
    }

    public void setReservationSpots(int reservationSpots) {
        this.reservationSpots = reservationSpots;
    }


}
