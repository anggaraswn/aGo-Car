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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.PopularCarsAdapter;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class CarsActivity extends AppCompatActivity {

    private RecyclerView rvPopularCars;
    private static PopularCarsAdapter popularCarsAdapter;

    private EditText etSearch;

    private LinearLayout navHome, navNews, navProfile;

    private FloatingActionButton fabAdd;
    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;



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

    void setComponents() {

        etSearch = findViewById(R.id.etSearch);

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
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure you want to delete the selected car ?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Car selectedCar = popularCarsAdapter.cars.get(position);
                        selectedCar.delete(new OnSuccessListener() {
                            @Override
                            public void onSuccess(boolean success) {
                                if (success) {
                                    Toast.makeText(CarsActivity.this, "Successfully deleted article !", Toast.LENGTH_LONG).show();
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