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

import edu.bluejack22_2.agocar.adapter.PreferencesBrandAdapter;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;

public class PreferencesActivity extends AppCompatActivity {

    TextView tvSkip;
    RecyclerView rvBrand;
    Button btnDone;
    PreferencesBrandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        tvSkip = findViewById(R.id.tvSkip);
        rvBrand = findViewById(R.id.rvBrand);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvBrand.setLayoutManager(gridLayoutManager);
        btnDone = findViewById(R.id.btnDone);
        adapter = new PreferencesBrandAdapter();
        rvBrand.setAdapter(adapter);

        Brand.getBrands(new RetrievedBrandsListener() {
            @Override
            public void retrievedBrands(ArrayList<Brand> retBrands) {
                if(!retBrands.isEmpty()){
                    adapter.setBrands(retBrands);
                }
            }
        });



        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}