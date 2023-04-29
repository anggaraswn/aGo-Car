package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.PreferencesBrandAdapter;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;

public class PreferencesActivity extends AppCompatActivity {


    private TextView tvSkip;
    private RecyclerView rvBrand;
    private Button btnDone;
    private PreferencesBrandAdapter adapter;

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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, HomeActivity.class);
                startActivity(intent);
                if(PreferencesBrandAdapter.getSelectedBrands().isEmpty()){
                    Toast.makeText(PreferencesActivity.this, "Please Select at least 1 preferred Brand!", Toast.LENGTH_LONG).show();
                }else{
                    HomeActivity.user.setPreference(PreferencesBrandAdapter.getSelectedBrands());
                    HomeActivity.user.update(new OnSuccessListener() {
                        @Override
                        public void onSuccess(boolean success) {
                            if(success){
                                Toast.makeText(PreferencesActivity.this, "Successfully save preferenced brand!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(PreferencesActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(PreferencesActivity.this, "Failed to save preferenced brand! Please try again...", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }

            }
        });
    }
}