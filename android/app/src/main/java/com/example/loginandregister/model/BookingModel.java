package com.example.loginandregister.model;

public class BookingModel {
    private String date;
    private String from;
    private String to;
    private String bookingId;
    private String busId;

    public BookingModel(String date, String from, String to, String bookingId, String busId) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.bookingId = bookingId;
        this.busId = busId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }
}
