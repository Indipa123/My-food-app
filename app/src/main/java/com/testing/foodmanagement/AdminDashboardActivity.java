package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Find the CardView for adding a menu
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView cardViewMenu;
        cardViewMenu = findViewById(R.id.card_view_menu);

        // Set a click listener on the CardView

        CardView cardAddCate = findViewById(R.id.card_add_categories);
        cardAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddCategoryActivity.class);
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

        cardViewMenu.setOnClickListener(new View.OnClickListener() {
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
    }
}
