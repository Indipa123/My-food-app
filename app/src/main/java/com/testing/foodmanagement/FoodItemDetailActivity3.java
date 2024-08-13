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

public class FoodItemDetailActivity3 extends AppCompatActivity {
    private ImageView imageViewFoodItem;
    private TextView textViewFoodItemName;
    private TextView textViewFoodItemDescription;
    private TextView textViewFoodItemPrice;
    private RecyclerView recyclerViewCustomizations;
    private Button buttonUpdate;
    private DBHelper dbHelper;
    private CartItem cartItem;
    private CustomizationAdapterUser adapter;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail3);

        imageViewFoodItem = findViewById(R.id.imageViewFoodItem);
        textViewFoodItemName = findViewById(R.id.textViewFoodItemName);
        textViewFoodItemDescription = findViewById(R.id.textViewFoodItemDescription);
        textViewFoodItemPrice = findViewById(R.id.textViewFoodItemPrice);
        recyclerViewCustomizations = findViewById(R.id.recyclerViewCustomizations);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        cartItem = (CartItem) intent.getSerializableExtra("cartItem");

        if (cartItem != null) {
            textViewFoodItemName.setText(cartItem.getName());
            textViewFoodItemPrice.setText(String.format("RS %.2f", cartItem.getPrice()));

            byte[] imageByteArray = cartItem.getImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                imageViewFoodItem.setImageBitmap(bitmap);
            } else {
                imageViewFoodItem.setImageResource(R.drawable.noodles); // Change this to your placeholder image
            }

            loadCustomizations(cartItem.getId());
        }

        buttonUpdate.setOnClickListener(v -> {
            // Collect selected customizations
            List<Customization> selectedCustomizations = adapter.getSelectedCustomizations();

            // Build the name with customizations
            StringBuilder customizedName = new StringBuilder(cartItem.getName());
            if (!selectedCustomizations.isEmpty()) {
                customizedName.append(" with ");
                for (int i = 0; i < selectedCustomizations.size(); i++) {
                    if (i > 0) {
                        customizedName.append(", ");
                    }
                    customizedName.append(selectedCustomizations.get(i).getName());
                }
            }

            // Create an updated CartItem with the new details
            CartItem updatedCartItem = new CartItem(
                    cartItem.getId(), // Use existing cart item ID
                    customizedName.toString(), // Name with customizations
                    calculateTotalPrice(cartItem.getPrice(), selectedCustomizations), // Update price with customizations
                    cartItem.getQuantity(), // Retain existing quantity
                    cartItem.getImage(), // Retain existing image
                    selectedCustomizations // Set selected customizations
            );

            dbHelper.updateCartItem(updatedCartItem);

            // Return to CartInfoActivity with the updated cart item
            Intent cartIntent = new Intent(FoodItemDetailActivity3.this, CartInfoActivity.class);
            startActivity(cartIntent);
            finish();
        });
    }

    private void loadCustomizations(int foodItemId) {
        List<Customization> customizations = dbHelper.getCustomizationsForFoodItem(foodItemId);
        adapter = new CustomizationAdapterUser(this, customizations);
        recyclerViewCustomizations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCustomizations.setAdapter(adapter);
    }

    private double calculateTotalPrice(double basePrice, List<Customization> customizations) {
        double totalPrice = basePrice;
        for (Customization customization : customizations) {
            totalPrice += customization.getPrice();
        }
        return totalPrice;
    }
}
