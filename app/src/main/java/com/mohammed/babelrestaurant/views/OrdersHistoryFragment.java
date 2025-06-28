package com.mohammed.babelrestaurant.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mohammed.babelrestaurant.R;
import com.mohammed.babelrestaurant.adapters.OrdersHistoryAdapter;
import com.mohammed.babelrestaurant.data.entity.FoodOrder;
import com.mohammed.babelrestaurant.databinding.FragmentOrdersHistoryBinding;

import java.util.ArrayList;


public class OrdersHistoryFragment extends Fragment implements OrdersHistoryAdapter.OnItemClickListener {
    private FragmentOrdersHistoryBinding binding;
    private FirebaseFirestore db;
    private ArrayList<FoodOrder> ordersHistoryItems;
    private String userId;
   private OrdersHistoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersHistoryBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        ordersHistoryItems = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());


        db.collection("Orders").whereEqualTo("userId", userId).
                orderBy("date", Query.Direction.DESCENDING).
                get().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                Snackbar.make(binding.getRoot(), "" + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                FoodOrder foodOrder = documentSnapshot.toObject(FoodOrder.class);
                ordersHistoryItems.add(foodOrder);
                adapter.notifyDataSetChanged();
            }
            if (ordersHistoryItems.size() < 1) {
                Snackbar.make(binding.getRoot(), R.string.no_orders, Snackbar.LENGTH_LONG).show();
            }
        });

        adapter = new OrdersHistoryAdapter(ordersHistoryItems,this);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerview.setHasFixedSize(true);
    }

    @Override
    public void onItemClick(FoodOrder foodOrder) {
        NavDirections action =
                OrdersHistoryFragmentDirections.actionOrdersHistoryFragmentToOrderDetailsFragment(foodOrder);
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}