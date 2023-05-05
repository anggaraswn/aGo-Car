package edu.bluejack22_2.agocar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.NewsFeaturedArticleAdapter;
import edu.bluejack22_2.agocar.adapter.RecommendedArticleAdapter;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;

public class NewsActivity extends AppCompatActivity {

    LinearLayout navHome, navCars, navProfile;
    RecyclerView rvFeaturedArticles, rvRecommendedArticles;
    public static NewsFeaturedArticleAdapter featuredArticleAdapter;
    public static RecommendedArticleAdapter recommendedArticleAdapter;

    FloatingActionButton fabAdd;
    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;



    void setComponents() {
        navHome = findViewById(R.id.navHome);
        navCars = findViewById(R.id.navCars);
        fabAdd = findViewById(R.id.fabAdd);

        //RecyclerView Featured Articles
        rvFeaturedArticles = findViewById(R.id.rvFeaturedArticles);
        featuredArticleAdapter = new NewsFeaturedArticleAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedArticles.setLayoutManager(linearLayoutManager);
        rvFeaturedArticles.setAdapter(featuredArticleAdapter);


        //RecyclerView Recommended Articles
        rvRecommendedArticles = findViewById(R.id.rvRecommendedForYou);
        recommendedArticleAdapter = new RecommendedArticleAdapter(this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecommendedArticles.setLayoutManager(linearLayoutManager1);
        rvRecommendedArticles.setAdapter(recommendedArticleAdapter);

        navProfile = findViewById(R.id.navProfile);

        //Authenticate User Action
        Log.d("Role", HomeActivity.user.getRole());
        if (HomeActivity.user.getRole().equals("Admin")) {

            fabAdd.setVisibility(View.VISIBLE);
        }
    }


    void loadArticles(){
        Article.getArticles(new RetrievedArticlesListener() {
            @Override
            public void retrievedArticles(ArrayList<Article> retArticles) {
                if (!retArticles.isEmpty()) {
                    recommendedArticleAdapter.setArticles(retArticles);
                }
            }
        });
        Article.getFeaturedArticles(new RetrievedArticlesListener() {
            @Override
            public void retrievedArticles(ArrayList<Article> retArticles) {
                if (!retArticles.isEmpty()) {
                    featuredArticleAdapter.setArticles(retArticles);
                }
            }
        });

    }


    public void handleImageSelection(Intent data){
        Uri selectedImageUri = data.getData();
        recommendedArticleAdapter.updateImage(selectedImageUri, recommendedArticleAdapter.getCurrentlyEditingPosition());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK){
            handleImageSelection(data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadArticles();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setComponents();


        loadArticles();



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
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
        navCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, CarsActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}