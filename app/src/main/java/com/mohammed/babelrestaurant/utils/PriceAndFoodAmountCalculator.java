package com.mohammed.babelrestaurant.utils;

import android.icu.text.NumberFormat;

import java.util.Locale;

public class PriceAndFoodAmountCalculator {
    private int priceCounter = 0;
    private int foodAmountCounter = 0;
    private boolean isFoodAmountCounterIncreased = true;
    private String foodQuantitySummary = " نص نفر";
    private final int deliveryPrice = 5000;

    public String increasePrice(int foodPrice) {
        priceCounter += foodPrice;
        return NumberFormat.getNumberInstance(Locale.US).format(priceCounter) + " د.ع ";
    }

    public String decreasePrice(int foodPrice) {
        if (stopDecreasingFoodAmount()) {
            priceCounter -= foodPrice;
        }
        return NumberFormat.getNumberInstance(Locale.US).format(priceCounter) + " د.ع ";
    }

    public String increaseOrDecreasePrice(int foodPrice, boolean increase) {
        if (increase) {
            priceCounter += foodPrice;
        } else {
            priceCounter -= foodPrice;
        }
        return NumberFormat.getNumberInstance(Locale.US).format(priceCounter) + " د.ع ";
    }

    public String increaseFoodAmount() {
        if (isFoodAmountCounterIncreased) {
            isFoodAmountCounterIncreased = false;
            foodAmountCounter++;
            foodQuantitySummary = foodAmountCounter + " نفر";
        } else {
            isFoodAmountCounterIncreased = true;
            foodQuantitySummary = foodAmountCounter + " و نص";
        }
        return foodQuantitySummary;
    }

    public String decreaseFoodAmount() {
        if (stopDecreasingFoodAmount()) {
            if (isFoodAmountCounterIncreased) {
                isFoodAmountCounterIncreased = false;
                foodQuantitySummary = foodAmountCounter + " نفر";

            } else {
                isFoodAmountCounterIncreased = true;
                foodAmountCounter--;
                if (foodAmountCounter == 0) {
                    foodQuantitySummary = " نص نفر";
                } else {
                    foodQuantitySummary = foodAmountCounter + " و نص";
                }
            }
        }
        return foodQuantitySummary;
    }


    private boolean stopDecreasingFoodAmount() {
        return foodAmountCounter != 0;
    }

    public void setPriceCounter(int priceCounter) {
        this.priceCounter = priceCounter;
    }

    public String getOrderPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(priceCounter) + " د.ع ";
    }

    public String getFoodAmount() {
        return foodQuantitySummary;
    }

    public  String getTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).
                format(priceCounter + deliveryPrice) + " د.ع ";
    }


}
