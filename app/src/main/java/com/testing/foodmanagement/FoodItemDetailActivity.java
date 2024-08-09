package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodItemDetailActivity extends AppCompatActivity {
    private ImageView imageViewFoodItem;
    private TextView textViewFoodItemName;
    private TextView textViewFoodItemDescription;
    private TextView textViewFoodItemPrice; // Added for price
    private RecyclerView recyclerViewCustomizations;
    private Button buttonAddToBasket;
    private DBHelper dbHelper;
    private FoodItem foodItem;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail1);

        imageViewFoodItem = findViewById(R.id.imageViewFoodItem);
        textViewFoodItemName = findViewById(R.id.textViewFoodItemName);
        textViewFoodItemDescription = findViewById(R.id.textViewFoodItemDescription);
        textViewFoodItemPrice = findViewById(R.id.textViewFoodItemPrice); // Initialize the price TextView
        recyclerViewCustomizations = findViewById(R.id.recyclerViewCustomizations);
        buttonAddToBasket = findViewById(R.id.buttonAddToBasket);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        foodItem = (FoodItem) intent.getSerializableExtra("foodItem");

        if (foodItem != null) {
            textViewFoodItemName.setText(foodItem.getName());
            textViewFoodItemDescription.setText(foodItem.getDescription());
            textViewFoodItemPrice.setText(String.format("RS %.2f", foodItem.getPrice())); // Set the price

            byte[] imageByteArray = foodItem.getImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                imageViewFoodItem.setImageBitmap(bitmap);
            } else {
                imageViewFoodItem.setImageResource(R.drawable.noodles);
            }

            loadCustomizations();
        }

        buttonAddToBasket.setOnClickListener(v -> {
            // Add food item to basket logic
        });
    }

    private void loadCustomizations() {
        List<Customization> customizations = dbHelper.getCustomizationsForFoodItem(foodItem.getId());
        CustomizationAdapterUser adapter = new CustomizationAdapterUser(this, customizations);
        recyclerViewCustomizations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCustomizations.setAdapter(adapter);
    }
}
