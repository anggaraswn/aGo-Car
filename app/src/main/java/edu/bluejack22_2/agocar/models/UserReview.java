package edu.bluejack22_2.agocar.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedBrandListener;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class UserReview {


    private String carID, comment, userID, documentID;

    private double rating;



    public UserReview(String carID, String comment, String userID, String documentID, double rating) {
        this.carID = carID;
        this.comment = comment;
        this.userID = userID;
        this.documentID = documentID;
        this.rating = rating;
    }

    public static void getReviews(String carID , RetrievedUserReviewsListener listener){
        ArrayList<UserReview> userReviews = new ArrayList<>();
        Database.getInstance().collection("userreviews").whereEqualTo("carid",carID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String comment = b.getString("comment");
                            Double rating = b.getDouble("rating");
                            String userID = b.getString("userid");
                            UserReview userReview = new UserReview( carID, comment, userID,id, rating);
                            userReviews.add(userReview);
                        }
                        listener.retrievedUserReviews(userReviews);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    }

    public static void getReviewsByUser(String userID, RetrievedUserReviewsListener listener){
        ArrayList<UserReview> userReviews = new ArrayList<>();
        Database.getInstance().collection("userreviews").whereEqualTo("userid", userID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String comment = b.getString("comment");
                            Double rating = b.getDouble("rating");
                            String carID = b.getString("carid");
                            userReviews.add(new UserReview(carID, comment, userID, id, rating));

                        }
                        listener.retrievedUserReviews(userReviews);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    }

    public static void getUserReview(RetrievedUserReviewListener listener, String userReviewID){

        Database.getInstance().collection("userreviews").document(userReviewID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots != null){
                        String id = queryDocumentSnapshots.getId();
                        String comment = queryDocumentSnapshots.getString("comment");
                        Double rating = queryDocumentSnapshots.getDouble("rating");
                        String carID = queryDocumentSnapshots.getString("carid");
                        String userID = queryDocumentSnapshots.getString("userid");

                        UserReview userReview = new UserReview(carID, comment, userID,id,rating);
                        listener.retrievedUserReview(userReview);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.retrievedUserReview(null);
                });
    };




    public void insert(OnSuccessListener listener){
        Map<String, Object> userReview = new HashMap<>();
        userReview.put("carid", this.carID);
        userReview.put("comment", this.comment);
        userReview.put("rating", this.rating);
        userReview.put("userid", this.userID);

        Database.getInstance().collection("userreviews")
                .add(userReview)
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentReference>() {
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

    public void update(edu.bluejack22_2.agocar.other.OnSuccessListener listener){
        Database.getInstance().collection("userreviews").document(this.getDocumentID()).update("comment", this.comment, "rating", this.rating).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSuccess(false);
            }
        });
    }


    public void delete(edu.bluejack22_2.agocar.other.OnSuccessListener listener, String reviewID){
        Database.getInstance().document("userreviews/"+reviewID).delete().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSuccess(false);
            }
        });
    }


    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;

    }
}
