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

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.bluejack22_2.agocar.EditProfileActivity;
import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.LoginActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.adapter.ProfileFavouritesAdapter;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.models.User;
import edu.bluejack22_2.agocar.other.RetrievedFavouritesListener;

public class FavouritesFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageView ivEdit;
    private CircleImageView civProfile;
    private Button btnLogOut;
    private User user;
    private RecyclerView rvFavourites;
    private ProfileFavouritesAdapter favouritesAdapter;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void getFavourites(RetrievedFavouritesListener listener) {
        ArrayList<String> listCardID = new ArrayList<>();
        Database.getInstance().collection("userlikes").whereEqualTo("userid", HomeActivity.user.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot b : queryDocumentSnapshots) {
                        String id = b.getString("carid");
                        listCardID.add(id);
                    }
                    listener.retrievedFavourites(listCardID);
                });
    }

    void loadFavourites() {
        getFavourites(new RetrievedFavouritesListener() {
            @Override
            public void retrievedFavourites(ArrayList<String> favourites) {
                favouritesAdapter.setFavourites(favourites);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivEdit = view.findViewById(R.id.ivEdit);
        civProfile = view.findViewById(R.id.ivProfile);
        btnLogOut = view.findViewById(R.id.btnLogOut);

        rvFavourites = view.findViewById(R.id.rvFavourites);
        favouritesAdapter = new ProfileFavouritesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvFavourites.setLayoutManager(linearLayoutManager);
        rvFavourites.setAdapter(favouritesAdapter);

        user = HomeActivity.user;
        tvName.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        Picasso.get().load(user.getImage()).into(civProfile);
        loadFavourites();

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