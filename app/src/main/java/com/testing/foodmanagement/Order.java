package com.testing.foodmanagement;

public class Order {
    private String itemName;
    private String itemPrice;
    private String itemQuantity;
    private String branch;
    private String phone;
    private String customerLocation;

    public Order(String name, String price, String quantity, String branch, String phone, String customerLocation) {
        this.itemName = name;
        this.itemPrice = price;
        this.itemQuantity = quantity;
        this.branch = branch;
        this.phone = phone;
        this.customerLocation = customerLocation;
    }


    // Getters and setters...

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

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }
}

