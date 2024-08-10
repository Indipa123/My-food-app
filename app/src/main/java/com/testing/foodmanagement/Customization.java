package com.testing.foodmanagement;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Customization implements Parcelable, Serializable {
    private int id;
    private String name;
    private double price;
    private boolean isSelected;

    // Default constructor
    public Customization() {
        this.isSelected = false; // Default to not selected
    }

    // Parameterized constructor
    public Customization(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isSelected = false; // Default to not selected
    }

    // Getters and Setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Parcelable implementation

    // Constructor used for parcel
    protected Customization(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        isSelected = in.readByte() != 0; // Read boolean as byte
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeByte((byte) (isSelected ? 1 : 0)); // Write boolean as byte
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Customization> CREATOR = new Creator<Customization>() {
        @Override
        public Customization createFromParcel(Parcel in) {
            return new Customization(in);
        }

        @Override
        public Customization[] newArray(int size) {
            return new Customization[size];
        }
    };
}
