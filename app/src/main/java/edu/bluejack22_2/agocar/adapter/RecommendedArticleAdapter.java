package edu.bluejack22_2.agocar.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.ArticleDetailActivity;
import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.NewsActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;

public class RecommendedArticleAdapter extends RecyclerView.Adapter<RecommendedArticleAdapter.HomeViewHolder> {

    private ArrayList<Article> articles = new ArrayList<>();
    private LayoutInflater inflater;

    private Context context;

    private int currentlyEditingPosition = -1; // initialize to -1 to indicate no currently editing position
    private Uri imageUri;


    public int getCurrentlyEditingPosition() {
        return currentlyEditingPosition;
    }

    public RecommendedArticleAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void updateImage(Uri imageUri, int pos){
        articles.get(pos).setImageUri(imageUri);
        this.imageUri = imageUri;
        Log.d("Uriasd", ""+articles.get(currentlyEditingPosition).getImageUri());
        notifyItemChanged(pos);
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_articles_card, parent, false);
        return new RecommendedArticleAdapter.HomeViewHolder(view, inflater, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Article article = articles.get(position);
        Picasso.get().load(article.getImage()).into(holder.ivArticleImage);
        holder.tvArticleTitle.setText(article.getTitle());
        holder.tvArticleDate.setText(article.getPostDate().toDate().toString());
        holder.tvArticleCreator.setText(article.getPostedBy());

        if(article.getImageUri() != null){
            Log.d("qwer", ""+position);
            Log.d("test", ""+article.getImageUri());
            holder.showModal();
            holder.ivDisplayedEditImage.setImageURI(article.getImageUri());
        }
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
        Button btnSave;
        Context context;
        private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
        private Map<String, String> cloudinaryConfig = new HashMap<>();
        AlertDialog dialog;

        void authenticateUser(){
            if(!HomeActivity.user.getRole().equals("Admin")){
                clEdit.setVisibility(View.INVISIBLE);
            }
        }

        public void setImageUri(Uri imageUri) {
            ivDisplayedEditImage.setImageURI(imageUri);
        }

        void setElements(View modalView){
            Article selectedArticle = articles.get(getPosition());
            etArticleTitle = modalView.findViewById(R.id.etArticleTitle);
            etArticleDescription = modalView.findViewById(R.id.etArticleDescription);
            ivDisplayedEditImage = modalView.findViewById(R.id.ivDisplayedImage);
            btnSave = modalView.findViewById(R.id.button2);


            etArticleTitle.setText(selectedArticle.getTitle());
            etArticleDescription.setText(selectedArticle.getContent());
            Picasso.get().load(selectedArticle.getImage()).into(ivDisplayedEditImage);
        }

        void setConnection(){
            cloudinaryConfig.put("cloud_name", "dmpbgjnrc");
            cloudinaryConfig.put("api_key", "596936696592152");
            cloudinaryConfig.put("api_secret", "m5pavlf12IXv1Y4maOCv5OE5DNU");
        }

        void updateData(Article updateArticle ,String imageUrl){
            Log.d("test", "AGin");
            Log.d("asd", imageUrl);
            updateArticle.setImage(imageUrl);

            updateArticle.update(new OnSuccessListener() {
                @Override
                public void onSuccess(boolean success) {
                    dialog.dismiss();
                    NewsActivity.loadArticles();
                    Toast.makeText(context, "Successfully Update an Article!", Toast.LENGTH_LONG).show();
                }
            });
        }

        void uploadImage(Article updatedArticle){
            Log.d("uplimg", "Test");
            Cloudinary cloudinary = new Cloudinary(cloudinaryConfig);

            try {
                ContentResolver resolver = context.getContentResolver();
                InputStream inputStream = resolver.openInputStream(updatedArticle.getImageUri());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > -1 ) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                byte[] imageData = baos.toByteArray();


                new Thread(new Runnable() {
                    public void run() {

                        // Perform Cloudinary upload operation here
                        try {
                            Map uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.emptyMap());
                            String imageUrl = (String) uploadResult.get("secure_url");

                            if(imageUrl != null){
                                imageUri = null;
                                articles.get(currentlyEditingPosition).setImageUri(null);
                                currentlyEditingPosition = -1;
                                updateData(updatedArticle, imageUrl);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void showModal() {
            setConnection();
            LayoutInflater inflater = this.inflater;
            View modalView = inflater.inflate(R.layout.edit_article_modal, null);
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



            ivDisplayedEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentlyEditingPosition = getAdapterPosition();
                    Log.d("bnmx", ""+  getAdapterPosition());
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((NewsActivity) context).startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
                    dialog.dismiss();
                }
            });

            btnSave.setOnClickListener(e -> {
                Log.d("asdcposition", ""+ currentlyEditingPosition);
                Log.d("zxc", ""+getAdapterPosition());

                String title, description;
                title = etArticleTitle.getText().toString();
                description = etArticleDescription.getText().toString();
                Article article = articles.get(currentlyEditingPosition != -1 ? currentlyEditingPosition : getAdapterPosition());
                Article updatedArticle = new Article(article.getArticleID(), title, "", article.getPostedBy(), description, article.getPostDate());
                updatedArticle.setImageUri(imageUri);

                Log.d("Uri", ""+article.getImageUri());
                if(imageUri != null){
                    uploadImage(updatedArticle);
                }else{
//                    Article a = articles.get(getAdapterPosition());
                    updateData(updatedArticle, article.getImage());
                }
            });
        }

        public HomeViewHolder(@NonNull View itemView, LayoutInflater inflater, Context context) {
            super(itemView);
            this.inflater = inflater;
            this.context = context;

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

                                                    }
                                                });

                                                Article.getFeaturedArticles(new RetrievedArticlesListener() {
                                                    @Override
                                                    public void retrievedArticles(ArrayList<Article> articles) {
                                                        NewsActivity.featuredArticleAdapter.setArticles(articles);
                                                    }
                                                });

                                                //Update yang featured

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

        public ImageView getDisplayedEditImage() {
            return ivDisplayedEditImage;
        }

    }

    public void setArticles(ArrayList<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }
}
