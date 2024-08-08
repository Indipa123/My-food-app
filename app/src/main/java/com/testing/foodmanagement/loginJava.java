package com.testing.foodmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginJava extends AppCompatActivity {
    EditText e1, e2;
    TextView t;
    Button b1;
    CheckBox saveCredentials;
    DBHelper DB;
    public static String LOGINEMAIL = "";
    SharedPreferences sharedPreferences;

    // Admin credentials
    private static final String ADMIN_EMAIL = "adminfoodplaza@gmail.com";
    private static final String ADMIN_PASSWORD = "Admin@321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        e1 = findViewById(R.id.loginEmail);
        e2 = findViewById(R.id.loginPwd);
        b1 = findViewById(R.id.loginBtn);
        t = findViewById(R.id.stext);
        saveCredentials = findViewById(R.id.saveCredentials);
        DB = new DBHelper(this);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // Load saved credentials if available
        loadSavedCredentials();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = e1.getText().toString();
                LOGINEMAIL = email;
                String pwd = e2.getText().toString();
                if (email.equals("") || pwd.equals("")) {
                    Toast.makeText(loginJava.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                } else {
                    if (email.equals(ADMIN_EMAIL) && pwd.equals(ADMIN_PASSWORD)) {
                        Toast.makeText(loginJava.this, "Logged In as Admin", Toast.LENGTH_LONG).show();

                        // Save credentials if checked
                        if (saveCredentials.isChecked()) {
                            saveCredentials(email, pwd);
                        } else {
                            clearSavedCredentials();
                        }

                        Intent adminIntent = new Intent(loginJava.this, AdminDashboardActivity.class);
                        startActivity(adminIntent);
                    } else {
                        Boolean checkemailpassword = DB.checkEmailPassword(email, pwd);
                        if (checkemailpassword == true) {
                            Toast.makeText(loginJava.this, "Logged In Successfully", Toast.LENGTH_LONG).show();

                            // Save credentials if checked
                            if (saveCredentials.isChecked()) {
                                saveCredentials(email, pwd);
                            } else {
                                clearSavedCredentials();
                            }

                            Intent i3 = new Intent(loginJava.this, MainActivity2.class);
                            i3.putExtra("EMAIL", email);
                            startActivity(i3);
                        } else {
                            Toast.makeText(loginJava.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(loginJava.this, signupJava.class);
                startActivity(i5);
            }
        });
    }

    private void loadSavedCredentials() {
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        String savedAdminEmail = sharedPreferences.getString("admin_email", "");
        String savedAdminPassword = sharedPreferences.getString("admin_password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            e1.setText(savedEmail);
            e2.setText(savedPassword);
            saveCredentials.setChecked(true);
        } else if (!savedAdminEmail.isEmpty() && !savedAdminPassword.isEmpty()) {
            e1.setText(savedAdminEmail);
            e2.setText(savedAdminPassword);
            saveCredentials.setChecked(true);
        }
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (email.equals(ADMIN_EMAIL)) {
            editor.putString("admin_email", email);
            editor.putString("admin_password", password);
        } else {
            editor.putString("email", email);
            editor.putString("password", password);
        }
        editor.apply();
    }

    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.remove("admin_email");
        editor.remove("admin_password");
        editor.apply();
    }
}