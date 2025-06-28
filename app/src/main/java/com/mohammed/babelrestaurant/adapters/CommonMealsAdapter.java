package com.mohammed.babelrestaurant.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammed.babelrestaurant.callback.ItemClickListener;
import com.mohammed.babelrestaurant.databinding.CommonMealsItemBinding;
import com.mohammed.babelrestaurant.data.entity.MealListItem;

import java.util.List;

public class CommonMealsAdapter extends RecyclerView.Adapter<CommonMealsAdapter.MealViewHolder> {
    private final List<MealListItem> mealListItems;
    private final ItemClickListener itemClickListener;


    public CommonMealsAdapter(List<MealListItem> mealsList, ItemClickListener itemClickListener) {
        this.mealListItems = mealsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CommonMealsAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommonMealsItemBinding binding =
                CommonMealsItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);

        return new MealViewHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonMealsAdapter.MealViewHolder holder, int position) {
            holder.bind(mealListItems.get(position));

    }

    @Override
    public int getItemCount() {
        if (mealListItems == null) {
            return 0;
        } else {
            return mealListItems.size();
        }
    }



    public static class MealViewHolder extends RecyclerView.ViewHolder {
        CommonMealsItemBinding binding;
        MealListItem item;

        public MealViewHolder(@NonNull CommonMealsItemBinding binding, ItemClickListener itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.like.setOnClickListener(view -> itemClickListener.isLike(item,binding.like, getAdapterPosition()));

            binding.getRoot().setOnClickListener(view -> itemClickListener.onClick(binding.getRoot(),item));
        }

        void bind(MealListItem item) {
            this.item = item;
            binding.setMealItem(item);
        }
    }

    public void updateArrayList(boolean like, int position) {
        mealListItems.get(position).setLike(like);
    }

}
