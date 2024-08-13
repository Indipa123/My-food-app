package com.testing.foodmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
            // Show options to Update or Delete
            new AlertDialog.Builder(this)
                    .setTitle("Choose Action")
                    .setItems(new CharSequence[]{"Update", "Delete"}, (dialog, which) -> {
                        if (which == 0) {
                            // Handle Update Customization
                            showUpdateCustomizationDialog(selectedCustomization);
                        } else if (which == 1) {
                            // Handle Delete Customization
                            new AlertDialog.Builder(this)
                                    .setTitle("Delete Customization")
                                    .setMessage("Are you sure you want to delete this customization?")
                                    .setPositiveButton(android.R.string.yes, (deleteDialog, deleteWhich) -> {
                                        // Remove customization from the database
                                        dbHelper.deleteCustomization(selectedCustomization.getId());

                                        // Remove customization from the list and notify the adapter
                                        customizations.remove(selectedCustomization);
                                        customizationAdapter.notifyDataSetChanged();

                                        Toast.makeText(this, "Customization deleted", Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();
                        }
                    })
                    .show();
        });
        recyclerViewCustomizations.setAdapter(customizationAdapter);
    }
    private void showUpdateCustomizationDialog(Customization customization) {
        // Inflate your dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_update_customization, null);

        // Get references to the dialog's UI elements
        EditText editTextName = dialogView.findViewById(R.id.editTextCustomizationName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextCustomizationPrice);

        // Prepopulate fields with existing customization data
        editTextName.setText(customization.getName());
        editTextPrice.setText(String.valueOf(customization.getPrice()));

        // Show the dialog
        new AlertDialog.Builder(this)
                .setTitle("Update Customization")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    // Get updated values
                    String updatedName = editTextName.getText().toString().trim();
                    double updatedPrice = Double.parseDouble(editTextPrice.getText().toString().trim());

                    // Update customization object
                    customization.setName(updatedName);
                    customization.setPrice(updatedPrice);

                    // Update in database
                    dbHelper.updateCustomization(customization);

                    // Notify adapter of changes
                    customizationAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "Customization updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



}
