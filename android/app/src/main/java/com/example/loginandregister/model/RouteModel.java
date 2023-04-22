package com.example.loginandregister.model;

import java.util.ArrayList;

public class RouteModel {
    private String id;
    private String name;
    private String description;
    private String from;
    private String to;
    private ArrayList<Seats> seats;
    private String price;
    private String travellingTime;
    private String timing;

    public RouteModel(String id, String name, String description, String from, String to, ArrayList<Seats> seats, String price, String travellingTime, String timing) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.seats = seats;
        this.price = price;
        this.travellingTime = travellingTime;
        this.timing = timing;
    }

    public RouteModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ArrayList<Seats> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seats> seats) {
        this.seats = seats;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTravellingTime() {
        return travellingTime;
    }

    public void setTravellingTime(String travellingTime) {
        this.travellingTime = travellingTime;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
