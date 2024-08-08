package com.testing.foodmanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {
    private LayoutInflater inflater;
    private List<FoodItem> foodItemList;
    private static final String TAG = "FoodItemAdapter";

    public FoodItemAdapter(Context context, List<FoodItem> foodItems) {
        super(context, 0, foodItems);
        inflater = LayoutInflater.from(context);
        foodItemList = foodItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_admin_add_food, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.foodImageView);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);

        textViewCategory.setVisibility(View.VISIBLE);
        textViewPrice.setVisibility(View.VISIBLE);
        textViewName.setVisibility(View.VISIBLE);

        FoodItem foodItem = foodItemList.get(position);

        textViewName.setText(foodItem.getName());
        textViewCategory.setText(foodItem.getCategory());
        textViewPrice.setText(String.valueOf(foodItem.getPrice()));

        byte[] imageByteArray = foodItem.getImage();
        if (imageByteArray != null && imageByteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or placeholder if the imageByteArray is null or empty
            imageView.setImageResource(R.drawable.noodles);
        }

        return convertView;
    }
}
