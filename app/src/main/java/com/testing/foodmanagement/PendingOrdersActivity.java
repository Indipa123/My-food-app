package com.testing.foodmanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<Order> pendingOrdersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        dbHelper = new DBHelper(this);
        pendingOrdersList = getPendingOrders();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        OrdersAdapter adapter = new OrdersAdapter(this, pendingOrdersList, dbHelper.getWritableDatabase());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Order> getPendingOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("finalOrder", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setItemName(cursor.getString(cursor.getColumnIndexOrThrow("orderName")));
                order.setItemQuantity(cursor.getString(cursor.getColumnIndexOrThrow("orderQuantity")));
                order.setItemPrice(cursor.getString(cursor.getColumnIndexOrThrow("orderPrice")));
                order.setBranch(cursor.getString(cursor.getColumnIndexOrThrow("branch")));
                order.setCustomerLocation(cursor.getString(cursor.getColumnIndexOrThrow("customerLocation")));
                order.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));

                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                if (status == null || status.isEmpty()) {
                    status = "Pending"; // Default status
                }
                order.setStatus(status);



                // Additional fields if necessary
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }
}
