package com.mohammed.babelrestaurant.data.entity;

import java.io.Serializable;

public class SnackItem implements Serializable {
    private String photo;
    private String name;
    private int price;


    public SnackItem() {
    }

    public SnackItem(String photo, String name, int price) {
        this.photo = photo;
        this.name = name;
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
