package edu.bluejack22_2.agocar.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.RetrievedArticleListener;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;
import edu.bluejack22_2.agocar.other.RetrievedBrandListener;
import edu.bluejack22_2.agocar.other.RetrievedCarListener;

public class Article {

    private String articleID, title, image, postedBy, content;
    private Timestamp postDate;

    public Article(String articleID, String title, String image, String postedBy, String content, Timestamp postDate) {
        this.articleID = articleID;
        this.title = title;
        this.image = image;
        this.postedBy = postedBy;
        this.content = content;
        this.postDate = postDate;
    }
    public Article(String title, String image, String postedBy, String content, Timestamp postDate) {
        this.title = title;
        this.image = image;
        this.postedBy = postedBy;
        this.content = content;
        this.postDate = postDate;
    }

    public String getArticleID() {
        return articleID;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

    public static void getArticles(RetrievedArticlesListener listener){
        ArrayList<Article> articles = new ArrayList<>();
        Database.getInstance().collection("articles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String documentID = b.getId();
                            String title = b.getString("title");
                            String image = b.getString("image");
                            String postedBy = b.getString("postedBy");
                            String content = b.getString("content");
                            Timestamp postDate = b.getTimestamp("postDate");
                            articles.add(new Article(documentID, title, image, postedBy, content, postDate));
                        }
                        listener.retrievedArticles(articles);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    };

    public static void getFeaturedArticles(RetrievedArticlesListener listener){
        ArrayList<Article> articles = new ArrayList<>();
        Database.getInstance().collection("articles")
                .orderBy("postDate", Query.Direction.DESCENDING) // Sort by postDate in ascending order
                .limit(5) // Limit the result to 5
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String documentID = b.getId();
                            String title = b.getString("title");
                            String image = b.getString("image");
                            String postedBy = b.getString("postedBy");
                            String content = b.getString("content");
                            Timestamp postDate = b.getTimestamp("postDate");
                            articles.add(new Article(documentID, title, image, postedBy, content, postDate));
                        }
                        listener.retrievedArticles(articles);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    };

    public static void getArticle(RetrievedArticleListener listener, String articleID){
        Database.getInstance().collection("articles").document(articleID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots != null){
                        String documentID = queryDocumentSnapshots.getId();
                        String title = queryDocumentSnapshots.getString("title");
                        String image = queryDocumentSnapshots.getString("image");
                        String postedBy = queryDocumentSnapshots.getString("postedBy");
                        String content = queryDocumentSnapshots.getString("content");
                        Timestamp postDate = queryDocumentSnapshots.getTimestamp("postDate");

                        Article article = new Article(documentID, title, image, postedBy, content, postDate);
                        listener.retrievedArticle(article);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.retrievedArticle(null);
                });
    };



    public void delete(edu.bluejack22_2.agocar.other.OnSuccessListener listener, String articleID){
        Database.getInstance().document("articles/"+articleID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSuccess(false);
            }
        });
    }

    public void insert(edu.bluejack22_2.agocar.other.OnSuccessListener listener) {
        Map<String, Object> article = new HashMap<>();
        article.put("title", this.title);
        article.put("image", this.image);
        article.put("content", this.content);
        article.put("postedBy", this.postedBy);
        article.put("postDate", this.postDate);



        Database.getInstance().collection("articles")
                .add(article)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onSuccess(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onSuccess(false);
                    }
                });


    }

}