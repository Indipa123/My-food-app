package com.testing.foodmanagement;

import android.os.Bundle;
import android.widget.GridView;
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
    }

    private void loadAvailableFoodItems() {
        foodItemList.clear();
        List<FoodItem> items = dbHelper.getAvailableFoodItems(); // Fetch available food items
        foodItemList.addAll(items);
        foodItemAdapter.notifyDataSetChanged();
    }
}
