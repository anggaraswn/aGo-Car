package edu.bluejack22_2.agocar.other;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;

public interface RetrievedCarsListener {
    void retrievedCars(ArrayList<Car> cars);
}
