package edu.bluejack22_2.agocar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddArticleActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
    private ImageView ivDisplayedImage;

    void getComponents(){
        ivDisplayedImage = findViewById(R.id.ivDisplayedImage);
        //Test
    }

    void handleImageSelection(Intent data){
        Uri selectedImageUri = data.getData();
//        ivDisplayedImage.setImageURI(selectedImageUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        getComponents();

        ivDisplayedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
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