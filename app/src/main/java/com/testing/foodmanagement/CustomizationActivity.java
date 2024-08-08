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

        recyclerViewCustomizations = findViewById(R.id.recyclerViewCustomizations);
        dbHelper = new DBHelper(this);

        setupRecyclerView();

        // Initialize UI elements
        editTextCustomizationName = findViewById(R.id.editTextCustomizationName);
        editTextCustomizationPrice = findViewById(R.id.editTextCustomizationPrice);
        buttonAddCustomization = findViewById(R.id.buttonAddCustomization);

        // Set up button click listener
        buttonAddCustomization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomization();
            }
        });
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

        DBHelper dbHelper = new DBHelper(this);
        long result = dbHelper.addCustomization(customization);

        if (result != -1) {
            Toast.makeText(this, "Customization added successfully", Toast.LENGTH_SHORT).show();
            // Optionally, clear the input fields or finish the activity
            editTextCustomizationName.setText("");
            editTextCustomizationPrice.setText("");
        } else {
            Toast.makeText(this, "Error adding customization", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupRecyclerView() {
        recyclerViewCustomizations.setLayoutManager(new LinearLayoutManager(this));

        List<Customization> customizations = dbHelper.getAllCustomizations();
        customizationAdapter = new CustomizationAdapter(customizations);
        recyclerViewCustomizations.setAdapter(customizationAdapter);
    }
}

