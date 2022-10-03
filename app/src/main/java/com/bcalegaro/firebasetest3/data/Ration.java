package com.bcalegaro.firebasetest3.data;

public class Ration {
    public String name;
    public double quantity;
    public String timestamp;

    public Ration(){}

    public Ration(String name, double quantity, String timestamp) {
        this.name = name;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }
}
