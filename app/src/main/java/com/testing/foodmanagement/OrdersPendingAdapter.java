package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersPendingAdapter extends RecyclerView.Adapter<OrdersPendingAdapter.OrderPendingViewHolder> {

    private Context context;
    private ArrayList<OrderPending> ordersPendingList;
    private DBHelper dbHelper;

    public OrdersPendingAdapter(Context context, ArrayList<OrderPending> ordersPendingList) {
        this.context = context;
        this.ordersPendingList = ordersPendingList;
        this.dbHelper = new DBHelper(context); // Initialize DatabaseHelper
    }

    @NonNull
    @Override
    public OrderPendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_pending_item, parent, false);
        return new OrderPendingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderPendingViewHolder holder, int position) {
        OrderPending orderPending = ordersPendingList.get(position);

        holder.orderName.setText(orderPending.getOrderName());
        holder.orderDetails.setText("Quantity: " + orderPending.getOrderQuantity() + ", Price: " + orderPending.getOrderPrice());
        holder.orderDateTime.setText("Date: " + orderPending.getCurDate() + " Time: " + orderPending.getCurTime());
        holder.customerInfo.setText("Customer: " + orderPending.getEmail() + " Location: " + orderPending.getCustomerLocation() + " Phone: " + orderPending.getPhone());
        holder.branch.setText("Branch: " + orderPending.getBranch());
        holder.status.setText("Status: " + orderPending.getStatus());

        // Set onClickListeners for buttons
        holder.btnOrderReceived.setOnClickListener(v -> updateOrderStatus(holder, orderPending, "Your Order is Received"));

        holder.btnOrderPreparing.setOnClickListener(v -> updateOrderStatus(holder, orderPending, "Your Order is Prepared"));

        holder.btnReadyForPickup.setOnClickListener(v -> updateOrderStatus(holder, orderPending, "Ready To Pick up Order"));
    }

    @Override
    public int getItemCount() {
        return ordersPendingList.size();
    }

    private void updateOrderStatus(OrderPendingViewHolder holder, OrderPending orderPending, String status) {
        // Update the status in the database
        boolean isUpdated = dbHelper.updateOrderStatus(orderPending.getOrderPId(), status);

        if (isUpdated) {
            if (status.equals("Ready To Pick up Order")) {
                // Remove the item from the list
                int position = holder.getAdapterPosition();
                ordersPendingList.remove(position);
                notifyItemRemoved(position);
            } else {
                // Update the status in the UI
                orderPending.setStatus(status);
                holder.status.setText("Status: " + status);
            }

            // Notify the user
            Toast.makeText(context, "Order status updated: " + status, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update order status.", Toast.LENGTH_SHORT).show();
        }
    }

    public static class OrderPendingViewHolder extends RecyclerView.ViewHolder {

        TextView orderName, orderDetails, orderDateTime, customerInfo, branch, status;
        Button btnOrderReceived, btnOrderPreparing, btnReadyForPickup;

        public OrderPendingViewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            orderDateTime = itemView.findViewById(R.id.orderDateTime);
            customerInfo = itemView.findViewById(R.id.customerInfo);
            branch = itemView.findViewById(R.id.branch);
            status = itemView.findViewById(R.id.status);
            btnOrderReceived = itemView.findViewById(R.id.btnOrderReceived);
            btnOrderPreparing = itemView.findViewById(R.id.btnOrderPreparing);
            btnReadyForPickup = itemView.findViewById(R.id.btnReadyForPickup);
        }
    }
}
