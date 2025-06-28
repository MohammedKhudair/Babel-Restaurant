package com.mohammed.babelrestaurant.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammed.babelrestaurant.callback.ItemClickListener;
import com.mohammed.babelrestaurant.databinding.MealListItemBinding;
import com.mohammed.babelrestaurant.data.entity.MealListItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryAndFavoritesAdapter extends RecyclerView.Adapter<CategoryAndFavoritesAdapter.MealListViewHolder> {
    private List<MealListItem> mealListItems = new ArrayList<>();
   private final ItemClickListener itemClickListener;

    public CategoryAndFavoritesAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryAndFavoritesAdapter.MealListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealListItemBinding binding =
                MealListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MealListViewHolder(binding,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAndFavoritesAdapter.MealListViewHolder holder, int position) {
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



    public static class MealListViewHolder extends RecyclerView.ViewHolder {
        MealListItemBinding binding;
        MealListItem item;

        public MealListViewHolder(@NonNull MealListItemBinding binding, ItemClickListener itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.like.setOnClickListener(view ->
                    itemClickListener.isLike(item,binding.like, getAdapterPosition()));

            binding.getRoot().setOnClickListener(view ->
                    itemClickListener.onClick(binding.getRoot(),item));
        }

        void bind(MealListItem item) {
            this.item = item;
            binding.setMealItem(item);
        }
    }

    public void setMealListItems(List<MealListItem> mealListItems){
        this.mealListItems = mealListItems;
        notifyDataSetChanged();
    }

    public void updateArrayList(boolean like, int position) {
        mealListItems.get(position).setLike(like);
    }
}
