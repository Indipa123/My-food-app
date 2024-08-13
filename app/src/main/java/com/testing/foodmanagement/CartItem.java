package com.testing.foodmanagement;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable{
    private int id;
    private String name;
    private double price;
    private int quantity;
    private byte[] image;
    private List<Customization> customizations;

    // Constructor with all six parameters
    public CartItem(int id, String name, double price, int quantity, byte[] image, List<Customization> customizations) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.customizations = customizations;
    }

    public CartItem(int id, String name, double price, int quantity, byte[] image) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
    public CartItem(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Existing getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public List<Customization> getCustomizations() { return customizations; }
    public void setCustomizations(List<Customization> customizations) { this.customizations = customizations; }
}
