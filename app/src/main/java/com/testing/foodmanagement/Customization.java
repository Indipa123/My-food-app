package com.testing.foodmanagement;

import android.os.Parcel;
import android.os.Parcelable;

public class Customization implements Parcelable {
    private int id;
    private String name;
    private double price;

    // Default constructor
    public Customization() {
    }

    // Parameterized constructor
    public Customization(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    // Parcelable implementation

    // Constructor used for parcel
    protected Customization(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
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
