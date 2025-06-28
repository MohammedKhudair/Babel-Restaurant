package com.mohammed.babelrestaurant.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.callback.FoodOrderListener;
import com.mohammed.babelrestaurant.data.entity.FoodOrder;
import com.mohammed.babelrestaurant.databinding.CustomThanksCardDialogBinding;
import com.mohammed.babelrestaurant.databinding.FragmentOrderDetailsBinding;
import com.mohammed.babelrestaurant.utils.PlaceTheOrder;

public class OrderDetailsFragment extends Fragment {
    private FragmentOrderDetailsBinding binding;
    private FoodOrder foodOrder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false);
        // Initialise ToolBar.
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Receive Arguments data
        foodOrder = OrderDetailsFragmentArgs.fromBundle(getArguments()).getFoodOrder();
        binding.setFoodOrder(foodOrder);


        binding.contentCopyButton.setOnClickListener(view1 -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.orderNumberTextView.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), "تم النسخ الى الحافضه", Toast.LENGTH_SHORT).show();
        });

        binding.reOrderButton.setOnClickListener(view2 -> {
            if (!isNetworkAvailable()) {
                Snackbar.make(binding.getRoot(), R.string.no_network_available, Snackbar.LENGTH_SHORT).show();
                return;
            }
            reOrder();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netWorkInfo = connectivityManager.getActiveNetworkInfo();
        return netWorkInfo != null && netWorkInfo.isConnected();
    }

    private void reOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.Confirm);
        builder.setMessage("سنقوم بأعادة طلب هذه الوجبه.");
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) ->
                PlaceTheOrder.getInstance().reOrderFood(foodOrder, placed -> {
                    if (!placed) {
                        Snackbar.make(binding.getRoot(), R.string.network_issue, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    showThanksCard();
                }));
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
}