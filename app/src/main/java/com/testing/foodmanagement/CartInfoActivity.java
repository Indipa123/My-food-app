package com.testing.foodmanagement;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCartItems;
    private TextView textViewTotalPrice;
    private DBHelper dbHelper;
    private CartInfoAdapter cartInfoAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_info);

        String email = getIntent().getStringExtra("EMAIL");
        Log.d(TAG, "Received email: " + email);

        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        dbHelper = new DBHelper(this);

        // Fetch all cart items from the database
        cartItemList = dbHelper.getAllCartItems();

        // Calculate and display the total price
        updateTotalPrice();

        // Setup the RecyclerView with the CartInfoAdapter
        cartInfoAdapter = new CartInfoAdapter(this, cartItemList, dbHelper, new CartInfoAdapter.OnItemChangeListener() {
            @Override
            public void onItemDeleted() {
                updateTotalPrice();
            }
        }, new CartInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int cartId) {
                // Navigate to CheckoutActivity with the selected Cart ID
                Intent intent = new Intent(CartInfoActivity.this, CheckoutActivity.class);
                intent.putExtra("cartId", cartId);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
            }
        });

        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCartItems.setAdapter(cartInfoAdapter);
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        textViewTotalPrice.setText(String.format("Total: LKR %.2f", totalPrice));
    }
}
