package com.example.loginandregister.model;

public class Users {
    String mobileNo;
    String userId;
    String totalFare;
    String totalBookings;

    public Users(String mobileNo, String userId, String totalFare, String totalBookings) {
        this.mobileNo = mobileNo;
        this.userId = userId;
        this.totalFare = totalFare;
        this.totalBookings = totalBookings;
    }
    public Users() {}

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(String totalBookings) {
        this.totalBookings = totalBookings;
    }
}
