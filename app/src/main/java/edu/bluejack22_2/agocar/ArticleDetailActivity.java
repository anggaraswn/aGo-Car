package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.other.RetrievedArticleListener;

public class ArticleDetailActivity extends AppCompatActivity {

    private ImageView ivBack, ivArticleImage;

    private TextView tvArticleTitle, tvArticleContent;
    private String articleID;

    private Article retrievedArticle;

    void getExtras(){
        Bundle bundle = getIntent().getExtras();
        this.articleID = bundle.getString("articleID");
    }


    void setComponents(){
        tvArticleTitle.setText(retrievedArticle.getTitle());
        tvArticleContent.setText(retrievedArticle.getContent());
        Picasso.get().load(retrievedArticle.getImage()).into(ivArticleImage);
    }

    void getComponents(){
        ivBack = findViewById(R.id.ivBackButton);
        tvArticleTitle = findViewById(R.id.tvArticleTitle);
        tvArticleContent = findViewById(R.id.tvArticleContent);
        ivArticleImage = findViewById(R.id.ivArticleImage);

        Article.getArticle(new RetrievedArticleListener() {
            @Override
            public void retrievedArticle(Article article) {
                retrievedArticle = article;
                setComponents();
            }
        }, articleID);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        getExtras();


        if(articleID != null){
            getComponents();
        }



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}