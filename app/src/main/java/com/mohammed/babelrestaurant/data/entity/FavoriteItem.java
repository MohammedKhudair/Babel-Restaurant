package com.mohammed.babelrestaurant.data.entity;

public class FavoriteItem {
    private String description;
    private String mealName;
    private String userId;
    private String documentId;
    private String photo;
    private int price;

    public FavoriteItem() {
    }

    public FavoriteItem(MealListItem item, String userId) {
        this.userId = userId;
        this.mealName = item.getMealName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.photo = item.getPhoto();
        this.documentId = item.getDocumentId();

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
