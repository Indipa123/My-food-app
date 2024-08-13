package com.testing.foodmanagement;

import java.io.Serializable;
import java.util.List;

public class FoodItem implements Serializable {
    private int id;
    private String name;
    private String category;
    private String description;
    private double price;

    private boolean available;
    private byte[] image;
    private List<Customization> selectedCustomizations; // List to store selected customizations

    // Constructor
    public FoodItem(int id, String name, String category, String description, double price, boolean available, byte[] image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;

        this.available = available;
        this.image = image;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }





    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Customization> getSelectedCustomizations() {
        return selectedCustomizations;
    }

    public void setSelectedCustomizations(List<Customization> selectedCustomizations) {
        this.selectedCustomizations = selectedCustomizations;
    }

    // Method to calculate the total price of the food item including customizations
    public double getTotalPrice() {
        double totalPrice = price;
        if (selectedCustomizations != null) {
            for (Customization customization : selectedCustomizations) {
                totalPrice += customization.getPrice();
            }
        }
        return totalPrice;
    }

    // Method to generate a summary string for the food item including customizations
    public String getCustomizationSummary() {
        StringBuilder summary = new StringBuilder(name);
        if (selectedCustomizations != null && !selectedCustomizations.isEmpty()) {
            summary.append(" with ");
            for (int i = 0; i < selectedCustomizations.size(); i++) {
                summary.append(selectedCustomizations.get(i).getName());
                if (i < selectedCustomizations.size() - 1) {
                    summary.append(", ");
                }
            }
        }
        return summary.toString();
    }
}
