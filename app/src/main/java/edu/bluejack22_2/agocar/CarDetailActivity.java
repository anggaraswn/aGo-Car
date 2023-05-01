package edu.bluejack22_2.agocar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;

public class CarDetailActivity extends AppCompatActivity {
    private String carID;
    private Car retrievedCar;
    private TextView tvCarName;
    private ImageView ivCarImage;

    void getExtras(){
        Bundle bundle = getIntent().getExtras();
        this.carID = bundle.getString("carID");
    }

    void getComponents(){
        tvCarName = findViewById(R.id.tvCarName);
        ivCarImage = findViewById(R.id.ivCarImage);


        //Retrieve Car From DB
        Car.getCar(new RetrievedCarListener() {
            @Override
            public void retrievedCar(Car car) {
                this.retrievedCar = car;
            }
        }, carID);

    }
    void setComponents(){}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        getExtras();

        if(carID != null){
            getComponents();
            setComponents();
        }
    }
}