package com.testing.foodmanagement;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ViewCategoryActivity extends AppCompatActivity {
    private GridView gridViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        gridViewCategories = findViewById(R.id.gridViewCategories);

        dbHelper = new DBHelper(this);

        // Initialize category list
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(this, categoryList);
        gridViewCategories.setAdapter(categoryAdapter);

        // Load categories from the database
        loadCategories();
    }

    private void loadCategories() {
        categoryList.clear();
        List<Category> categories = dbHelper.getAllCategories(); // Implement this method in DBHelper
        categoryList.addAll(categories);
        categoryAdapter.notifyDataSetChanged();
    }
}
