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

    private String dateStart;
    private String dateEnd;

    private String timeStart;

    private String timeEnd;

    public RouteModel(String id, String name, String description, String from, String to, ArrayList<Seats> seats, String price, String dateStart, String dateEnd, String timeStart, String timeEnd) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.seats = seats;
        this.price = price;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String timingStart) {
        this.dateStart = timingStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
