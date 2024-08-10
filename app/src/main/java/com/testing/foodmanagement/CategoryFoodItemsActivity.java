package com.testing.foodmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CategoryFoodItemsActivity extends AppCompatActivity {

    private LinearLayout linearLayoutCategoryFoodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_food_items);

        linearLayoutCategoryFoodItems = findViewById(R.id.linearLayoutCategoryFoodItems);

        // Get the category name from the Intent
        String categoryName = getIntent().getStringExtra("categoryName");

        // Display the food items for the selected category
        displayCategoryFoodItems(categoryName);
    }

    private void displayCategoryFoodItems(String categoryName) {
        DBHelper dbHelper = new DBHelper(this);
        List<FoodItem> foodItems = dbHelper.getFoodItemsByCategory(categoryName);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (FoodItem foodItem : foodItems) {
            View foodItemView = inflater.inflate(R.layout.fragment_customer_menu, linearLayoutCategoryFoodItems, false);

            ImageView foodImageView = foodItemView.findViewById(R.id.foodImageView);
            TextView textViewName = foodItemView.findViewById(R.id.textViewName);
            TextView textViewCategory = foodItemView.findViewById(R.id.textViewCategory);
            TextView textViewPrice = foodItemView.findViewById(R.id.textViewPrice);

            // Set food item data
            textViewName.setVisibility(View.VISIBLE);
            textViewPrice.setVisibility(View.VISIBLE);

            byte[] imageByteArray = foodItem.getImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                foodImageView.setImageBitmap(bitmap);
            } else {
                foodImageView.setImageResource(R.drawable.noodles);
            }

            textViewName.setText(foodItem.getName());
            textViewCategory.setText(foodItem.getCategory());
            textViewPrice.setText(String.valueOf(foodItem.getPrice()));


            // Set OnClickListener to navigate to FoodItemDetailActivity
            foodItemView.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryFoodItemsActivity.this, FoodItemDetailActivity.class);
                intent.putExtra("foodItem", foodItem);
                startActivity(intent);
            });

            linearLayoutCategoryFoodItems.addView(foodItemView);
        }
    }

}
