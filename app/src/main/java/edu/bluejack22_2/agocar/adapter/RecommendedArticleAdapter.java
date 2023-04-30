package edu.bluejack22_2.agocar.adapter;

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
import edu.bluejack22_2.agocar.models.Article;

public class RecommendedArticleAdapter extends RecyclerView.Adapter<RecommendedArticleAdapter.HomeViewHolder> {

    private ArrayList<Article> articles = new ArrayList<>();


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_articles_card, parent, false);
        return new RecommendedArticleAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Article article = articles.get(position);
        Picasso.get().load(article.getImage()).into(holder.ivArticleImage);
        holder.tvArticleTitle.setText(article.getTitle());
        holder.tvArticleDate.setText(article.getPostDate().toDate().toString());
        holder.tvArticleCreator.setText(article.getPostedBy());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView ivArticleImage;
        TextView tvArticleTitle, tvArticleDate, tvArticleCreator;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivArticleImage = itemView.findViewById(R.id.ivRecArticleImage);
            tvArticleTitle = itemView.findViewById(R.id.tvRecArticleTitle);
            tvArticleDate = itemView.findViewById(R.id.tvRecArticleDate);
            tvArticleCreator = itemView.findViewById(R.id.tvRecArticleCreator);
        }


    }

    public void setArticles(ArrayList<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }
}
