package edu.bluejack22_2.agocar.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.CarDetailActivity;
import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.LoginActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class PopularCarsAdapter extends RecyclerView.Adapter<PopularCarsAdapter.HomeViewHolder> {

    private ArrayList<Car> cars = new ArrayList<>();


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_popular_cars_card, parent, false);
        return new PopularCarsAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Car car = cars.get(position);
        Picasso.get().load(car.getImage()).into(holder.ivImage);
        holder.tvName.setText(car.getName());
        holder.tvPrice.setText(String.format("%,f", car.getPrice()));
        holder.tvRatings.setText(car.getRating()+"");
        holder.tvHP.setText(car.getEngine()+"");
        holder.tvTransmission.setText(car.getTransmission());


    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivImage;
        TextView tvName, tvRatings, tvHP, tvTransmission, tvPrice;



        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvRatings = itemView.findViewById(R.id.tvRatings);
            tvHP = itemView.findViewById(R.id.tvHP);
            tvTransmission = itemView.findViewById(R.id.tvTransmission);
            tvPrice = itemView.findViewById(R.id.tvPrice);

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
