package com.testing.foodmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Set user's name (example)
        TextView textViewAccount = findViewById(R.id.textViewAccount);
        textViewAccount.setText("Indipa Gangoda");

        // Set up buttons and other elements
        Button buttonFavourites = findViewById(R.id.buttonFavourites);
        Button buttonOrders = findViewById(R.id.buttonOrders);

        // Set up button click listeners
        buttonFavourites.setOnClickListener(v -> {
            // Handle Favourites button click
            Toast.makeText(AccountActivity.this, "Favourites clicked", Toast.LENGTH_SHORT).show();
        });

        buttonOrders.setOnClickListener(v -> {
            // Handle Orders button click
            Toast.makeText(AccountActivity.this, "Orders clicked", Toast.LENGTH_SHORT).show();
        });

        // Set up other views and logic as needed
        TextView textViewPromotions = findViewById(R.id.textViewPromotions);
        textViewPromotions.setOnClickListener(v -> {
            // Handle Promotions click
            Toast.makeText(AccountActivity.this, "Promotions clicked", Toast.LENGTH_SHORT).show();
        });

        TextView textViewHelp = findViewById(R.id.textViewHelp);
        textViewHelp.setOnClickListener(v -> {
            // Handle Help click
            Toast.makeText(AccountActivity.this, "Help clicked", Toast.LENGTH_SHORT).show();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(AccountActivity.this, MainActivity2.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_browse) {
                    // Handle navigation to SearchActivity
                    Intent intent = new Intent(AccountActivity.this, SearchActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_account) {
                    Intent intent = new Intent(AccountActivity.this, AccountActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        // Additional views can be set up similarly
    }
}
