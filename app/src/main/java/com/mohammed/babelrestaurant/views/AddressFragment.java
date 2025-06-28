package com.mohammed.babelrestaurant.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mohammed.babelrestaurant.data.entity.User;
import com.mohammed.babelrestaurant.databinding.FragmentAddressBinding;
import com.mohammed.babelrestaurant.model.RemoteDataViewModel;

import java.util.HashMap;
import java.util.Map;

public class AddressFragment extends Fragment {
    private FragmentAddressBinding binding;
    private FirebaseFirestore db;
    private RemoteDataViewModel mRemoteDataViewModel;
    private String userId;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRemoteDataViewModel = new ViewModelProvider(this).get(RemoteDataViewModel.class);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editButton.setOnClickListener(view1 -> {
            showAddressEditUi(true);
            binding.editTextName.setText(user.getAddress().get("name"));
            binding.editTextAddress.setText(user.getAddress().get("address"));
            binding.editTextPhone.setText(user.getAddress().get("phone"));
        });

        binding.saveButton.setOnClickListener(view2 -> setUserAddress());


        mRemoteDataViewModel.getUserAddress(user -> {
           AddressFragment.this.user = user;
            showAddressEditUi(user.getAddress().isEmpty());
        });
    }


    private void setUserAddress() {
        String name = binding.editTextName.getText().toString();
        String address = binding.editTextAddress.getText().toString();
        String phone = binding.editTextPhone.getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.textInputLayoutName.setError("Name filed is empty");
            return;
        } else
            binding.textInputLayoutName.setErrorEnabled(false);

        if (TextUtils.isEmpty(address)) {
            binding.textInputLayoutAddress.setError("Address filed is empty");
            return;
        } else
            binding.textInputLayoutAddress.setErrorEnabled(false);

        if (TextUtils.isEmpty(phone)) {
            binding.textInputLayoutPhone.setError("Phone filed is empty");
            return;
        } else if (!(phone.length() == 11)) {
            binding.textInputLayoutPhone.setError("Phone should be 11 number");
            return;
        } else
            binding.textInputLayoutPhone.setErrorEnabled(false);

        // Add the address to the user
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("address", address);
        userData.put("phone", phone);

        Map<String, Object> userAddress = new HashMap<>();
        userAddress.put("address", userData);

        DocumentReference docRef = db.collection("users").document(userId);
        docRef.set(userAddress, SetOptions.merge()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(requireActivity(), HomeActivity.class));
                requireActivity().finish();
            } else
                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void showAddressEditUi(boolean show) {
        if (show) {
            binding.constraintLayout1.setVisibility(View.VISIBLE);
            binding.constraintLayout2.setVisibility(View.INVISIBLE);
        } else {
            binding.constraintLayout1.setVisibility(View.INVISIBLE);
            binding.constraintLayout2.setVisibility(View.VISIBLE);
            binding.nameTextView.setText(user.getAddress().get("name"));
            binding.addressTextView.setText(user.getAddress().get("address"));
            binding.phoneTextView.setText(user.getAddress().get("phone"));
        }
    }
}