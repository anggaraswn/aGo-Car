package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.bluejack22_2.agocar.adapter.ProfileFavouritesAdapter;
import edu.bluejack22_2.agocar.adapter.ProfileReviewAdapter;
import edu.bluejack22_2.agocar.adapter.ViewPagerAdapter;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.fragments.FavouritesFragment;
import edu.bluejack22_2.agocar.fragments.ReviewsFragment;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.RetrievedFavouritesListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvViewAllFavourites, tvViewAllReviews;
    private ImageView ivEdit;
    private CircleImageView civProfile;
    private Button btnLogOut;
    private User user;
    private LinearLayout navHome, navNews, navCars;
    private ViewPager2 viewPager;
    private ArrayList<Fragment> listFragment = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;



    void getComponents() {
        viewPager = findViewById(R.id.pagerView);
        listFragment.add(new FavouritesFragment());
        listFragment.add(new ReviewsFragment());
        viewPagerAdapter = new ViewPagerAdapter(this, listFragment);
        viewPager.setAdapter(viewPagerAdapter);

//        tvName = findViewById(R.id.tvName);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvViewAllFavourites = findViewById(R.id.tvViewAllFav);
//        tvViewAllReviews = findViewById(R.id.tvViewAllRev);
//        ivEdit = findViewById(R.id.ivEdit);
//        civProfile = findViewById(R.id.ivProfile);
//        btnLogOut = findViewById(R.id.btnLogOut);
        navHome = findViewById(R.id.navHome);
        navNews = findViewById(R.id.navNews);
        navCars = findViewById(R.id.navCars);
//
//        rvYourReviews = findViewById(R.id.rvYourReviews);
//        reviewAdapter = new ProfileReviewAdapter();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        rvYourReviews.setLayoutManager(linearLayoutManager);
//        rvYourReviews.setAdapter(reviewAdapter);
//
//        rvFavourites = findViewById(R.id.rvFavourites);
//        favouritesAdapter = new ProfileFavouritesAdapter();
//        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        rvFavourites.setLayoutManager(linearLayoutManager2);
//        rvFavourites.setAdapter(favouritesAdapter);


//        Gson gson = new Gson();
//        SharedPreferences mPrefs = getSharedPreferences("userPref", Context.MODE_PRIVATE);
//        String json = mPrefs.getString("user", "");
//        user = gson.fromJson(json, User.class);
//        loadReviews();
//        loadFavourites();
    }

//    void setComponents() {
//        tvName.setText(user.getUsername());
//        tvEmail.setText(user.getEmail());
//        Picasso.get().load(user.getImage()).into(civProfile);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("qwer", "Test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getComponents();
//        setComponents();



        navHome.setOnClickListener(e -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        navNews.setOnClickListener(e -> {
            Intent intent = new Intent(ProfileActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        navCars.setOnClickListener(e -> {
            Intent intent = new Intent(ProfileActivity.this, CarsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getComponents();
//        setComponents();
    }
}