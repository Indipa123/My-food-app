package com.testing.foodmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
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

    DBHelper dbHelper;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        dbHelper = new DBHelper(this);
        profileImageView = findViewById(R.id.profileImageView);

        // Retrieve logged-in email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("logged_in_user_email", "");

        // Fetch the first name, last name, and profile picture from the database
        String firstName = dbHelper.getFirstNameByEmail(email);
        String lastName = dbHelper.getLastNameByEmail(email);

        // Set User's name in TextView
        TextView textViewAccount = findViewById(R.id.textViewAccount);
        textViewAccount.setText(firstName + " " + lastName);

        // Display the profile picture if available
        if (!email.isEmpty()) {
            Bitmap profileImage = dbHelper.getProfileImage(email);
            if (profileImage != null) {
                Glide.with(this)
                        .load(profileImage)
                        .transform(new CircleCrop())
                        .into(profileImageView);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_account)
                        .transform(new CircleCrop())
                        .into(profileImageView);
            }
        }

        profileImageView.setOnClickListener(v -> {
            // Navigate to ActivityUserInfo
            Intent intent = new Intent(AccountActivity.this, ActivityUserInfo.class);
            intent.putExtra("EMAIL", email);
            startActivity(intent);
        });

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
