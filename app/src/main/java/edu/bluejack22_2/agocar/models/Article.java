package edu.bluejack22_2.agocar.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.RetrievedArticlesListener;

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

}
