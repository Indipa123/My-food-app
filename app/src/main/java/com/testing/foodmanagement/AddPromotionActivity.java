package com.testing.foodmanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddPromotionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText promotionNameEditText, promotionCodeEditText, descriptionEditText,
            promotionStartDateEditText, promotionEndDateEditText, promotionDiscountEditText;
    private ImageView imageView;
    private Bitmap selectedImage;
    private DBHelper dbHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion);

        promotionNameEditText = findViewById(R.id.promotion_name);
        promotionCodeEditText = findViewById(R.id.promotion_code);
        descriptionEditText = findViewById(R.id.description);
        promotionStartDateEditText = findViewById(R.id.promotion_start_date);
        promotionEndDateEditText = findViewById(R.id.promotion_end_date);
        promotionDiscountEditText = findViewById(R.id.promotion_discount);
        imageView = findViewById(R.id.image);
        Button chooseImageButton = findViewById(R.id.btn_choose_image);
        Button addPromotionButton = findViewById(R.id.btn_add_promotion);

        dbHelper = new DBHelper(this);
        calendar = Calendar.getInstance();

        chooseImageButton.setOnClickListener(v -> openGallery());

        addPromotionButton.setOnClickListener(v -> addPromotion());

        // Set DatePicker dialogs for the start and end date fields
        promotionStartDateEditText.setOnClickListener(v -> showDatePickerDialog(promotionStartDateEditText));
        promotionEndDateEditText.setOnClickListener(v -> showDatePickerDialog(promotionEndDateEditText));
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

    private void showDatePickerDialog(final EditText dateEditText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddPromotionActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year1);
                    calendar.set(Calendar.MONTH, month1);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                    dateEditText.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void addPromotion() {
        String promotionName = promotionNameEditText.getText().toString();
        String promotionCode = promotionCodeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String promotionStartDate = promotionStartDateEditText.getText().toString();
        String promotionEndDate = promotionEndDateEditText.getText().toString();
        String promotionDiscountStr = promotionDiscountEditText.getText().toString();

        if (promotionName.isEmpty() || promotionCode.isEmpty() || description.isEmpty() ||
                promotionStartDate.isEmpty() || promotionEndDate.isEmpty() || promotionDiscountStr.isEmpty() || selectedImage == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double promotionDiscount;
        try {
            promotionDiscount = Double.parseDouble(promotionDiscountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid discount value", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = getBytesFromBitmap(selectedImage);
        long result = dbHelper.addPromotion(promotionName, promotionCode, description, promotionStartDate, promotionEndDate, promotionDiscount, imageBytes);

        if (result != -1) {
            Toast.makeText(this, "Promotion added successfully", Toast.LENGTH_SHORT).show();
            sendPromotionEmails(promotionCode);  // Send emails to all registered users
            finish();  // Close the activity
        } else {
            Toast.makeText(this, "Failed to add promotion", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        return stream.toByteArray();
    }

    private void sendPromotionEmails(String promotionCode) {
        List<String> emails = dbHelper.getAllUserEmails();
        String subject = "New Promotion Available!";
        String body = "Use the following promotion code to get a discount: " + promotionCode;

        for (String email : emails) {
            new Thread(() -> EmailHelper.sendEmail(email, subject, body)).start();
        }
    }
}
