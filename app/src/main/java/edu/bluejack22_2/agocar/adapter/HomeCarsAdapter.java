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

public class HomeCarsAdapter extends RecyclerView.Adapter<HomeCarsAdapter.HomeViewHolder> {

    private ArrayList<Car> cars = new ArrayList<>();

    void loadCars(View v){
        Car.getPreferredCars(new RetrievedCarsListener() {
            @Override
            public void retrievedCars(ArrayList<Car> cars) {
                if(cars != null){
                    setCars(cars);
                }else{
                    Toast.makeText(v.getContext(), "Unable to like car right now! Please try again...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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
        holder.checkUserLiked(car);
        holder.countLikes(car);

        holder.ivLikeImageEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> userlike = new HashMap<>();
                userlike.put("carid", car.getId());
                userlike.put("userid", HomeActivity.user.getId());

                Database.getInstance().collection("userlikes")
                        .add(userlike)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                               loadCars(v);
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

                Database.getInstance().collection("userlikes")
                        .whereEqualTo("carid", car.getId())
                        .whereEqualTo("userid", HomeActivity.user.getId())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                if (documentSnapshot != null) {
                                    Database.getInstance().collection("userlikes").document(documentSnapshot.getId()).delete()
                                            .addOnSuccessListener(aVoid -> {
                                                loadCars(v);
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
        return cars.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivCarImage, ivLikeImageEmpty, ivLikeImageFilled, ivCommentImage;
        TextView tvCarName, tvLikeCount, tvCommentCount;

        void checkUserLiked(Car car){
            Database.getInstance().collection("userlikes").whereEqualTo("carid", car.getId()).whereEqualTo("userid", HomeActivity.user.getId())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                            ivLikeImageEmpty.setVisibility(View.INVISIBLE);
                            ivLikeImageFilled.setVisibility(View.VISIBLE);
                        }else{
                            ivLikeImageEmpty.setVisibility(View.VISIBLE);
                            ivLikeImageFilled.setVisibility(View.INVISIBLE);
                        }
                    });
        }

        void countLikes(Car car){
            Database.getInstance().collection("userlikes").whereEqualTo("carid", car.getId())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                            int count = queryDocumentSnapshots.size();
                            tvLikeCount.setText(count+"");
                        }else{
                            int count = 0;
                            tvLikeCount.setText(count+"");
                        }
                    });
        }


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCarImage = itemView.findViewById(R.id.ivCarImage);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            ivLikeImageEmpty = itemView.findViewById(R.id.ivLikeImageEmpty);
            ivLikeImageFilled = itemView.findViewById(R.id.ivLikeImageFilled);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);

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
