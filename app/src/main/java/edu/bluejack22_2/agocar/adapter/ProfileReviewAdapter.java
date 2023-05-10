package edu.bluejack22_2.agocar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.NewsActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class ProfileReviewAdapter extends RecyclerView.Adapter<ProfileReviewAdapter.ViewHolder> {

    ArrayList<UserReview> userReviews = new ArrayList<>();
    private LayoutInflater inflater;

    private Context context;

    public ProfileReviewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_review_card, parent, false);
        return new ProfileReviewAdapter.ViewHolder(view, inflater, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("zxc", "" + userReviews.size());
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

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivDelete, ivEdit, ivClose;
        TextView tvReviews;
        Context context;
        LayoutInflater inflater;
        private Map<String, String> cloudinaryConfig = new HashMap<>();
        AlertDialog dialog;
        EditText etRating, etComment;
        Button btnSave;


        void setConnection() {
            cloudinaryConfig.put("cloud_name", "dmpbgjnrc");
            cloudinaryConfig.put("api_key", "596936696592152");
            cloudinaryConfig.put("api_secret", "m5pavlf12IXv1Y4maOCv5OE5DNU");
        }

        void setElements(View modalView) {
            etRating = modalView.findViewById(R.id.etRating);
            etComment = modalView.findViewById(R.id.etComment);
            btnSave = modalView.findViewById(R.id.btnSave);

            UserReview selectedReview = userReviews.get(getAdapterPosition());

            etRating.setText("" + selectedReview.getRating());
            etComment.setText(selectedReview.getComment());
        }


        public void showModal() {
            setConnection();
            LayoutInflater inflater = this.inflater;
            View modalView = inflater.inflate(R.layout.edit_review_modal, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
            builder.setView(modalView);
            dialog = builder.create();
            // Set the window properties of the dialog
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            //Set Selected Data to TextField
            setElements(modalView);
            dialog.show();

            ivClose = modalView.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call dialog.dismiss() to close the dialog
                    dialog.dismiss();
                }
            });


            btnSave.setOnClickListener(e -> {
                UserReview selectedReview = userReviews.get(getAdapterPosition());
                String comment;
                Double rating;
                rating = Double.parseDouble(etRating.getText().toString());
                comment = etComment.getText().toString();
                UserReview updatedReview = new UserReview(selectedReview.getCarID(), comment, selectedReview.getUserID(), selectedReview.getDocumentID(), rating);
//                Log.d("vbn", updatedReview);
                updatedReview.update(new OnSuccessListener() {
                    @Override
                    public void onSuccess(boolean success) {
                        dialog.dismiss();
                        UserReview.getReviewsByUser(HomeActivity.user.getId(), new RetrievedUserReviewsListener() {
                            @Override
                            public void retrievedUserReviews(ArrayList<UserReview> userReviews) {
                                setUserReviews(userReviews);
                            }
                        });
                        Toast.makeText(context, "Successfully Update a Review!", Toast.LENGTH_LONG).show();
                    }
                });
            });
        }


        public ViewHolder(@NonNull View itemView, LayoutInflater inflater, Context context) {
            super(itemView);
            this.context = context;
            this.inflater = inflater;


            ivImage = itemView.findViewById(R.id.ivImage);
            tvReviews = itemView.findViewById(R.id.tvReviews);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete Confirmation");
                    builder.setMessage("Are you sure you want to delete the selected review ?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserReview selectedReview = userReviews.get(getAdapterPosition());
                                    selectedReview.delete(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(boolean success) {
                                            if (success) {
                                                Toast.makeText(v.getContext(), "Successfully deleted review !", Toast.LENGTH_LONG).show();
                                                UserReview.getReviewsByUser(HomeActivity.user.getId(), new RetrievedUserReviewsListener() {
                                                    @Override
                                                    public void retrievedUserReviews(ArrayList<UserReview> userReviews) {
                                                        setUserReviews(userReviews);
                                                    }
                                                });
                                            } else {

                                            }
                                        }
                                    }, selectedReview.getDocumentID());
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showModal();
                }
            });


        }
    }

    public void setUserReviews(ArrayList<UserReview> newReviews) {
        userReviews.clear();
        userReviews.addAll(newReviews);
        notifyDataSetChanged();
    }

}
