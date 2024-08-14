package com.testing.foodmanagement;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CheckoutActivity2 extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity2";
    private int cartId;
    private TextView textViewTotalPrice, tvCustomerLocation,tvCustomerPhone;
    private LinearLayout llFoodItems;
    private Spinner branchSpinner; // Add Spinner field
    private Button PlaceOrder;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        textViewTotalPrice = findViewById(R.id.tvTotalPrice);
        llFoodItems = findViewById(R.id.llFoodItems);
        tvCustomerLocation = findViewById(R.id.tvCustomerLocation);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        branchSpinner = findViewById(R.id.branchSpinner); // Initialize Spinner
        PlaceOrder = findViewById(R.id.btnPlaceOrder);
        dbHelper = new DBHelper(this);

        // Retrieve the cartId passed from CheckoutActivity
        cartId = getIntent().getIntExtra("cartId", -1);
        String email = getIntent().getStringExtra("EMAIL");
        Log.d(TAG, "Received email: " + email);

        if (cartId != -1) {
            // Log the cartId for debugging
            Log.d(TAG, "Received cartId: " + cartId);

            // Fetch the items in the cart from the database
            List<CartItem> cartItems = dbHelper.getCartItemByCartId(cartId);

            // Display food items and total price
            displayCartItems(cartItems);
            updateTotalPrice(cartItems);

            // Populate the Spinner with branch names
            populateBranchSpinner();
        } else {
            textViewTotalPrice.setText("Error: No cart ID received");
            Toast.makeText(this, "Error: No cart ID received", Toast.LENGTH_LONG).show();
        }
        if (email != null) {
            // Retrieve the user's address from the database
            String userLocation = dbHelper.getCustomerLocation(email);
            String userPhone = dbHelper.getCustomerPhone(email);

            // Display the user's location in the TextView
            tvCustomerLocation.setText(userLocation);
            tvCustomerPhone.setText(userPhone);

            // Set up PlaceOrder button click listener
            PlaceOrder.setOnClickListener(v -> {
                if (branchSpinner.getSelectedItem() != null) {
                    String selectedBranch = branchSpinner.getSelectedItem().toString();
                    String paymentMethod = "cash"; // Set default or get from UI
                    String location = tvCustomerLocation.getText().toString();
                    String phone = tvCustomerPhone.getText().toString();

                    // Insert the order into the database
                    addOrder(email, selectedBranch, paymentMethod, location, phone);

                } else {
                    Toast.makeText(this, "Please select a branch.", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            tvCustomerLocation.setText("Error: No email received");
            tvCustomerPhone.setText("Error: No phone number received");
            Toast.makeText(this, "Error: No email received", Toast.LENGTH_LONG).show();
        }
    }

    private void displayCartItems(List<CartItem> cartItems) {
        llFoodItems.removeAllViews(); // Clear any existing views

        for (CartItem item : cartItems) {
            // Inflate a layout for each item
            TextView itemTextView = new TextView(this);
            itemTextView.setText(item.getName() + " x" + item.getQuantity());
            itemTextView.setPadding(0, 10, 0, 10);
            llFoodItems.addView(itemTextView);
        }
    }

    private void updateTotalPrice(List<CartItem> cartItems) {
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        textViewTotalPrice.setText(String.format("Total: LKR %.2f", totalPrice));
    }

    private void populateBranchSpinner() {
        List<String> branchNames = dbHelper.getAllBranchNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, branchNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(adapter);
    }
    private void addOrder(String email, String branch, String paymentMethod, String location, String phone) {
        // Fetch the cart items
        List<CartItem> cartItems = dbHelper.getCartItemByCartId(cartId);

        int orderId = dbHelper.getNextOrderId();

        // Insert each item into the Orders table

        for (CartItem item : cartItems) {
            dbHelper.addOrder(email, item.getName(), String.valueOf(item.getPrice()),
                    String.valueOf(item.getQuantity()), branch, phone,
                    paymentMethod, location);
        }

        dbHelper.clearCart(cartId);

        // Notify user
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(CheckoutActivity2.this, OrderDetailsActivity.class);
        intent.putExtra("orderId", orderId); // Pass the orderId
        startActivity(intent);

        // Optional: Clear the cart or navigate to another activity
    }
}
