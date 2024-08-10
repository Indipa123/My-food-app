package com.testing.foodmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class AddPromotionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText promotionNameEditText, descriptionEditText;
    private ImageView imageView;
    private Bitmap selectedImage;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion);

        promotionNameEditText = findViewById(R.id.promotion_name);
        descriptionEditText = findViewById(R.id.description);
        imageView = findViewById(R.id.image);
        Button chooseImageButton = findViewById(R.id.btn_choose_image);
        Button addPromotionButton = findViewById(R.id.btn_add_promotion);

        dbHelper = new DBHelper(this);

        chooseImageButton.setOnClickListener(v -> openGallery());

        addPromotionButton.setOnClickListener(v -> addPromotion());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                imageView.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addPromotion() {
        String promotionName = promotionNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if (promotionName.isEmpty() || description.isEmpty() || selectedImage == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = getBytesFromBitmap(selectedImage);
        long result = dbHelper.addPromotion(promotionName, description, imageBytes);

        if (result != -1) {
            Toast.makeText(this, "Promotion added successfully", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity
        } else {
            Toast.makeText(this, "Failed to add promotion", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
