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
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;

public class ProfileReviewAdapter extends RecyclerView.Adapter<ProfileReviewAdapter.ViewHolder> {

    ArrayList<UserReview> userReviews = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_review_card, parent, false);
        return new ProfileReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("qwer", ""+userReviews.size());
        UserReview userReview = userReviews.get(position);
        Car.getCar(new RetrievedCarListener() {
            @Override
            public void retrievedCar(Car car) {
                Picasso.get().load(car.getImage()).into(holder.ivImage);
            }
        }, userReview.getCarID());
        holder.tvReviews.setText(userReview.getComment());
    }

    @Override
    public int getItemCount() {
        return userReviews.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvReviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvReviews = itemView.findViewById(R.id.tvReviews);
        }
    }

    public void setUserReviews(ArrayList<UserReview> newReviews) {
        userReviews.clear();
        userReviews.addAll(newReviews);
        notifyDataSetChanged();
    }

}
