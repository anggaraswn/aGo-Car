package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.PopularCarsAdapter;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class CarsActivity extends AppCompatActivity {

    private RecyclerView rvPopularCars;
    private PopularCarsAdapter popularCarsAdapter;

    private EditText etSearch;



    void loadCars(){
        Car.getCars(new RetrievedCarsListener() {
            @Override
            public void retrievedCars(ArrayList<Car> cars) {
                if(!cars.isEmpty()){
                    popularCarsAdapter.setCars(cars);
                }
            }
        });

    }

    void setComponents(){

        etSearch = findViewById(R.id.etSearch);

        //Recycler View
        rvPopularCars = findViewById(R.id.rvPopularCars);
        popularCarsAdapter = new PopularCarsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPopularCars.setLayoutManager(linearLayoutManager);
        rvPopularCars.setAdapter(popularCarsAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        setComponents();

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() <= etSearch.getCompoundPaddingLeft()) {
                        // Drawable left clicked
                        Car.getSearchedCars(new RetrievedCarsListener() {
                            @Override
                            public void retrievedCars(ArrayList<Car> cars) {
                                Log.d("KECLICKWOY", etSearch.getText().toString());
                                popularCarsAdapter.setCars(cars);
                            }
                        }, etSearch.getText().toString());

                        return true;
                    }
                }
                return false;
            }
        });

        loadCars();
    }
}