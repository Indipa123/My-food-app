package com.testing.foodmanagement;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
//import android.widget.RecyclerView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodItemDetailActivity1 extends AppCompatActivity {

    private ImageView imageViewFoodItem;
    private TextView textViewFoodItemName;
    private TextView textViewFoodItemDescription;
    private RecyclerView recyclerViewCustomizations;
    private Button buttonAddToBasket;
    private DBHelper dbHelper;
    private int foodItemId;
    private List<Customization> selectedCustomizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail1);

        imageViewFoodItem = findViewById(R.id.imageViewFoodItem);
        textViewFoodItemName = findViewById(R.id.textViewFoodItemName);
        textViewFoodItemDescription = findViewById(R.id.textViewFoodItemDescription);
        recyclerViewCustomizations = findViewById(R.id.recyclerViewCustomizations);
        buttonAddToBasket = findViewById(R.id.buttonAddToBasket);

        dbHelper = new DBHelper(this);
        selectedCustomizations = new ArrayList<>();

        // Get the food item ID passed from the previous activity
        foodItemId = getIntent().getIntExtra("foodItemId", -1);

        if (foodItemId != -1) {
            loadFoodItemDetails(foodItemId);
            loadCustomizations(foodItemId);
        }

        buttonAddToBasket.setOnClickListener(v -> {
            // Handle adding the item with selected customizations to the basket
            addToBasket();
        });
    }

    private void loadFoodItemDetails(int foodItemId) {
        FoodItem foodItem = dbHelper.getFoodItemById(foodItemId);
        if (foodItem != null) {
            textViewFoodItemName.setText(foodItem.getName());
            textViewFoodItemDescription.setText(foodItem.getDescription());

            // Assuming FoodItem has a method getImage() that returns a byte[]
            byte[] image = foodItem.getImage();
            if (image != null) {
                imageViewFoodItem.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            }
        }
    }

    private void loadCustomizations(int foodItemId) {
        List<Customization> customizations = dbHelper.getCustomizationsForFoodItem(foodItemId);

        // Assuming you have a CustomizationAdapter to populate the RecyclerView
        CustomizationAdapter customizationAdapter = new CustomizationAdapter(customizations, selectedCustomization -> {
            if (selectedCustomizations.contains(selectedCustomization)) {
                selectedCustomizations.remove(selectedCustomization);
            } else {
                selectedCustomizations.add(selectedCustomization);
            }
        });

        recyclerViewCustomizations.setAdapter(customizationAdapter);
    }

    private void addToBasket() {
        // Logic to add the food item with selected customizations to the basket
        // You might need to create an Order class or similar to manage the basket
    }
}
