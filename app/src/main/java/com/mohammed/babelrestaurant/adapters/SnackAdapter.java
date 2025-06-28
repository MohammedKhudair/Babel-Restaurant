package com.mohammed.babelrestaurant.adapters;

import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohammed.babelrestaurant.data.entity.SnackItem;
import com.mohammed.babelrestaurant.databinding.SnackItemBinding;
import com.mohammed.babelrestaurant.utils.PriceAndFoodAmountCalculator;

import java.util.List;

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.SnackViewHolder> {
    private List<SnackItem> snackItems;
    private final OnCheckedListener onCheckedListener;

    public SnackAdapter(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    @NonNull
    @Override
    public SnackAdapter.SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SnackItemBinding binding = SnackItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new SnackViewHolder(binding, onCheckedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SnackAdapter.SnackViewHolder holder, int position) {
        holder.bind(snackItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (snackItems == null)
            return 0;
        else {
            return snackItems.size();
        }
    }

    public static class SnackViewHolder extends RecyclerView.ViewHolder {
        private final SnackItemBinding binding;
        private SnackItem snackItem;

        public SnackViewHolder(@NonNull SnackItemBinding binding, OnCheckedListener onCheckedListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.checkBox.setOnCheckedChangeListener((compoundButton, checked) ->
                    onCheckedListener.onChecked(snackItem.getPrice(),snackItem.getName(), checked));
        }

        void bind(SnackItem snackItem) {
            this.snackItem = snackItem;
            binding.setSnackItem(snackItem);
        }
    }

    public void setSnackItems(List<SnackItem> items) {
        this.snackItems = items;
        notifyDataSetChanged();
    }

    public interface OnCheckedListener {
        void onChecked(int price,String name, boolean checked);
    }
}
