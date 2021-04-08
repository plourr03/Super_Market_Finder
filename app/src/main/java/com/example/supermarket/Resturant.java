package com.example.supermarket;

import java.util.Calendar;

public class Resturant {
    private int resturantID;
    private String resturantName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private float liquerRating;
    private float produceRating;
    private float cheeseRating;

    public Resturant() {
        resturantID = -1;

    }

    public int getResturantID() {
        return resturantID;
    }

    public float getLiquerRating() {
        return liquerRating;
    }

    public void setLiquerRating(float liquerRating) {
        this.liquerRating = liquerRating;
    }

    public float getProduceRating() {
        return produceRating;
    }

    public void setProduceRating(float produceRating) {
        this.produceRating = produceRating;
    }

    public float getCheeseRating() {
        return cheeseRating;
    }

    public void setCheeseRating(float cheeseRating) {
        this.cheeseRating = cheeseRating;
    }

    public void setResturantID(int resturantID) {
        this.resturantID = resturantID;
    }

    public String getResturantName() {
        return resturantName;
    }

    public void setResturantName(String resturantName) {
        this.resturantName = resturantName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
