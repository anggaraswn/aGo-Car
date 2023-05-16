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
import edu.bluejack22_2.agocar.models.Notification;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class CarReviewsAdapter extends RecyclerView.Adapter<CarReviewsAdapter.HomeViewHolder> {

    private ArrayList<UserReview> userReviews = new ArrayList<>();

    void loadUserReviews(View v){
        UserReview.getReviews(CarDetailActivity.carID, new RetrievedUserReviewsListener() {
            @Override
            public void retrievedUserReviews(ArrayList<UserReview> userReviews) {
                if(userReviews != null){
                    setUserReviews(userReviews);
                }
            }
        });
    }

    void notifyLikedReviewUser(String userReviewId){
        UserReview.getUserReview(new RetrievedUserReviewListener() {
            @Override
            public void retrievedUserReview(UserReview userReview) {
                Notification notification = new Notification(null, "unread", "You have received new like at post " + userReview.getComment() +"!", userReview.getUserID());
                notification.insert();
            }
        }, userReviewId);

    }



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
        holder.tvRating.setText(userReview.getRating() + " / 5.0");
        holder.tvComment.setText(userReview.getComment());
        holder.checkUserLiked(userReview);
        holder.checkOwnComment(userReview);

        holder.ivLikeImageEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> userReviewLikes = new HashMap<>();
                userReviewLikes.put("reviewid", userReview.getDocumentID());
                userReviewLikes.put("userid", HomeActivity.user.getId());

                Database.getInstance().collection("userreviewlikes")
                        .add(userReviewLikes)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                loadUserReviews(v);
                                notifyLikedReviewUser(userReview.getDocumentID());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Unable to like car right now! Please try again...", Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });

        holder.ivLikeImageFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.getInstance().collection("userreviewlikes")
                        .whereEqualTo("reviewid", userReview.getDocumentID())
                        .whereEqualTo("userid", HomeActivity.user.getId())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                if (documentSnapshot != null) {
                                    Database.getInstance().collection("userreviewlikes").document(documentSnapshot.getId()).delete()
                                            .addOnSuccessListener(aVoid -> {
                                                loadUserReviews(v);
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error occurred while deleting the document
                                                Toast.makeText(v.getContext(), "Unable to unlike car right now! Please try again...", Toast.LENGTH_LONG).show();
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Error occurred while getting the document
                            Toast.makeText(v.getContext(), "Unable to unlike car right now! Please try again...", Toast.LENGTH_LONG).show();
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return userReviews.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView civUserProfile, ivLikeImageEmpty, ivLikeImageFilled;
        TextView tvUsername, tvRating, tvComment;

//        void checkUserLiked(Car car){
//            Database.getInstance().collection("userreviewlikes").whereEqualTo("carid", car.getId()).whereEqualTo("userid", HomeActivity.user.getId())
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
//                            ivLikeImageEmpty.setVisibility(View.INVISIBLE);
//                            ivLikeImageFilled.setVisibility(View.VISIBLE);
//                        }else{
//                            ivLikeImageEmpty.setVisibility(View.VISIBLE);
//                            ivLikeImageFilled.setVisibility(View.INVISIBLE);
//                        }
//                    });
//        }

        boolean checkOwnComment(UserReview userReview){
            if(userReview.getUserID().equals(HomeActivity.user.getId())){
                return true;
            }
            return false;
        }
            void checkUserLiked(UserReview userReview){
                Database.getInstance().collection("userreviewlikes").whereEqualTo("reviewid", userReview.getDocumentID()).whereEqualTo("userid", HomeActivity.user.getId())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                                if(checkOwnComment(userReview) == false){
                                    ivLikeImageEmpty.setVisibility(View.INVISIBLE);
                                    ivLikeImageFilled.setVisibility(View.VISIBLE);
                                }else{
                                    ivLikeImageEmpty.setVisibility(View.INVISIBLE);
                                    ivLikeImageFilled.setVisibility(View.INVISIBLE);
                                }

                            }else{
                                if(checkOwnComment(userReview) == false){
                                    ivLikeImageEmpty.setVisibility(View.VISIBLE);
                                    ivLikeImageFilled.setVisibility(View.INVISIBLE);
                                }else{
                                    ivLikeImageEmpty.setVisibility(View.INVISIBLE);
                                    ivLikeImageFilled.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
            }


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            civUserProfile = itemView.findViewById(R.id.civUserProfile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivLikeImageEmpty = itemView.findViewById(R.id.ivLikeImageEmpty);
            ivLikeImageFilled = itemView.findViewById(R.id.ivLikeImageFilled);
        }



    }

    public void setUserReviews(ArrayList<UserReview> newReviews) {
        userReviews.clear();
        userReviews.addAll(newReviews);
        notifyDataSetChanged();
    }
}
