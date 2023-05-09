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
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;

public class ProfileFavouritesAdapter extends RecyclerView.Adapter<ProfileFavouritesAdapter.ViewHolder> {

    ArrayList<String> listCarID = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_favourites_card, parent, false);
        return new ProfileFavouritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("jkl", "Testing");
        Car.getCar(new RetrievedCarListener() {
            @Override
            public void retrievedCar(Car car) {
                Log.d("zxc", car.getName());
                Picasso.get().load(car.getImage()).into(holder.ivImage);
                holder.tvCarName.setText(car.getName());
                holder.tvEngine.setText(""+car.getEngine());
                holder.tvTransmission.setText(car.getTransmission());
                holder.tvPrice.setText(""+car.getPrice());
                holder.tvRating.setText(""+car.getRating());
            }
        }, listCarID.get(position));
    }

    @Override
    public int getItemCount() {
        return listCarID.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView ivImage;
        TextView tvCarName, tvEngine, tvTransmission, tvPrice, tvRating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvEngine = itemView.findViewById(R.id.tvEngine);
            tvTransmission = itemView.findViewById(R.id.tvTransmission);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
        }


    }
    public void setFavourites(ArrayList<String> newFavourites) {
        Log.d("asd", "Test");
        listCarID.clear();
        listCarID.addAll(newFavourites);
        notifyDataSetChanged();
    }
}
