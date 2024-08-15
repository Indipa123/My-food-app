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
    private TextView noOrdersMessage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noOrdersMessage = findViewById(R.id.noOrdersMessage);
        ordersPendingList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        ordersPendingAdapter = new OrdersPendingAdapter(this, ordersPendingList);
        recyclerView.setAdapter(ordersPendingAdapter);

        loadAllOrders(); // Load all types of orders initially
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllOrders(); // Reload all orders when the activity resumes
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadAllOrders() {
        ordersPendingList.clear(); // Clear the list to avoid duplication

        // Load Pending Orders
        Cursor pendingCursor = dbHelper.getPendingOrders();
        addOrdersFromCursor(pendingCursor);

        // Load Received Orders
        Cursor receivedCursor = dbHelper.getReceivedOrders();
        addOrdersFromCursor(receivedCursor);

        // Load Picked Up Orders
        Cursor pickedUpCursor = dbHelper.getPickedupOrders();
        addOrdersFromCursor(pickedUpCursor);

        // Update UI based on the results
        if (ordersPendingList.isEmpty()) {
            noOrdersMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noOrdersMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        ordersPendingAdapter.notifyDataSetChanged();
    }

    private void addOrdersFromCursor(Cursor cursor) {
        if (cursor == null) return;

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
    }
}
