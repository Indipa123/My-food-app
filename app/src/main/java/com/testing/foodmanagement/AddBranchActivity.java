package com.testing.foodmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;

public class AddBranchActivity extends AppCompatActivity {

    private EditText branchNameEditText;
    private EditText branchPhoneEditText;
    private EditText branchEmailEditText;
    private EditText branchOpenHoursEditText;
    private Button selectLocationButton;
    private Button registerBranchButton;
    private TextView branchAddressTextView;

    private LatLng selectedLocation;
    private String selectedAddress; // To store the selected address

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        branchNameEditText = findViewById(R.id.branchNameEditText);
        branchPhoneEditText = findViewById(R.id.branchPhoneEditText);
        branchEmailEditText = findViewById(R.id.branchEmailEditText);
        branchOpenHoursEditText = findViewById(R.id.branchOpenHoursEditText);
        selectLocationButton = findViewById(R.id.selectLocationButton);
        registerBranchButton = findViewById(R.id.registerBranchButton);
        branchAddressTextView = findViewById(R.id.selectedAddressTextView); // Updated TextView ID

        selectLocationButton.setOnClickListener(v -> {
            // Launch Google Maps activity to select location
            Intent intent = new Intent(AddBranchActivity.this, SelectLocationActivity.class);
            startActivityForResult(intent, 1);
        });
        registerBranchButton.setOnClickListener(v -> {
            // Register branch with the details
            String branchName = branchNameEditText.getText().toString().trim();
            String phone = branchPhoneEditText.getText().toString().trim();
            String email = branchEmailEditText.getText().toString().trim();
            String openHours = branchOpenHoursEditText.getText().toString().trim();
            String address = branchAddressTextView.getText().toString().trim(); // Use TextView to get address

            // Validate input fields
            if (branchName.isEmpty() || phone.isEmpty() || email.isEmpty() || openHours.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save branch information to database
            DBHelper dbHelper = new DBHelper(this);
            boolean isInserted = dbHelper.addBranch(new Branch(branchName, phone, email, openHours, address)); // Use address

            if (isInserted) {
                Toast.makeText(this, "Branch added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add branch", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Handle location result from Google Maps
            selectedLocation = data.getParcelableExtra("location");
            String address = data.getStringExtra("address");
            if (selectedLocation != null && address != null) {
                // Update TextView with selected address
                branchAddressTextView.setText(address);
                Toast.makeText(this, "Location selected: " + address, Toast.LENGTH_SHORT).show();
                // Store the address for later use when saving to the database
                this.selectedAddress = address;
            }
        }
    }


}
