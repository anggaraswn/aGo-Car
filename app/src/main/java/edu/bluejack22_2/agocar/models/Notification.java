package edu.bluejack22_2.agocar.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;
import edu.bluejack22_2.agocar.other.RetrievedNotificationsListener;

public class Notification {

    private String id, status, content, userid;

    public Notification(String id, String status, String content, String userid) {
        this.id = id;
        this.status = status;
        this.content = content;
        this.userid = userid;
    }

    public void insert(){
        Map<String, Object> notification = new HashMap<>();
        notification.put("content", this.content);
        notification.put("status", this.status);
        notification.put("userid", this.userid);



        Database.getInstance().collection("notifications")
                .add(notification);
    }

    public void delete(OnSuccessListener listener){
        Database.getInstance().document("notifications/"+id).delete().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
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

    public static void getNotifications(RetrievedNotificationsListener listener){
        ArrayList<Notification> notifications = new ArrayList<>();
        Database.getInstance().collection("notifications")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String content = b.getString("content");
                            String status = b.getString("status");
                            String userid = b.getString("userid");

                            notifications.add(new Notification(id, status, content, userid));
                        }
                        listener.retrievedNotifications(notifications);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred

                });
    };

    public static void getUserNotifications(RetrievedNotificationsListener listener, String userid){
        ArrayList<Notification> notifications = new ArrayList<>();
        Database.getInstance().collection("notifications").whereEqualTo("userid", userid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String id = b.getId();
                            String content = b.getString("content");
                            String status = b.getString("status");

                            notifications.add(new Notification(id, status, content, userid));
                        }
                        listener.retrievedNotifications(notifications);
                    }else{
                        listener.retrievedNotifications(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.retrievedNotifications(null);

                });
    };


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
