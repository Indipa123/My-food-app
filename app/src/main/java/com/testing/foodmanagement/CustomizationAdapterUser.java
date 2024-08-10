package com.testing.foodmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomizationAdapterUser extends RecyclerView.Adapter<CustomizationAdapterUser.CustomizationViewHolder> {
    private Context context;
    private List<Customization> customizations;
    private List<Customization> selectedCustomizations = new ArrayList<>();

    public CustomizationAdapterUser(Context context, List<Customization> customizations) {
        this.context = context;
        this.customizations = customizations;
    }

    @NonNull
    @Override
    public CustomizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customization, parent, false);
        return new CustomizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomizationViewHolder holder, int position) {
        Customization customization = customizations.get(position);
        holder.textViewCustomizationName.setText(customization.getName());
        holder.textViewCustomizationPrice.setText(String.format("RS %.2f", customization.getPrice()));

        // Set the CheckBox listener
        holder.checkBoxCustomization.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCustomizations.add(customization);
            } else {
                selectedCustomizations.remove(customization);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customizations.size();
    }

    public List<Customization> getSelectedCustomizations() {
        return selectedCustomizations;
    }

    public static class CustomizationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustomizationName;
        TextView textViewCustomizationPrice;
        CheckBox checkBoxCustomization; // Add CheckBox for selection

        public CustomizationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomizationName = itemView.findViewById(R.id.textViewCustomizationName);
            textViewCustomizationPrice = itemView.findViewById(R.id.textViewCustomizationPrice);
            checkBoxCustomization = itemView.findViewById(R.id.checkBoxSelectCustomization); // Initialize CheckBox
        }
    }
}
