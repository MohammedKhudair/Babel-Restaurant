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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mohammed.babelrestaurant.adapters.CategoryAndFavoritesAdapter;
import com.mohammed.babelrestaurant.callback.ItemClickListener;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.databinding.FragmentFavoriteBinding;
import com.mohammed.babelrestaurant.model.RemoteDataViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment implements ItemClickListener {
    private FragmentFavoriteBinding binding;
    private ArrayList<MealListItem> mMealArrayList;
    private RemoteDataViewModel mRemoteDataViewModel;
    private CategoryAndFavoritesAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        adapter = new CategoryAndFavoritesAdapter(this);
        binding.favoriteRecyclerview.setAdapter(adapter);
        binding.favoriteRecyclerview.setHasFixedSize(true);
        binding.favoriteRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRemoteDataViewModel = new ViewModelProvider(this).get(RemoteDataViewModel.class);
        mMealArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRemoteDataViewModel.getFavorites().observe(getViewLifecycleOwner(), items -> {
            adapter.setMealListItems(items);
          if(items.size() > 0){
              binding.imageView.setVisibility(View.GONE);
              binding.noFavoriteTextView.setVisibility(View.GONE);
          }
        });


    }

    @Override
    public void isLike(MealListItem item, ImageView view, int position) {
// Remove the item from favorites in the server.
        mRemoteDataViewModel.addItemToFavorites(item).observe(getViewLifecycleOwner(), status ->
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show());

        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onClick(View view, MealListItem item) {
        NavDirections action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(item);
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}