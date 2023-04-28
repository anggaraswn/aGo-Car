package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.BrandAdapter;
import edu.bluejack22_2.agocar.models.Brand;

public class PreferencesActivity extends AppCompatActivity {

    TextView tvSkip;
    RecyclerView rvBrand;
    Button btnDone;
    BrandAdapter adapter;
    ArrayList<Brand> brands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        brands = new ArrayList<Brand>();



        tvSkip = findViewById(R.id.tvSkip);
        rvBrand = findViewById(R.id.rvBrand);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvBrand.setLayoutManager(gridLayoutManager);
        btnDone = findViewById(R.id.btnDone);
        adapter = new BrandAdapter(brands);
        rvBrand.setAdapter(adapter);


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}