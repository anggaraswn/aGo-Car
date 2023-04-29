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
import edu.bluejack22_2.agocar.models.Brand;

public class HomeBrandsAdapter extends RecyclerView.Adapter<HomeBrandsAdapter.HomeViewHolder> {

    private ArrayList<Brand> brands = new ArrayList<>();


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_brand_card, parent, false);
        return new HomeBrandsAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Brand brand = brands.get(position);
        Picasso.get().load(brand.getImage()).into(holder.ivBrandImage);
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBrandImage;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBrandImage = itemView.findViewById(R.id.ivBrandImage);
        }


    }

    public void setBrands(ArrayList<Brand> newBrands) {
        brands.clear();
        brands.addAll(newBrands);
        notifyDataSetChanged();
    }
}
