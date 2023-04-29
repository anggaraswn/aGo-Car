package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.HomeBrandsAdapter;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;

public class HomeActivity extends AppCompatActivity {
    private User user = null;
    TextView tvGreetings;
    RecyclerView rvBrands;

    HomeBrandsAdapter brandsAdapter;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreetings = findViewById(R.id.tvGreetings);
        rvBrands = findViewById(R.id.rvBrands);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvBrands.setLayoutManager(linearLayoutManager);
        brandsAdapter = new HomeBrandsAdapter();
        rvBrands.setAdapter(brandsAdapter);
        Brand.getBrands(new RetrievedBrandsListener() {
            @Override
            public void retrievedBrands(ArrayList<Brand> retBrands) {
                if(!retBrands.isEmpty()){
                    brandsAdapter.setBrands(retBrands);
                }
            }
        });
        authenticateUser();
        tvGreetings.setText("Hello, " + user.getUsername());

    }
}