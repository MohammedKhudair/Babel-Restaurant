package com.mohammed.babelrestaurant.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohammed.babelrestaurant.databinding.FragmentCatagoryBinding;
import com.mohammed.babelrestaurant.data.entity.MealListItem;

public class CategoryFragment extends Fragment {
    private FragmentCatagoryBinding binding;
    public static final String CATEGORY_TYPE = "CATEGORY_TYPE";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCatagoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.meatCardView.setOnClickListener(view2 -> {
            CategoryFragmentDirections.ActionCategoryFragmentToCategoryListFragment action =
                    CategoryFragmentDirections.actionCategoryFragmentToCategoryListFragment();

            action.setMealType(MealListItem.MealType.MEAT.name());

            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.riceCardView.setOnClickListener(view2 -> {
            CategoryFragmentDirections.ActionCategoryFragmentToCategoryListFragment action =
                    CategoryFragmentDirections.actionCategoryFragmentToCategoryListFragment();

            action.setMealType(MealListItem.MealType.RICE.name());

            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.soupCardView.setOnClickListener(view2 -> {
            CategoryFragmentDirections.ActionCategoryFragmentToCategoryListFragment action =
                    CategoryFragmentDirections.actionCategoryFragmentToCategoryListFragment();

            action.setMealType(MealListItem.MealType.SOUP.name());

            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.startersCardView.setOnClickListener(view2 -> {
            CategoryFragmentDirections.ActionCategoryFragmentToCategoryListFragment action =
                    CategoryFragmentDirections.actionCategoryFragmentToCategoryListFragment();

            action.setMealType(MealListItem.MealType.SNACKS.name());

            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}