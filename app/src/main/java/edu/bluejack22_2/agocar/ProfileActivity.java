package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvViewAllFavourites, tvViewAllReviews;
    private ImageView ivEdit;
    private CircleImageView civProfile;
    private Button btnLogOut;
    private User user;
    private LinearLayout navHome, navNews, navCars;
    private RecyclerView rvYourReviews, rvFavourites;
    private ProfileReviewAdapter reviewAdapter;
    private ProfileFavouritesAdapter favouritesAdapter;

    void loadReviews() {
        UserReview.getReviewsByUser(this.user.getId(), new RetrievedUserReviewsListener() {
            @Override
            public void retrievedUserReviews(ArrayList<UserReview> userReviews) {
                reviewAdapter.setUserReviews(userReviews);
            }
        });
    }

    void loadFavourites() {
        ArrayList<String> listCardID = new ArrayList<>();
        Database.getInstance().collection("userlikes").whereEqualTo("userid", HomeActivity.user.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                        String id = b.getString("carid");
                        listCardID.add(id);
                        Log.d("qwer", id);
                    }
                });
        favouritesAdapter.setFavourites(listCardID);
    }

    void getComponents() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvViewAllFavourites = findViewById(R.id.tvViewAllFav);
        tvViewAllReviews = findViewById(R.id.tvViewAllRev);
        ivEdit = findViewById(R.id.ivEdit);
        civProfile = findViewById(R.id.ivProfile);
        btnLogOut = findViewById(R.id.btnLogOut);
        navHome = findViewById(R.id.navHome);
        navNews = findViewById(R.id.navNews);
        navCars = findViewById(R.id.navCars);

        rvYourReviews = findViewById(R.id.rvYourReviews);
        reviewAdapter = new ProfileReviewAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvYourReviews.setLayoutManager(linearLayoutManager);
        rvYourReviews.setAdapter(reviewAdapter);

        rvFavourites = findViewById(R.id.rvFavourites);
        favouritesAdapter = new ProfileFavouritesAdapter();
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFavourites.setLayoutManager(linearLayoutManager2);
        rvFavourites.setAdapter(favouritesAdapter);


        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);
        loadReviews();
        loadFavourites();
    }

    void setComponents() {
        tvName.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        Picasso.get().load(user.getImage()).into(civProfile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("asd", "Test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getComponents();
        setComponents();

        btnLogOut.setOnClickListener(e -> {
            SharedPreferences mPrefs = getSharedPreferences("userPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        ivEdit.setOnClickListener(e -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

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
        getComponents();
        setComponents();
    }
}