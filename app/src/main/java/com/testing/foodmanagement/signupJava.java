package com.testing.foodmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class signupJava extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int LOCATION_REQUEST_CODE = 1;

    EditText fname, lname, email, pass, conpass, phone;
    Button b1, takePhotoButton, importPhotoButton, chooseLocationButton;
    TextView t1;
    ImageView profileImageView;
    DBHelper DB;
    public static String SIGNUPEMAIL = "";
    private Bitmap profileBitmap;
    private String selectedAddress; // To store the selected address

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
        conpass = findViewById(R.id.confirmPwd);
        phone = findViewById(R.id.phone);
        b1 = findViewById(R.id.signinbtn);
        t1 = findViewById(R.id.signintxt);
        profileImageView = findViewById(R.id.profileImageView);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        importPhotoButton = findViewById(R.id.importPhotoButton);
        chooseLocationButton = findViewById(R.id.chooseLocationButton);  // Initialize the button
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

        chooseLocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(signupJava.this, SelectLocationActivity.class);
            startActivityForResult(intent, LOCATION_REQUEST_CODE);
        });

        b1.setOnClickListener(view -> {
            String fnameStr = fname.getText().toString();
            String lnameStr = lname.getText().toString();
            String emailStr = email.getText().toString();
            String passStr = pass.getText().toString();
            String conpassStr = conpass.getText().toString();
            String phoneStr = phone.getText().toString();

            if (fnameStr.equals("") || lnameStr.equals("") || emailStr.equals("") || passStr.equals("") || conpassStr.equals("") || phoneStr.equals("")) {
                Toast.makeText(signupJava.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                return; // Exit early to avoid proceeding with incomplete data
            }

            if (selectedAddress == null) {
                Toast.makeText(signupJava.this, "Please select a location", Toast.LENGTH_LONG).show();
                return; // Exit early if location is not selected
            }

            if (!passStr.equals(conpassStr)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Boolean checkemail = DB.checkEmail(emailStr);
                if (!checkemail) {
                    byte[] imageBytes = null;
                    if (profileBitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        imageBytes = stream.toByteArray();
                    }

                    // Save location as a string (address)
                    Boolean insert = DB.insertData(fnameStr, lnameStr, emailStr, passStr, phoneStr, selectedAddress, imageBytes);
                    if (insert) {
                        Toast.makeText(signupJava.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        SIGNUPEMAIL = emailStr;
                        Intent intent = new Intent(signupJava.this, loginJava.class);
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
            Intent intent = new Intent(getApplicationContext(), loginJava.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedAddress = data.getStringExtra("address");
            // Optionally, update the UI to indicate that an address was selected
            Toast.makeText(this, "Location Selected: " + selectedAddress, Toast.LENGTH_SHORT).show();
        }
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
