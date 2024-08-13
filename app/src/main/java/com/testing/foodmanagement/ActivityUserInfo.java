package com.testing.foodmanagement;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

public class ActivityUserInfo extends AppCompatActivity {

    private DBHelper dbHelper;
    private ImageView profileImageView;
    private TextView textViewFirstName, textViewLastName, textViewEmail, textViewPhoneNo, textViewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        dbHelper = new DBHelper(this);

        profileImageView = findViewById(R.id.profileImageView);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhoneNo = findViewById(R.id.textViewPhoneNo);
        textViewAddress = findViewById(R.id.textViewAddress);

        // Retrieve the email passed from AccountActivity
        String email = getIntent().getStringExtra("EMAIL");

        // Fetch user details from DB
        User user = dbHelper.getUserDetailsByEmail(email);

        if (user != null) {
            // Display user details
            textViewFirstName.setText(user.getFirstName());
            textViewLastName.setText(user.getLastName());
            textViewEmail.setText(user.getEmail()); // Display email
            textViewPhoneNo.setText(user.getPhoneNo());
            textViewAddress.setText(user.getAddress());

            // Display profile image
            if (user.getProfileImage() != null) {
                Glide.with(this)
                        .load(user.getProfileImage())
                        .transform(new CircleCrop())
                        .into(profileImageView);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_account)
                        .transform(new CircleCrop())
                        .into(profileImageView);
            }
        }
    }
}
