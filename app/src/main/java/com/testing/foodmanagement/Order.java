package com.testing.foodmanagement;

public class Order {
    private int orderId;
    private String email;
    private String itemName;
    private String itemPrice;
    private String itemQuantity;
    private String branch;
    private String phone;
    private String paymentMethod;
    private String customerLocation;

    public Order(int orderId, String email, String itemName, String itemPrice, String itemQuantity, String branch, String phone, String paymentMethod, String customerLocation) {
        this.orderId = orderId;
        this.email = email;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.branch = branch;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.customerLocation = customerLocation;
    }

    public Order(String name, String price, String quantity, String branch, String phone, String customerLocation) {
        this.itemName = name;
        this.itemPrice = price;
        this.itemQuantity = quantity;
        this.branch = branch;
        this.phone = phone;
        this.customerLocation = customerLocation;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", email='" + email + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", branch='" + branch + '\'' +
                ", phone='" + phone + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customerLocation='" + customerLocation + '\'' +
                '}';
    }
}
