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
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;

public class CarReviewsAdapter extends RecyclerView.Adapter<CarReviewsAdapter.HomeViewHolder> {

    private ArrayList<UserReview> userReviews = new ArrayList<>();

//    void loadCars(View v){
//        Car.getPreferredCars(new RetrievedCarsListener() {
//            @Override
//            public void retrievedCars(ArrayList<Car> cars) {
//                if(cars != null){
//                    setCars(cars);
//                }else{
//                    Toast.makeText(v.getContext(), "Unable to like car right now! Please try again...", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new CarReviewsAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        UserReview userReview = userReviews.get(position);
        User.getUser(userReview.getUserID(), new RetrievedUserListener() {
            @Override
            public void retrievedUser(User user) {
                if(user != null){
                    Picasso.get().load(user.getImage()).into(holder.civUserProfile);
                    holder.tvUsername.setText(user.getUsername());
                }
            }
        });
        holder.tvRating.setText(userReview.getRating() + " / 5");
        holder.tvComment.setText(userReview.getComment());

    }

    @Override
    public int getItemCount() {
        return userReviews.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView civUserProfile;
        TextView tvUsername, tvRating, tvComment;


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            civUserProfile = itemView.findViewById(R.id.civUserProfile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvComment = itemView.findViewById(R.id.tvComment);
        }



    }

    public void setUserReviews(ArrayList<UserReview> newReviews) {
        userReviews.clear();
        userReviews.addAll(newReviews);
        notifyDataSetChanged();
    }
}
