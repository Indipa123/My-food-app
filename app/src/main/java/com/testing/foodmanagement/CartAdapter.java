package com.testing.foodmanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<FoodItem> cartItems;

    public CartAdapter(Context context, List<FoodItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        FoodItem foodItem = cartItems.get(position);

        // Set the food item name with customizations summary
        holder.textViewItemName.setText(foodItem.getCustomizationSummary());

        // Set the total price of the food item
        holder.textViewItemPrice.setText(String.format("RS %.2f", foodItem.getTotalPrice()));

        // Set the food item image
        byte[] imageByteArray = foodItem.getImage();
        if (imageByteArray != null && imageByteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            holder.imageViewItem.setImageBitmap(bitmap);
        } else {
            holder.imageViewItem.setImageResource(R.drawable.noodles); // Default image if no image is available
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        TextView textViewItemPrice;
        ImageView imageViewItem; // ImageView for food item image

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.textViewFoodItemSummary);
            textViewItemPrice = itemView.findViewById(R.id.textViewFoodItemPrice);
            imageViewItem = itemView.findViewById(R.id.imageViewFoodItem); // Initialize the ImageView
        }
    }
}
