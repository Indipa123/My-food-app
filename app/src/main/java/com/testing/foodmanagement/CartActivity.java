package com.testing.foodmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private RecyclerView recyclerViewCart;
    private TextView textViewTotalPrice;
    private Button buttonCheckout;
    private List<FoodItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        dbHelper = new DBHelper(this);

        // Fetch cart items from the database if needed
        // cartItems = dbHelper.getAllCartItems();

        // Get the data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("cartItems")) {
            cartItems = (List<FoodItem>) intent.getSerializableExtra("cartItems");
        }

        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

        // Set up the RecyclerView
        CartAdapter cartAdapter = new CartAdapter(this, cartItems);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Update the total price
        textViewTotalPrice.setText(String.format("Total: RS %.2f", totalPrice));

        // Set a click listener for the checkout button
        buttonCheckout.setOnClickListener(v -> {

        });
    }
}
