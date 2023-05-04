package edu.bluejack22_2.agocar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etCurrentPassword, etNewPassword;
    private Button btnSave;
    private ImageView ivBack;
    private CircleImageView civImage;
    private User user;
    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;


    void getComponents(){
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etCurrentPassword = findViewById(R.id.etCurrPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSave = findViewById(R.id.btnSave);
        ivBack = findViewById(R.id.ivBack);
        civImage = findViewById(R.id.civImage);
    }

    void getUser(){
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);
    }

    void setComponents(){
        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
        Picasso.get().load(user.getImage()).into(civImage);
    }

    void handleImageSelection(Intent data){
        Uri selectedImageUri = data.getData();
        civImage.setImageURI(selectedImageUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getComponents();
        getUser();
        setComponents();

        civImage.setOnClickListener(e -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
        });

        ivBack.setOnClickListener(e -> {
            finish();
        });

        btnSave.setOnClickListener(e -> {
            String username, email, cPassword, newPassword;
            username = etUsername.getText().toString();
            email = etEmail.getText().toString();
            cPassword = etCurrentPassword.getText().toString();
            newPassword = etNewPassword.getText().toString();

            User.getUser(email, cPassword.toString(), new RetrievedUserListener() {
                @Override
                public void retrievedUser(User u) {

                    if(u !=null){
                        Log.d("AGTest", user.getId());
                        User updatedUser = new User(user.getId(), username, email, newPassword, "User", "https://res.cloudinary.com/dwtby8jpe/image/upload/v1683130416/TPA_Android/users/user_vs5pyh.png", user.getPreference());

                        SharedPreferences mPrefs = getSharedPreferences("userPref", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(updatedUser);
                        prefsEditor.putString("user", json);
                        prefsEditor.apply();

                        updatedUser.update(new OnSuccessListener() {
                            @Override
                            public void onSuccess(boolean success) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        });

                    }else{
                        Toast.makeText(EditProfileActivity.this, "Invalid Email / Password !", Toast.LENGTH_LONG).show();
                    }
                }
            });

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK){
            handleImageSelection(data);
        }
    }
}