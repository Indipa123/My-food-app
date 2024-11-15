package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView pendingOrdersCount,completedOrdersCount;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DBHelper(this);

        pendingOrdersCount = findViewById(R.id.pendingOrdersCount);
        completedOrdersCount = findViewById(R.id.completedOrdersCount);

        // Set a click listener on the CardView

        CardView cardAddCate = findViewById(R.id.card_add_categories);
        cardAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        CardView orderCard = findViewById(R.id.ordercard);
        orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });

        CardView viewCateCard = findViewById(R.id.card_view_categories);
        viewCateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ViewCategoryActivity.class);
                startActivity(intent);
            }
        });

        CardView viewMenuCard = findViewById(R.id.card_view_menu);
        viewMenuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddMenuActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewMenuActivity.class);
                startActivity(intent);
            }
        });

        CardView cardAddMenu = findViewById(R.id.card_add_menu);

        cardAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddMenuActivity.class);
                startActivity(intent);
            }
        });
        CardView addBranchCard = findViewById(R.id.addBranch);
        addBranchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddBranchActivity.class);
                startActivity(intent);
            }
        });


        CardView viewBranchesCard = findViewById(R.id.viewBranch);

        viewBranchesCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ViewBranchesActivity.class);
            startActivity(intent);
        });

        CardView customizCard = findViewById(R.id.customizCard);
        customizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddCustomizationActivity
                Intent intent = new Intent(AdminDashboardActivity.this, CustomizationActivity.class);
                startActivity(intent);
            }
        });

        CardView PromoCard = findViewById(R.id.promocard);

        PromoCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddPromotionActivity.class);
            startActivity(intent);
        });

        CardView BranchCard = findViewById(R.id.shippercard);

        BranchCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, PendingOrdersActivity.class);
            startActivity(intent);
        });
        loadOrderCounts();
    }
    private void loadOrderCounts() {
        int pendingCount = dbHelper.getPendingOrdersCount();
        int preparedCount = dbHelper.getPreparedOrdersCount();
        int receivedCount = dbHelper.getReceivedOrdersCount();
        int comleteCount = dbHelper.getCompletedOrdersCount();


        // Set the count to TextView (if you want to show all three counts)
        pendingOrdersCount.setText("Pending: " + pendingCount + "\nPrepared: " + preparedCount + "\nReceived: " + receivedCount);
        completedOrdersCount.setText("Completed :"+ comleteCount);
    }
    }

