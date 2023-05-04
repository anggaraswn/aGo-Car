package edu.bluejack22_2.agocar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.Timestamp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.util.Calculator;

public class AddArticleActivity extends AppCompatActivity {
    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
    private ImageView ivDisplayedImage;
    private TextView tvSave;
    private EditText etTitle, etDescription;
    private Uri selectedImageUri;

    private Map<String, String> cloudinaryConfig = new HashMap<>();

    void getComponents(){
        ivDisplayedImage = findViewById(R.id.ivDisplayedImage);
        tvSave = findViewById(R.id.tvSave);
        etTitle = findViewById(R.id.etArticleTitle);
        etDescription = findViewById(R.id.etArticleDescription);
    }

    void handleImageSelection(Intent data){
        selectedImageUri = data.getData();
        ivDisplayedImage.setImageURI(selectedImageUri);
    }

    void setConnection(){
        cloudinaryConfig.put("cloud_name", "dmpbgjnrc");
        cloudinaryConfig.put("api_key", "596936696592152");
        cloudinaryConfig.put("api_secret", "m5pavlf12IXv1Y4maOCv5OE5DNU");
    }

    void insertData(String imageUrl){
        Article article = new Article(etTitle.getText().toString(), imageUrl, HomeActivity.user.getUsername(), etDescription.getText().toString(), new Timestamp(new Date()));


        article.insert(new OnSuccessListener() {
            @Override
            public void onSuccess(boolean success) {
                if(success){
                    Toast.makeText(AddArticleActivity.this, "Successfully Inserted new Article!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    void uploadImage(){
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
                            insertData(imageUrl);
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
        setContentView(R.layout.activity_add_article);

        getComponents();
        setConnection();

        ivDisplayedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTitle.getText().equals("") || etDescription.getText().equals("") || selectedImageUri == null){
                    Toast.makeText(AddArticleActivity.this, "All Fields must be Filled!", Toast.LENGTH_LONG).show();
                }else if(etDescription.getText().length() < 100){
                    Toast.makeText(AddArticleActivity.this, "Article Description minimum 100 characters !", Toast.LENGTH_LONG).show();
                }else if(Calculator.getImageSize(selectedImageUri, v.getContext()) > (1024 * 1024)){
                    Toast.makeText(AddArticleActivity.this, "Article Image size maximum 1 MegaBytes !", Toast.LENGTH_LONG).show();

                }else{
                    uploadImage();
                }

            }
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