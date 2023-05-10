package edu.bluejack22_2.agocar.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.bluejack22_2.agocar.EditProfileActivity;
import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.LoginActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.adapter.ProfileFavouritesAdapter;
import edu.bluejack22_2.agocar.adapter.ProfileReviewAdapter;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.models.UserReview;
import edu.bluejack22_2.agocar.other.RetrievedUserReviewsListener;

public class ReviewsFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageView ivEdit;
    private CircleImageView civProfile;
    private Button btnLogOut;
    private User user;
    private RecyclerView rvReviews;
    private ProfileReviewAdapter reviewAdapter;


    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void loadReviews(){
        UserReview.getReviewsByUser(this.user.getId(), new RetrievedUserReviewsListener() {
            @Override
            public void retrievedUserReviews(ArrayList<UserReview> userReviews) {
                reviewAdapter.setUserReviews(userReviews);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivEdit = view.findViewById(R.id.ivEdit);
        civProfile = view.findViewById(R.id.ivProfile);
        btnLogOut = view.findViewById(R.id.btnLogOut);

        user = HomeActivity.user;
        tvName.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        Picasso.get().load(user.getImage()).into(civProfile);

        rvReviews = view.findViewById(R.id.rvYourReviews);
        reviewAdapter = new ProfileReviewAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(linearLayoutManager);
        rvReviews.setAdapter(reviewAdapter);
        loadReviews();

        btnLogOut.setOnClickListener(e -> {
            SharedPreferences mPrefs = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        ivEdit.setOnClickListener(e -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });


        return view;
    }
}