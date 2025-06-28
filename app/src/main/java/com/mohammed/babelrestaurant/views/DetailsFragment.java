package com.mohammed.babelrestaurant.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.GeoPoint;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.adapters.SnackAdapter;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.data.entity.User;
import com.mohammed.babelrestaurant.databinding.CustomAlertDialogBinding;
import com.mohammed.babelrestaurant.databinding.CustomThanksCardDialogBinding;
import com.mohammed.babelrestaurant.databinding.FragmentDetailsBinding;
import com.mohammed.babelrestaurant.model.RemoteDataViewModel;
import com.mohammed.babelrestaurant.utils.PlaceTheOrder;
import com.mohammed.babelrestaurant.utils.PriceAndFoodAmountCalculator;

import java.util.ArrayList;
import java.util.List;


public class DetailsFragment extends Fragment implements SnackAdapter.OnCheckedListener {
    private FragmentDetailsBinding binding;
    private PriceAndFoodAmountCalculator priceAndFoodAmountCalculator;
    private FusedLocationProviderClient fusedLocationClient;
    private GeoPoint mGeoPoint;
    private MealListItem mealItem;
    private SnackAdapter adapter;
    private RemoteDataViewModel mRemoteDataViewModel;
    private User user;
    private List<String> snacksList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Request current location
        requestLocationPermission();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        // Initialise tool bar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view2 -> requireActivity().onBackPressed());

        // Initialise location provider.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialise  PriceAndFoodAmountCalculator object;
        priceAndFoodAmountCalculator = new PriceAndFoodAmountCalculator();

        mRemoteDataViewModel = new ViewModelProvider(this).get(RemoteDataViewModel.class);

        adapter = new SnackAdapter(this);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Receive Arguments data
        mealItem = DetailsFragmentArgs.fromBundle(getArguments()).getMealItem();
        priceAndFoodAmountCalculator.setPriceCounter(mealItem.getPrice());
        binding.toolbar.setTitle(null);
        binding.setMealItem(mealItem);


        // Increase price and food amount.
        binding.plusButton.setOnClickListener(view2 -> {
            binding.priceTextView.setText(priceAndFoodAmountCalculator.increasePrice(mealItem.getPrice()));
            binding.amountTextView.setText(priceAndFoodAmountCalculator.increaseFoodAmount());
        });

        // Decrease price and food amount.
        binding.minusButton.setOnClickListener(view2 -> {
            binding.priceTextView.setText(priceAndFoodAmountCalculator.decreasePrice(mealItem.getPrice()));
            binding.amountTextView.setText(priceAndFoodAmountCalculator.decreaseFoodAmount());
        });

        binding.likeImageView.setOnClickListener(view1 -> {
            // Add the item to the favorites in the server.
            mRemoteDataViewModel.addItemToFavorites(mealItem).observe(getViewLifecycleOwner(), status ->
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show());

            if (mealItem.isLike()) {
                binding.likeImageView.setImageResource(R.drawable.ic_heart_unfill2);
                mealItem.setLike(false);
            } else {
                binding.likeImageView.setImageResource(R.drawable.ic_heart_fill);
                mealItem.setLike(true);
            }
        });


        binding.orderButton.setOnClickListener(view2 -> {
            if (!isNetworkAvailable()) {
                Snackbar.make(binding.getRoot(),R.string.no_network_available,Snackbar.LENGTH_SHORT).show();
                return;
            }
            mRemoteDataViewModel.getUserAddress(user -> {
                if (user.getAddress().isEmpty()) {
                    showNoAddressDialog();
                } else {
                    this.user = user;
                    showOrderDetailsCard();
                }
            });

        });

        mRemoteDataViewModel.getStartersLis().observe(getViewLifecycleOwner(), snackItems ->
                adapter.setSnackItems(snackItems));


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)requireActivity().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netWorkInfo = connectivityManager.getActiveNetworkInfo();
        return  netWorkInfo != null && netWorkInfo.isConnected();
    }

    @SuppressLint("MissingPermission")
    private void showOrderDetailsCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        final AlertDialog dialog = builder.create();

        Glide.with(dialogBinding.imageView).load(mealItem.getPhoto()).into(dialogBinding.imageView);
        dialogBinding.mealName.setText(mealItem.getMealName());
        dialogBinding.amount.setText(priceAndFoodAmountCalculator.getFoodAmount());
        dialogBinding.mealPrice.setText(priceAndFoodAmountCalculator.getTotalPrice());

        dialogBinding.cancel.setOnClickListener(view3 -> dialog.dismiss());
        dialogBinding.confirmButton.setOnClickListener(view4 -> {
            dialog.dismiss();
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if(location != null){
                    mGeoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                }
                // Place teh order
                placeTheOrder();
            });
        });

        dialog.show();
    }

    private void placeTheOrder() {
        PlaceTheOrder.getInstance().orderNow(
                mealItem.getMealName(),mGeoPoint,user,snacksList, priceAndFoodAmountCalculator, placed -> {
            if(!placed){
                Snackbar.make(binding.getRoot(), R.string.network_issue, Snackbar.LENGTH_LONG).show();
                return;
            }
            showThanksCard();
        });
    }

    private void showNoAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.no_address_alert);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            NavDirections action = DetailsFragmentDirections.actionDetailsFragmentToAddressFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void showThanksCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        CustomThanksCardDialogBinding cardDialogBinding =
                CustomThanksCardDialogBinding.inflate(getLayoutInflater());
        builder.setView(cardDialogBinding.getRoot());
        final AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cardDialogBinding.okButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void requestLocationPermission() {
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        getLastLocation();
                    } else {
                        Toast.makeText(getContext(),
                                R.string.location_permission, Toast.LENGTH_LONG).show();
                    }
                });

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage(R.string.location_activate);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                });
                builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                builder.show();
                return;
            }

            mGeoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        });
    }

    @Override
    public void onChecked(int price,String name, boolean checked) {
        snacksList.add(name);
        String foodPrice = priceAndFoodAmountCalculator.increaseOrDecreasePrice(price, checked);
        binding.priceTextView.setText(foodPrice);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mRemoteDataViewModel.getStartersLis().removeObservers(this);
    }


}