package edu.bluejack22_2.agocar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.ArticleDetailActivity;
import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.LoginActivity;
import edu.bluejack22_2.agocar.NewsActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;

public class RecommendedArticleAdapter extends RecyclerView.Adapter<RecommendedArticleAdapter.HomeViewHolder> {

    private ArrayList<Article> articles = new ArrayList<>();
    private LayoutInflater inflater;

    public RecommendedArticleAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_articles_card, parent, false);
        return new RecommendedArticleAdapter.HomeViewHolder(view, inflater);
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

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivArticleImage, ivEdit, ivDelete, ivClose, ivDisplayedEditImage;
        TextView tvArticleTitle, tvArticleDate, tvArticleCreator;
        LayoutInflater inflater;
        ConstraintLayout clEdit;
        EditText etArticleTitle, etArticleDescription;

        void authenticateUser(){
            if(!HomeActivity.user.getRole().equals("Admin")){
                clEdit.setVisibility(View.INVISIBLE);
            }
        }

        void setElements(View modalView){
            Article selectedArticle = articles.get(getPosition());
            etArticleTitle = modalView.findViewById(R.id.etArticleTitle);
            etArticleDescription = modalView.findViewById(R.id.etArticleDescription);
            ivDisplayedEditImage = modalView.findViewById(R.id.ivDisplayedImage);

            etArticleTitle.setText(selectedArticle.getTitle());
            etArticleDescription.setText(selectedArticle.getContent());
            Picasso.get().load(selectedArticle.getImage()).into(ivDisplayedEditImage);
        }

        public void showModal() {
            LayoutInflater inflater = this.inflater;
            View modalView = inflater.inflate(R.layout.edit_article_modal, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
            builder.setView(modalView);
            AlertDialog dialog = builder.create();
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

        }



        public HomeViewHolder(@NonNull View itemView, LayoutInflater inflater) {
            super(itemView);
            this.inflater = inflater;

            ivArticleImage = itemView.findViewById(R.id.ivRecArticleImage);
            tvArticleTitle = itemView.findViewById(R.id.tvRecArticleTitle);
            tvArticleDate = itemView.findViewById(R.id.tvRecArticleDate);
            tvArticleCreator = itemView.findViewById(R.id.tvRecArticleCreator);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            clEdit = itemView.findViewById(R.id.clEdit);




            authenticateUser();


            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Position", getAdapterPosition()+"");

                    showModal();
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete Confirmation");
                    builder.setMessage("Are you sure you want to delete the selected article ?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Article selectedArticle = articles.get(getPosition());
                                    selectedArticle.delete(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(boolean success) {
                                            if(success){
                                                Toast.makeText(v.getContext(), "Successfully deleted article !", Toast.LENGTH_LONG).show();
                                                Article.getArticles(new RetrievedArticlesListener() {
                                                    @Override
                                                    public void retrievedArticles(ArrayList<Article> articles) {
                                                        setArticles(articles);
                                                        //Update yang featured
                                                    }
                                                });
                                            }else{

                                            }
                                        }
                                    }, selectedArticle.getArticleID());
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

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
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
