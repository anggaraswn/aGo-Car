package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginBTN;
    TextView forgotPassword, signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = findViewById(R.id.etEmail);
        passwordField = findViewById(R.id.etPassword);
        loginBTN = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        signUp = findViewById(R.id.tvSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}