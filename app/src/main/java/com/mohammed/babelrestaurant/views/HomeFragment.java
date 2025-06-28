package com.mohammed.babelrestaurant.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.adapters.MealListAdapter;
import com.mohammed.babelrestaurant.callback.ItemClickListener;
import com.mohammed.babelrestaurant.data.entity.FavoriteItem;
import com.mohammed.babelrestaurant.data.entity.SnackItem;
import com.mohammed.babelrestaurant.databinding.FragmentHomeBinding;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.model.RemoteDataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeFragment extends Fragment implements ItemClickListener {
    private FragmentHomeBinding binding;
    private MealListAdapter adapter;
    private RemoteDataViewModel mRemoteDataViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialise variables data;
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mRemoteDataViewModel = new ViewModelProvider(this).get(RemoteDataViewModel.class);
        adapter = new MealListAdapter(getContext(), this);
        binding.mealListRecyclerView.setAdapter(adapter);
        binding.mealListRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgressBar(true);

        mRemoteDataViewModel.getMealList().observe(getViewLifecycleOwner(), items -> {
            adapter.setMealListItems((ArrayList<MealListItem>) items);
            showProgressBar(false);
        });


    }


    @Override
    public void isLike(MealListItem item, ImageView likeIcon,int position) {
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
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item);
        Navigation.findNavController(binding.getRoot()).navigate(action);

    }

    private void showProgressBar(boolean show) {
        if (show) {
            binding.googleProgressBar.setVisibility(View.VISIBLE);
            binding.mealListRecyclerView.setVisibility(View.GONE);
        } else {
            binding.googleProgressBar.setVisibility(View.GONE);
            binding.mealListRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mRemoteDataViewModel.getMealList().removeObservers(this);
    }

}