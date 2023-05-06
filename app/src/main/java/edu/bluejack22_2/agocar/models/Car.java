package edu.bluejack22_2.agocar.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.RetrievedBrandListener;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class Car {
    private String id, name, transmission, image, fuel, description, brandID;
    private Brand brand;
    private double engine, rating, price;

    public Car(String id, String name, String transmission, String image, String fuel, String description, Brand brand, double engine, double rating, double price) {
        this.id = id;
        this.name = name;
        this.transmission = transmission;
        this.image = image;
        this.fuel = fuel;
        this.description = description;
        this.brand = brand;
        this.brandID = brand.getDocumentID();
        this.engine = engine;
        this.rating = rating;
        this.price = price;
    }
    public Car(String id, String name, String transmission, String image, String fuel, String description, String brandID, double engine, double rating, double price) {
        this.id = id;
        this.name = name;
        this.transmission = transmission;
        this.image = image;
        this.fuel = fuel;
        this.description = description;
        this.brandID = brandID;
        this.engine = engine;
        this.rating = rating;
        this.price = price;
    }

    public Car(String name, String transmission, String image, String fuel, String description, Brand brand, double engine, double rating, double price) {
        this.name = name;
        this.transmission = transmission;
        this.image = image;
        this.fuel = fuel;
        this.description = description;
        this.brand = brand;
        this.brandID = brand.getDocumentID();
        this.engine = engine;
        this.rating = rating;
        this.price = price;
    }

    public static void getCars(RetrievedCarsListener listener){
        ArrayList<Car> cars = new ArrayList<>();
        Database.getInstance().collection("cars")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String name = b.getString("name");
                            String description = b.getString("description");
                            String transmission = b.getString("transmission");
                            String image = b.getString("image");
                            String fuel = b.getString("fuel");
                            String brandID = b.getString("brand");
                            Double engine = b.getDouble("engine");
                            Double rating = b.getDouble("rating");
                            Double price = b.getDouble("price");
                            cars.add(new Car(id, name, transmission, image, fuel, description, brandID, engine, rating, price));
                        }
                        listener.retrievedCars(cars);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    };

    public static void getSearchedCars(RetrievedCarsListener listener, String search){
        ArrayList<Car> cars = new ArrayList<>();
        Database.getInstance().collection("cars")
                .whereGreaterThanOrEqualTo("name", search)
                .whereLessThanOrEqualTo("name", search + "\uf8ff") // \uf8ff is a high Unicode character used to indicate the end of a string
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String name = b.getString("name");
                            String description = b.getString("description");
                            String transmission = b.getString("transmission");
                            String image = b.getString("image");
                            String fuel = b.getString("fuel");
                            String brandID = b.getString("brand");
                            Double engine = b.getDouble("engine");
                            Double rating = b.getDouble("rating");
                            Double price = b.getDouble("price");
                            cars.add(new Car(id, name, transmission, image, fuel, description, brandID, engine, rating, price));
                        }
                        listener.retrievedCars(cars);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    };

    public void insert(edu.bluejack22_2.agocar.other.OnSuccessListener listener) {
        Map<String, Object> car = new HashMap<>();
        car.put("name", this.name);
        car.put("brand", this.brandID);
        car.put("description", this.description);
        car.put("engine", this.engine);
        car.put("fuel", this.fuel);
        car.put("image", this.image);
        car.put("price", this.price);
        car.put("rating", this.rating);
        car.put("transmission", this.transmission);



        Database.getInstance().collection("cars")
                .add(car)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onSuccess(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onSuccess(false);
                    }
                });


    }

    public static void getPreferredCars(RetrievedCarsListener listener){
        Log.d("TotalPreferred", HomeActivity.user.getPreference().size()+"");

        HomeActivity.user.getPreference().forEach(e -> {
            Log.d("BrandID", e);
        });
        ArrayList<Car> cars = new ArrayList<>();

        List<String> list = new ArrayList<>();
        list.addAll(HomeActivity.user.getPreference());
        Database.getInstance().collection("cars").whereIn("brand", list)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        Log.d("Gakosong", "Gakosong");
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String name = b.getString("name");
                            String description = b.getString("description");
                            String transmission = b.getString("transmission");
                            String image = b.getString("image");
                            String fuel = b.getString("fuel");
                            String brandID = b.getString("brand");
                            Double engine = b.getDouble("engine");
                            Double rating = b.getDouble("rating");
                            Double price = b.getDouble("price");
                            cars.add(new Car(id, name, transmission, image, fuel, description, brandID, engine, rating, price));
                        }
                        listener.retrievedCars(cars);
                    }else{
                        Log.d("Kosong", "Kosong");
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    }

    public static void getCar(RetrievedCarListener listener, String carID){

        Database.getInstance().collection("cars").document(carID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots != null){
                            String id = queryDocumentSnapshots.getId();
                            String name = queryDocumentSnapshots.getString("name");
                            String description = queryDocumentSnapshots.getString("description");
                            String transmission = queryDocumentSnapshots.getString("transmission");
                            String image = queryDocumentSnapshots.getString("image");
                            String fuel = queryDocumentSnapshots.getString("fuel");
                            double engine = queryDocumentSnapshots.getDouble("engine");
                            double rating = queryDocumentSnapshots.getDouble("rating");
                            double price = queryDocumentSnapshots.getDouble("price");
                            Brand.getBrand(new RetrievedBrandListener() {
                                @Override
                                public void retrievedBrand(Brand brand) {
                                    if(brand != null){
                                        Car car = new Car(id, name, transmission, image, fuel, description, brand, engine, rating, price);
                                        listener.retrievedCar(car);
                                    }
                                }
                            }, queryDocumentSnapshots.getString("brand"));



                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.retrievedCar(null);
                });
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
