package com.testing.foodmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private ArrayList<Order> ordersList;
    private SQLiteDatabase MyDB;
    private Context context;
    private DBHelper dbHelper;

    public OrdersAdapter(Context context, ArrayList<Order> ordersList, SQLiteDatabase MyDB) {
        this.context = context;
        this.ordersList = ordersList;
        this.MyDB = MyDB;
        this.dbHelper = new DBHelper(context); // Initialize DBHelper
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

        holder.confirmOrderButton.setOnClickListener(v -> {

            String userEmail = dbHelper.getUserEmail(order.getPhone()); // Use DBHelper to get email
            if (userEmail == null || userEmail.isEmpty()) {
                Toast.makeText(context, "User email is not available", Toast.LENGTH_SHORT).show();
                return;
            }

            String subject = "Order Confirmation";
            String body = "Dear Customer,\n\nYour order has been confirmed!\n\nThank you!";

            // Send email in a background thread
            new SendEmailTask(order).execute(userEmail, subject, body);
        });

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQuantity, branch, phone, customerLocation;
        Button confirmOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            branch = itemView.findViewById(R.id.branch);
            phone = itemView.findViewById(R.id.phone);
            customerLocation = itemView.findViewById(R.id.customer_location);
            confirmOrderButton = itemView.findViewById(R.id.confirm_order_button);
        }
    }

    // AsyncTask to handle email sending in background
    private class SendEmailTask extends AsyncTask<String, Void, Boolean> {

        private Order order;

        public SendEmailTask(Order order) {
            this.order = order;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            String to = params[0];
            String subject = params[1];
            String body = params[2];
            try {
                EmailHelper.sendEmail(to, subject, body);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                ordersList.remove(order);
                notifyDataSetChanged();
                Toast.makeText(context, "Order Confirmed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to send email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
