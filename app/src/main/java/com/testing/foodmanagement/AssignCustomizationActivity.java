package com.testing.foodmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AssignCustomizationActivity extends AppCompatActivity {

    private ListView listViewCustomizations;
    private Button buttonSaveCustomizations;
    private DBHelper dbHelper;
    private CustomizationAdapter adapter;
    private List<Customization> customizations;
    private List<Customization> selectedCustomizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_customization);

        listViewCustomizations = findViewById(R.id.listViewCustomizations);
        buttonSaveCustomizations = findViewById(R.id.buttonSaveCustomizations);

        dbHelper = new DBHelper(this);
        customizations = dbHelper.getAllCustomizations();
        selectedCustomizations = new ArrayList<>();

        adapter = new CustomizationAdapter(this, customizations);
        listViewCustomizations.setAdapter(adapter);

        listViewCustomizations.setOnItemClickListener((parent, view, position, id) -> {
            Customization customization = customizations.get(position);
            CheckBox checkBoxSelected = view.findViewById(R.id.checkBoxSelected);

            if (selectedCustomizations.contains(customization)) {
                selectedCustomizations.remove(customization);
                checkBoxSelected.setChecked(false);
            } else {
                selectedCustomizations.add(customization);
                checkBoxSelected.setChecked(true);
            }

            // Log the selected customizations for debugging
            Log.d("AssignCustomizationActivity", "Selected Customizations: " + selectedCustomizations);
        });

        buttonSaveCustomizations.setOnClickListener(v -> {
            int foodId = getIntent().getIntExtra("FOOD_ID", -1);
            Log.d("AssignCustomizationActivity", "Food ID received: " + foodId);
            if (foodId != -1) {
                List<Integer> customizationIds = new ArrayList<>();
                for (Customization customization : selectedCustomizations) {
                    customizationIds.add(customization.getId());
                }
                Log.d("AssignCustomizationActivity", "Customization IDs to assign: " + customizationIds);
                dbHelper.assignCustomizationsToFoodItem(foodId, customizationIds);
                Toast.makeText(this, "Customizations assigned successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to assign customizations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CustomizationAdapter extends ArrayAdapter<Customization> {

        private final List<Customization> customizations;
        private final LayoutInflater inflater;

        public CustomizationAdapter(Context context, List<Customization> customizations) {
            super(context, R.layout.assign_customization_item, customizations);
            this.customizations = customizations;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.assign_customization_item, parent, false);
            }

            Customization customization = customizations.get(position);

            TextView textViewName = convertView.findViewById(R.id.textViewName);
            TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);
            CheckBox checkBoxSelected = convertView.findViewById(R.id.checkBoxSelected);

            textViewName.setText(customization.getName());
            textViewPrice.setText(String.format(Locale.getDefault(), "%.2f", customization.getPrice()));
            checkBoxSelected.setChecked(selectedCustomizations.contains(customization));

            // Ensure that checkbox state is updated when scrolling
            checkBoxSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!selectedCustomizations.contains(customization)) {
                        selectedCustomizations.add(customization);
                    }
                } else {
                    selectedCustomizations.remove(customization);
                }
                Log.d("AssignCustomizationActivity", "Selected Customizations: " + selectedCustomizations);
            });

            return convertView;
        }
    }
}
