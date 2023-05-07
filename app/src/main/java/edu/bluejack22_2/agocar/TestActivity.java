package edu.bluejack22_2.agocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.TestAdapter;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;

public class TestActivity extends AppCompatActivity {

    ArrayList<Brand> listBrand = new ArrayList<>();
    private RecyclerView recyclerView;
    TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        adapter = new TestAdapter(listBrand);
        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TestActivity.this));
        recyclerView.setAdapter(adapter);
        Log.d("asd", "Test");

        Brand.getBrands(new RetrievedBrandsListener() {
            @Override
            public void retrievedBrands(ArrayList<Brand> retBrands) {
                if(!retBrands.isEmpty()){
                    adapter.setCars(retBrands);
                }
            }
        });


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case 101:
                Toast.makeText(this, "Added to Wishlist", Toast.LENGTH_LONG);
                return true;
            case 102:
                Toast.makeText(this, "Delete", Toast.LENGTH_LONG);
                adapter.RemoveItem(item.getGroupId());
                return true;
        }
        return false;
    }
}