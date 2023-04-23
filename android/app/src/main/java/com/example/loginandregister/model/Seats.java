package com.example.loginandregister.model;

public class Seats {
    private String available;
    private String seat_no;

    public Seats(String available, String seat_no) {
        this.available = available;
        this.seat_no = seat_no;
    }

    public Seats() {}

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

}
