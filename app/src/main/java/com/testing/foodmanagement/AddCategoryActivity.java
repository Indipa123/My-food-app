package com.testing.foodmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText categoryNameEditText;
    private ImageView categoryImageView;
    private Button selectImageButton, addCategoryButton;
    private Bitmap selectedImageBitmap;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        categoryImageView = findViewById(R.id.categoryImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        // Set the ImageView to display a fixed icon-sized image
        categoryImageView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(100, 100));
        categoryImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Resize image to icon size
                selectedImageBitmap = resizeBitmap(selectedImageBitmap, 100, 100);
                categoryImageView.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private byte[] convertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); // Adjust quality as needed
        return stream.toByteArray();
    }

    private void addCategory() {
        String categoryName = categoryNameEditText.getText().toString().trim();
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageBitmap == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert image to byte array
        byte[] imageByteArray = convertImageToByteArray(selectedImageBitmap);

        // Save the category to the database
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addCategory(new Category(0, categoryName, imageByteArray));

        Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
