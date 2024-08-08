package com.testing.foodmanagement;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddMenuActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText editTextName, editTextCategory, editTextDescription, editTextPrice, editTextIngredients;
    private CheckBox checkBoxAvailable;
    private ImageView imageView;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        editTextName = findViewById(R.id.editTextName);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextIngredients = findViewById(R.id.editTextIngredients);
        checkBoxAvailable = findViewById(R.id.checkBoxAvailable);
        imageView = findViewById(R.id.addFoodimage);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        Button buttonAddItem = findViewById(R.id.buttonAddItem);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToDatabase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = decodeSampledBitmapFromUri(imageUri, 800, 800); // Resize image
                Bitmap correctedBitmap = rotateBitmapIfRequired(this, bitmap, imageUri);
                imageView.setImageBitmap(correctedBitmap);
                selectedImageBitmap = correctedBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap rotateBitmapIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei = new ExifInterface(input);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(img, 270);
            default:
                return img;
        }
    }

    private Bitmap rotateBitmap(Bitmap img, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    private Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream input = getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(input, null, options);
        if (input != null) {
            input.close();
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
        if (input != null) {
            input.close();
        }

        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private byte[] convertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); // Adjust quality as needed
        return stream.toByteArray();
    }

    private void addItemToDatabase() {
        String name = editTextName.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String ingredients = editTextIngredients.getText().toString().trim();
        boolean available = checkBoxAvailable.isChecked();

        if (name.isEmpty() || category.isEmpty() || description.isEmpty() || priceString.isEmpty() || ingredients.isEmpty() || selectedImageBitmap == null) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);
        byte[] imageByteArray = convertImageToByteArray(selectedImageBitmap);

        // Save the data to your database here
        DBHelper dbHelper = new DBHelper(this);
        int foodId = dbHelper.addFoodItem(new FoodItem(0, name, category, description, price, ingredients, available, imageByteArray));

        if (foodId != -1) {
            Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
            // Start AssignCustomizationActivity and pass the foodId
            Intent intent = new Intent(AddMenuActivity.this, AssignCustomizationActivity.class);
            intent.putExtra("FOOD_ID", foodId);
            startActivity(intent);
            Log.d("AddMenuActivity", "Food ID to pass: " + foodId);

            finish(); // Optional: close the current activity if not needed anymore
        } else {
            Toast.makeText(this, "Failed to add item.", Toast.LENGTH_SHORT).show();
        }
    }
}
