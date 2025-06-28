package com.mohammed.babelrestaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.callback.ItemClickListener;
import com.mohammed.babelrestaurant.databinding.CommonMealsListItemBinding;
import com.mohammed.babelrestaurant.databinding.MealListItemBinding;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.model.RemoteDataViewModel;
import com.mohammed.babelrestaurant.viewmodels.MealViewModel;
import com.mohammed.babelrestaurant.views.HomeFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MealListItem> mealListItems = new ArrayList<>();
    private final int ITEM_VIEW_TYPE_COMMON_MEAL = 0;
    private final int ITEM_VIEW_TYPE_MEAL_LIST = 1;
    private final Context context;
    private final ItemClickListener itemClickListener;

    public MealListAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        mealListItems.add(new MealListItem());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_COMMON_MEAL) {
            CommonMealsListItemBinding binding =
                    CommonMealsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new CommonMealViewHolder(binding);
        } else {
            MealListItemBinding binding =
                    MealListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MealListViewHolder(binding, itemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_VIEW_TYPE_COMMON_MEAL) {
            CommonMealViewHolder commonMealViewHolder = (CommonMealViewHolder) holder;
            /**
             * If the meal was common it will insert in the commonMealList.
             */
            ArrayList<MealListItem> commonMealList = new ArrayList<>();
            for (MealListItem meal : mealListItems) {
                if (meal.isCommon()) {
                    commonMealList.add(meal);
                }
            }
            commonMealViewHolder.bind(commonMealList, context);

        } else {
            MealListViewHolder mealListViewHolder = (MealListViewHolder) holder;
            mealListViewHolder.bind(mealListItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mealListItems == null) {
            return 0;
        } else {
            return mealListItems.size();
        }
    }

    public static class CommonMealViewHolder extends RecyclerView.ViewHolder {
       private final CommonMealsListItemBinding binding;
       private CommonMealsAdapter adapter;
        private RemoteDataViewModel mRemoteDataViewModel;

        public CommonMealViewHolder(@NonNull CommonMealsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void bind(List<MealListItem> mealListItem, Context context) {
            mRemoteDataViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(RemoteDataViewModel.class);

            adapter = new CommonMealsAdapter(mealListItem, new ItemClickListener() {
                @Override
                public void isLike(MealListItem item, ImageView likeIcon, int position) {
                    // Add the item to the favorites in the server.
                    mRemoteDataViewModel.addItemToFavorites(item);

                    if (item.isLike()) {
                        likeIcon.setImageResource(R.drawable.ic_heart_unfill);
                        adapter.updateArrayList(false,position);
                    } else {
                        likeIcon.setImageResource(R.drawable.ic_heart_fill);
                        adapter.updateArrayList(true,position);
                    }
                }

                @Override
                public void onClick(View view, MealListItem item) {
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item);
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });

            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setLayoutManager(new
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    public static class MealListViewHolder extends RecyclerView.ViewHolder {
        MealListItemBinding binding;
        MealListItem item;

        public MealListViewHolder(@NonNull MealListItemBinding binding, ItemClickListener itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.like.setOnClickListener(view ->
                    itemClickListener.isLike(item,binding.like,getAdapterPosition()));

            binding.getRoot().setOnClickListener(view ->
                itemClickListener.onClick(binding.mealImageView, item));

        }

        void bind(MealListItem item) {
            this.item = item;
            binding.setMealItem(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_VIEW_TYPE_COMMON_MEAL;
        } else {
            return ITEM_VIEW_TYPE_MEAL_LIST;
        }
    }

    public void setMealListItems(ArrayList<MealListItem> items) {
        this.mealListItems = items;
        notifyDataSetChanged();
    }


    public void updateArrayList(boolean like,int position) {
        mealListItems.get(position).setLike(like);
    }
}
