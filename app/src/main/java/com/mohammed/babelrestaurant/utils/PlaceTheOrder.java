package com.mohammed.babelrestaurant.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.mohammed.babelrestaurant.callback.FoodOrderListener;
import com.mohammed.babelrestaurant.data.entity.FoodOrder;
import com.mohammed.babelrestaurant.data.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class PlaceTheOrder {
    private final FoodOrder foodOrder;
    private final int deliveryPrice = 5000;
    private final FirebaseFirestore db;
    private final String id;

    public PlaceTheOrder() {
        foodOrder = new FoodOrder();
        db = FirebaseFirestore.getInstance();
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static PlaceTheOrder getInstance(){
        return new PlaceTheOrder();
    }

    public void orderNow(String mealName, GeoPoint mGeoPoint, User user, List<String> snacksList ,PriceAndFoodAmountCalculator priceAndFoodAmountCalculator, FoodOrderListener foodOrderListener) {
        foodOrder.setOrderName(mealName);
        foodOrder.setAmount(priceAndFoodAmountCalculator.getFoodAmount());
        foodOrder.setSnacks(snacksList);
        foodOrder.setDate(new Timestamp(new Date()));
        foodOrder.setStatus(FoodOrder.OrderStatus.UNDER_REVIEW.name());
        foodOrder.setOrderNumber(new Random().nextInt(1000000));
        foodOrder.setLocation(mGeoPoint);
        foodOrder.setOrderPrice(priceAndFoodAmountCalculator.getOrderPrice());
        foodOrder.setDeliveryPrice(deliveryPrice + " د.ع ");
        foodOrder.setTotalPrice(priceAndFoodAmountCalculator.getTotalPrice());
        // User address
        foodOrder.setCustomerName(user.getAddress().get("name"));
        foodOrder.setAddress(user.getAddress().get("address"));
        foodOrder.setPhoneNumber(user.getAddress().get("phone"));
        foodOrder.setUserId(id);


        db.collection("Orders").add(foodOrder).addOnCompleteListener(task -> {
            foodOrderListener.foodOrder(task.isSuccessful());
        });

    }

    public void reOrderFood(FoodOrder foodOrder,FoodOrderListener foodOrderListener) {
        foodOrder.setDate(new Timestamp(new Date()));
        foodOrder.setStatus(FoodOrder.OrderStatus.UNDER_REVIEW.name());
        foodOrder.setOrderNumber(new Random().nextInt(1000000));

        db.collection("Orders").add(foodOrder).addOnCompleteListener(task ->
                foodOrderListener.foodOrder(task.isSuccessful()));

    }


}
