package edu.bluejack22_2.agocar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String, String> cloudinaryConfig = new HashMap<>();
    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
    private Uri selectedImageUri;
    private  User updatedUser;



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

    void setConnection(){
        cloudinaryConfig.put("cloud_name", "dmpbgjnrc");
        cloudinaryConfig.put("api_key", "596936696592152");
        cloudinaryConfig.put("api_secret", "m5pavlf12IXv1Y4maOCv5OE5DNU");
    }

    void handleImageSelection(Intent data){
        selectedImageUri = data.getData();
        civImage.setImageURI(selectedImageUri);
    }

    void updateData(String imageUrl){
        Log.d("test", "AGin");
        Log.d("asd", imageUrl);
        updatedUser.setImage(imageUrl);
        SharedPreferences mPrefs = getSharedPreferences("userPref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(updatedUser);
        prefsEditor.putString("user", json);
        prefsEditor.apply();

        updatedUser.update(new OnSuccessListener() {
            @Override
            public void onSuccess(boolean success) {
                HomeActivity.user = updatedUser;
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    void uploadImage(){
        Log.d("uplimg", "Test");
        Cloudinary cloudinary = new Cloudinary(cloudinaryConfig);

        try {
            ContentResolver resolver = getContentResolver();
            InputStream inputStream = resolver.openInputStream(selectedImageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            byte[] imageData = baos.toByteArray();


            new Thread(new Runnable() {
                public void run() {

                    // Perform Cloudinary upload operation here
                    try {
                        Map uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("secure_url");

                        if(imageUrl != null){
                            updateData(imageUrl);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getComponents();
        getUser();
        setConnection();
        setComponents();

        civImage.setOnClickListener(e -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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
                        updatedUser = new User(user.getId(), username, email, newPassword, "User", "https://res.cloudinary.com/dwtby8jpe/image/upload/v1683130416/TPA_Android/users/user_vs5pyh.png", user.getPreference());
                        if(selectedImageUri != null){
                            uploadImage();
                        }else{
                            updateData(u.getImage());
                        }
                    }else{
                        Toast.makeText(EditProfileActivity.this, "Invalid Email / Password !", Toast.LENGTH_LONG).show();
                    }
                }
            });

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK){
            handleImageSelection(data);
        }
    }
}