package com.example.loginandregister.model;

public class Seats {
    private String available;
    private String real_seat;
    private String seat_no;
    private String type;

    public Seats(String available, String real_seat, String seat_no, String type) {
        this.available = available;
        this.real_seat = real_seat;
        this.seat_no = seat_no;
        this.type = type;
    }

    public Seats() {}

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getReal_seat() {
        return real_seat;
    }

    public void setReal_seat(String real_seat) {
        this.real_seat = real_seat;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
