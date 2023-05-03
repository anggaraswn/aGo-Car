package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;

public class CarDetailActivity extends AppCompatActivity {
    private String carID;
    private Car retrievedCar;
    private TextView tvCarName;
    private ImageView ivCarImage, ivBackButton;

    void getExtras(){
        Bundle bundle = getIntent().getExtras();
        this.carID = bundle.getString("carID");
    }

    void getComponents(){
        tvCarName = findViewById(R.id.tvCarName);
        ivCarImage = findViewById(R.id.ivCarImage);
        ivBackButton = findViewById(R.id.ivBackButton);


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
        }else{
            Log.d("AGTest", "Test");
        }

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