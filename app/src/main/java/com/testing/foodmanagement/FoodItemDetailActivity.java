package com.testing.foodmanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ImageView foodImageView = findViewById(R.id.foodImageView);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        TextView textViewPrice = findViewById(R.id.textViewPrice);

        // Get the food item details from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("foodName");
            String category = extras.getString("foodCategory");
            double price = extras.getDouble("foodPrice");
            byte[] imageByteArray = extras.getByteArray("foodImage");

            textViewName.setText(name);
            textViewCategory.setText(category);
            textViewPrice.setText(String.valueOf(price));

            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                foodImageView.setImageBitmap(bitmap);
            } else {
                foodImageView.setImageResource(R.drawable.noodles); // Default image
            }
        }
    }
}
