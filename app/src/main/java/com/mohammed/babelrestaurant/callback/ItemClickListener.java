package com.mohammed.babelrestaurant.callback;

import android.view.View;
import android.widget.ImageView;

import com.mohammed.babelrestaurant.data.entity.MealListItem;

public interface ItemClickListener {
    void isLike(MealListItem item, ImageView view, int position);
    void onClick(View view, MealListItem item);
}
