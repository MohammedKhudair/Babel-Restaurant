package com.mohammed.babelrestaurant.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.adapters.CategoryAndFavoritesAdapter;
import com.mohammed.babelrestaurant.callback.ItemClickListener;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.databinding.FragmentCategoryListBinding;
import com.mohammed.babelrestaurant.model.RemoteDataViewModel;


public class CategoryListFragment extends Fragment implements ItemClickListener {
    private FragmentCategoryListBinding binding;
    private String mealType;
    private CategoryAndFavoritesAdapter adapter;
    private RemoteDataViewModel mRemoteDataViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false);
        // Initialise ToolBar.
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        // Initialise viewModel
        mRemoteDataViewModel = new ViewModelProvider(this).get(RemoteDataViewModel.class);
        // Receive Arguments data
        mealType = CategoryListFragmentArgs.fromBundle(getArguments()).getMealType();
        binding.toolbar.setTitle(mealType);

        adapter = new CategoryAndFavoritesAdapter(this);
        binding.categoryRecyclerView.setAdapter(adapter);
        binding.categoryRecyclerView.setHasFixedSize(true);
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgressBar(true);

        mRemoteDataViewModel.getMealTypeList(mealType).observe(getViewLifecycleOwner(), items ->{
            adapter.setMealListItems(items);
            showProgressBar(false);
        });

    }

    private void showProgressBar(boolean show){
        if (show){
            binding.googleProgressBar.setVisibility(View.VISIBLE);
            binding.categoryRecyclerView.setVisibility(View.GONE);
        }else {
            binding.googleProgressBar.setVisibility(View.GONE);
            binding.categoryRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isLike(MealListItem item, ImageView likeIcon, int position) {
        // Add the item to the favorites in the server.
        mRemoteDataViewModel.addItemToFavorites(item).observe(getViewLifecycleOwner(), status ->
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show());

        if (item.isLike()) {
            likeIcon.setImageResource(R.drawable.ic_heart_unfill2);
            adapter.updateArrayList(false,position);
        } else {
            likeIcon.setImageResource(R.drawable.ic_heart_fill);
            adapter.updateArrayList(true,position);
        }
    }

    @Override
    public void onClick(View view, MealListItem item) {
        NavDirections action = CategoryListFragmentDirections.actionCategoryListFragmentToDetailsFragment(item);
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mRemoteDataViewModel.getMealTypeList(mealType).removeObservers(this);
    }
}