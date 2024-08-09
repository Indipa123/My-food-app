package com.testing.foodmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CustomerMenuActivity extends AppCompatActivity {
    private GridView gridViewFoodItems;
    private FoodItemAdapter foodItemAdapter;
    private List<FoodItem> foodItemList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_menu);

        gridViewFoodItems = findViewById(R.id.gridViewFoodItems);
        dbHelper = new DBHelper(this);

        // Initialize food item list
        foodItemList = new ArrayList<>();

        foodItemAdapter = new FoodItemAdapter(this, foodItemList);
        gridViewFoodItems.setAdapter(foodItemAdapter);

        // Load available food items from the database
        loadAvailableFoodItems();

        // Set item click listener to handle selection
        gridViewFoodItems.setOnItemClickListener((parent, view, position, id) -> {
            FoodItem selectedFoodItem = foodItemList.get(position);

            // Load customizations for the selected food item
            List<Customization> customizations = dbHelper.getCustomizationsForFoodItem(selectedFoodItem.getId());

            // Pass the selected food item and its customizations to the FoodItemDetailActivity
            Intent intent = new Intent(CustomerMenuActivity.this, FoodItemDetailActivity1.class);
            intent.putExtra("foodItem", selectedFoodItem);
            intent.putParcelableArrayListExtra("customizations", (ArrayList<Customization>) customizations);
            startActivity(intent);
        });
    }

    private void loadAvailableFoodItems() {
        foodItemList.clear();
        List<FoodItem> items = dbHelper.getAvailableFoodItems(); // Fetch available food items
        foodItemList.addAll(items);
        foodItemAdapter.notifyDataSetChanged();
    }
}
