package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView forgotPassword, signUp;

    void getComponents(){
        emailField = findViewById(R.id.etEmail);
        passwordField = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        signUp = findViewById(R.id.tvSignUp);
    }

    void storeUser(User user){
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("user", json);
        prefsEditor.commit();
    }


    void setListener(){
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 User.getUser(emailField.getText().toString(), passwordField.getText().toString(), new RetrievedUserListener() {
                     @Override
                     public void retrievedUser(User user) {
                        if(user!=null){
                            storeUser(user);
                        }else{
                            Toast.makeText(LoginActivity.this, "Invalid Username / Password !", Toast.LENGTH_LONG).show();
                        }
                     }
                 });
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get All the Components
        getComponents();

        //Add OnClick Listener
        setListener();

    }


}