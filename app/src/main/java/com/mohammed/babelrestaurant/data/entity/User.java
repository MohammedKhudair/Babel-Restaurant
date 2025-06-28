package com.mohammed.babelrestaurant.data.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private ArrayList<String> bookmarks;
    private Map<String ,String> address;

    public User() {
        this.bookmarks = new ArrayList<>();
        this.address = new HashMap<>();
    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }
}
