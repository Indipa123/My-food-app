package com.testing.foodmanagement;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCheckoutItems;
    private TextView textViewTotalPrice;
    private DBHelper dbHelper;
    private List<CartItem> checkoutItemList;
    private int cartId;
    private Button buttonCheckout;
    private CheckoutAdapter checkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCheckoutItems = findViewById(R.id.recyclerViewCart);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        dbHelper = new DBHelper(this);

        // Retrieve the Cart ID passed from CartInfoActivity
        String email = getIntent().getStringExtra("EMAIL");
        Log.d(TAG, "Received email: " + email);
        cartId = getIntent().getIntExtra("cartId", -1);

        if (cartId != -1) {
            // Fetch the items in the selected cart from the database
            checkoutItemList = dbHelper.getCartItemsByCartId(cartId);

            // Calculate and display the total price
            updateTotalPrice();

            // Setup the RecyclerView with the CheckoutAdapter
            checkoutAdapter = new CheckoutAdapter(this, checkoutItemList);
            recyclerViewCheckoutItems.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCheckoutItems.setAdapter(checkoutAdapter);
        } else {
            // Handle the error scenario where no valid Cart ID was provided
            textViewTotalPrice.setText("Error: No cart found");
        }
        buttonCheckout.setOnClickListener(v -> {
            // Create an Intent to start CheckoutActivity2
            Intent intent = new Intent(CheckoutActivity.this, CheckoutActivity2.class);

            // Pass the cartId to CheckoutActivity2
            intent.putExtra("EMAIL", email);
            intent.putExtra("cartId", cartId);
            // Start CheckoutActivity2
            startActivity(intent);
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : checkoutItemList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        textViewTotalPrice.setText(String.format("Total: LKR %.2f", totalPrice));
    }

}
