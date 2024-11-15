package com.testing.foodmanagement;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private LinearLayout linearLayoutRecentlyAdded;
    private LinearLayout linearLayoutCategories;
    private LinearLayout linearLayoutPromotions;
    private CardView cardViewFoodItems;
    private LinearLayout searchBar;
    private EditText searchEditText;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main2);

        String email = getIntent().getStringExtra("EMAIL");
        Log.d(TAG, "Received email: " + email);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize views
        linearLayoutRecentlyAdded = findViewById(R.id.linearLayoutRecentlyAdded);
        linearLayoutCategories = findViewById(R.id.linearLayoutCategories);
        linearLayoutPromotions = findViewById(R.id.linearLayoutPromotions);
        cardViewFoodItems = findViewById(R.id.cardViewFoodItems);
        searchBar = findViewById(R.id.searchBar);
        searchEditText = findViewById(R.id.search_edit_text);
        Button basketButton = findViewById(R.id.basket);
        Button rating = findViewById(R.id.rating);

        basketButton.setOnClickListener(view -> {
            // Create an Intent to start CartInfoActivity
            Intent intent = new Intent(MainActivity2.this, CartInfoActivity.class);
            intent.putExtra("EMAIL", email);
            // Start the new activity
            startActivity(intent);
        });

        rating.setOnClickListener(view -> {
            // Create an Intent to start CartInfoActivity
            Intent intent = new Intent(MainActivity2.this, PendingOrdersActivity.class);
            intent.putExtra("EMAIL", email);
            // Start the new activity
            startActivity(intent);
        });

        // Display recently added items
        displayRecentlyAddedItems();
        displayCategories();
        displayPromotions();

        // Set click listener for food items card view
        cardViewFoodItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, CustomerMenuActivity.class);
            startActivity(intent);
        });

        // Add the search functionality
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String searchQuery = searchEditText.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    searchFoodItem(searchQuery);
                }
                return true;
            }
            return false;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Handle navigation to Home
                // Replace with your fragment transaction or activity launch code
                return true;
            } else if (itemId == R.id.nav_browse) {
                // Handle navigation to SearchActivity
                Intent intent = new Intent(MainActivity2.this, SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_account) {
                // Handle navigation to AccountActivity
                Intent intent = new Intent(MainActivity2.this, AccountActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void displayRecentlyAddedItems() {
        DBHelper dbHelper = new DBHelper(this);
        List<FoodItem> recentItems = dbHelper.getLastFiveAddedFoodItems(); // Method to get the last 5 items

        LayoutInflater inflater = LayoutInflater.from(this);
        for (FoodItem foodItem : recentItems) {
            View foodItemView = inflater.inflate(R.layout.fragment_customer_menu, linearLayoutRecentlyAdded, false);

            ImageView foodImageView = foodItemView.findViewById(R.id.foodImageView);
            TextView textViewName = foodItemView.findViewById(R.id.textViewName);
            TextView textViewCategory = foodItemView.findViewById(R.id.textViewCategory);
            TextView textViewPrice = foodItemView.findViewById(R.id.textViewPrice);

            textViewName.setVisibility(View.VISIBLE);

            // Decode the byte array into a bitmap
            byte[] imageByteArray = foodItem.getImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                foodImageView.setImageBitmap(bitmap);
            } else {
                // Set a default image or placeholder if the imageByteArray is null or empty
                foodImageView.setImageResource(R.drawable.noodles);
            }

            textViewName.setText(foodItem.getName());
            textViewCategory.setText(foodItem.getCategory());
            textViewPrice.setText(String.valueOf(foodItem.getPrice()));

            linearLayoutRecentlyAdded.addView(foodItemView);
        }
    }

    private void displayCategories() {
        DBHelper dbHelper = new DBHelper(this);
        List<Category> categories = dbHelper.getAllCategories();

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Category category : categories) {
            View categoryView = inflater.inflate(R.layout.categories, linearLayoutCategories, false);

            ImageView categoryImageView = categoryView.findViewById(R.id.categoryImageView);
            TextView textViewCategoryName = categoryView.findViewById(R.id.categoryNameTextView);

            // Decode the byte array into a bitmap
            byte[] imageByteArray = category.getCategoryImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                categoryImageView.setImageBitmap(bitmap);
            } else {
                // Set a default image or placeholder if the imageByteArray is null or empty
                categoryImageView.setImageResource(R.drawable.ic_category_placeholder);
            }

            textViewCategoryName.setText(category.getCategoryName());

            // Set the OnClickListener for the category view
            categoryView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity2.this, CategoryFoodItemsActivity.class);
                intent.putExtra("categoryName", category.getCategoryName());
                startActivity(intent);
            });

            linearLayoutCategories.addView(categoryView);
        }
    }

    private void searchFoodItem(String query) {
        DBHelper dbHelper = new DBHelper(this);
        List<FoodItem> allItems = dbHelper.getAllFoodItems(); // Method to get all items

        // Find the item that matches the search query exactly
        FoodItem matchedItem = null;
        for (FoodItem item : allItems) {
            if (item.getName().equalsIgnoreCase(query)) {
                matchedItem = item;
                break;
            }
        }

        if (matchedItem != null) {
            // Start the FoodDetailActivity with the matched food item details
            Intent intent = new Intent(MainActivity2.this, FoodItemDetailActivity1.class);
            intent.putExtra("foodName", matchedItem.getName());
            intent.putExtra("foodCategory", matchedItem.getCategory());
            intent.putExtra("foodPrice", matchedItem.getPrice());
            intent.putExtra("foodImage", matchedItem.getImage());
            startActivity(intent);
        } else {
            // Handle the case where no matching item is found
            // For now, we will clear the recently added section
            linearLayoutRecentlyAdded.removeAllViews();

            // Display a message or take appropriate action
            TextView noResultsTextView = new TextView(this);
            noResultsTextView.setText("No matching food item found.");
            linearLayoutRecentlyAdded.addView(noResultsTextView);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DBHelper dbHelper = new DBHelper(this);
        List<Branch> branches = dbHelper.getAllBranches(); // Method to get all branches

        LatLng firstBranchLocation = null;

        for (Branch branch : branches) {
            LatLng latLng = getLatLngFromAddress(branch.getLocation());
            if (latLng != null) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(branch.getBranchName()));
                if (firstBranchLocation == null) {
                    firstBranchLocation = latLng;
                }
            } else {
                Toast.makeText(this, "Error getting location for " + branch.getBranchName(), Toast.LENGTH_SHORT).show();
            }
        }

        if (firstBranchLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstBranchLocation, 10));
        }
    }

    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoding error: ", e);
        }
        return null;
    }

    private void displayPromotions() {
        DBHelper dbHelper = new DBHelper(this);
        List<Promotion> promotions = dbHelper.getAllPromotions();

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Promotion promotion : promotions) {
            View categoryView = inflater.inflate(R.layout.promotions, linearLayoutPromotions, false);

            ImageView categoryImageView = categoryView.findViewById(R.id.categoryImageView);
            TextView textViewCategoryName = categoryView.findViewById(R.id.categoryNameTextView);

            // Decode the byte array into a bitmap
            byte[] imageByteArray = promotion.getImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                categoryImageView.setImageBitmap(bitmap);
            } else {
                // Set a default image or placeholder if the imageByteArray is null or empty
                categoryImageView.setImageResource(R.drawable.ic_category_placeholder);
            }

            textViewCategoryName.setText(promotion.getPromotionName());

            categoryView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity2.this, ActivityPromotionDetails.class);
                intent.putExtra("categoryName", promotion.getPromotionName());
                startActivity(intent);
            });

            linearLayoutPromotions.addView(categoryView);
        }
    }
}
