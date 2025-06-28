package com.mohammed.babelrestaurant.databinding;

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.data.entity.FoodOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBindingAdapter {

    @BindingAdapter("setImage")
    public static void loadImage(ImageView image, String imageUrl) {
        Glide
                .with(image)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(image);
    }

    @BindingAdapter("setTextPrice")
    public static void currencyFormatter(TextView text, int price) {
        String mPrice = NumberFormat.getNumberInstance(Locale.US).format(price) + " د.ع ";
        text.setText(mPrice);
    }

    @BindingAdapter("setIconImage")
    public static void likeIcon(ImageView image, boolean like) {
        if (like) {
            image.setImageResource(R.drawable.ic_heart_fill);
        } else {
            image.setImageResource(R.drawable.ic_heart_unfill2);
        }
    }

    @BindingAdapter("setIconImage2")
    public static void likeIcon2(ImageView image, boolean like) {
        if (like) {
            image.setImageResource(R.drawable.ic_heart_fill);
        } else {
            image.setImageResource(R.drawable.ic_heart_unfill);
        }
    }

    @BindingAdapter("setOrderStatus")
    public static void orderStatus(TextView textView, String status) {
        if (FoodOrder.OrderStatus.ORDER_ACCEPTED.name().equals(status)) {
            textView.setText(R.string.order_accepted);
            textView.setTextColor(Color.parseColor("#3DDB84"));

        } else if (FoodOrder.OrderStatus.ORDER_DECLINED.name().equals(status)) {
            textView.setText(R.string.order_declined);
            textView.setTextColor(Color.parseColor("#FFEF5350"));

        } else if (FoodOrder.OrderStatus.UNDER_REVIEW.name().equals(status)) {
            textView.setText(R.string.under_review);
            textView.setTextColor(Color.parseColor("#FFC400"));
        }
    }

    @BindingAdapter("setDate")
    public static void dateFormatter(TextView textView, Timestamp timestampDate) {
        Date date = timestampDate.toDate();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyy hh:mm a", Locale.getDefault());
        textView.setText(dateFormat.format(date));
    }

    @BindingAdapter("setSnacks")
    public static void snacks(TextView textView, List<String> snacksList) {
        StringBuilder snacks = new StringBuilder();
        if (!snacksList.isEmpty()) {
            for (String snack : snacksList) {
                snacks.append(". ").append(snack).append("\n");
            }
        } else {
            snacks.append("لايوجد").append("\n");
        }
        textView.setText(snacks.toString());
    }

    @BindingAdapter("setAmountAndName")
    public static void amountAntNameOrder(TextView textView,FoodOrder foodOrder) {
        textView.setText(foodOrder.getAmount() +" "+foodOrder.getOrderName());
    }
}
