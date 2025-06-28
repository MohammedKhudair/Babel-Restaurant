package com.mohammed.babelrestaurant.callback;

import com.mohammed.babelrestaurant.data.entity.FoodOrder;

public interface FoodOrderListener {
    void foodOrder(boolean placed);
}
