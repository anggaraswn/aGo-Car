package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.HomeBrandsAdapter;
import edu.bluejack22_2.agocar.adapter.HomeCarsAdapter;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class HomeActivity extends AppCompatActivity {
    public static User user = null;
    private TextView tvGreetings;
    private RecyclerView rvBrands, rvCars;

    private HomeBrandsAdapter brandsAdapter;

    private HomeCarsAdapter carsAdapter;
    private LinearLayout navNews, navProfile, navCars;

    private ImageView notification;


    void authenticateUser(){
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);


        if(user == null){
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    void getComponents(){
        this.tvGreetings = findViewById(R.id.tvGreetings);

    }

    void setComponents(){
        tvGreetings.setText("Hello, "+this.user.getUsername()+ " !");
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreetings = findViewById(R.id.tvGreetings);
        rvBrands = findViewById(R.id.rvBrands);
        rvCars = findViewById(R.id.rvCars);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvBrands.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCars.setLayoutManager(linearLayoutManager2);
        brandsAdapter = new HomeBrandsAdapter();
        carsAdapter = new HomeCarsAdapter();
        rvBrands.setAdapter(brandsAdapter);
        rvCars.setAdapter(carsAdapter);
        navNews = findViewById(R.id.navNews);
        navProfile = findViewById(R.id.navProfile);
        notification = findViewById(R.id.ivNotification);
        navCars = findViewById(R.id.navCars);

        Brand.getBrands(new RetrievedBrandsListener() {
            @Override
            public void retrievedBrands(ArrayList<Brand> retBrands) {
                if(!retBrands.isEmpty()){
                    brandsAdapter.setBrands(retBrands);
                }
            }
        });

        Car.getPreferredCars(new RetrievedCarsListener() {
            @Override
            public void retrievedCars(ArrayList<Car> cars) {
                if(!cars.isEmpty()){
                    carsAdapter.setCars(cars);
                }
            }
        });

        navNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
        navCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CarsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });


        authenticateUser();

        getComponents();

        setComponents();
    }
}