package com.testing.foodmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private LinearLayout linearLayoutRecentlyAdded;
    private LinearLayout linearLayoutCategories;
    private CardView cardViewFoodItems;
    private LinearLayout searchBar;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main2);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize views
        linearLayoutRecentlyAdded = findViewById(R.id.linearLayoutRecentlyAdded);
        linearLayoutCategories = findViewById(R.id.linearLayoutCategories);
        cardViewFoodItems = findViewById(R.id.cardViewFoodItems);
        searchBar = findViewById(R.id.searchBar);
        searchEditText = findViewById(R.id.search_edit_text);

        // Display recently added items
        displayRecentlyAddedItems();
        displayCategories();

        // Set click listener for food items card view
        cardViewFoodItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, CustomerMenuActivity.class);
                startActivity(intent);
            }
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
            categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity2.this, CategoryFoodItemsActivity.class);
                    intent.putExtra("categoryName", category.getCategoryName());
                    startActivity(intent);
                }
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
            Intent intent = new Intent(MainActivity2.this, FoodItemDetailActivity.class);
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
        // Logic to initialize the map
        LatLng location = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
