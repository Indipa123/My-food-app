package com.testing.foodmanagement;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ViewMenuActivity extends AppCompatActivity {
    private GridView gridViewFoodItems;
    private FoodItemAdapter foodItemAdapter;
    private List<FoodItem> foodItemList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        gridViewFoodItems = findViewById(R.id.gridViewFoodItems);

        dbHelper = new DBHelper(this);

        // Initialize food item list
        foodItemList = new ArrayList<>();

        foodItemAdapter = new FoodItemAdapter(this, foodItemList);
        gridViewFoodItems.setAdapter(foodItemAdapter);

        // Load food items from the database
        loadFoodItems();

    }

    private void loadFoodItems() {
        foodItemList.clear();
        List<FoodItem> items = dbHelper.getAllFoodItems(); // Implement this method in DBHelper
        foodItemList.addAll(items);
        foodItemAdapter.notifyDataSetChanged();
    }

}
