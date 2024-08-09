package com.testing.foodmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class CustomizationAdapter extends RecyclerView.Adapter<CustomizationAdapter.ViewHolder> {

    private List<Customization> customizationList;
    private Consumer<Customization> onCustomizationSelected;

    public CustomizationAdapter(List<Customization> customizationList, Consumer<Customization> onCustomizationSelected) {
        this.customizationList = customizationList;
        this.onCustomizationSelected = onCustomizationSelected;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPrice;
        public Button buttonUpdate;
        public Button buttonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewCustomizationName);
            textViewPrice = itemView.findViewById(R.id.textViewCustomizationPrice);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdateCustomization);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteCustomization);
        }
    }

    @Override
    public CustomizationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customization_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Customization customization = customizationList.get(position);
        holder.textViewName.setText(customization.getName());
        holder.textViewPrice.setText(String.valueOf(customization.getPrice()));

        holder.buttonUpdate.setOnClickListener(v -> {
            // Handle update customization
            if (onCustomizationSelected != null) {
                onCustomizationSelected.accept(customization);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            // Handle delete customization
            if (onCustomizationSelected != null) {
                onCustomizationSelected.accept(customization);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customizationList.size();
    }
}
