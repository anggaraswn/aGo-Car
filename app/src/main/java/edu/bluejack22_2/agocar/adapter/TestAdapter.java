package edu.bluejack22_2.agocar.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    List<Brand> list = new ArrayList<>();

    public TestAdapter(List<Brand> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Brand b = list.get(position);
        Picasso.get().load(b.getImage()).into(holder.imageView);
        holder.textView.setText(b.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.imageTitle);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Any One");
            contextMenu.add(getAdapterPosition(), 101, 0, "Add to Wishlist");
            contextMenu.add(getAdapterPosition(), 102, 1, "Delete");
        }
    }

    public void setCars(ArrayList<Brand> newBrands) {
        list.clear();
        list.addAll(newBrands);
        notifyDataSetChanged();
    }

    public void RemoveItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }
}
