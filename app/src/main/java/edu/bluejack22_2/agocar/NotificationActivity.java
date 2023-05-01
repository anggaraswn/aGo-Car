package edu.bluejack22_2.agocar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.adapter.NotificationsAdapter;
import edu.bluejack22_2.agocar.models.Notification;
import edu.bluejack22_2.agocar.other.RetrievedNotificationsListener;

public class NotificationActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvNotifications;
    private NotificationsAdapter notificationsAdapter;


    void getComponents(){
        ivBack = findViewById(R.id.ivBackButton);
    }

    void setComponents(){
        rvNotifications = findViewById(R.id.rvNotifications);
        notificationsAdapter = new NotificationsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNotifications.setLayoutManager(linearLayoutManager);
        rvNotifications.setAdapter(notificationsAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getComponents();
        setComponents();

        Notification.getUserNotifications(new RetrievedNotificationsListener() {
            @Override
            public void retrievedNotifications(ArrayList<Notification> notifications) {
                if(!notifications.isEmpty()){
                    for (Notification n:
                         notifications) {
                        Log.d("NotifContent", n.getContent());

                    }
                    notificationsAdapter.setNotifications(notifications);
                }
            }
        }, HomeActivity.user.getId());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}