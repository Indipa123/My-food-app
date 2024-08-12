package com.testing.foodmanagement;



public class Promotion {
    // Attributes in the desired order
    private int id;
    private String promotionName; // Moved to first position
    private String promotionCode;
    private String description;
    private String promotionStartDate;
    private String promotionEndDate;
    private double promotionDiscount;
    private byte[] image;

    // Constructor
    public Promotion(int id, String promotionName, String promotionCode, String description,
                     String promotionStartDate, String promotionEndDate, double promotionDiscount, byte[] image) {
        this.id = id;
        this.promotionName = promotionName;
        this.promotionCode = promotionCode;
        this.description = description;
        this.promotionStartDate = promotionStartDate;
        this.promotionEndDate = promotionEndDate;
        this.promotionDiscount = promotionDiscount;
        this.image = image;
    }

    // Getters and Setters in the same order as attributes
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

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(String promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public String getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(String promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public double getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(double promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
