package edu.bluejack22_2.agocar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.PopularCarsAdapter;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;
import edu.bluejack22_2.agocar.other.RetrievedCarRatingUsersReviewsListener;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class CarsActivity extends AppCompatActivity {

    private RecyclerView rvPopularCars;
    private static PopularCarsAdapter popularCarsAdapter;

    private EditText etSearch;

    private LinearLayout navHome, navNews, navProfile;

    private FloatingActionButton fabAdd;
    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
    private Spinner spSort;
    private String spSortValue;
    private ArrayList<Double> listRating = new ArrayList<>();
    private ArrayList<Car> listCar = new ArrayList<>();



    public static void loadCars() {
        Car.getCars(new RetrievedCarsListener() {
            @Override
            public void retrievedCars(ArrayList<Car> cars) {
                if (!cars.isEmpty()) {
                    popularCarsAdapter.setCars(cars);
                }
            }
        });

    }

    public void handleImageSelection(Intent data){
        Uri selectedImageUri = data.getData();
        popularCarsAdapter.updateImage(selectedImageUri ,popularCarsAdapter.currentlyEditingPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK){
            handleImageSelection(data);
        }
    }

    void loadCarsRating(){
        listRating.clear();
        Car.getCars(new RetrievedCarsListener() {
            @Override
            public void retrievedCars(ArrayList<Car> cars) {
                if (!cars.isEmpty()) {
                    listCar.clear();
                    listCar.addAll(cars);
                }
            }
        });
        for (Car car : listCar) {
            UserReview.getCarRatingByUserReviews(car.getId(), new RetrievedCarRatingUsersReviewsListener() {
                @Override
                public void retrievedCarRatingUserReviews(Double ratings) {
                    Log.d("qwerty", ""+ratings);
                    listRating.add(ratings);
                    Log.d("njkdn", ""+listRating.size());
                }
            });
        }
    }

    private void sortCarsByRatingAscending() {
        for (int i = 0; i < listRating.size() - 1; i++) {
            for (int j = i + 1; j < listRating.size(); j++) {
                if (listRating.get(i) > listRating.get(j)) {
                    double tempRating = listRating.get(i);
                    Car tempCar = listCar.get(i);

                    listRating.set(i, listRating.get(j));
                    listCar.set(i, listCar.get(j));

                    listRating.set(j, tempRating);
                    listCar.set(j, tempCar);
                }
            }
        }
    }

    private void sortCarsByRatingDescending() {
        for (int i = 0; i < listRating.size() - 1; i++) {
            for (int j = i + 1; j < listRating.size(); j++) {
                if (listRating.get(i) < listRating.get(j)) {
                    double tempRating = listRating.get(i);
                    Car tempCar = listCar.get(i);

                    listRating.set(i, listRating.get(j));
                    listCar.set(i, listCar.get(j));

                    listRating.set(j, tempRating);
                    listCar.set(j, tempCar);
                }
            }
        }
    }


    void setSpinnerValue() {
        String[] sort = new String[]{"Highest", "Lowest"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sort);
        spSort.setAdapter(sortAdapter);


        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spSortValue = adapterView.getItemAtPosition(i).toString();
                loadCarsRating();

                switch (spSortValue) {
                    case "Highest":
                        Log.d("hjkl", "Highest");
                        sortCarsByRatingDescending();
                        break;
                    case "Lowest":
                        Log.d("hjkl", "Lowest");
                        sortCarsByRatingAscending();
                        break;
                }
                for(Car c : listCar){
                    Log.d("vbnm", c.getName());
                }
                for (Double d : listRating){
                    Log.d("dfgh", ""+d);
                }
                popularCarsAdapter.setCars(listCar);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setComponents() {

        etSearch = findViewById(R.id.etSearch);
//        spSort = findViewById(R.id.spSort);
        //Recycler View
        rvPopularCars = findViewById(R.id.rvPopularCars);
        popularCarsAdapter = new PopularCarsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPopularCars.setLayoutManager(linearLayoutManager);
        rvPopularCars.setAdapter(popularCarsAdapter);


        navHome = findViewById(R.id.navHome);
        navNews = findViewById(R.id.navNews);
        navProfile = findViewById(R.id.navProfile);

        fabAdd = findViewById(R.id.fabAdd);

        if (HomeActivity.user.getRole().equals("Admin")) {

            fabAdd.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dfgh", "Test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        setComponents();
//        setSpinnerValue();

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


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarsActivity.this, AddCarActivity.class);
                startActivity(intent);
            }
        });

        navNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarsActivity.this, NewsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarsActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarsActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    public void deleteAction(int position) {
        Log.d("asdelete", "Called");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.deleteConfirmation);
        builder.setMessage(R.string.deleteCar);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Car selectedCar = popularCarsAdapter.cars.get(position);
                        selectedCar.delete(new OnSuccessListener() {
                            @Override
                            public void onSuccess(boolean success) {
                                if (success) {
                                    Toast.makeText(CarsActivity.this, R.string.deleteArticle, Toast.LENGTH_LONG).show();
                                    Car.getCars(new RetrievedCarsListener() {
                                        @Override
                                        public void retrievedCars(ArrayList<Car> cars) {
                                            popularCarsAdapter.setCars(cars);
                                        }
                                    });

                                    Car.getCars(new RetrievedCarsListener() {
                                        @Override
                                        public void retrievedCars(ArrayList<Car> cars) {
                                            popularCarsAdapter.setCars(cars);
                                        }
                                    });

                                    //Update yang featured

                                } else {

                                }
                            }
                        }, selectedCar.getId());
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Log.d("zzzzzzzz", ""+item.getGroupId());
        switch (item.getItemId()) {
            case 1001:
                PopularCarsAdapter.HomeViewHolder viewHolder = (PopularCarsAdapter.HomeViewHolder) rvPopularCars.findViewHolderForAdapterPosition(item.getGroupId());
                viewHolder.showModal(item.getGroupId());
                popularCarsAdapter.currentlyEditingPosition = item.getGroupId();
                return true;
            case 1002:
                deleteAction(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}