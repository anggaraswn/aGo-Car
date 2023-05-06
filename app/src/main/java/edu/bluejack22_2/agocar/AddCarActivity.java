package edu.bluejack22_2.agocar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.Timestamp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;

public class AddCarActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
    private Spinner spCarTransmission, spCarBrand, spCarFuel;
    private EditText etCarName, etCarEngineHP, etCarPrice, etCarRating, etCarDescription;


    private String spBrandValue, spTransmissionValue, spFuelValue;

    private ArrayList<Brand> brandList;

    private TextView tvSave;

    private ImageView ivBackButton, ivDisplayedImage;

    private Uri selectedImageUri;

    private Map<String, String> cloudinaryConfig = new HashMap<>();

    void setConnection(){
        cloudinaryConfig.put("cloud_name", "dmpbgjnrc");
        cloudinaryConfig.put("api_key", "596936696592152");
        cloudinaryConfig.put("api_secret", "m5pavlf12IXv1Y4maOCv5OE5DNU");
    }

    void handleImageSelection(Intent data){
        selectedImageUri = data.getData();
        ivDisplayedImage.setImageURI(selectedImageUri);
    }

    Brand getCarBrand(){
        for(Brand brand : brandList){
            if(brand.getName().equals(spBrandValue)){
                return brand;
            }
        }
        return null;
    }
    void insertData(String imageUrl){
        Brand brand = getCarBrand();
        if(brand != null){
            Car car = new Car(etCarName.getText().toString(), spTransmissionValue, imageUrl, spFuelValue, etCarDescription.getText().toString(), brand, Double.parseDouble(etCarEngineHP.getText().toString()), Double.parseDouble(etCarRating.getText().toString()), Double.parseDouble(etCarPrice.getText().toString()));
            car.insert(new OnSuccessListener() {
                @Override
                public void onSuccess(boolean success) {
                    if(success){
                        Toast.makeText(AddCarActivity.this, "Successfully Inserted new Car!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }


    }

    void uploadImage(){
        Cloudinary cloudinary = new Cloudinary(cloudinaryConfig);

        try {
            ContentResolver resolver = getContentResolver();
            InputStream inputStream = resolver.openInputStream(selectedImageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            byte[] imageData = baos.toByteArray();


            new Thread(new Runnable() {
                public void run() {

                    // Perform Cloudinary upload operation here
                    try {
                        Map uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("secure_url");

                        if(imageUrl != null){
                            insertData(imageUrl);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void setComponents(){
        spCarTransmission = findViewById(R.id.spCarTransmission);
        spCarBrand = findViewById(R.id.spCarBrand);
        spCarFuel = findViewById(R.id.spCarFuel);

        etCarName = findViewById(R.id.etCarName);
        etCarEngineHP = findViewById(R.id.etCarEngineHP);
        etCarPrice = findViewById(R.id.etCarPrice);
        etCarRating = findViewById(R.id.etCarRating);
        etCarDescription = findViewById(R.id.etCarDescription);

        tvSave = findViewById(R.id.tvSave);

        ivBackButton = findViewById(R.id.ivBackButton);
        ivDisplayedImage = findViewById(R.id.ivDisplayedImage);
    }

    void setSpinnerValue(){
        //Car Transmission Spinner
        String[] transmissions = new String[]{"Automatic", "Manual", "Hybrid"};
        ArrayAdapter<String> transmissionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, transmissions);
        spCarTransmission.setAdapter(transmissionAdapter);

        spCarTransmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spTransmissionValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Car Brand Spinner
        Brand.getBrands(new RetrievedBrandsListener() {
            @Override
            public void retrievedBrands(ArrayList<Brand> brands) {
                if(brands != null){
                    brandList = brands;

                    ArrayList<String> brandNames = new ArrayList<>();
                    for(Brand brand : brands){
                        brandNames.add(brand.getName());
                    }

                    ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_dropdown_item, brandNames);
                    spCarBrand.setAdapter(brandAdapter);

                    spCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            spBrandValue = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });



        //Car Fuel Spinner
        String[] fuels = new String[]{"Petrol", "Electric", "Hybrid"};
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fuels);
        spCarFuel.setAdapter(fuelAdapter);

        spCarFuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spFuelValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    boolean validateFields(){
        if(etCarPrice.getText().toString().isEmpty() || etCarEngineHP.getText().toString().isEmpty() || etCarRating.getText().toString().isEmpty()){
            return false;
        }
        else if(etCarName.getText().toString() == "" || etCarEngineHP.getText().toString() == "" || Double.parseDouble(etCarPrice.getText().toString()) < 0 || Double.parseDouble(etCarRating.getText().toString()) < 0 || etCarDescription.getText().toString() == ""){
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setComponents();
        setConnection();

        setSpinnerValue();


        ivDisplayedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
            }
        });

        //Save
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields() == false){
                    Toast.makeText(AddCarActivity.this, "All Fields must be Filled !", Toast.LENGTH_LONG).show();
                }else{
                    uploadImage();
                }
            }
        });

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK){
            handleImageSelection(data);
        }
    }
}