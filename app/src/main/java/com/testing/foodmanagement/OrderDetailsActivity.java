package com.testing.foodmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView textViewOrderDetails;
    private Button buttonCancelOrder, buttonUpdateOrder;
    private DBHelper dbHelper;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        textViewOrderDetails = findViewById(R.id.textViewOrderDetails);
        buttonCancelOrder = findViewById(R.id.buttonCancelOrder);
        buttonUpdateOrder = findViewById(R.id.buttonUpdateOrder);
        dbHelper = new DBHelper(this);

        // Get orderId from intent
        orderId = getIntent().getIntExtra("orderId", -1);

        if (orderId != -1) {
            // Fetch and display order details
            String orderDetails = dbHelper.getOrderDetails(orderId);
            textViewOrderDetails.setText(orderDetails);
        } else {
            textViewOrderDetails.setText("Error: No order ID received");
            Toast.makeText(this, "Error: No order ID received", Toast.LENGTH_LONG).show();
        }

        buttonCancelOrder.setOnClickListener(v -> cancelOrder());
        buttonUpdateOrder.setOnClickListener(v -> updateOrder());
    }

    private void cancelOrder() {
        if (orderId != -1) {
            dbHelper.cancelOrder(orderId);
            Toast.makeText(this, "Order canceled successfully!", Toast.LENGTH_LONG).show();
            finish(); // Close the activity after canceling
        } else {
            Toast.makeText(this, "Error: No order ID received", Toast.LENGTH_LONG).show();
        }
    }

    private void updateOrder() {
        // Implement update order functionality
        Toast.makeText(this, "Update order functionality not implemented yet.", Toast.LENGTH_LONG).show();
    }
}
