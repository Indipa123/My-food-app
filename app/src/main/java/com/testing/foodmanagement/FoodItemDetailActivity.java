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

import java.util.ArrayList;
import java.util.List;

public class FoodItemDetailActivity extends AppCompatActivity {
    private ImageView imageViewFoodItem;
    private TextView textViewFoodItemName;
    private TextView textViewFoodItemDescription;
    private TextView textViewFoodItemPrice;
    private RecyclerView recyclerViewCustomizations;
    private Button buttonAddToBasket;
    private DBHelper dbHelper;
    private FoodItem foodItem;
    private CustomizationAdapterUser adapter;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail1);

        imageViewFoodItem = findViewById(R.id.imageViewFoodItem);
        textViewFoodItemName = findViewById(R.id.textViewFoodItemName);
        textViewFoodItemDescription = findViewById(R.id.textViewFoodItemDescription);
        textViewFoodItemPrice = findViewById(R.id.textViewFoodItemPrice);
        recyclerViewCustomizations = findViewById(R.id.recyclerViewCustomizations);
        buttonAddToBasket = findViewById(R.id.buttonAdd);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        foodItem = (FoodItem) intent.getSerializableExtra("foodItem");

        if (foodItem != null) {
            textViewFoodItemName.setText(foodItem.getName());
            textViewFoodItemDescription.setText(foodItem.getDescription());
            textViewFoodItemPrice.setText(String.format("RS %.2f", foodItem.getPrice()));

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
            // Collect selected customizations
            List<Customization> selectedCustomizations = adapter.getSelectedCustomizations();
            foodItem.setSelectedCustomizations(selectedCustomizations);

            // Calculate the total price
            double totalPrice = foodItem.getTotalPrice();

            // Create an intent to pass data to CartActivity
            Intent cartIntent = new Intent(FoodItemDetailActivity.this, CartActivity.class);
            cartIntent.putExtra("cartItems", new ArrayList<FoodItem>() {{
                add(foodItem);
            }});
            cartIntent.putExtra("totalPrice", totalPrice);

            startActivity(cartIntent);
        });
    }

    private void loadCustomizations() {
        List<Customization> customizations = dbHelper.getCustomizationsForFoodItem(foodItem.getId());
        adapter = new CustomizationAdapterUser(this, customizations);
        recyclerViewCustomizations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCustomizations.setAdapter(adapter);
    }
}
