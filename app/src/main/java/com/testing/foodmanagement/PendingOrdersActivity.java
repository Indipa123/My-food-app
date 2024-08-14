package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersPendingAdapter ordersPendingAdapter;
    private ArrayList<OrderPending> ordersPendingList;
    private DBHelper dbHelper;
    private TextView noOrdersMessage;  // Ensure this view is declared

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noOrdersMessage = findViewById(R.id.noOrdersMessage);  // Initialize the view
        ordersPendingList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        ordersPendingAdapter = new OrdersPendingAdapter(this, ordersPendingList);
        recyclerView.setAdapter(ordersPendingAdapter);

        loadPendingOrders(); // Load data initially
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPendingOrders(); // Reload data when the activity resumes
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadPendingOrders() {
        ordersPendingList.clear(); // Clear the list to avoid duplication
        Cursor cursor = dbHelper.getPendingOrders();

        if (cursor.getCount() == 0) {
            if (noOrdersMessage != null) {  // Null check before setting visibility
                noOrdersMessage.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
            cursor.close();
            return;
        }

        if (noOrdersMessage != null) {  // Null check before setting visibility
            noOrdersMessage.setVisibility(View.GONE);
        }
        recyclerView.setVisibility(View.VISIBLE);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int orderPId = cursor.getInt(cursor.getColumnIndex("orderPId"));
            @SuppressLint("Range") String curDate = cursor.getString(cursor.getColumnIndex("cur_date"));
            @SuppressLint("Range") String curTime = cursor.getString(cursor.getColumnIndex("cur_time"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String orderName = cursor.getString(cursor.getColumnIndex("orderName"));
            @SuppressLint("Range") String orderQuantity = cursor.getString(cursor.getColumnIndex("orderQuantity"));
            @SuppressLint("Range") String orderPrice = cursor.getString(cursor.getColumnIndex("orderPrice"));
            @SuppressLint("Range") String branch = cursor.getString(cursor.getColumnIndex("branch"));
            @SuppressLint("Range") String customerLocation = cursor.getString(cursor.getColumnIndex("customerLocation"));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));

            OrderPending orderPending = new OrderPending(orderPId, curDate, curTime, email, orderName, orderQuantity, orderPrice, branch, customerLocation, phone, status);
            ordersPendingList.add(orderPending);
        }
        cursor.close();
        ordersPendingAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }
}
