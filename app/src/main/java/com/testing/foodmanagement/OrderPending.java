package com.testing.foodmanagement;

import android.database.Cursor;

public class OrderPending {
    private int orderPId;
    private String curDate;
    private String curTime;
    private String email;
    private String orderName;
    private String orderQuantity;
    private String orderPrice;
    private String branch;
    private String customerLocation;
    private String phone;
    private String status;

    public OrderPending(int orderPId, String curDate, String curTime, String email, String orderName, String orderQuantity, String orderPrice, String branch, String customerLocation, String phone, String status) {
        this.orderPId = orderPId;
        this.curDate = curDate;
        this.curTime = curTime;
        this.email = email;
        this.orderName = orderName;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
        this.branch = branch;
        this.customerLocation = customerLocation;
        this.phone = phone;
        this.status = status;
    }

    public OrderPending(Cursor cursor) {

    }

    public OrderPending(String orderName, String orderPrice, String status) {
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.status = status;


    }

    // Getters for each field
    public int getOrderPId() {
        return orderPId;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurTime() {
        return curTime;
    }

    public String getEmail() {
        return email;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getBranch() {
        return branch;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}
