package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.bluejack22_2.agocar.models.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvViewAllFavourites, tvViewAllReviews;
    private ImageView ivEdit;
    private CircleImageView civProfile;
    private Button btnLogOut;
    private User user;
    private LinearLayout navHome, navNews;

    void getComponents(){
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvViewAllFavourites = findViewById(R.id.tvViewAllFav);
        tvViewAllReviews = findViewById(R.id.tvViewAllRev);
        ivEdit = findViewById(R.id.ivEdit);
        civProfile = findViewById(R.id.ivProfile);
        btnLogOut = findViewById(R.id.btnLogOut);
        navHome = findViewById(R.id.navHome);
        navNews = findViewById(R.id.navNews);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);
    }

    void setComponents(){
        tvName.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        Picasso.get().load(user.getImage()).into(civProfile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getComponents();
        setComponents();

        btnLogOut.setOnClickListener(e ->{
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComponents();
        setComponents();
    }
}