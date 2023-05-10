package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.CarReviewsAdapter;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;
import edu.bluejack22_2.agocar.other.RetrievedRatingListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class CarDetailActivity extends AppCompatActivity {
    private String carID;
    private Car retrievedCar;
    private TextView tvCarName, tvCarEngine, tvCarTransmission, tvCarFuel, tvCarDescription;
    private ImageView ivCarImage, ivBackButton;

    private EditText etCarRating, etCarComment;
    private Button btnSave;

    private RecyclerView rvUserReviews;
    private CarReviewsAdapter carReviewsAdapter;

    void getExtras(){
        Bundle bundle = getIntent().getExtras();
        this.carID = bundle.getString("carID");
    }

    void loadReviews(){
        UserReview.getReviews(this.carID,new RetrievedUserReviewsListener() {
            @Override
            public void retrievedUserReviews(ArrayList<UserReview> userReviews) {
                carReviewsAdapter.setUserReviews(userReviews);
            }
        });
    }

    void clear(){
        etCarRating.setText("");
        etCarComment.setText("");
        etCarRating.clearFocus();
        etCarComment.clearFocus();


    }
    void getComponents(){
        tvCarName = findViewById(R.id.tvCarName);
        ivCarImage = findViewById(R.id.ivCarImage);
        ivBackButton = findViewById(R.id.ivBackButton);
        tvCarEngine = findViewById(R.id.tvHP);
        tvCarTransmission = findViewById(R.id.tvTransmissionCar);
        tvCarFuel = findViewById(R.id.tvFuelCar);

        etCarRating = findViewById(R.id.etCarRating);
        etCarComment = findViewById(R.id.etCarComment);
        btnSave = findViewById(R.id.btnAddComment);

        rvUserReviews = findViewById(R.id.rvReviews);
        carReviewsAdapter = new CarReviewsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvUserReviews.setLayoutManager(linearLayoutManager);
        rvUserReviews.setAdapter(carReviewsAdapter);
        tvCarDescription = findViewById(R.id.tvCarDescription);

        loadReviews();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields() == false){
                    Toast.makeText(CarDetailActivity.this, "Rating and Comment Fields must be filled !", Toast.LENGTH_LONG).show();
                }else{
                    UserReview rev = new UserReview(carID, etCarComment.getText().toString(), HomeActivity.user.getId(), Double.parseDouble(etCarRating.getText().toString()));

                    rev.insert(new OnSuccessListener() {
                        @Override
                        public void onSuccess(boolean success) {
                            if(success){
                                Toast.makeText(CarDetailActivity.this, "Successfully posted new review !", Toast.LENGTH_LONG).show();
                                loadReviews();
                                clear();

                            }else{
                                Toast.makeText(CarDetailActivity.this, "Unable to post review... Please try again!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Retrieve Car From DB
        Car.getCar(new RetrievedCarListener() {
            @Override
            public void retrievedCar(Car car) {
                retrievedCar = car;
                setComponents();
            }
        }, carID);

    }
    void setComponents(){
        if (retrievedCar != null){
            Log.d("AGTest", retrievedCar.getName());
            tvCarName.setText(retrievedCar.getName());
            Picasso.get().load(retrievedCar.getImage()).into(ivCarImage);
            tvCarEngine.setText(""+retrievedCar.getEngine());
            tvCarTransmission.setText(retrievedCar.getTransmission());
            tvCarFuel.setText(retrievedCar.getFuel());
            tvCarDescription.setText(retrievedCar.getDescription());
        }else{
            Log.d("AGTest", "Test");
        }

    }

    boolean validateFields(){
        if(etCarRating.getText().toString().isEmpty()){
            return false;
        }
        else if(etCarComment.getText().toString() == "" ){
            return false;
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        getExtras();

        if(carID != null){
            getComponents();
        }
    }
}