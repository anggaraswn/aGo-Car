package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.bluejack22_2.agocar.other.OnUserExistListener;
import edu.bluejack22_2.agocar.models.User;

public class RegisterActivity extends AppCompatActivity {

    //Attributes
    private EditText usernameET, emailET, passwordET, cPasswordET;
    private Button registerBtn;
    private TextView loginTV;


    boolean isAlphanumeric(String str){
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9]).+$";

        return str.matches(regex);
    }

    void getComponents(){
        usernameET = findViewById(R.id.etUsername);
        emailET = findViewById(R.id.etEmail);
        passwordET = findViewById(R.id.etPassword);
        cPasswordET = findViewById(R.id.etCPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginTV = findViewById(R.id.tvLogin2);
    }

    void validateFields(){
        if(usernameET.getText().toString().equals("aa") || emailET.getText().toString().equals("") || passwordET.getText().toString().equals("") || cPasswordET.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this, "All Fields must be Filled !", Toast.LENGTH_LONG).show();
        }else{
            User.checkUserExist(emailET.getText().toString(), new OnUserExistListener() {
                @Override
                public void onUserExist(boolean exist) {
                    if (exist) {
                        Toast.makeText(RegisterActivity.this, "Email Already Exist !", Toast.LENGTH_LONG).show();
                    } else {
                        if (!isAlphanumeric(passwordET.getText().toString())){
                            Toast.makeText(RegisterActivity.this, "Password must be alphanumeric !", Toast.LENGTH_LONG).show();
                        }else{
                            if(passwordET.getText().toString().equals(cPasswordET.getText().toString()) == false){
                                Toast.makeText(RegisterActivity.this, "Invalid Confirmation Password !", Toast.LENGTH_LONG).show();
                            }else{
                                //Success
                                User newUser = new User(usernameET.getText().toString(), emailET.getText().toString(), passwordET.getText().toString(), "User", null);

                                //Cara biar bisa retrieve hasil Booleannya
                                newUser.insert();



                            }
                        }

                    }
                }
            });
        }
    }
    void setListener(){
        //RegisterButton Listener
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();

            }
        });

        //LoginButton Listener
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //Ini biar ganti activitynya current activity di clear dlu (gabisa di back jadinya)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

    void setAttributes(){
        passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get All the Components
        getComponents();

        //Set Password Field Value to be Masked
        setAttributes();

        //Add OnClick Listener To Each button
        setListener();


    }
}