package com.activity.productinventory.Model;

import java.io.Serializable;

public class Product implements Serializable {
    String name;
    double price;
    String unit;
    String image;
    String date;
    int inventory;
    double inventoryPrice;

    public Product(String name, String image,String unit, double price, String date, int inventory, double inventoryPrice) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.unit = unit;
        this.date = date;
        this.inventory = inventory;
        this.inventoryPrice = inventoryPrice;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public double getInventoryPrice() {
        return inventoryPrice;
    }

    public void setInventoryPrice(double inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }
}
