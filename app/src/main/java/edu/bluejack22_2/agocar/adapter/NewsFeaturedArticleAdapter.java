package edu.bluejack22_2.agocar.adapter;

import android.content.Intent;
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

import edu.bluejack22_2.agocar.ArticleDetailActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Article;

public class NewsFeaturedArticleAdapter extends RecyclerView.Adapter<NewsFeaturedArticleAdapter.FeaturedArticleViewHolder> {

    private ArrayList<Article> articles = new ArrayList<>();

    @NonNull
    @Override
    public FeaturedArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_articles_card, parent, false);
        return new FeaturedArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        Picasso.get().load(article.getImage()).into(holder.ivImage);
        holder.tvTitle.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class FeaturedArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivImage;
        TextView tvTitle;

        public FeaturedArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        @Override
        public void onClick(View view) {
            Log.d("AGTest", "Clicked");
            int position = getAdapterPosition();
            Article article = articles.get(position);
            Intent intent = new Intent(view.getContext(), ArticleDetailActivity.class);
            intent.putExtra("articleID", article.getArticleID());
            intent.putExtra("image", article.getImage());
            intent.putExtra("title", article.getTitle());
            intent.putExtra("content", article.getContent());
            view.getContext().startActivity(intent);
        }
    }

    public void setArticles(ArrayList<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }
}
