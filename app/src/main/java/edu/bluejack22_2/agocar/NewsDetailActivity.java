package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    private  String title, image, content;

    private TextView tvTitle, tvContent;
    private ImageView ivImage;

    void getExtras(){
        Bundle bundle = getIntent().getExtras();
        this.title = bundle.getString("title");
        this.image = bundle.getString("image");
        this.content = bundle.getString("content");
    }

    void getComponents(){
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        ivImage = findViewById(R.id.ivImage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        getExtras();
        getComponents();

        tvTitle.setText(title);
        tvContent.setText(content);
        Picasso.get().load(image).into(ivImage);
    }
}