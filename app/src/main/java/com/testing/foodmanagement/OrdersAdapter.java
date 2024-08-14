package com.testing.foodmanagement;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private ArrayList<Order> ordersList;
    private SQLiteDatabase MyDB;

    public OrdersAdapter(ArrayList<Order> ordersList, SQLiteDatabase MyDB) {
        this.ordersList = ordersList;
        this.MyDB = MyDB;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);
        holder.itemName.setText(order.getItemName());
        holder.itemPrice.setText(order.getItemPrice());
        holder.itemQuantity.setText(order.getItemQuantity());
        holder.branch.setText(order.getBranch());
        holder.phone.setText(order.getPhone());
        holder.customerLocation.setText(order.getCustomerLocation());

        // Add functionality for updating/deleting orders here
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQuantity, branch, phone, customerLocation;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            branch = itemView.findViewById(R.id.branch);
            phone = itemView.findViewById(R.id.phone);
            customerLocation = itemView.findViewById(R.id.customer_location);
        }
    }
}

