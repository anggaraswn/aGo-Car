package edu.bluejack22_2.agocar.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;

public class HomeCarsAdapter extends RecyclerView.Adapter<HomeCarsAdapter.HomeViewHolder> {

    private ArrayList<Car> cars = new ArrayList<>();


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_car_card, parent, false);
        return new HomeCarsAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Car car = cars.get(position);
        Picasso.get().load(car.getImage()).into(holder.ivCarImage);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCarImage;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCarImage = itemView.findViewById(R.id.ivCarImage);
        }


    }

    public void setCars(ArrayList<Car> newCars) {
        cars.clear();
        cars.addAll(newCars);
        notifyDataSetChanged();
    }
}
