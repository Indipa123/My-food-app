package com.testing.foodmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FoodItemDetailActivity1 extends AppCompatActivity {

    private ImageView foodImageView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        foodImageView = findViewById(R.id.foodImageView);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        TextView textViewPrice = findViewById(R.id.textViewPrice);

        // Get the food item details from the intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("foodName");
        String category = intent.getStringExtra("foodCategory");
        double price = intent.getDoubleExtra("foodPrice", 0.0);
        byte[] imageByteArray = intent.getByteArrayExtra("foodImage");
        int foodId = intent.getIntExtra("foodId", -1);

        // Set the food item details to the views
        textViewName.setText(name);
        textViewCategory.setText(category);
        textViewPrice.setText(String.valueOf(price));

        if (imageByteArray != null && imageByteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            foodImageView.setImageBitmap(bitmap);
        } else {
            foodImageView.setImageResource(R.drawable.noodles); // Default image
        }

        getFoodItemsByName(name);

    }

    private void getFoodItemsByName(String name) {

        DBHelper dbHelper = new DBHelper(this);
        List<FoodItem> foodItems = dbHelper.getFoodItemsByName(name);

        // Assuming you have a view to display the food items, you'll need to inflate and add them dynamically.
        // For example, you might want to add them to a LinearLayout or RecyclerView.
        // Example: Assuming `foodItemView` is a layout item in your XML to display a single food item.

        for (FoodItem foodItem : foodItems) {
            View foodItemView = LayoutInflater.from(this).inflate(R.layout.fragment_customer_menu, null);

            // Set the views with foodItem details
            // Assuming there are TextViews or other views to show the food item data in `food_item_view`

            // Add the `foodItemView` to your main layout
            // Example:
            // ((LinearLayout) findViewById(R.id.foodItemContainer)).addView(foodItemView);

            // Set OnClickListener to navigate to FoodItemDetailActivity
            foodImageView.setOnClickListener(v -> {
                Intent intent = new Intent(FoodItemDetailActivity1.this, FoodItemDetailActivity.class);
                intent.putExtra("foodItem", foodItem); // Assuming FoodItem is Serializable or Parcelable
                startActivity(intent);
            });
        }
    }
}
