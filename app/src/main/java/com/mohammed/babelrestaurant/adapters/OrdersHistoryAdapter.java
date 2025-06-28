package com.mohammed.babelrestaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammed.babelrestaurant.data.entity.FoodOrder;
import com.mohammed.babelrestaurant.databinding.OrdersHistoryItemBinding;
import java.util.List;

public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.OrdersHistoryViewHolder> {
    private final List<FoodOrder> ordersHistoryItems;
    private final OnItemClickListener onItemClickListener;

    public OrdersHistoryAdapter(List<FoodOrder> ordersHistoryItems,OnItemClickListener onItemClickListener) {
        this.ordersHistoryItems = ordersHistoryItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public OrdersHistoryAdapter.OrdersHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrdersHistoryItemBinding binding =
                OrdersHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrdersHistoryViewHolder(binding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersHistoryAdapter.OrdersHistoryViewHolder holder, int position) {
        holder.bind(ordersHistoryItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (ordersHistoryItems == null) {
            return 0;
        } else {
            return ordersHistoryItems.size();

        }
    }

    public static class OrdersHistoryViewHolder extends RecyclerView.ViewHolder {
       private final OrdersHistoryItemBinding binding;
       private FoodOrder foodOrder;

        public OrdersHistoryViewHolder(@NonNull OrdersHistoryItemBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(view ->
                    onItemClickListener.onItemClick(foodOrder));


        }
        void bind(FoodOrder order) {
            this.foodOrder = order;
            binding.setFoodOrder(order);
        }
    }

   public interface OnItemClickListener {
        void onItemClick(FoodOrder foodOrder);
    }
}
