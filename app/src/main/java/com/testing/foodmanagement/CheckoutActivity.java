package com.testing.foodmanagement;

import android.os.Bundle;
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
    private CheckoutAdapter checkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCheckoutItems = findViewById(R.id.recyclerViewCart);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        dbHelper = new DBHelper(this);

        // Retrieve the Cart ID passed from CartInfoActivity
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
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : checkoutItemList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        textViewTotalPrice.setText(String.format("Total: LKR %.2f", totalPrice));
    }
}
