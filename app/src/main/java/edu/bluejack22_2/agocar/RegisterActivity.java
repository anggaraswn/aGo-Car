package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameET, emailET, passwordET, cPasswordET;
    Button registerBtn;
    TextView loginTV;


    boolean isAlphanumeric(String str){
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9]).+$";

        return str.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.etUsername);
        emailET = findViewById(R.id.etEmail);
        passwordET = findViewById(R.id.etPassword);
        cPasswordET = findViewById(R.id.etCPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginTV = findViewById(R.id.tvLogin2);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!isAlphanumeric(passwordET.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Password must be alphanumeric", Toast.LENGTH_LONG).show();
                    Log.d("Test", "in");
                }else{

                }
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
            }
        });

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}