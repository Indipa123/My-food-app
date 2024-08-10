package com.testing.foodmanagement;

public class Promotion {
    private int id;
    private String promotionName;
    private String description;
    private byte[] image;

    // Constructor
    public Promotion(int id, String promotionName, String description, byte[] image) {
        this.id = id;
        this.promotionName = promotionName;
        this.description = description;
        this.image = image;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
