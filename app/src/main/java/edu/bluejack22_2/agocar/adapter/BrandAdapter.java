package edu.bluejack22_2.agocar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Brand;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    private ArrayList<Brand> brands;

    public BrandAdapter(ArrayList<Brand> brands){
        this.brands = brands;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(images.get(position)).into(holder.ivBrand);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBrand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBrand = itemView.findViewById(R.id.ivBrand);
        }
    }
}
