package com.mohammed.babelrestaurant.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MealListItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String documentId;
    private String photo;
    private String mealName;
    private String description;
    private String type;
    private int price;
    private boolean like;
    private boolean common;


    public MealListItem() {
    }


    public MealListItem(String photo, String mealName, String description, int price, boolean like) {
        this.photo = photo;
        this.mealName = mealName;
        this.description = description;
        this.price = price;
        this.like = like;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }
//===================================================
// Parcelable methods ********************************
//===================================================

    protected MealListItem(Parcel in) {
        id = in.readInt();
        photo = in.readString();
        mealName = in.readString();
        description = in.readString();
        price = in.readInt();
        like = in.readByte() != 0;
    }

    public static final Creator<MealListItem> CREATOR = new Creator<MealListItem>() {
        @Override
        public MealListItem createFromParcel(Parcel in) {
            return new MealListItem(in);
        }

        @Override
        public MealListItem[] newArray(int size) {
            return new MealListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(photo);
        parcel.writeString(mealName);
        parcel.writeString(description);
        parcel.writeInt(price);
        parcel.writeByte((byte) (like ? 1 : 0));
    }

    public enum MealType {
        MEAT, RICE, SOUP, SNACKS
    }
}
