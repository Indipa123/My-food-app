package com.testing.foodmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class signupJava extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    EditText fname, lname, email, pass, phone, address;
    Button b1, takePhotoButton, importPhotoButton;
    TextView t1;
    ImageView profileImageView;
    DBHelper DB;
    public static String SIGNUPEMAIL = "";
    private Bitmap profileBitmap;

    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickGalleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.emailId);
        pass = findViewById(R.id.pwd);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        b1 = findViewById(R.id.signinbtn);
        t1 = findViewById(R.id.signintxt);
        profileImageView = findViewById(R.id.profileImageView);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        importPhotoButton = findViewById(R.id.importPhotoButton);
        DB = new DBHelper(this);

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        profileBitmap = (Bitmap) extras.get("data");
                        profileImageView.setImageBitmap(profileBitmap);
                    }
                }
        );

        pickGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        try {
                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            profileBitmap = BitmapFactory.decodeStream(imageStream);
                            profileImageView.setImageBitmap(profileBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        takePhotoButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureLauncher.launch(takePictureIntent);
            }
        });

        importPhotoButton.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickGalleryLauncher.launch(pickPhoto);
        });

        b1.setOnClickListener(view -> {
            String fnameStr = fname.getText().toString();
            String lnameStr = lname.getText().toString();
            String emailStr = email.getText().toString();
            String passStr = pass.getText().toString();
            String phoneStr = phone.getText().toString();
            String addressStr = address.getText().toString();

            if (fnameStr.equals("") || lnameStr.equals("") || emailStr.equals("") || passStr.equals("") || phoneStr.equals("") || addressStr.equals("")) {
                Toast.makeText(signupJava.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
            } else {
                Boolean checkemail = DB.checkEmail(emailStr);
                if (!checkemail) {
                    byte[] imageBytes = null;
                    if (profileBitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        imageBytes = stream.toByteArray();
                    }

                    Boolean insert = DB.insertData(fnameStr, lnameStr, emailStr, passStr, phoneStr, addressStr, imageBytes);
                    if (insert) {
                        Toast.makeText(signupJava.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        SIGNUPEMAIL = emailStr;
                        Intent intent = new Intent(signupJava.this, MainActivity2.class);
                        intent.putExtra("EMAIL", emailStr);
                        startActivity(intent);
                    } else {
                        Toast.makeText(signupJava.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(signupJava.this, "User already exists, Please Sign in", Toast.LENGTH_LONG).show();
                }
            }
        });

        t1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureLauncher.launch(takePictureIntent);
            } else {
                Toast.makeText(this, "Camera permission is required to take photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}