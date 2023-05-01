package edu.bluejack22_2.agocar.adapter;

import android.content.Intent;
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

import edu.bluejack22_2.agocar.CarDetailActivity;
import edu.bluejack22_2.agocar.HomeActivity;
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
        holder.tvCarName.setText(car.getName());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivCarImage;
        TextView tvCarName;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCarImage = itemView.findViewById(R.id.ivCarImage);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Car car = cars.get(position);
            Intent i = new Intent(v.getContext(), CarDetailActivity.class);
            i.putExtra("carID", car.getId());
            v.getContext().startActivity(i);
        }
    }

    public void setCars(ArrayList<Car> newCars) {
        cars.clear();
        cars.addAll(newCars);
        notifyDataSetChanged();
    }
}
