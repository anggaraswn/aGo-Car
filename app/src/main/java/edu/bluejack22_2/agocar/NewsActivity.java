package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.NewsFeaturedArticleAdapter;
import edu.bluejack22_2.agocar.adapter.RecommendedArticleAdapter;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;

public class NewsActivity extends AppCompatActivity {

    LinearLayout navHome;
    RecyclerView rvFeaturedArticles, rvRecommendedArticles;
    NewsFeaturedArticleAdapter featuredArticleAdapter;
    RecommendedArticleAdapter recommendedArticleAdapter;


    void setComponents(){
        navHome = findViewById(R.id.navHome);

        //RecyclerView Featured Articles
        rvFeaturedArticles = findViewById(R.id.rvFeaturedArticles);
        featuredArticleAdapter = new NewsFeaturedArticleAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedArticles.setLayoutManager(linearLayoutManager);
        rvFeaturedArticles.setAdapter(featuredArticleAdapter);


        //RecyclerView Recommended Articles
        rvRecommendedArticles = findViewById(R.id.rvRecommendedForYou);
        recommendedArticleAdapter = new RecommendedArticleAdapter();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecommendedArticles.setLayoutManager(linearLayoutManager1);
        rvRecommendedArticles.setAdapter(recommendedArticleAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setComponents();

        //Hai

        //Ini Buat Featured yang Diatas
        Article.getArticles(new RetrievedArticlesListener() {
            @Override
            public void retrievedArticles(ArrayList<Article> retArticles) {
                if(!retArticles.isEmpty()){
                    featuredArticleAdapter.setArticles(retArticles);
                    recommendedArticleAdapter.setArticles(retArticles);
                }
            }
        });

        //Ini Buat yang Recommended (Belom ada algoritma jadi langsung getall aja)
//        Article.getArticles(new RetrievedArticlesListener() {
//            @Override
//            public void retrievedArticles(ArrayList<Article> retArticles) {
//                if(!retArticles.isEmpty()){
//                    recommendedArticleAdapter.setArticles(retArticles);
//                }
//            }
//        });


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, AddArticleActivity.class);
                startActivity(intent);

            }
        });
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}