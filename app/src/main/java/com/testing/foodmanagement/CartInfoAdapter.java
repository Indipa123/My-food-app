package com.testing.foodmanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartInfoAdapter extends RecyclerView.Adapter<CartInfoAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private DBHelper dbHelper;
    private OnItemChangeListener onItemChangeListener;

    public interface OnItemChangeListener {
        void onItemDeleted();
    }

    public CartInfoAdapter(Context context, List<CartItem> cartItemList, DBHelper dbHelper, OnItemChangeListener onItemChangeListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.dbHelper = dbHelper;
        this.onItemChangeListener = onItemChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        // Set image
        byte[] image = cartItem.getImage();
        if (image != null && image.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.imageViewCartItem.setImageBitmap(bitmap);
        } else {
            holder.imageViewCartItem.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set name
        holder.textViewCartItemName.setText(cartItem.getName());

        // Set price
        holder.textViewCartItemPrice.setText(String.format("LKR %.2f", cartItem.getPrice()));

        // Handle delete button
        holder.buttonDeleteCartItem.setOnClickListener(v -> {
            dbHelper.deleteCartItem(cartItem.getId());
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
            onItemChangeListener.onItemDeleted();
        });

        // Handle update button
        holder.buttonUpdateCartItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodItemDetailActivity3.class);
            intent.putExtra("cartItem", cartItem);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewCartItem;
        TextView textViewCartItemName, textViewCartItemPrice;
        Button buttonDeleteCartItem, buttonUpdateCartItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCartItem = itemView.findViewById(R.id.imageViewCartItem);
            textViewCartItemName = itemView.findViewById(R.id.textViewCartItemName);
            textViewCartItemPrice = itemView.findViewById(R.id.textViewCartItemPrice);
            buttonDeleteCartItem = itemView.findViewById(R.id.buttonDeleteCartItem);
            buttonUpdateCartItem = itemView.findViewById(R.id.buttonUpdateCartItem);
        }
    }
}
