package com.bcalegaro.firebasetest3.data;

public class Ration {
    public String name;
    public double quantity;
    public String timestamp;

    //necessário para o firebase
    public Ration(){}

    public Ration(String name, double quantity, String timestamp) {
        this.name = name;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getQuantity() {
        return quantity;
    }
}
