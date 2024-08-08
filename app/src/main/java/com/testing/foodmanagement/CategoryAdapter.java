package com.testing.foodmanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private LayoutInflater inflater;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
        inflater = LayoutInflater.from(context);
        categoryList = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_item_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.categoryImageView);
        TextView textViewName = convertView.findViewById(R.id.categoryNameTextView);

        Category category = categoryList.get(position);

        textViewName.setText(category.getCategoryName());

        byte[] imageByteArray = category.getCategoryImage();
        if (imageByteArray != null && imageByteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or placeholder if the imageByteArray is null or empty
            imageView.setImageResource(R.drawable.default_category_image);
        }

        return convertView;
    }
}
