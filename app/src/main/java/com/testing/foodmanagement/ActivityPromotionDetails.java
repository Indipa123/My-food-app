package com.testing.foodmanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPromotionDetails extends AppCompatActivity {

    private TextView promotionNameTextView, descriptionTextView,
            promotionStartDateTextView, promotionEndDateTextView, promotionDiscountTextView;
    private ImageView promotionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_details);

        promotionNameTextView = findViewById(R.id.promotionNameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        promotionStartDateTextView = findViewById(R.id.promotionStartDateTextView);
        promotionEndDateTextView = findViewById(R.id.promotionEndDateTextView);
        promotionDiscountTextView = findViewById(R.id.promotionDiscountTextView);
        promotionImageView = findViewById(R.id.promotionImageView);

        // Get the promotion name from the intent
        String promotionName = getIntent().getStringExtra("categoryName");

        // Fetch promotion details from the database using promotion name
        DBHelper dbHelper = new DBHelper(this);
        Promotion promotion = dbHelper.getPromotionByName(promotionName);

        if (promotion != null) {
            promotionNameTextView.setText(promotion.getPromotionName());
            descriptionTextView.setText(promotion.getDescription());
            promotionStartDateTextView.setText(promotion.getPromotionStartDate());
            promotionEndDateTextView.setText(promotion.getPromotionEndDate());
            promotionDiscountTextView.setText(String.valueOf(promotion.getPromotionDiscount()) + "%");

            byte[] imageByteArray = promotion.getImage();
            if (imageByteArray != null && imageByteArray.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                promotionImageView.setImageBitmap(bitmap);
            } else {
                // Set a default image or placeholder if the imageByteArray is null or empty
                promotionImageView.setImageResource(R.drawable.ic_promotion_placeholder);
            }
        }
    }
}
