package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersPendingAdapter2 extends RecyclerView.Adapter<OrdersPendingAdapter2.OrderPendingViewHolder> {

    private Context context;
    private ArrayList<OrderPending> ordersPendingList;

    public OrdersPendingAdapter2(Context context, ArrayList<OrderPending> ordersPendingList) {
        this.context = context;
        this.ordersPendingList = ordersPendingList;
    }

    @NonNull
    @Override
    public OrderPendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_pending_item2, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return ordersPendingList.size();
    }

    public static class OrderPendingViewHolder extends RecyclerView.ViewHolder {

        TextView orderName, orderDetails, orderDateTime, customerInfo, branch, status;

        public OrderPendingViewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            orderDateTime = itemView.findViewById(R.id.orderDateTime);
            customerInfo = itemView.findViewById(R.id.customerInfo);
            branch = itemView.findViewById(R.id.branch);
            status = itemView.findViewById(R.id.status);
        }
    }
}
