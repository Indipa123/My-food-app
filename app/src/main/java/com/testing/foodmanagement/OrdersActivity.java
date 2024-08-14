package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private OrdersAdapter ordersAdapter;
    private ArrayList<Order> ordersList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        dbHelper = new DBHelper(this);

        // Populate spinner with branch names
        Spinner branchFilterSpinner = findViewById(R.id.branch_filter_spinner);
        List<String> branchNames = dbHelper.getAllBranchNames();
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branchNames);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchFilterSpinner.setAdapter(branchAdapter);

        RecyclerView ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersList = dbHelper.getAllOrders();
        ordersAdapter = new OrdersAdapter(ordersList, dbHelper.getWritableDatabase());
        ordersRecyclerView.setAdapter(ordersAdapter);

        // Set up the filter functionality by branch
        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String selectedBranch = branchFilterSpinner.getSelectedItem().toString();
                ordersList.clear();
                ordersList.addAll(dbHelper.getOrdersByBranch(selectedBranch));
                ordersAdapter.notifyDataSetChanged();
            }
        });
    }
}
