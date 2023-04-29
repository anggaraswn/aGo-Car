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
import java.util.HashSet;

import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    private ArrayList<Brand> brands = new ArrayList<>();
    private static ArrayList<String> selectedBrands = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Brand brand = brands.get(position);
        Log.d("Debug", brand.getImage());
        Picasso.get().load(brand.getImage()).into(holder.ivBrand);
        holder.tvName.setText(brand.getName());

//        holder.che
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivBrand, ivSelected;
        TextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBrand = itemView.findViewById(R.id.ivBrand);
            ivSelected = itemView.findViewById(R.id.ivSelected);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Brand brand = brands.get(position);
            if(selectedBrands.contains(brand.getDocumentID())) selectedBrands.remove(brand.getDocumentID());
            else selectedBrands.add(brand.getDocumentID());
            ivSelected.setVisibility(selectedBrands.contains(brand.getDocumentID()) ? View.VISIBLE : View.GONE);
        }
    }

    public void setBrands(ArrayList<Brand> newBrands) {
        brands.clear();
        brands.addAll(newBrands);
        notifyDataSetChanged();
    }

    public static ArrayList<String> getSelectedBrands() {
        return selectedBrands;
    }
}
