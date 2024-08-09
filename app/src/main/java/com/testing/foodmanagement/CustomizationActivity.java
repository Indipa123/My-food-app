package com.testing.foodmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class CustomizationActivity extends AppCompatActivity {
    private EditText editTextCustomizationName;
    private EditText editTextCustomizationPrice;
    private Button buttonAddCustomization;
    private RecyclerView recyclerViewCustomizations;
    private DBHelper dbHelper;
    private CustomizationAdapter customizationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customization);

        // Initialize UI elements
        recyclerViewCustomizations = findViewById(R.id.recyclerViewCustomizations);
        editTextCustomizationName = findViewById(R.id.editTextCustomizationName);
        editTextCustomizationPrice = findViewById(R.id.editTextCustomizationPrice);
        buttonAddCustomization = findViewById(R.id.buttonAddCustomization);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Setup RecyclerView
        setupRecyclerView();

        // Set up button click listener
        buttonAddCustomization.setOnClickListener(v -> addCustomization());
    }

    private void addCustomization() {
        String name = editTextCustomizationName.getText().toString().trim();
        String priceString = editTextCustomizationPrice.getText().toString().trim();

        if (name.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Customization object and add it to the database
        Customization customization = new Customization();
        customization.setName(name);
        customization.setPrice(price);

        long result = dbHelper.addCustomization(customization);

        if (result != -1) {
            Toast.makeText(this, "Customization added successfully", Toast.LENGTH_SHORT).show();
            // Clear the input fields
            editTextCustomizationName.setText("");
            editTextCustomizationPrice.setText("");
            // Refresh the RecyclerView
            setupRecyclerView();
        } else {
            Toast.makeText(this, "Error adding customization", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        recyclerViewCustomizations.setLayoutManager(new LinearLayoutManager(this));

        List<Customization> customizations = dbHelper.getAllCustomizations();
        customizationAdapter = new CustomizationAdapter(customizations, selectedCustomization -> {
            // Handle update or delete customization
            // For example, show a dialog or new activity for update/delete
            // You could also directly handle it here, like:
            // Toast.makeText(this, "Selected: " + selectedCustomization.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerViewCustomizations.setAdapter(customizationAdapter);
    }
}
