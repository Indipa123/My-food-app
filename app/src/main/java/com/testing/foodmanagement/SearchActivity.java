package com.testing.foodmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private GridView gridViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.search_edit_text);

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

        gridViewCategories = findViewById(R.id.gridViewCategories);

        dbHelper = new DBHelper(this);

        // Initialize category list
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(this, categoryList);
        gridViewCategories.setAdapter(categoryAdapter);

        // Load categories from the database
        loadCategories();

        // Set item click listener for GridView
        gridViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected category
                Category selectedCategory = categoryList.get(position);

                // Start CategoryFoodItemsActivity with the selected category name
                Intent intent = new Intent(SearchActivity.this, CategoryFoodItemsActivity.class);
                intent.putExtra("categoryName", selectedCategory.getCategoryName());
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(SearchActivity.this, MainActivity2.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_browse) {
                    // Handle navigation to SearchActivity
                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_account) {
                    // Handle navigation to AccountActivity
                    Intent intent = new Intent(SearchActivity.this, AccountActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
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
            Intent intent = new Intent(SearchActivity.this, FoodItemDetailActivity1.class);
            intent.putExtra("foodName", matchedItem.getName());
            intent.putExtra("foodCategory", matchedItem.getCategory());
            intent.putExtra("foodPrice", matchedItem.getPrice());
            intent.putExtra("foodImage", matchedItem.getImage());
            startActivity(intent);
        } else {
            TextView noResultsTextView = new TextView(this);
            noResultsTextView.setText("No matching food item found.");

        }
    }

    private void loadCategories() {
        categoryList.clear();
        List<Category> categories = dbHelper.getAllCategories();
        categoryList.addAll(categories);
        categoryAdapter.notifyDataSetChanged();
    }
}
