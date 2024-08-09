package com.testing.foodmanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {
    private LayoutInflater inflater;
    private List<FoodItem> foodItemList;

    public FoodItemAdapter(Context context, List<FoodItem> foodItems) {
        super(context, 0, foodItems);
        inflater = LayoutInflater.from(context);
        foodItemList = foodItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_food_detail, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.foodImageView);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);

        FoodItem foodItem = foodItemList.get(position);

        textViewName.setText(foodItem.getName());
        textViewCategory.setText(foodItem.getCategory());
        textViewPrice.setText(String.valueOf(foodItem.getPrice()));

        byte[] imageByteArray = foodItem.getImage();
        if (imageByteArray != null && imageByteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.noodles);
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FoodItemDetailActivity.class);
            intent.putExtra("foodItem", foodItem);
            getContext().startActivity(intent);
        });

        return convertView;
    }
}
